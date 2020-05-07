/* 
 * @(#)SuperAdminAction.java    Created on 2017-3-31
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.ServletUtils;

@Controller
@RequestMapping("/superAdmin")
public class SuperAdminAction extends BaseAction {

    @Autowired
    private UserRemoteService userRemoteService;

    @RequestMapping("")
    public String login() {
        return "/system/superAdmin/login.ftl";
    }

    @RequestMapping("logout")
    public String logout(HttpSession httpSession, HttpServletRequest request) {
        if (httpSession != null) {
            // del redis
        	httpSession.invalidate();
        }
        return "redirect:/superAdmin";
    }

    @RequestMapping("/index")
    public String index(ModelMap map, HttpSession httpSession, HttpServletRequest request) {
        if (httpSession == null) {
            return "redirect:/superAdmin";
        }
        return "/system/superAdmin/index.ftl";
    }

    @ResponseBody
    @RequestMapping("/verify")
    @ControllerInfo("校验账号密码")
    public String verify(String username, String password, HttpSession httpSession) {
        // 调用basedata-remote 查询超管用户密码
        User user = SUtils.dc(
                userRemoteService.findOneBy(new String[] { "username", "isDeleted", "ownerType" }, new Object[] {
                        username, 0, User.OWNER_TYPE_SUPER }), User.class);
        if (user == null) {
            return error("请输入正确的超管账号");
        }
        if (!(PWD.decode(user.getPassword()).equals(password))) {
            return error("密码不正确");
        }

        initLoginInfo(httpSession, user);

        return success("登录成功");
    }

    private void initLoginInfo(HttpSession httpSession, User user) {
        if (httpSession == null) {
        	httpSession = getSession();
        }
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setOwnerType(user.getOwnerType());
        loginInfo.setUnitId(user.getUnitId());
        loginInfo.setUserId(user.getId());
        loginInfo.setOwnerId(user.getOwnerId());
        loginInfo.setUserName(user.getUsername());
        loginInfo.setRealName(user.getRealName());
        ServletUtils.setLoginInfo(httpSession, loginInfo);
    }
}
