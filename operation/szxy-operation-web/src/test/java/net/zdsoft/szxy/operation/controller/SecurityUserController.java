package net.zdsoft.szxy.operation.controller;

import net.zdsoft.szxy.operation.security.CurrentUser;
import net.zdsoft.szxy.operation.security.SecurityUser;
import net.zdsoft.szxy.plugin.mvc.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author shenke
 * @since 2019/1/11 下午4:29
 */
@Controller
@RequestMapping("/operation/security-user/test")
public class SecurityUserController {

    @ResponseBody
    @GetMapping("")
    public Response execute(@CurrentUser SecurityUser securityUser,
                            @RequestParam String dataKey) {
        return Response.ok().data(dataKey, securityUser).build();
    }
}
