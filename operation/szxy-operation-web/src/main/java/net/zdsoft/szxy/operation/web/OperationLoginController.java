package net.zdsoft.szxy.operation.web;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author shenke
 * @since 2019/1/9 下午3:29
 */
@Controller
@RequestMapping("/operation")
public class OperationLoginController {

    @RequestMapping(value = "login")
    public String execute(Model model, HttpServletRequest request) {
        AuthenticationException exception
                = (AuthenticationException) request.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        if (exception != null) {
            model.addAttribute("loginErrorMessage", exception.getMessage());
        }
        return "/opLogin.ftl";
    }
}