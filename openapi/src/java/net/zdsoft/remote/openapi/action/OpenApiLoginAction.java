package net.zdsoft.remote.openapi.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.ControllerException;
import net.zdsoft.framework.config.OpenApiSession;
import net.zdsoft.framework.entity.OpenApiLoginInfo;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.remote.openapi.entity.Developer;
import net.zdsoft.remote.openapi.service.DeveloperService;

@Controller
@RequestMapping(value = { "/openapiSup/home/" })
public class OpenApiLoginAction extends OpenApiBaseAction {

    @Resource
    private DeveloperService developerService;

    @ControllerInfo("进入开发者登录页")
    @RequestMapping("/login/page")
    public String showLogin(ModelMap map, HttpSession httpSession) {

        return "/openapiOffer/homepage/login.ftl";
    }

    @ControllerInfo("进入开发者注册页")
    @RequestMapping("/regist/page")
    public String showRegist(ModelMap map, HttpSession httpSession) {

        return "/openapiOffer/homepage/regist.ftl";
    }

    @ResponseBody
    @ControllerInfo("注册开发者")
    @RequestMapping("/regist/save/page")
    public String showRegistSave(Developer user, String password, HttpServletRequest request,
            HttpServletResponse response, HttpSession httpSession, ModelMap map) {
        try {
            String id = UuidUtils.generateUuid();
            user.setId(id);
            user.setTicketKey(UuidUtils.generateUuid());
            developerService.save(user);
            initLoginInfo(httpSession, user);
        }
        catch (Exception e) {
            e.printStackTrace();
            return error("操作失败");
        }
        return success("注册成功");
    }

    @ResponseBody
    @ControllerInfo("登入开发者平台")
    @RequestMapping("/loginUser/page")
    public String showLoginUser(String username, String password, HttpServletRequest request,
            HttpServletResponse response, HttpSession httpSession, ModelMap map) {
        Developer user = developerService.findByUsername(username);
        if (user == null) {
            throw new ControllerException("username", "找不到此用户！");
        }
        String pwd = user.getPassword();
        if (StringUtils.length(pwd) == 64) {
            pwd = PWD.decode(pwd);
        }
        if (StringUtils.equalsIgnoreCase(pwd, password)) {
            initLoginInfo(httpSession, user);
            return success("登陆成功,跳转中……");
        }
        else {
            throw new ControllerException("password", "密码错误！");
        }
    }

    private void initLoginInfo(HttpSession httpSession, Developer user) {
        OpenApiSession session = OpenApiSession.get(httpSession.getId());
        OpenApiLoginInfo loginInfo = new OpenApiLoginInfo();
        loginInfo.setUserId(user.getId());
        loginInfo.setTicketKey(user.getTicketKey());
        session.setLoginInfo(loginInfo);
    }

    @ControllerInfo("进入开发者首页")
    @RequestMapping("/index/page")
    public String showIndex(ModelMap map, HttpSession httpSession) {

        return "/openapiOffer/homepage/index.ftl";
    }

    @RequestMapping("/logout/page")
    @ControllerInfo("登出系统")
    public String showLogout(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession,
            ModelMap map) {
        OpenApiSession session = OpenApiSession.get(httpSession.getId());
        session.invalidate();
        httpSession.invalidate();
        return "redirect:/openapiSup/home/login/page";
    }

}
