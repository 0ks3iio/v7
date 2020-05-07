package net.zdsoft.szxy.operation.inner.controller;

import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.operation.inner.dto.UserInfoUpdater;
import net.zdsoft.szxy.operation.inner.dto.UserPasswordUpdater;
import net.zdsoft.szxy.operation.inner.entity.OpUser;
import net.zdsoft.szxy.operation.inner.service.OpUserService;
import net.zdsoft.szxy.operation.security.CurrentUser;
import net.zdsoft.szxy.operation.security.SecurityUser;
import net.zdsoft.szxy.plugin.mvc.Response;
import net.zdsoft.szxy.utils.crypto.PasswordUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Optional;

/**
 * @author shenke
 * @since 2019/4/16 下午5:01
 */
@Controller
@RequestMapping(value = "/operation/setting")
@Secured(SecurityUser.LOGINED)
public class UserSettingController {

    @Resource
    private OpUserService opUserService;

    @RequestMapping(
            value = "/index",
            method = RequestMethod.GET
    )
    public String execute() {
        return "/inner/user-setting/user-setting-index.ftl";
    }

    @GetMapping(value = "password")
    public String doPasswordSet() {
        return "/inner/user-setting/user-setting-password.ftl";
    }


    @Record(type = RecordType.URL)
    @GetMapping(value = "info")
    public String doInfoSet(@CurrentUser("id") String userId, Model model) {
        Optional<OpUser> user = opUserService.getUserById(userId);
        model.addAttribute("opUser", user.orElseThrow(() -> new RuntimeException("该用户不存在")));
        return "/inner/user-setting/user-setting-info.ftl";
    }

    @Record(type = RecordType.URL)
    @ResponseBody
    @PostMapping(value = "info")
    public Response doUpdateInfo(@CurrentUser("id") String id, @Valid UserInfoUpdater updater,
                                 BindingResult errors) {

        Optional<OpUser> optional = opUserService.getUserById(id);
        if (optional.isPresent()) {
            OpUser user = optional.get();
            user.setSex(updater.getSex());
            user.setRealName(updater.getRealName());
            user.setPhone(updater.getMobilePhone());
            user.setEmail(updater.getEmail());
            opUserService.save(user);
            return Response.ok().message("用户信息更新成功").build();
        }
        return Response.error().message("当前用户已不存在").build();
    }

    @Record(type = RecordType.URL)
    @ResponseBody
    @PostMapping(value = "password")
    public Response doUpdatePassword(@CurrentUser("id") String userId, @Valid UserPasswordUpdater updater,
                                     BindingResult errors) {
        if (!StringUtils.equals(updater.getNewPassword1(), updater.getNewPassword2())) {
            return Response.error().message("新密码不一致").build();
        }
        if (!checkPassword(updater.getPassword(), userId)) {
            return Response.error().message("原密码错误").build();
        } else {
            opUserService.updatePassword(userId, updater.getNewPassword1());
            return Response.ok().message("密码更新成功").build();
        }
    }

    private boolean checkPassword(String password, String userId) {
        Optional<OpUser> optional =  opUserService.getUserById(userId);
        if (optional.isPresent()) {
            return StringUtils.equals(PasswordUtils.decode(optional.get().getPassword()), password);
        }
        throw new RuntimeException("当前登录用户不存在");
    }

    @ResponseBody
    @Record(type = RecordType.URL)
    @PostMapping(value = "/checkPassword")
    public Response doCheckPassword(String password, @CurrentUser("id") String userId) {

        if (checkPassword(password, userId)) {
            return Response.ok().data("valid", true).build();
        }
        return Response.ok().data("valid", false).message("原密码错误").build();
    }
}
