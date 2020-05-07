package net.zdsoft.szxy.operation.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author shenke
 * @since 2019/2/13 下午2:11
 */
public class OpAccessDeniedHandler implements AccessDeniedHandler {

    private Logger logger = LoggerFactory.getLogger(OpAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                       AccessDeniedException e) throws IOException, ServletException {
        SecurityUser userDetails = getPrincipal();
        if (userDetails != null) {
            logger.warn("{} try to access [{}] denied", userDetails.getUsername(),
                    httpServletRequest.getRequestURL().toString());
        }
        if (isAjax(httpServletRequest)) {
            //AJAX做统一拦截处理
            httpServletResponse.setStatus(SzxyResponseStatusCode.NO_AUTHORIZATION.getCode());
            httpServletResponse.getOutputStream().write("权限不足".getBytes(StandardCharsets.UTF_8));
        }
        else {
            if (!httpServletResponse.isCommitted()) {
                httpServletRequest.getRequestDispatcher(SecurityController.ACCESS_DENIED_PAGE)
                        .forward(httpServletRequest, httpServletResponse);
            }
        }
    }

    private SecurityUser getPrincipal() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return null;
        }
        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof SecurityUser)) {
            return null;
        }
        return (SecurityUser) principal;
    }

    private boolean isAjax(HttpServletRequest request) {
        String ajaxHeader = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(ajaxHeader);
    }
}
