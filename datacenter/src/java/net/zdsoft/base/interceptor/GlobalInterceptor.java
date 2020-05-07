package net.zdsoft.base.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.ServletUtils;
import net.zdsoft.openapi.remote.openapi.constant.OpenApiConstants;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
public class GlobalInterceptor implements HandlerInterceptor {
    private String[] whiteList;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
        String requestURI = request.getRequestURI();

        if ("/".equals(requestURI)) {
            response.sendRedirect(Evn.getString(Constant.OPENAPI_LOGIN_URL));
            return false;
        }
        if (null == request.getSession().getAttribute(OpenApiConstants.DEVELOPER_SESSION)) {
            String loginUrl = Evn.getString(Constant.OPENAPI_LOGIN_URL);
            if (null == request.getHeader("X-Requested-With")) {
                response.sendRedirect(loginUrl);
            }
            else {
                ServletUtils.print(response,
                        Json.toJSONString(new ResultDto().setSuccess(false).setCode("-2").setMsg(loginUrl)));
            }
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse, Object obj,
            ModelAndView modelandview) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object obj, Exception exception)
            throws Exception {

    }

    public String[] getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(String[] whiteList) {
        this.whiteList = whiteList;
    }

}
