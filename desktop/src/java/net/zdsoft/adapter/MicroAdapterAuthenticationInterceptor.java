package net.zdsoft.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.entity.LoginInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

/**
 * 这种做法是不安全的，authorization中的token一直有效
 * @author shenke
 * @date 2019/10/23 下午1:26
 */
public class MicroAdapterAuthenticationInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(MicroAdapterAuthenticationInterceptor.class);

    static final String ATTRIBUTE_LOGIN_KEY = "loginInfo";

    static final String ERROR_KEY = "micro-adapter-interceptor-error";

    private ObjectMapper jacksonObjectMapper = new Jackson2ObjectMapperBuilder().createXmlMapper(false).build();

    @Autowired
    private AdapterSessionHolder sessionHolder;
    @Autowired
    private UserRemoteService userRemoteService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        if ("/micro/adapter/error".equals(requestURI)
                || "/micro/adapter/invalid".equals(requestURI)) {
            return true;
        }

        String authorization = request.getParameter(HttpHeaders.AUTHORIZATION.toLowerCase());
        if (StringUtils.isBlank(authorization)) {
            request.setAttribute(ERROR_KEY, "不合法的请求");
            request.getRequestDispatcher("/micro/adapter/error").forward(request, response);
            return false;
        }

        try {
            HttpSession session = request.getSession(false);
            //未登录
            if (Objects.isNull(session) || Objects.isNull(session.getAttribute(ATTRIBUTE_LOGIN_KEY))) {
                Authorization auth = getAuthorization(authorization);
                return initSession(request, response, authorization, auth);
            }
            //验证当前session用户是否和给定的authorization用户一致
            else {
                LoginInfo loginInfo = (LoginInfo) session.getAttribute(ATTRIBUTE_LOGIN_KEY);
                Authorization auth = getAuthorization(authorization);
                if (!auth.getCredentials().equals(loginInfo.getUserId())) {
                    return initSession(request, response, authorization, auth);
                }
            }
        } catch (JWTErrorException e) {
            request.setAttribute(ERROR_KEY, "Authorization解析错误");
            request.getRequestDispatcher("/micro/adapter/error").forward(request, response);
            return false;
        }
        return true;
    }

    private boolean initSession(HttpServletRequest request, HttpServletResponse response, String authorization, Authorization auth) throws ServletException, IOException {
        User user = userRemoteService.findOneObjectById(auth.getCredentials());
        if (Objects.isNull(user)) {
            //用户信息不存在 跳转错误页面 TODO
            request.getRequestDispatcher("/micro/adapter/error").forward(request, response);
            return false;
        }
        sessionHolder.initLoginInfo(request.getSession(), user, authorization);
        return true;
    }

    private Authorization getAuthorization(String authorization) throws java.io.IOException, JWTErrorException {
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey("secret").parseClaimsJws(authorization).getBody();
        } catch (Exception e) {
            logger.error("authorization parse error", e);
            throw new JWTErrorException();
        }
        //这里应当验证过期时间
        return jacksonObjectMapper.readValue(claims.getSubject(), Authorization.class);
    }

    static class JWTErrorException extends Exception {
        @Override
        public synchronized Throwable fillInStackTrace() {
            return this;
        }
    }
}
