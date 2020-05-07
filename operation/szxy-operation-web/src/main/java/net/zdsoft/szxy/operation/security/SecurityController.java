package net.zdsoft.szxy.operation.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author shenke
 * @since 2019/2/13 下午2:23
 */
@Controller
@RequestMapping("/operation/security/")
public class SecurityController {

    public static final String ACCESS_DENIED_PAGE = "/operation/security/access-denied";
    public static final String PAGE_NOT_FOUND_PAGE = "/operation/security/page-not-found";

    @RequestMapping("/access-denied")
    public String accessDenied() {

        return "";
    }

    @RequestMapping("/page-not-found")
    public String pageNotFound() {

        return "";
    }


}
