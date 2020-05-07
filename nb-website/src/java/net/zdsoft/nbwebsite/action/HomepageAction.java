package net.zdsoft.nbwebsite.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.ControllerException;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.config.Session;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.nbwebsite.constant.WebsiteConstants;
import net.zdsoft.passport.entity.Account;
import net.zdsoft.passport.exception.PassportException;
import net.zdsoft.passport.service.client.PassportClient;
import net.zdsoft.system.dto.server.ModelDto;
import net.zdsoft.system.dto.server.SubSystemDto;
import net.zdsoft.system.entity.server.Model;
import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.entity.server.SubSystem;
import net.zdsoft.system.entity.user.Role;
import net.zdsoft.system.entity.user.UserRole;
import net.zdsoft.system.remote.service.ModelRemoteService;
import net.zdsoft.system.remote.service.RolePermissionRemoteService;
import net.zdsoft.system.remote.service.RoleRemoteService;
import net.zdsoft.system.remote.service.ServerRemoteService;
import net.zdsoft.system.remote.service.SubSystemRemoteService;
import net.zdsoft.system.remote.service.UserRoleRemoteService;

@Controller
@RequestMapping(value = { "/homepage", "/fpf" })
public class HomepageAction extends BaseAction {

    @Autowired
    private UserRemoteService userRemoteService;
    @Autowired
    private TeacherRemoteService teacherRemoteService;
    @Autowired
    private UnitRemoteService unitRemoteService;
    @Autowired
    private SubSystemRemoteService subSystemRemoteService;
    @Autowired
    private ModelRemoteService modelRemoteService;

    @ResponseBody
    @RequestMapping("/common/decodePwd")
    public String decodePwd(String pwd) {
        return PWD.decode(pwd);
    }

    @RequestMapping(value = { "/login/invalidate.action", "/login/invalidate" })
    public String invalidatePassport(String ticket, String auth, HttpServletRequest request,
                                     HttpServletResponse response, HttpSession httpSession, ModelMap map) {
        if (StringUtils.isBlank(ticket)) {
            return showLogout(request, response, httpSession, map);
        }
        String sessionId = RedisUtils.get("session.id.by.ticket.id." + ticket);
        if (StringUtils.isNotBlank(sessionId)) {
            Session session = Session.get(sessionId);
            if (session != null) {
                session.invalidate();
                httpSession.invalidate();
                return "redirect:/homepage/login/page";
            }
        }
        return "redirect:/homepage/login/page";
    }

    @RequestMapping(value = { "/login/verify.action", "/login/verify" })
    public String verifyPassport(String ticket, String auth, HttpSession httpSession, ModelMap map) {
        Account account;
        try {
            if (StringUtils.isBlank(ticket) || StringUtils.isBlank(auth)) {
                map.put("showFramework", true);
                map.put("errorMsg", "Ticket以及Auth信息不能为空，请确认访问方式是否正确！");
                return "/fw/homepage/error.ftl";
            }
            account = PassportClient.getInstance().checkTicket(ticket);
            RedisUtils.set("session.id.by.ticket.id." + ticket, httpSession.getId(), RedisUtils.TIME_ONE_WEEK);
            Session session = Session.get(httpSession.getId());
            session.removeAttribute("ticket");
            session.setAttribute("ticket", ticket);
            session.setAttribute("sessionId", httpSession.getId());
            String username = account.getUsername();
            User user = SUtils.dc(userRemoteService.findByUsername(username), User.class);
            if (user == null) {
                map.put("showFramework", true);
                map.put("errorMsg", "找不到对应的用户，请确认用户名[" + username + "]在系统中存在且状态正常！");
                return "/fw/homepage/error.ftl";
            }
            initLoginInfo(httpSession, user);
        } catch (PassportException e) {
            e.printStackTrace();
        }
        return "redirect:/homepage/login/page";
    }

    @RequestMapping("/logout/page")
    @ControllerInfo("登出系统")
    public String showLogout(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession,
                             ModelMap map) {
        Session session = Session.get(httpSession.getId());
        String ticket = session.getAttribute("ticket", String.class);

        session.invalidate();
        httpSession.invalidate();
        if (StringUtils.isNotBlank(ticket) && Evn.getBoolean("connection_passport")) {
            try {
                PassportClient.getInstance().invalidate(ticket);
                // 如果未置统一的登出地址，则登出到默认的地址（~）
                String logOutUrl = Evn.getLogOutUrl();
                System.out.println("[获取统一登出Url:" + logOutUrl + "]");
                String url = PassportClient.getInstance().getLogoutURL(ticket,
                        StringUtils.isBlank(logOutUrl) ? Evn.getWebUrl() : logOutUrl);
                response.sendRedirect(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }
        return "redirect:/homepage/login/page";
    }

    @RequestMapping("/login/page")
    @ControllerInfo("进入首页")
    public String showLogin(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession,
                            ModelMap map) {
        Session session = Session.get(httpSession.getId());
        if (session.getLoginInfo() != null) {
            return "redirect:/homepage/index/page";
        }
        map.put("showFramework", true);
        map.put("showOnlyBase", true);
        return "/fw/homepage/loginMain.ftl";
    }

    @RequestMapping("/loginPage/page")
    @ControllerInfo("进入登陆页")
    public String showLoginPage(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession,
                                ModelMap map) {
        if (Evn.isPassport()) {
            String index = Evn.getWebUrl();
            String s = PassportClient.getInstance().getLoginURL(index, request.getContextPath());
            try {
                response.sendRedirect(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Session session = Session.get(httpSession.getId());
        if (session != null && session.getLoginInfo() != null) {
            return "redirect:/homepage/index/page";
        }
        map.put("showFramework", true);
        return "/fw/homepage/login.ftl";
    }

    @ResponseBody
    @RequestMapping("/loginUser/page")
    @ControllerInfo("登入系统")
    public String showLoginUser(String username, String password, HttpServletRequest request,
                                HttpServletResponse response, HttpSession httpSession, ModelMap map) {
        User user = SUtils.dc(userRemoteService.findByUsername(username), User.class);
        if (user == null) {
            throw new ControllerException("username", "找不到此用户！");
        }
        String pwd = user.getPassword();
        if (StringUtils.length(pwd) == 64)
            pwd = PWD.decode(pwd);
        if (StringUtils.equalsIgnoreCase(pwd, password)) {
            initLoginInfo(httpSession, user);
            return success("登陆成功,跳转中……");
        } else {
            throw new ControllerException("password", "密码错误！");
        }
    }

    @Autowired private RoleRemoteService roleRemoteService;
    @Autowired private UserRoleRemoteService userRoleRemoteService;
    @Autowired private RolePermissionRemoteService rolePermissionRemoteService;
    @Autowired private ServerRemoteService serverRemoteService;

    @ResponseBody
    @RequestMapping("/verify/login")
    public String verifyLogin(String username,String password,ModelMap map){
        try {
            boolean isContainSystem = Boolean.FALSE.booleanValue();
            boolean isContainSitedata = Boolean.FALSE.booleanValue();
            if (StringUtils.isNotBlank(password)) {
                Account account = PassportClient.getInstance().queryAccountByUsername(username);
                if(account == null){
                    return error("用户不存在");
                }
                if(!StringUtils.equals(PWD.decode(account.getPassword()),password)){
                    return error("密码错误");
                }
            }

            User user = SUtils.dc(userRemoteService.findByUsername(username), User.class);
            if (user == null) {
                return error("用户不存在");
            }
            //TODO 非空判断
            List<UserRole> userRoles = SUtils.dt(userRoleRemoteService.findByUserId(user.getId()), new TypeReference<List<UserRole>>() {});
            List<String> roleIds = EntityUtils.getList(userRoles, "roleId");
            List<Role> roles = SUtils.dt(roleRemoteService.findByIds(roleIds.toArray(new String[0])), new TypeReference<List<Role>>() {});
            for (Role role : roles) {
                if(StringUtils.contains(role.getSubSystem(), WebsiteConstants.SYSTEM_SYSTEM_ID)){
                    isContainSystem = Boolean.TRUE.booleanValue();
                }
                if(StringUtils.contains(role.getSubSystem(),WebsiteConstants.SYSTEM_SITEDATA_ID)){
                    isContainSitedata = Boolean.TRUE.booleanValue();
                }
                if(isContainSitedata && isContainSystem){
                    break;
                }
            }

            map.put("containSystem",isContainSystem);
            map.put("containSitedata",isContainSitedata);
            map.put("success",Boolean.TRUE.booleanValue());
            Server server = SUtils.dt(serverRemoteService.findByServerCode(WebsiteConstants.SYSTEM_SYSTEM_CODE), new TypeReference<Server>(){});
            map.put("systemContext",StringUtils.equalsIgnoreCase(server.getContext(),"/")?"0":"1");
            map.put("websiteContext",getRequest().getContextPath().equals("/")?"0":"1");
            return JSONUtils.toJSONString(map);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    private void initLoginInfo(HttpSession httpSession, User user) {
        Session session = Session.get(httpSession.getId());
        LoginInfo loginInfo = new LoginInfo();
        if (user.getOwnerType() == 2) {
            Teacher teacher = SUtils.dc(teacherRemoteService.findById(user.getOwnerId()), Teacher.class);
            if (teacher != null) {
                loginInfo.setOwnerId(teacher.getId());
                loginInfo.setDeptId(teacher.getDeptId());
            }
        }
        loginInfo.setOwnerType(user.getOwnerType());
        loginInfo.setUnitId(user.getUnitId());
        loginInfo.setUserId(user.getId());
        Unit unit = SUtils.dc(unitRemoteService.findById(user.getUnitId()), Unit.class);
        if (unit != null) {
            loginInfo.setUnitClass(unit.getUnitClass());
        }
        session.setLoginInfo(loginInfo);
    }

    @RequestMapping("/index/page")
    @ControllerInfo("显示首页")
    public String showIndex(ModelMap map, HttpSession httpSession) {
        map.addAttribute("showNotice", false);
        map.addAttribute("showStat", false);
        map.addAttribute("showMessage", false);
        map.addAttribute("showTodo", false);
        map.addAttribute("showInfo", true);
        // map.addAttribute("showSidebarCollapse", false);
        // map.addAttribute("showSidebarShortcuts", false);
        map.addAttribute("showBreadcrumb", true);
        // map.addAttribute("showSearch", false);
        map.addAttribute("showHeader", true);
        map.addAttribute("showTip", false);
        // map.addAttribute("showSetting", false);
        return "/fw/homepage/index.ftl";
    }

    @RequestMapping("/cus/info/list/page")
    @ControllerInfo("显示信息条")
    public String doInfo(ModelMap map, HttpSession httpSession) {
        Session session = getSession(httpSession);
        LoginInfo loginInfo = session.getLoginInfo();
        map.put("user",SUtils.dc(userRemoteService.findById(loginInfo.getUserId()), User.class));
        String welcome = "你好";
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        if (hour >= 12) {
            if (hour > 22)
                welcome = "夜深了";
            else if (hour >= 18)
                welcome = "晚上好";
            else
                welcome = "下午好";
        } else {
            if (hour < 3)
                welcome = "夜深了";
            else if (hour < 12) {
                welcome = "上午好";
            } else
                welcome = "中午好";
        }

        map.put("welcome", welcome);
        return "/fw/homepage/cus/nf-info.ftl";
    }

    @RequestMapping("/cus/nav/list/page")
    @ControllerInfo("显示模块列表")
    public String doNav(ModelMap map, HttpSession httpSession, HttpServletRequest request) {
        Session session = getSession(httpSession);
        List<SubSystem> subSystems = SUtils.dt(subSystemRemoteService.findAll(), new TR<List<SubSystem>>(){});
        Map<Integer, SubSystemDto> subSystemMap = new HashMap<Integer, SubSystemDto>();
        for (SubSystem sub : subSystems) {
            Integer id = sub.getIntId();
            SubSystemDto dto = new SubSystemDto();
            dto.setSubSystem(sub);
            subSystemMap.put(id, dto);
        }
        List<Model> models = session.getAttribute("models", new TypeReference<List<Model>>() {
        });
        if (models == null) {
            models = SUtils.dt(modelRemoteService.findByUserId(session.getLoginInfo().getUserId()), new TypeReference<List<Model>>(){});
            session.setAttribute("models", models);
        }
        Map<Integer, ModelDto> modelMap = new HashMap<Integer, ModelDto>();
        for (Model model : models) {
            if ("7".equals(model.getVersion())) {
                ModelDto dto = new ModelDto();
                String url = model.getUrl();
                if (!StringUtils.startsWith(url, "http://") && !StringUtils.startsWith(url, "/")) {
                    model.setUrl(request.getContextPath() + "/" + url);
                }
                dto.setModel(model);
                modelMap.put(model.getIntId(), dto);
            }
        }
        Set<SubSystemDto> validateSystems = new HashSet<SubSystemDto>();
        for (Model model : models) {
            if ("7".equals(model.getVersion())) {
                ModelDto modelDto = modelMap.get(model.getIntId());
                Integer subSystemId = model.getSubSystem();
                SubSystemDto subSystem = subSystemMap.get(subSystemId);
                if (subSystem == null)
                    continue;
                validateSystems.add(subSystem);
                Integer parentId = model.getParentId();
                if (parentId == Model.PARENT_ID_DIRECT_SUBSYSTEM)
                    subSystem.addModelDto(modelDto);

                ModelDto rm = modelMap.get(parentId);
                if (rm != null) {
                    rm.addModelDto(modelDto);
                }
            }
        }
        List<SubSystemDto> subs = new ArrayList<SubSystemDto>();
        for (SubSystemDto sub : validateSystems) {
            Integer parentId = sub.getSubSystem().getParentId();
            if (parentId != null && parentId.intValue() > 0) {
                SubSystemDto parent = subSystemMap.get(parentId);
                if (parent != null)
                    parent.addSubSystemDto(subSystemMap.get(parentId));
            } else {
                subs.add(sub);
            }
        }
        map.addAttribute("subSystemDtos", subs);
        return "/fw/homepage/cus/nf-navList.ftl";
    }

    @RequestMapping("/cus/{ftlName}/page")
    @ControllerInfo("显示其他信息")
    public String doCus(@PathVariable String ftlName, ModelMap map, HttpSession httpSession) {
        return "/fw/homepage/cus/" + ftlName + ".ftl";
    }
}
