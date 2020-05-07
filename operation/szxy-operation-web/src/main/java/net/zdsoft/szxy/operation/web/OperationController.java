package net.zdsoft.szxy.operation.web;

import net.zdsoft.szxy.operation.inner.permission.entity.Module;
import net.zdsoft.szxy.operation.inner.permission.service.InnerPermissionService;
import net.zdsoft.szxy.operation.security.CurrentUser;
import net.zdsoft.szxy.operation.security.SecurityUser;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;

/**
 * @author shenke
 * @since 2019/1/30 下午1:49
 */
@Controller
@RequestMapping("")
@Secured(SecurityUser.LOGINED)
public class OperationController {

    @Resource
    private InnerPermissionService innerPermissionService;

    @RequestMapping(
            value = "",
            method = {RequestMethod.GET, RequestMethod.POST}
    )
    public String execute(@CurrentUser SecurityUser user,
                          Model model) {
        //根据模块名称进行排序
        List<Module> moduleList = innerPermissionService.getModuleByUserId(user.getId());
        moduleList.sort(Comparator.comparing(Module::getName));

        model.addAttribute("modules", moduleList);
        model.addAttribute("realName", user.getRealName());
        model.addAttribute("sex", user.getSex());
        return "/operationIndex.ftl";
    }
}
