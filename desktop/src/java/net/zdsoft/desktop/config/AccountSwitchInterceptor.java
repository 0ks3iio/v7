package net.zdsoft.desktop.config;


import net.zdsoft.desktop.action.LoginAction;
import org.springframework.ui.ModelMap;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author shenke
 * @since 2017.07.14
 */
public class AccountSwitchInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        if ( uri.contains("switch/account") ){
            if ((((HandlerMethod)handler).getMethod().equals(LoginAction.class.getMethod("switchAccount", String.class, String.class, ModelMap.class)))) {
                if ( request.getParameter("userName") == null || request.getParameter("password") == null ) {
                    response.sendRedirect("/homepage/loginPage/page");
                    return false;
                }
                return true;
            }else {
                return false;
            }
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
