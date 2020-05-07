package net.zdsoft.szxy.operation.usermanage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author shenke
 * @since 2019/4/16 下午2:47
 */
@Controller
@RequestMapping("/operation/user/manage/page")
public class UserManageController {

    @GetMapping("/index")
    public String execute() {
        return "/usermanage/usermanage-index.ftl";
    }
}
