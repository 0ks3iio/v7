package net.zdsoft.szxy.operation.inner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author shenke
 * @since 2019/4/9 上午10:08
 */
@Controller
@RequestMapping("/operation/management")
public class ManagementController {


    @RequestMapping(
            value = "index",
            method = RequestMethod.GET
    )
    public String execute() {

        return "/inner/management/manage-index.ftl";
    }
}
