package net.zdsoft.desktop.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.CountOnlineTime;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Family;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.SysUserBind;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CountOnlineTimeRemoteService;
import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.remote.service.FamilyRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.SysUserBindRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.basedata.remote.utils.PJHeadUrlUtils;
import net.zdsoft.desktop.constant.DeployRegion;
import net.zdsoft.desktop.constant.DeskTopConstant;
import net.zdsoft.desktop.constant.WenXunConstant;
import net.zdsoft.desktop.dto.Classify;
import net.zdsoft.desktop.dto.DServerDto;
import net.zdsoft.desktop.entity.FunctionArea;
import net.zdsoft.desktop.entity.FunctionAreaUser;
import net.zdsoft.desktop.entity.UserSet;
import net.zdsoft.desktop.login.vo.LoginOptionDTO;
import net.zdsoft.desktop.service.FunctionAreaService;
import net.zdsoft.desktop.service.FunctionAreaUserService;
import net.zdsoft.desktop.utils.CodeUtils;
import net.zdsoft.desktop.utils.Protocol;
import net.zdsoft.desktop.utils.Utils;
import net.zdsoft.desktop.utils.Version;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.PassportClientUtils;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.SortUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.license.entity.LicenseInfo;
import net.zdsoft.passport.dto.AccountResult;
import net.zdsoft.passport.entity.Account;
import net.zdsoft.passport.exception.PassportException;
import net.zdsoft.remote.openapi.service.OpenApiOfficeService;
import net.zdsoft.system.dto.server.ModelDto;
import net.zdsoft.system.entity.LoginDomain;
import net.zdsoft.system.entity.ServerRegion;
import net.zdsoft.system.entity.config.SysOption;
import net.zdsoft.system.entity.server.Model;
import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.remote.dto.UnitServerClassify;
import net.zdsoft.system.remote.service.LicenseRemoteService;
import net.zdsoft.system.remote.service.LoginDomainRemoteService;
import net.zdsoft.system.remote.service.ModelRemoteService;
import net.zdsoft.system.remote.service.ServerClassifyRemoteService;
import net.zdsoft.system.remote.service.ServerRegionRemoteService;
import net.zdsoft.system.remote.service.ServerRemoteService;
import net.zdsoft.system.remote.service.SmsRemoteService;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import net.zdsoft.system.remote.service.SystemIniRemoteService;
import net.zdsoft.system.remote.service.UserRoleRemoteService;
import net.zdsoft.system.service.sms.entity.ZDConstant;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * @author shenke
 * @since 2016/12/22 15:49
 */
@Controller
@RequestMapping(value = {"/homepage", "/desktop", "/{region}/homepage"})
public class HomePageAction extends DeskTopBaseAction {

    @Autowired
    private UnitRemoteService unitRemoteService;
    @Autowired
    private UserRemoteService userRemoteService;
    @Autowired
    private ModelRemoteService modelRemoteService;
    @Autowired
    private ServerRemoteService serverRemoteService;
    @Autowired
    private FunctionAreaUserService functionAreaUserService;
    @Autowired
    private FunctionAreaService functionAreaService;
    @Autowired
    private SysOptionRemoteService sysOptionRemoteService;
    @Autowired
    private TeacherRemoteService teacherRemoteService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private FamilyRemoteService familyRemoteService;
    @Autowired
    private SmsRemoteService smsRemoteService;
    @Autowired
    private SystemIniRemoteService systemIniRemoteService;
    @Autowired
    private UserRoleRemoteService userRoleRemoteService;
    @Autowired
    private ServerRegionRemoteService serverRegionRemoteService;
    @Autowired
    private LicenseRemoteService licenseRemoteService;
    @Autowired
    private ServerClassifyRemoteService serverClassifyRemoteService;
    @Autowired
    private LoginDomainRemoteService loginDomainRemoteService;
    @Autowired
    private DeptRemoteService deptRemoteService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private SysUserBindRemoteService sysUserBindRemoteService;
    @Autowired
    private CountOnlineTimeRemoteService countOnlineTimeRemoteService;

    private static final String USER_SETTING_IMG_CODE = "user_setting_img_code_";
    private static final String USER_SETTING_MSG_CODE = "user_setting_msg_code_";
    private static final String LOGIN_NAME = "logoName";

    private String getPassportURL() {
        return sysOptionRemoteService.isSecondUrl(getRequest().getServerName()) ?
                sysOptionRemoteService.findValue(net.zdsoft.system.constant.Constant.PASSPORT_SECOND_URL)
                : sysOptionRemoteService.findValue(net.zdsoft.system.constant.Constant.PASSPORT_URL);
    }

    @ControllerInfo(value = "进入首页")
    @RequestMapping("/index/page")
    public String execute(@RequestParam(name = "appId", required = false, defaultValue = "-1") int appId,
                          @RequestParam(name = "module", required = false, defaultValue = "-1") int moduleId,
                          HttpServletRequest request, ModelMap map) {
        HttpSession session = getSession();
       
        String domianName = null;
        String redisKey = "loginOption" + getRequest().getServerName();
        if (session != null && session.getAttribute("domainName") != null) {
            LoginDomain loginDomain = SUtils.dc(loginDomainRemoteService
                    .findByRegionAdmin(session.getAttribute("domainName")
                            .toString()), LoginDomain.class);
            if (loginDomain != null) {
                domianName = loginDomain.getRegionAdmin();
            }
            redisKey = redisKey + domianName;
        }
        LoginOptionDTO loginOptionDTO = RedisUtils.getObject(redisKey, LoginOptionDTO.class);
        map.put("favicon", BooleanUtils.toBoolean(RedisUtils.get("favicon")));
        map.put("owerType", getLoginInfo().getOwnerType());
        map.put("eis6_domain", UrlUtils
                .ignoreLastRightSlash(sysOptionRemoteService
                        .findValue(DeskTopConstant.EIS6_DOMAIN, new SysOption()).getNowValue()));
        map.put("eis5_domain", UrlUtils
                .ignoreLastRightSlash(sysOptionRemoteService
                        .findValue(DeskTopConstant.EIS5_DOMAIN, new SysOption()).getNowValue()));
        //是否强制弹出修改密码
        LoginInfo loginInfo = getLoginInfo();
        User user = userRemoteService.findOneObjectById(loginInfo.getUserId(), new String[]{"ownerType", "password", "username", "expireDate"});
        //TODO 
        deal4Password(user, map);
        //序列号提前提醒
        if (!Evn.isDevModel()) {
            if (Integer.valueOf(User.OWNER_TYPE_TEACHER).equals(user.getUserType())) {
                Unit unit = unitRemoteService.findTopUnitObject(loginInfo.getUnitId());
                boolean result = false;
                StringBuilder msg = new StringBuilder();
                if (unit != null && loginInfo.getUnitId().equals(unit.getId())) {
                    String aheadNotifyDayStr = sysOptionRemoteService.findValue("SYSTEM.EXPIRE.ALERT.ADVANCE.DAYS");
                    int aheadNotifyDay = 7;
                    if (aheadNotifyDayStr != null) {
                        aheadNotifyDay = NumberUtils.toInt(aheadNotifyDayStr);
                    }
                    LicenseInfo licenseInfo = SUtils.dc(licenseRemoteService.getLicenseInfo(), LicenseInfo.class);
                    result = licenseInfo.getExpireDate().before(DateUtils.addDay(new Date(), aheadNotifyDay));
                    if (result) {
                        String aheadNotifyMessageFormat = sysOptionRemoteService.findValue("SYSTEM.EXPIRE.ALERT.MESSAGE");
                        msg.append(String.format(aheadNotifyMessageFormat, DateUtils.date2String(licenseInfo.getExpireDate(), "yyyy-MM-dd")));
                    }
                }

                if (user.getExpireDate() != null
                        && org.apache.commons.lang3.time.DateUtils.truncatedCompareTo(user.getExpireDate(), new Date(), Calendar.DATE) >= 0) {
                    String val = systemIniRemoteService.findValue("USER.EXPIRE.ALERT.ADVANCE.DAYS");
                    int days = NumberUtils.toInt(val);
                    String dateStr = DateUtils.date2StringByDay(user.getExpireDate());
                    Date exd = DateUtils.string2Date(dateStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
                    int differentDays = differentDaysByMillisecond(new Date(), exd);
                    if (differentDays <= days) {
                        if (msg.length() > 0) {
                            msg.append("<br>");
                        }
                        msg.append("用户账号将在" + dateStr + "到期，届时将无法使用系统，请及时联系管理员进行更新");
                        result = true;
                    }
                }
                map.addAttribute("expireAheadNotify", result);
                map.addAttribute("expireAheadNotifyMessage", msg.toString());
            }
        }

        if(2 == user.getOwnerType()){
			String userId = user.getId();
			
			String sessionId = session.getId();
			CountOnlineTime countOnlineTimeExist = countOnlineTimeRemoteService.getCountOnlineTimeBySessId(sessionId);
			if(null==countOnlineTimeExist){				
				CountOnlineTime countOnlineTimeBefore = countOnlineTimeRemoteService.getCountOnlineTimeListByLastloginUserId(userId);
				if(null!=countOnlineTimeBefore && null==countOnlineTimeBefore.getLogoutTime()){
					long a = countOnlineTimeBefore.getLoginTime().getTime();
		            long b = System.currentTimeMillis();
		        	int c = (int)((b - a) / 1000);
		        	countOnlineTimeBefore.setLogoutTime(new Date());
		        	if(c>999999){//防止超出存储最大值
		        	    c=1800;
                    }
		        	countOnlineTimeBefore.setOnlineTime(c);
		        	countOnlineTimeRemoteService.save(countOnlineTimeBefore);
				}
				
				CountOnlineTime countOnlineTime = new CountOnlineTime();
				countOnlineTime.setUserId(userId);
				countOnlineTime.setSessionId(sessionId);
				countOnlineTime.setLogoutTime(null);
				countOnlineTime.setLoginTime(new Date());
				countOnlineTime.setOnlineTime(0);
				countOnlineTime.setUnitId(user.getUnitId());
				countOnlineTime.setId(UuidUtils.generateUuid());
				countOnlineTimeRemoteService.save(countOnlineTime);
			}
		}
        
        //判断是否屏蔽页面按钮
        isHideLoginOut(map);
        //西固单点没有权限进行提示
        isShowTips(map);
        //玉林修改密码屏蔽 需求调整基础数据走我们这边
        //isShowXgm(map);
        deal4Header(request, map, loginOptionDTO, user);
        doLoadSideBar(map, request.getSession(), appId, moduleId);
        map.put("sessionId", getRequest().getRequestedSessionId());
        return "/desktop/homepage/index.ftl";
    }

    public static int differentDaysByMillisecond(Date date1, Date date2) {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
        return days;
    }

    /**
     * 是否提示信息 （西固定制）
     *
     * @param map
     */
    private void isShowTips(ModelMap map) {
        String deployRegion = systemIniRemoteService.findValue(DeployRegion.SYS_OPTION_REGION);
        if (DeployRegion.DEPLOY_XIGU.equals(deployRegion)) {
            map.put("isShowTips", true);
        }
    }
    
    /**
     * 屏蔽修改密码
     * @param map
     */
    //private void isShowXgm(ModelMap map) {
    //    String deployRegion = systemIniRemoteService.findValue(DeployRegion.SYS_OPTION_REGION);
    //    if (DeployRegion.DEPLOY_YUlING.equals(deployRegion)) {
    //        map.put("isYulin", true);
    //    }
    //}
    /**
     * 是否隐藏退出按钮
     * 1先走配置，2根据各个地区的配置
     * @param map
     */
    private void isHideLoginOut(ModelMap map) {
    	String showLogOut = systemIniRemoteService.findValue("SYSTEM.LOGOUT");
    	if (StringUtils.isNotBlank(showLogOut)) {
    		map.put("isHide", "N".equals(showLogOut));
    	}
        String deployRegion = systemIniRemoteService.findValue(DeployRegion.SYS_OPTION_REGION);
        if (DeployRegion.DEPLOY_BINJIANG.equals(deployRegion)) {
            map.put("isHide", true);
        }
        //验证用户名是否是文轩拉取的用户
        isWXLogin(map);
    }

    private void deal4Header(HttpServletRequest request, ModelMap map, LoginOptionDTO loginOptionDTO, User user) {
        boolean isSuper = Integer.valueOf(User.OWNER_TYPE_SUPER).equals(getLoginInfo().getOwnerType());
        Unit unit = unitRemoteService.findOneObjectById(getLoginInfo().getUnitId(), new String[]{"unitName"});

        String logoUrl = "#";
        String logoPic = "";
        if (isSuper) {
            map.put(LOGIN_NAME, "Deploy");
        } else {
            String region = systemIniRemoteService.findValue(DeployRegion.SYS_OPTION_REGION);
            String title = loginOptionDTO != null ? loginOptionDTO.getCommonHeader() : null;
            if (StringUtils.isBlank(title)) {
                title = DeployRegion.INDEX_TITLE_MAP.get(region);
            }
            map.put("title", StringUtils.defaultIfEmpty(title, DeployRegion.DEFAULT_INDEX_TITLE));
            if (DeployRegion.DEPLOY_DAQIONG.equals(region)) {
                title = StringUtils.isBlank(title) ? DeployRegion.DEFAULT_INDEX_TITLE
                        : title;
                map.put(LOGIN_NAME, title + "/" + unit.getUnitName());
            } else if (DeployRegion.DEPLOY_TIANCHANG.equals(region)) {
                Unit root = SUtils.dc(unitRemoteService.findTopUnit(unit.getRootUnitId()), Unit.class);
                String prefix = "";
                if (root != null && !root.getId().equals(unit.getId())) {
                    prefix = "天长市智慧教育管理平台—";
                }
                map.put(LOGIN_NAME, prefix + unit.getUnitName());
            } else {
                map.put(LOGIN_NAME, unit.getUnitName());
            }
//            if(DeployRegion.DEPLOY_XINGYUN.equals(region)){
//            	logoPic = XyConstant.XY_LOGIN_IMAGE;
//            }
        }
        map.put("logoUrl", logoUrl);
        map.put("logoPic", logoPic);
        List<String> appList = Lists.newArrayList();
        if (!isSuper && !Constant.GUID_ZERO.equals(getLoginInfo().getOwnerId())) {
            if (Integer.valueOf(User.OWNER_TYPE_TEACHER).equals(getLoginInfo().getOwnerType())) {
                appList.add("message");
            }
            if (Objects.equals(User.OWNER_TYPE_TEACHER, user.getOwnerType())) {
                appList.add("addrBook");
            }
            if (!Integer.valueOf(User.OWNER_TYPE_STUDENT).equals(getLoginInfo().getOwnerType())) {
                appList.add("calendar");
            }
            appList.add("setting");
        }
        if (Constant.GUID_ZERO.equals(getLoginInfo().getOwnerId())) {
            appList.add("setting");
        }
        Object sessionAppId = getRequest().getSession().getAttribute("appId");
        if (sessionAppId != null) {
            if (NumberUtils.toInt(sessionAppId.toString()) != -1) {
                appList.clear();
            }
            ;
        }
        map.put("multiIdentity", multiIdentity());
        map.put("realName", user.getRealName());
        map.put("avatarUrl", PJHeadUrlUtils.getShowAvatarUrl(request.getContextPath(), user.getAvatarUrl(), getFileURL()));
        map.put("loginInfoUserId", getLoginInfo().getUserId());
        map.put("appList", appList);
    }

    private void deal4Password(User user, ModelMap map) {
        String oldPwd = user.getPassword();
        String regex = systemIniRemoteService.findValue(net.zdsoft.system.constant.Constant.SYSTEM_PASSWORD_STRONG);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(PWD.decode(oldPwd));
        //弱密码
        String deployRegion = systemIniRemoteService.findValue(DeployRegion.SYS_OPTION_REGION);
        if (!DeployRegion.DEPLOY_TIANCHANG.equals(deployRegion)) {
            map.put("isPop", matcher.matches());
        }
        String alert = sysOptionRemoteService.findValue("SYSTEM.PASSWORD.ALERT");

        String showUpdatePasswordStr = sysOptionRemoteService.findValue("UPDATE.OR.INIT.PASSWORD");
        if (StringUtils.isNotBlank(showUpdatePasswordStr)) {
            boolean allowUpdate = BooleanUtils.toBoolean(showUpdatePasswordStr, "1", "0");
            if (!allowUpdate) {
                map.put("isPop", false);
            }
        }
        map.put("passwordWarn", (StringUtils.isBlank(alert) ? "密码强度不够（建议使用数字、字母、符号组合）" : alert));
        map.put("passportUrl", getPassportURL());
        
        String pwdValidityPeriodDayStr = systemIniRemoteService.findValue(net.zdsoft.system.constant.Constant.PWD_VALIDITY_PERIOD);
        pwdValidityPeriodDayStr = StringUtils.isNotBlank(pwdValidityPeriodDayStr)?pwdValidityPeriodDayStr:"90";
        int pwdValidityPeriodDay = NumberUtils.toInt(pwdValidityPeriodDayStr);
        map.put("pwdValidityPeriodDay",pwdValidityPeriodDay);
        map.put("pwdValidityPeriod", false);
        if(user.getUpPwdDate() == null) {
        	return;
        }
        boolean result = false;
        result = new Date().before(DateUtils.addDay(user.getUpPwdDate(), pwdValidityPeriodDay));
        if(!result) {
        	map.put("pwdValidityPeriod", true);
        }
    }

    @ControllerInfo(value = "显示右侧桌面内容/点击桌面")
    @RequestMapping("/index/index-home")
    public String doLoadHome(ModelMap map) {
        boolean functionAreaUserSetOpen = isFunctionAreaUserSetOpen();
        String layout = getUserSetLayout();

        if (Constant.GUID_ZERO.equals(getLoginInfo().getOwnerId()) || new Integer(User.OWNER_TYPE_SUPER).equals(getLoginInfo().getOwnerType())) {
            layout = UserSet.LAYOUT_DEFAULT;
        }

        List<FunctionAreaUser> faus = functionAreaUserService.findByUserInfo(getUserId(), getLoginInfo().getUnitClass());
        if (CollectionUtils.isNotEmpty(faus)) {
            if (UserSet.LAYOUT_TWO2ONE.equals(layout)) {

                List<FunctionAreaUser> right = faus.stream().filter(t -> t.getLayoutType().equals(FunctionAreaUser.LAYOUT_TYPE_RIGHT))
                        .collect(Collectors.toList());

                SortUtils.ASC(right, "displayOrder");

                List<FunctionAreaUser> left = faus.stream().filter(t -> t.getLayoutType().equals(FunctionAreaUser.LAYOUT_TYPE_LEFT))
                        .collect(Collectors.toList());

                SortUtils.ASC(left, "displayOrder");
                map.put("oneFunctionAreaUsers", right);
                map.put("twoFunctionAreaUsers", left);
            } else {
                List<FunctionArea> fas = functionAreaService.findListByIdIn(EntityUtils.getArray(faus, FunctionAreaUser::getFunctionAreaId, String[]::new));
                if (Constant.GUID_ZERO.equals(getLoginInfo().getOwnerId())) {
                    fas = EntityUtils.filter2(fas, f -> f.getDataUrl().contains("showCommonUseApp2"));
                    faus.clear();
                    faus.add(functionAreaUserService.findOwnerByFunctionAreaId(getLoginInfo().getUserId(), fas.get(0).getId()));
                }
                if (Integer.valueOf(User.OWNER_TYPE_SUPER).equals(getLoginInfo().getOwnerType())) {
                    faus = Lists.newArrayList();
                }
                map.put("functionAreas", faus);
            }
        }
        map.put("functionSet", functionAreaUserSetOpen);
        map.put("layout", layout);
        return "/desktop/homepage/index-home.ftl";
    }

    @ControllerInfo(ignoreLog = ControllerInfo.LOG_FORCE_IGNORE, value = "显示头部{type}信息")
    @RequestMapping("/index/header/{type}/page")
    public String doLoadHeaderApp(@PathVariable("type") String type,
                                  ModelMap map, HttpServletRequest request, HttpSession httpSession) {

        if (StringUtils.equals(type, "info")) {
        }
        if (StringUtils.equals(type, "addrBook")) {
            doLoadAddressBook(map, request);
            return "/desktop/homepage/cus/nf-addrBook.ftl";
        }
        if (StringUtils.equals(type, "message")) {
            doLoadMessage(map);
            return "/desktop/homepage/cus/nf-message.ftl";
        }

        if (StringUtils.equals(type, "code")) {
            return "redirect:/desktop/index/header/code/page";
        }

        return "/desktop/homepage/cus/" + "nf-" + type + ".ftl";
    }

    public String doLoadAddressBook(ModelMap map, HttpServletRequest request) {
        // 先取缓存
        try {

            List<JSONObject> results = RedisUtils.getObject("address.book." + getLoginInfo().getUserId(), new TypeReference<List<JSONObject>>() {
            });
            List<String> uNames = RedisUtils.getObject("address.book.unames." + getLoginInfo().getUserId(), new TypeReference<List<String>>() {
            });
            if (CollectionUtils.isNotEmpty(results) && CollectionUtils.isNotEmpty(uNames)) {
                map.put("adList", results);
                map.put("userNames", uNames);
                return "/desktop/homepage/cus/nf-addrBook.ftl";
            }

            OpenApiOfficeService openApiOfficeService = this.<OpenApiOfficeService>getDubboService("openApiOfficeService");
            if (openApiOfficeService == null) {
                return "/desktop/homepage/cus/nf-addrBook.ftl";
            }

            JSONObject remoteMsgParam = new JSONObject();
            remoteMsgParam.put("userId", getLoginInfo().getUserId());
            remoteMsgParam.put("groupType", DeskTopConstant.ADDBOOK_DEPT_GROUP);
            String msg = openApiOfficeService.remoteAddressBookDetails(remoteMsgParam.toJSONString());
            List<JSONObject> adList = SUtils.dt(msg, JSONObject.class);
            // 得到真实姓名集合（userNames） 和 用户的owenId集合
            List<String> userNames = new ArrayList<String>();
            List<String> userOwenIds = new ArrayList<String>();
            for (JSONObject jsonObject : adList) {
                String userObjects = jsonObject.getString("userDetails");
                List<JSONObject> userList = SUtils.dt(userObjects, JSONObject.class);
                for (JSONObject jsonObject2 : userList) {
                    userNames.add(jsonObject2.getString("userName"));
                    userOwenIds.add(jsonObject2.getString("teacherId"));
                }
            }
            // 拼接头像url，并放入avatarUrl中
            List<User> userList = SUtils.dt(userRemoteService.findByOwnerIds(userOwenIds.toArray(new String[userOwenIds.size()])), User.class);
            for (User user : userList) {
                user.setAvatarUrl(PJHeadUrlUtils.getShowAvatarUrl(request.getContextPath(), user.getAvatarUrl(), getFileURL()));
            }
            Map<String, User> teacherIdUserMap = EntityUtils.getMap(userList, User::getOwnerId);
            // 把头像url放进adlist中
            for (JSONObject jsonObject : adList) {
                String userObjects = jsonObject.getString("userDetails");
                List<JSONObject> userList1 = SUtils.dt(userObjects, JSONObject.class);
                List<JSONObject> userList2 = new ArrayList<JSONObject>();
                for (JSONObject jsonObject2 : userList1) {
                    String dutyString = jsonObject2.getString("duty");
                    // 职务的截取，显示
                    String dutyShow = StringUtils.substring(dutyString, 0,
                            StringUtils.indexOf(dutyString, ",", StringUtils
                                    .indexOf(dutyString, ",") + 1));
                    jsonObject2.put("duty", dutyShow);
                    User user = teacherIdUserMap.get(jsonObject2.getString("teacherId"));
                    jsonObject2.put("avatarUrl", user != null ? user.getAvatarUrl() : "");
                    userList2.add(jsonObject2);
                }
                jsonObject.remove("userDetails");
                jsonObject.put("userDetails", userList2);
            }
            map.put("adList", adList);
            map.put("userNames", userNames);
            RedisUtils.setObject("address.book." + getLoginInfo().getUserId(), adList, 300);
            RedisUtils.setObject("address.book.unames" + getLoginInfo().getUserId(), userNames, 300);
        } catch (Exception e) {
            LOG.error("exception", ExceptionUtils.getStackTrace(e));

        }
        return "/desktop/homepage/cus/nf-addrBook.ftl";
    }

    @ControllerInfo(ignoreLog = ControllerInfo.LOG_FORCE_IGNORE, value = "加载二维码")
    @RequestMapping("/index/header/code/page")
    public String doLoadQrCode() {

        return "/desktop/homepage/cus/nf-code.ftl";
    }

    public String doLoadMessage(ModelMap map) {

        try {
            Model messageModel = SUtils.dc(modelRemoteService.findOneById(new Integer(69052), true), Model.class);
            Server server = SUtils.dc(serverRemoteService.findBySubId(messageModel.getSubSystem()), Server.class);

            String serverUrl = server != null ? sysOptionRemoteService.isSecondUrl(getRequest().getServerName()) ? server.getSecondUrl() : server.getUrl() : null;
            User user = SUtils.dc(userRemoteService.findOneById(getLoginInfo().getUserId(), true), User.class);

            String singleMessageUrl = UrlUtils.ignoreLastRightSlash(serverUrl) + "/" + messageModel.getUrl()
                    + "?desktopIn=1&{readType}=0";
            String showUrl = serverUrl + "/" + DeskTopConstant.UNIFY_LOGIN_URL + "?uid=" + user.getUsername() + "&"
                    + "url=" + singleMessageUrl;
            map.put("showUrl", showUrl);
            OpenApiOfficeService openApiOfficeService = this.<OpenApiOfficeService>getDubboService("openApiOfficeService");
            String msg = openApiOfficeService == null ? StringUtils.EMPTY : openApiOfficeService.remoteOfficeMsgDetails(getLoginInfo().getUserId());
            List<JSONObject> jsonObjects = SUtils.dt(msg, new TypeReference<List<JSONObject>>() {
            });
            int unReadNum = 0;
            if (jsonObjects != null) {
                for (JSONObject jsonObject : jsonObjects) {
                    unReadNum += Objects.equals("0", jsonObject.getString("isRead")) ? 1 : 0;
                }
            }
            map.put("unreadNum", unReadNum);

        } catch (Exception e) {
            log.error(e);
        }
        return "/desktop/homepage/cus/nf-message.ftl";
    }

    @ControllerInfo(ignoreLog = ControllerInfo.LOG_FORCE_IGNORE, value = "显示模块导航")
    @RequestMapping("/index/nav")
    public String doLoadSideBar(ModelMap map, HttpSession httpSession,
                                @RequestParam(name = "appId", required = false, defaultValue = "-1") int appId,
                                @RequestParam(name = "module", required = false, defaultValue = "-1") int moduleId) {

        httpSession.setAttribute("deployRegion", systemIniRemoteService.findValue(DeployRegion.SYS_OPTION_REGION));

    	String freemarkerPage = "/desktop/homepage/cus/nf-navList.ftl";
        map.put("moduleId", moduleId);
        List<DServerDto> serverDtos = RedisUtils.getObject(
                "navServerModelList." + getUserId() + "." + getSession().getId() + appId, RedisUtils.TIME_ONE_HOUR * 2,
                new TypeReference<List<DServerDto>>() {
                }, new RedisInterface<List<DServerDto>>() {
                    @Override
                    public List<DServerDto> queryData() {
                        return Lists.newArrayList();
                    }
                });
        List<Classify> classifies = RedisUtils.getObject("classifies." + getUserId() + "." + getSession().getId() + appId,
                RedisUtils.TIME_ONE_HOUR * 2,new TypeReference<List<Classify>>() {} , new RedisInterface<List<Classify>>() {
                    @Override
                    public List<Classify> queryData() {
                        return new ArrayList<>();
                    }
                });
        String username = getLoginInfo().getUserName();

        //超管，未绑定教师的用户不需要显示模块
        boolean notShowModel = Constant.GUID_ZERO.equals(getLoginInfo().getOwnerId())
                || "super".equalsIgnoreCase(username);
        map.put("secondUrl", sysOptionRemoteService.isSecondUrl(getRequest().getServerName()));
        User user = userRemoteService.findOneObjectById(getLoginInfo().getUserId(), new String[]{"userType", "ownerType"});
        map.put("topAdmin", user.getUserType() == null ? Boolean.FALSE : user.getUserType() == User.USER_TYPE_TOP_ADMIN);
        map.put("superAdmin", user.getOwnerType() == null ? Boolean.FALSE : user.getOwnerType() == User.OWNER_TYPE_SUPER);
        map.put("sessionId", getSession().getId());
        map.put("userName", username);
        if (appId == -1) {
            Object sessionAppId = getRequest().getSession().getAttribute("appId");
            if (sessionAppId != null) {
                appId = NumberUtils.toInt(sessionAppId.toString());
            } else {
                getRequest().getSession().setAttribute("appId", -1);
            }
        }
        map.put("hasClassifies", Boolean.valueOf(RedisUtils.get("hasClassifies")));
        map.put("classifies", classifies);
        if (CollectionUtils.isNotEmpty(serverDtos)
                || notShowModel) {
            map.put("serverDtos", serverDtos);
            return freemarkerPage;
        }


        //得到多域名的设置
        Map<Integer, ServerRegion> serverDomainMap = getDomainServer();

        HttpSession session = getSession();
        List<Model> models = (List<Model>) session.getAttribute(
                DeskTopConstant.DESKTOP_SESSION_MODEL + getUserId() + appId);
        if (models == null) {
            if (appId == -1) {
                models = SUtils.dt(modelRemoteService.findByUserId(getLoginInfo()
                        .getUserId()), new TypeReference<List<Model>>() {
                });
            } else {
                final int subSystem = appId;
                models = SUtils.dt(modelRemoteService.findByUserId(getLoginInfo().getUserId()), Model.class);
                models = models.stream().filter(m -> m.getSubSystem() == subSystem).collect(Collectors.toList());
            }
            session.setAttribute(DeskTopConstant.DESKTOP_SESSION_MODEL + getUserId() + appId, models);
        }
        Set<Integer> subSystemIds = EntityUtils.getSet(models, Model::getSubSystem);

        List<Server> servers = SUtils.dt(serverRemoteService.findBySubIds(subSystemIds.toArray(new Integer[0])), Server.class);
        servers = servers.stream().filter(java.util.Objects::nonNull).collect(Collectors.toList());
        Map<Integer, Server> serverSubIdMap = EntityUtils.getMap(servers, Server::getSubId);
        Map<Integer, DServerDto> dServerDtoMap = Maps.newHashMap();
        for (Server server : servers) {
            DServerDto dServerDto = new DServerDto();
            dServerDto.setServer(server);

            String iconURLString = UrlUtils.ignoreLastRightSlash(getFileURL())
                    + UrlUtils.SEPARATOR_SIGN + UrlUtils.ignoreFirstLeftSlash(server.getIconUrl());
            dServerDto.setImageUrl(iconURLString);
            
            dServerDtoMap.put(server.getSubId(), dServerDto);
        }

        Map<Integer, ModelDto> modelMap = new HashMap<>();
        boolean isSecondUrl = sysOptionRemoteService.isSecondUrl(getRequest().getServerName());

        Map<Integer, String> serverIdUrlMap = new HashMap<>();
        for (Model model : models) {
            Server server = serverSubIdMap.get(model.getSubSystem());
            ModelDto dto = new ModelDto();
            dto.setModel(model);
            
            if (server == null) {
                continue;
            }
            String serverUrl = isSecondUrl ? server.getSecondUrl() : server.getUrl();
            if (Evn.isDevModel() && StringUtils.equals(Version._7.getVersion(), model.getVersion())) {
                serverUrl = Evn.getWebUrl();
            }
            serverUrl="http://localhost:8080";
            //判断是否部署了多域名
            if (serverDomainMap != null && serverDomainMap.get(server.getId()) != null) {
                ServerRegion serverRegion = serverDomainMap.get(server.getId());
                serverUrl = serverRegion.getProtocol() + "://" + serverRegion.getDomain() + ":" + serverRegion.getPort();
                String contextPath = serverRegion.getContextPath();
                if (contextPath.substring(0, 1).equals("/")) {
                    serverUrl = serverUrl + contextPath;
                } else {
                    serverUrl = serverUrl + "/" + contextPath;
                }
                serverIdUrlMap.put(server.getId(), serverUrl);
            }

            if (StringUtils.isBlank(serverUrl)) {
                LOG.error("该模块所在的子系统没有在Server中配置URL,modelId:" + model.getId());
                continue;
            }
            if (model.getOpenType() == null) {
                model.setOpenType(DeskTopConstant.MODEL_OPEN_TYPE_IFRAME);
            }
            String modelFullURL = UrlUtils.ignoreLastRightSlash(serverUrl) + UrlUtils.SEPARATOR_SIGN
                    + UrlUtils.ignoreFirstLeftSlash(model.getUrl());

            String fullUrl = StringUtils.EMPTY;
            if (null == model.getOpenType()) {
                model.setOpenType(DeskTopConstant.MODEL_OPEN_TYPE_IFRAME);
            }
            if (DeskTopConstant.MODEL_OPEN_TYPE_DIV.equals(model.getOpenType())) {
                if (StringUtils.startsWithIgnoreCase(model.getUrl(), Protocol.HTTP.getValue())) {
                    fullUrl = model.getUrl();
                } else {
                    fullUrl = modelFullURL;
                }
            } else if (DeskTopConstant.MODEL_OPEN_TYPE_IFRAME.equals(model.getOpenType())
                    || DeskTopConstant.MODEL_OPEN_TYPE_NEW.equals(model.getOpenType())) {
            	 //综合素质
            	String cUrl = model.getUrl();
                if(StringUtils.isNotBlank(cUrl) && Objects.equals(model.getSubSystem(), 78)) {
            		cUrl=cUrl.trim();
              		if(cUrl.indexOf("?")>-1) {
              			cUrl=cUrl+"&cid="+UrlUtils.getPrefix(getRequest());
              		}else {
              			cUrl=cUrl+"?cid="+UrlUtils.getPrefix(getRequest());
              		}
                }
                
                fullUrl = Utils.getFinalModelUrl(cUrl, serverUrl, model.getVersion(),
                        username, Objects.getVaule(model.getSubSystem(), "-1"));
                if (DeskTopConstant.MODEL_OPEN_TYPE_DIV.equals(model.getOpenType())) {
                    model.setOpenType(DeskTopConstant.MODEL_OPEN_TYPE_IFRAME);
                }
                //防止有些哥们写语法的时候7.0的open_type不是div
                //if (Version._7.getVersion().equals(model.getVersion())) {
                //    model.setOpenType(DeskTopConstant.MODEL_OPEN_TYPE_DIV);
                //}
               
                
            }
            
            dto.setFullUrl(fullUrl);
            dto.setPreUrl(UrlUtils.ignoreLastRightSlash(serverUrl));

            String iconUrlString;
            if (Version._7.getVersion().equals(StringUtils.trim(model.getVersion()))) {
                iconUrlString = UrlUtils.ignoreLastRightSlash(getFileURL())
                        + UrlUtils.SEPARATOR_SIGN + UrlUtils.ignoreFirstLeftSlash(model.getPicture());
            } else {
                iconUrlString = UrlUtils.ignoreLastRightSlash(serverUrl) + UrlUtils.SEPARATOR_SIGN
                        + UrlUtils.ignoreFirstLeftSlash(model.getPicture());
            }

            dto.setImageUrl(iconUrlString);

            modelMap.put(model.getId(), dto);
        }
        Set<DServerDto> dServerDtos = Sets.newHashSet();
        for (Map.Entry<Integer, ModelDto> modelDtoEntry : modelMap.entrySet()) {
            Model model = modelDtoEntry.getValue().getModel();
            ModelDto modelDto = modelMap.get(model.getId());
            if (modelDto == null) {
                continue;
            }
            Integer subSystemId = model.getSubSystem();
            DServerDto dServerDto = dServerDtoMap.get(subSystemId);
            if (dServerDto == null) {
                continue;
            }
            dServerDtos.add(dServerDto);
            Integer parentId = model.getParentId();
            if (parentId == Model.PARENT_ID_DIRECT_SUBSYSTEM) {
                dServerDto.addSubModeldto(modelDto);
            }
            ModelDto rm = modelMap.get(parentId);
            if (rm != null) {
                rm.addModelDto(modelDto);
            }
        }
        Set<DServerDto> dss = new HashSet<DServerDto>();

        // 若该系统下面所有模块数量少于或等于10个 则不显示目录
        for (DServerDto dServerDto : dServerDtos) {
            if (CollectionUtils.isEmpty(dServerDto.getModelDtos()))
                continue;
            int total = 0;
            List<ModelDto> totalModels = Lists.newArrayList();
            for (ModelDto modelDto : dServerDto.getModelDtos()) {
                total += CollectionUtils.size(modelDto.getSubModelDtos());
                totalModels.addAll(modelDto.getSubModelDtos());
            }
            if (total <= 10) {
                dServerDto.setNoDirModelDtos(totalModels);
            } else {
                totalModels = null;
            }
            dServerDto.setDir(total > 10);

            String serverUrl = null;
            if (serverIdUrlMap != null && serverIdUrlMap.get(dServerDto.getServer().getId()) != null) {
                serverUrl = serverIdUrlMap.get(dServerDto.getServer().getId());
            }
            dServerDto.doSetUrlIndex(serverUrl);
            dServerDto.doSetUrlIndexSecond(serverUrl);
            dss.add(dServerDto);
        }
        serverDtos = Lists.newArrayList(dss);

        Collections.sort(serverDtos, new Comparator<DServerDto>() {
            @Override
            public int compare(DServerDto o1, DServerDto o2) {

                int orderId1 = o1 != null ? (o1.getServer().getOrderId() != null ? o1.getServer().getOrderId() : o1.getServer().getId()) : 0;
                int orderId2 = o2 != null ? (o2.getServer().getOrderId() != null ? o2.getServer().getOrderId() : o2.getServer().getId()) : 0;
                String o1Str = StringUtils.leftPad(String.valueOf(orderId1), 9, "0") + o1.getServer().getName();
                String o2Str = StringUtils.leftPad(String.valueOf(orderId2), 9, "0") + o2.getServer().getName();
                return o1Str.compareTo(o2Str);
            }
        });

        List<UnitServerClassify> unitServerClassifies = serverClassifyRemoteService.getClassifyByUnitId(getLoginInfo().getUnitId());
        if (!unitServerClassifies.isEmpty()) {
            classifies = new ArrayList<>(unitServerClassifies.size() + 1);
            List<String> hasClassifyDtos = new ArrayList<>(serverDtos.size());
            for (UnitServerClassify unitServerClassify : unitServerClassifies) {
                Classify classify = new Classify();
                classify.setUnitServerClassify(unitServerClassify);
                List<DServerDto> dtos = new ArrayList<>();
                for (DServerDto serverDto : serverDtos) {
                    if (unitServerClassify.getServerCodes().contains(serverDto.getServer().getCode())) {
                        dtos.add(serverDto);
                        hasClassifyDtos.add(serverDto.getServer().getCode());
                    }
                    if (unitServerClassify.getServerCodes().contains("system")
                            || unitServerClassify.getServerCodes().contains("eis_basedata")) {
                        classify.setSystem(true);
                    }
                }
                classify.setServerDtos(dtos);
                classifies.add(classify);
            }
            //没有分类的系统
            Classify noNameClassify = new Classify();
            UnitServerClassify nc = new UnitServerClassify();
            nc.setId(UuidUtils.generateUuid());
            nc.setOrderNumber(Integer.MAX_VALUE);
            nc.setName("未知分类");
            nc.setServerCodes(new HashSet<>());
            List<DServerDto> noDtos = new ArrayList<>();
            for (DServerDto dto : serverDtos) {
                if (!hasClassifyDtos.contains(dto.getServer().getCode())) {
                    noDtos.add(dto);
                }
            }
            noNameClassify.setUnitServerClassify(nc);
            noNameClassify.setServerDtos(noDtos);
            classifies.add(noNameClassify);

            classifies = classifies.stream().filter(e -> !e.getServerDtos().isEmpty())
                    .sorted(Comparator.comparing(e2 -> e2.getUnitServerClassify().getOrderNumber()))
                    .collect(Collectors.toList());
            map.put("classifies", classifies);
            map.put("hasClassifies", true);
            RedisUtils.set("hasClassifies", Boolean.TRUE.toString());
            RedisUtils.setObject("classifies." + getUserId() + "." + getSession().getId() + appId, classifies, RedisUtils.TIME_ONE_HOUR * 2);
        }

        map.addAttribute("serverDtos", serverDtos);

        Set<Object> modelAndServerIds = EntityUtils.getSet(servers, Server::getId);
        modelAndServerIds.addAll(EntityUtils.getSet(models, Model::getId));

        RedisUtils.setObject("navServerModelList." + getUserId() + "." + getSession().getId() + appId, serverDtos, RedisUtils.TIME_ONE_HOUR * 2);
        //判断文轩对接的名字判断
        if (StringUtils.startsWithIgnoreCase(username, WenXunConstant.WENXUN_BEFORE_USERNAME_VALUE)) {
            map.put("isDesktopShow", false);
        }
        
        //判段子系统是否为空
        map.put("serverIsNull", CollectionUtils.isEmpty(serverDtos));       
        return freemarkerPage;
    }

    private Map<Integer, ServerRegion> getDomainServer() {
        //判断当前的unitId
        String unitId = getLoginInfo().getUnitId();
        Map<Integer, ServerRegion> serverDomainMap = SUtils.dt(serverRegionRemoteService.findByUnitIdMap(unitId),
                new TypeReference<Map<Integer, ServerRegion>>() {
                });
        boolean isNull = (serverDomainMap == null || serverDomainMap.isEmpty());
        while (isNull) {
            Unit unit = unitRemoteService.findOneObjectById(unitId);
            if (unit != null) {
                Integer unitClass = unit.getUnitClass();
                unitId = unit.getParentId();
                if (unitClass != null && !(unitClass == Unit.UNIT_CLASS_EDU && Unit.TOP_UNIT_GUID.equals(unitId))) {
                    serverDomainMap = SUtils.dt(serverRegionRemoteService.findByUnitIdMap(unitId),
                            new TypeReference<Map<Integer, ServerRegion>>() {
                            });
                    isNull = (serverDomainMap == null || serverDomainMap.isEmpty());
                } else {
                    isNull = Boolean.FALSE;
                }
            } else {
                isNull = Boolean.FALSE;
            }
        }
        //如果都没有配置，根据地区来取
        if (serverDomainMap == null || serverDomainMap.isEmpty()) {
            String deployRegion = serverRegionRemoteService.findRegionByDomain(getRequest().getServerName());
            //2、根据部署地区取得登录页相关参数信息
            if (StringUtils.isNotBlank(deployRegion)) {
                serverDomainMap = SUtils.dt(serverRegionRemoteService.findByRegionMap(deployRegion),
                        new TypeReference<Map<Integer, ServerRegion>>() {
                        });
            }
        }
        return serverDomainMap;
    }

    @ControllerInfo(ignoreLog = ControllerInfo.LOG_FORCE_IGNORE, value = "加载右侧框架页面")
    @RequestMapping("/home")
    public String doLoadContainer() {

        return "/desktop/homepage/index-inner.ftl";
    }

    @ControllerInfo(value = "显示密码修改")
    @RequestMapping(value = {"/ex/pwd"})
    public String doLoadChangePwd(ModelMap map) {
        map.put("showFramework", true);
        return "/desktop/homepage/ap/set/changePwd.ftl";
    }

    @ResponseBody
    @RequestMapping(value = {"/ex/doPwdSave"})
    public String doSavePwd(String username, String password, String newPassword) {
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
        boolean ret = newPassword.matches(regex);
        if (!ret) {
            return error("新密码必须由8~16个字母和数字组成！");
        }
        User user = User.dc(userRemoteService.findByUsername(username));
        if (user == null || !StringUtils.equals(PWD.decode(user.getPassword()), password)) {
            return error("用户名或者原有密码输入错误！");
        }
        user.setPassword(newPassword);
        try {
            userRemoteService.updatePassportPasswordByUsername(username, newPassword);
        } catch (PassportException e) {
            log.error("passport passpord error", e);
            success("修改该密码失败！");
        }
        return success("密码修改成功！");
    }

    @ControllerInfo(value = "显示个人设置")
    @RequestMapping("/index/userSetting")
    public String doLoadUserSetting(ModelMap map) {
        User user = SUtils.dt(
                userRemoteService.findOneById(getLoginInfo().getUserId(), true),
                new TypeReference<User>() {
                });
        if (user != null) {
            map.put("realName", user.getRealName());
            map.put("mobilePhone", user.getMobilePhone());
            map.put("username", user.getUsername());
            map.put("userId", user.getId());
            if(user.getOwnerType() == User.OWNER_TYPE_STUDENT) {
            	
            }else if(user.getOwnerType() == User.OWNER_TYPE_FAMILY) {
            	//家长
            	Family family = SUtils.dt(familyRemoteService.findOneById(user.getOwnerId(), true),
                        new TypeReference<Family>() {});
            	Student stu = SUtils.dt(studentRemoteService.findOneById(family.getStudentId(), true),
                        new TypeReference<Student>() {});
            	Clazz cls = SUtils.dt(classRemoteService.findOneById(stu.getClassId()),
                        new TypeReference<Clazz>() {});
            	User stuUser = SUtils.dt(userRemoteService.findByOwnerId(stu.getId()),new TypeReference<User>() {});
            	map.put("stuName",stu.getStudentName());
            	map.put("sex", stu.getSex() + "");
            	map.put("clsName",cls.getClassNameDynamic());
            	map.put("identitycardType",stu.getIdentitycardType());
            	map.put("identityCard",StringUtils.isBlank(stu.getIdentityCard())?"":stu.getIdentityCard());
            	if(stuUser != null) {
            		map.put("stuusername",stuUser.getUsername());
            	}else {
            		map.put("stuusername","未开通");
            	}
            	
            }else {
            	//教师或者管理员
            	map.put("sex", user.getSex() + "");
            	Teacher teacher = SUtils.dt(teacherRemoteService.findOneById(user.getOwnerId(), false),
                        new TypeReference<Teacher>() {});
            	map.put("identityCard", StringUtils.isBlank(teacher.getIdentityCard())?"":teacher.getIdentityCard());
            	map.put("polity", teacher.getPolity());//政治面貌
            	map.put("nation",teacher.getNation());//民族
            	Dept dept = SUtils.dt(deptRemoteService.findOneById(user.getDeptId(), true),
            			new TypeReference<Dept>() {});
            	map.put("deptName",dept==null?"":dept.getDeptName());//所在部门
            	String updateInfo = systemIniRemoteService.findValue("TEACHER.UPDATE.INFO");
            	updateInfo = StringUtils.isBlank(updateInfo)?"1":updateInfo;
            	if(StringUtils.equals(updateInfo, "0")) {
            		map.put("updateInfo","0");
            	}else {
            		map.put("updateInfo","1");
            	}
            	
            }
            
           map.put("showBirthday", true);
           if (user.getBirthday() != null) {
              map.put("birthday", user.getBirthday());
           }
           String region = systemIniRemoteService.findValue(DeployRegion.SYS_OPTION_REGION);
           if (Objects.equals(DeployRegion.DEPLOY__HAIKOU, region)) {
             map.put("haikou", true);
           } 
//            //判断是否是大庆，不显示出生日期
//            String region = systemIniRemoteService.findValue(DeployRegion.SYS_OPTION_REGION);
//            if (Objects.equals(DeployRegion.DEPLOY_DAQIONG, region)) {
//                map.put("showBirthday", false);
//            } else {
//                map.put("showBirthday", true);
//                if (user.getBirthday() != null) {
//                    map.put("birthday", DateFormatUtils.format(user.getBirthday(),
//                            "yyyy-MM-dd"));
//                }
//            }
            map.put("ownerType", user.getOwnerType());
            map.put("layout", getUserSetLayout());
            map.put("layoutUserSet", isFunctionAreaUserSetOpen());
            map.put("success", "true");
            if (user.getOwnerId().equals(User.ADMIN_USER_ID)) {
                if (User.OWNER_TYPE_TEACHER == user.getOwnerType()) {
                    List<Teacher> teachers = SUtils.dt(teacherRemoteService.findByTeacherNameAndMobilePhone(user.getRealName(), user.getMobilePhone()), Teacher.class);
                    if (CollectionUtils.isNotEmpty(teachers)) {
                        map.put("teacherList", teachers);
                        map.put("binding", true);
                    }
                } else if (User.OWNER_TYPE_STUDENT == user.getOwnerType()) {
                    List<Student> studentList = SUtils.dt(studentRemoteService.findByStudentNameAndIdentityCard(user.getRealName(), user.getIdentityCard()), Student.class);
                    if (CollectionUtils.isNotEmpty(studentList)) {
                        map.put("studentList", studentList);
                        map.put("binding", true);
                    }
                } else if (User.OWNER_TYPE_FAMILY == user.getOwnerType()) {
                    List<Family> familys = SUtils.dt(familyRemoteService.findByRealNameAndMobilePhone(user.getRealName(), user.getMobilePhone()), Family.class);
                    if (CollectionUtils.isNotEmpty(familys)) {
                        map.put("familyList", familys);
                        map.put("binding", true);
                    }
                }
            }

        } else {
            map.put("success", "false");
            map.put("msg", "加载用户信息失败");
        }

        //判断是否是大庆，解除qq和微信登录功能
        String region = systemIniRemoteService.findValue(DeployRegion.SYS_OPTION_REGION);
        if (Objects.equals("daqing", region)) {
            map.put("relieveQQ", true);
            //得到当前账号是否绑定了qq和微信
            String isRelieveQQ = "未绑定";
            String isRelieveWX = "未绑定";
            try {
                String url = Evn.getString(Constant.DQ_WEB_URL);
                String accountId = user.getAccountId();
                String dataUrl = UrlUtils.ignoreLastRightSlash(url)
                        + "/bindingMessage.htm?accountId=" + accountId;
                String data = UrlUtils.readContent(dataUrl, getRequest().getSession()
                        .getId(), "utf8");
                JSONObject json = JSONObject.parseObject(data);
                String typeString1 = json.getString("message");
                isRelieveQQ = typeString1.contains("2") ? "解绑Q Q" : "未绑定";
                isRelieveWX = typeString1.contains("1") ? "解绑微信" : "未绑定";
            } catch (IOException e) {
                LOG.error("没有获取到数据");
                e.printStackTrace();
            }
            map.put("isRelieveQQ", isRelieveQQ);
            map.put("isRelieveWX", isRelieveWX);
        }

        //天长的修改密码页面特殊处理
        Unit unit = SUtils.dc(unitRemoteService.findOneById(user.getUnitId()), Unit.class);
        if (DeployRegion.DEPLOY_TIANCHANG.equals(region) && !Objects.equals(unit.TOP_UNIT_GUID,unit.getParentId())) {
            map.put("isTianChang", Boolean.TRUE);
        }

        String showUpdatePasswordStr = sysOptionRemoteService.findValue("UPDATE.OR.INIT.PASSWORD");
        if (StringUtils.isNotBlank(showUpdatePasswordStr)) {
            map.addAttribute("updatePassword", BooleanUtils.toBoolean(showUpdatePasswordStr, "1", "0"));
        }
        
        //是否显示账号绑定功能
        String showUserBing = sysOptionRemoteService.findValue("DESKTOP.USER.BIND.OPEN");
        SysUserBind sysUserBind = null ;
        if (StringUtils.isNotBlank(showUserBing)) {
        	boolean showBind = BooleanUtils.toBoolean(showUserBing, "true", "false");
        	if(DeployRegion.DEPLOY__HAIKOU.equals(region)){
        		if(User.OWNER_TYPE_TEACHER == user.getOwnerType()){
        			map.addAttribute("showUserBing", showBind);
        		}
        	}else{
        		map.addAttribute("showUserBing", showBind);
        	}
            if(showBind){
            	sysUserBind = SUtils.dc(sysUserBindRemoteService.findByUserId(user.getId()), SysUserBind.class);
            }
        }
        map.addAttribute("sysUserBind", sysUserBind == null ? new SysUserBind() : sysUserBind);
        if (DeployRegion.DEPLOY__HAIKOU.equals(region)) {
            map.put("showHkoa", Boolean.TRUE);
        }
        
        return "/desktop/homepage/ap/set/set-userInfo.ftl";
    }

    @ResponseBody
    @RequestMapping("/user/verifyQuestionImage")
    @ControllerInfo("获取问答式验证码")
    public String verifyQuestionImage(HttpServletResponse response,
                                      HttpServletRequest request) {
        try {
            CodeUtils.export(request, response, USER_SETTING_IMG_CODE + request.getSession().getId(), 60 * 5, 150, 30);
        } catch (Exception e) {
            log.error("验证码生成失败", e);
        }
        return "";
    }

    @ResponseBody
    @RequestMapping("/user/registerMsgCode")
    @ControllerInfo("获取短信验证码")
    public String showRegisterMsgCode(String mobilePhone, String imgCode, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession, ModelMap map) {
        String imgCodeKey = USER_SETTING_IMG_CODE + getSession().getId();
        String redisImgCode = RedisUtils.get(imgCodeKey);
        if (StringUtils.isNotEmpty(redisImgCode)) {
            RedisUtils.del(imgCodeKey);
            if (redisImgCode.equals(imgCode)) {
                try {
                    String msgCode = CodeUtils.getNumberCode(6);
                    String msgCodeKey = USER_SETTING_MSG_CODE + mobilePhone;
                    RedisUtils.set(msgCodeKey, msgCode, 60 * 5);
                    String msgContent = "你的验证码是" + msgCode + ",在5分钟内有效,请在有效时间内尽快完成绑定,如果非本人操作请忽略本短信";
                    System.out.println("短信验证码" + msgCode);
                    System.out.println(msgContent);
                    String result = smsRemoteService.sendSms(new String[]{mobilePhone}, msgContent, null);
                    if (result != null && result.length() > 3) {
                        result = result.substring(1, 3);
                    }
                    if (!result.equals(ZDConstant.JSON_RESULT_CODE_00)) {
                        return error(ZDConstant.resultDescriptionMap.get(result));
                    }
                    return success("短信验证码获取成功");
                } catch (Exception e) {
                    e.printStackTrace();
                    return error("短信验证码获取失败");
                }
            } else {
                return error("图片验证码错误,请重新输入");
            }
        } else {
            return returnError("0", "图片验证码已经失效,请刷新验证码重新获取");
        }
    }


    @ResponseBody
    @RequestMapping("/user/bindingSave")
    @ControllerInfo("绑定用户信息")
    public String bindingSave(String msgCode, String imgCode, String bindingIds, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        User user = SUtils.dt(
                userRemoteService.findOneById(getLoginInfo().getUserId(), true),
                new TypeReference<User>() {
                });
        if (user.getOwnerType().equals(User.OWNER_TYPE_STUDENT)) {
            String imgCodeKey = USER_SETTING_IMG_CODE + getSession().getId();
            String redisImgCode = RedisUtils.get(imgCodeKey);
            if (StringUtils.isNotEmpty(redisImgCode)) {
                if (redisImgCode.equals(imgCode)) {
                    RedisUtils.del(imgCodeKey);
                    try {
                        Student student = SUtils.dc(studentRemoteService.findOneById(bindingIds), Student.class);
                        if (!student.getIdentityCard().equals(msgCode)) {
                            return error("绑定失败:身份证号码和绑定的学生不同！");
                        }
                        User oldUser = SUtils.dc(userRemoteService.findByOwnerId(bindingIds), User.class);
                        user.setOwnerId(student.getId());
                        user.setUnitId(student.getSchoolId());
                        user.setClassId(student.getClassId());
                        user.setBirthday(student.getBirthday());
                        user.setSex(student.getSex());
                        if (userRemoteService.updateUser(user)) {
                            if (oldUser != null) {
                                oldUser.setIsDeleted(1);
                                PassportClientUtils.getPassportClient().deleteAccounts(new String[]{oldUser.getAccountId()});
                                userRemoteService.updateUserDeleted(oldUser);
                                userRoleRemoteService.updateRemoteId(user.getId(), oldUser.getId());
                            }
                            return success("绑定成功");
                        } else {
                            return error("绑定失败");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return error("绑定失败");
                    }
                } else {
                    return error("图片验证码错误,请重新获取图片验证码");
                }
            } else {
                return error("图片验证码已经失效,请重新获取图片验证码");
            }
        } else if (user.getOwnerType().equals(User.OWNER_TYPE_TEACHER)) {
            String msgCodeKey = USER_SETTING_MSG_CODE + user.getMobilePhone();
            String redisMsgCode = RedisUtils.get(msgCodeKey);
            if (StringUtils.isNotEmpty(redisMsgCode)) {
                if (redisMsgCode.equals(msgCode)) {
                    RedisUtils.del(msgCodeKey);
                    try {
                        Teacher teacher = SUtils.dc(teacherRemoteService.findOneById(bindingIds), Teacher.class);
                        User oldUser = SUtils.dc(userRemoteService.findByOwnerId(bindingIds), User.class);
                        user.setOwnerId(teacher.getId());
                        user.setUnitId(teacher.getUnitId());
                        user.setDeptId(teacher.getDeptId());
                        user.setBirthday(teacher.getBirthday());
                        user.setSex(teacher.getSex());
                        if (userRemoteService.updateUser(user)) {
                            if (oldUser != null) {
                                oldUser.setIsDeleted(1);
                                PassportClientUtils.getPassportClient().deleteAccounts(new String[]{oldUser.getAccountId()});
                                userRemoteService.updateUserDeleted(oldUser);
                                userRoleRemoteService.updateRemoteId(user.getId(), oldUser.getId());
                            }
                            return success("绑定成功");
                        } else {
                            return error("绑定失败");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return error("绑定失败");
                    }
                } else {
                    return error("短信验证码错误,请确认后重新提交或者重新获取短信验证码");
                }
            } else {
                return error("短信验证码已经失效,请重新获取短信验证码");
            }
        } else {
            String msgCodeKey = USER_SETTING_MSG_CODE + user.getMobilePhone();
            String redisMsgCode = RedisUtils.get(msgCodeKey);
            if (StringUtils.isNotEmpty(redisMsgCode)) {
                if (redisMsgCode.equals(msgCode)) {
                    RedisUtils.del(msgCodeKey);
                    try {
                        String[] bindingIdArray = bindingIds.split(",");
                        List<User> userList = new ArrayList<User>();
                        List<Account> accountList = new ArrayList<Account>();
                        Map<String, String> userMap = new HashMap<String, String>();
                        if (bindingIdArray.length > 1) {
                            List<Family> families = SUtils.dt(familyRemoteService.findListByIds(bindingIdArray), Family.class);
                            List<User> oldUsers = SUtils.dt(userRemoteService.findByOwnerIds(bindingIdArray), User.class);

                            for (int i = 0; i < families.size(); i++) {
                                Family family = families.get(i);
                                if (i == 0) {
                                    user.setOwnerId(family.getId());
                                    user.setUnitId(family.getSchoolId());
                                    user.setBirthday(family.getBirthday());
                                    user.setSex(family.getSex());
                                    userRemoteService.updateUser(user);
                                } else {
                                    User newUser = SUtils.dc(SUtils.s(user), User.class);
                                    newUser.setId(UuidUtils.generateUuid());
                                    newUser.setUsername("s_" + new Date().getTime());
                                    newUser.setOwnerId(family.getId());
                                    newUser.setUnitId(family.getSchoolId());
                                    newUser.setBirthday(family.getBirthday());
                                    newUser.setSex(family.getSex());

                                    Account account = new Account();
                                    account.setId(UuidUtils.generateUuid());
                                    account.setUsername(newUser.getUsername());
                                    account.setPassword(newUser.getPassword());
                                    account.setRealName(newUser.getRealName());
                                    account.setFixedType(2);
                                    account.setType(11);
                                    accountList.add(account);
                                    newUser.setAccountId(account.getId());
                                    userList.add(newUser);
                                }
                                userMap.put(family.getId(), user.getId());
                            }
                            if (userList.size() > 0) {
                                userRemoteService.saveAllEntitys(SUtils.s(userList.toArray(new User[0])));
                                Set<String> accountIds = new HashSet<String>();
                                for (User oldUser : oldUsers) {
                                    oldUser.setIsDeleted(1);
                                    userRemoteService.updateUserDeleted(oldUser);
                                    accountIds.add(oldUser.getAccountId());
                                    String userId = userMap.get(oldUser.getOwnerId());
                                    if (StringUtils.isNotBlank(userId)) {
                                        userRoleRemoteService.updateRemoteId(userId, oldUser.getId());
                                    }
                                }
                                if (CollectionUtils.isNotEmpty(accountIds)) {
//    								PassportClient.getInstance().deleteAccounts(accountIds.toArray(new String[0]));
                                }
                            }
                            if (accountList.size() > 0) {
                                AccountResult[] results = PassportClientUtils.getPassportClient().addValidAccounts(accountList.toArray(new Account[1])
                                );
                                for (AccountResult accountResult : results) {
                                    System.out.println(accountResult.getMessage());
                                }
                            }
                            return success("绑定成功");
                        } else {
                            Family family = SUtils.dc(familyRemoteService.findOneById(bindingIds), Family.class);
                            User oldUser = SUtils.dc(userRemoteService.findByOwnerId(bindingIds), User.class);
                            user.setOwnerId(family.getId());
                            user.setUnitId(family.getSchoolId());
                            user.setBirthday(family.getBirthday());
                            user.setSex(family.getSex());
                            if (userRemoteService.updateUser(user)) {
                                if (oldUser != null) {
                                    oldUser.setIsDeleted(1);
                                    PassportClientUtils.getPassportClient().deleteAccounts(new String[]{oldUser.getAccountId()});
                                    userRemoteService.updateUserDeleted(oldUser);
                                    userRoleRemoteService.updateRemoteId(user.getId(), oldUser.getId());
                                }
                                return success("绑定成功");
                            } else {
                                return error("绑定失败");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return error("绑定失败");
                    }
                } else {
                    return error("短信验证码错误,请确认后重新提交或者重新获取短信验证码");
                }
            } else {
                return error("短信验证码已经失效,请重新获取短信验证码");
            }
        }
    }
    //7.0和6.0统一了redis缓存，
    //

    @ControllerInfo(value = "显示可用功能区列表")
    @RequestMapping("/index/addModule")
    public String doLoadAddModule(ModelMap map) {
        String freemarkerPage = "/desktop/homepage/ap/add/add-module.ftl";
        Map<String, List<FunctionArea>> fMap = RedisUtils.getObject("functionMap." + getUserId(), new TypeReference<Map<String, List<FunctionArea>>>() {
        });
        if (fMap != null) {
            map.put("functionMap", fMap);
            return freemarkerPage;
        }

        Integer unitClass = getLoginInfo().getUnitClass();
        Integer ownerType = getLoginInfo().getOwnerType();
        List<FunctionArea> functionAreas = functionAreaService.findAll();
        List<FunctionArea> functionAreas2 = new ArrayList<FunctionArea>();
        for (FunctionArea functionArea : functionAreas) {
            boolean isAccord = functionArea == null || functionArea.getState() == FunctionArea.STATE_ILLEGAL;
            isAccord = isAccord || (unitClass != null && functionArea.getUnitClass() != null && functionArea.getUnitClass() != 0 && !Objects.equals(unitClass, functionArea.getUnitClass()));
            isAccord = isAccord || (!StringUtils.defaultString(functionArea.getUserType(), "0").equals("0") && !StringUtils.contains(functionArea.getUserType(), Objects.getValue(ownerType)));
            if (!isAccord) {
                functionAreas2.add(functionArea);
            }
        }
        List<FunctionAreaUser> functionAreaUsers = functionAreaUserService
                .findByUserInfo(getLoginInfo().getUserId(), unitClass);
        List<String> functionAreaIds = EntityUtils.getList(functionAreaUsers,
                FunctionAreaUser::getFunctionAreaId);
        List<FunctionArea> functionAreas3 = functionAreaService
                .findListByIdIn(EntityUtils.toArray(functionAreaIds, String.class));

        //全部删除的情况
        if (CollectionUtils.isEmpty(functionAreas3)) {
            for (FunctionArea functionArea : functionAreas2) {
                functionArea.setIsAdd(DeskTopConstant.FUNCTIONAREA_IS_NOT_ADD);
            }
        } else {
            for (FunctionArea functionArea : functionAreas2) {
                for (FunctionArea functionArea1 : functionAreas3) {
                    if (functionArea.getId().equals(functionArea1.getId())) {
                        functionArea.setIsAdd(DeskTopConstant.FUNCTIONAREA_IS_ADD);
                        break;
                    } else {
                        functionArea.setIsAdd(DeskTopConstant.FUNCTIONAREA_IS_NOT_ADD);
                    }
                }
            }
        }

        if (!getLoginInfo().getOwnerType().equals(new Integer(User.OWNER_TYPE_TEACHER))) {
            for (FunctionArea fua : functionAreas2) {
                if (fua.getType().equals("9")) {
                    fua.setIsAdd(DeskTopConstant.FUNCTIONAREA_IS_NOT_ADD);
                }
            }
        }
        // 放入map中
        fMap = EntityUtils.getListMap(functionAreas2, FunctionArea::getType, Function.identity());
        RedisUtils.setObject("functionMap." + getUserId(), fMap);
        map.put("functionMap", fMap);
        return freemarkerPage;
    }

    @ControllerInfo(value = "显示日历信息", ignoreLog = ControllerInfo.LOG_FORCE_WRITE)
    @RequestMapping("/index/userCalendar")
    public String doLoadUserCalendar(ModelMap map) {
        return "/desktop/homepage/ap/set/set-calendar.ftl";
    }

    /**
     * 判断是否多身份
     *
     * @return
     */
    private List<JSONObject> multiIdentity() {

        List<JSONObject> jsons = RedisUtils.getObject("multiIdentity." + getUserId() + getSession().getId(), new TypeReference<List<JSONObject>>() {
        });
        jsons = null;
        if (jsons != null) {
            return jsons;
        }
        User u = userRemoteService.findOneObjectById(getUserId(), new String[]{"mobilePhone", "password", "ownerType"});
//		final User u = SUtils.dc(userRemoteService.findOneById(getUserId()), User.class);
        if (StringUtils.isBlank(u.getMobilePhone()) || u.getOwnerType() == null || User.OWNER_TYPE_STUDENT == u.getOwnerType()) {
            return Lists.newArrayList();
        }
        List<User> users = SUtils.dt(userRemoteService.findByMobilePhones(ArrayUtils.toArray(u.getMobilePhone())), User.class);

        Set<String> unitIds = EntityUtils.getSet(users, User::getUnitId);
//		List<Unit> units = SUtils.dt(unitRemoteService.findListByIds(EntityUtils.toArray(unitIds,String.class)), Unit.class);
        List<Unit> units = unitRemoteService.findListObjectBy(Unit.class, null, null, "id", unitIds.toArray(new String[0]), new String[]{"id", "unitName"});
        //过滤软删 状态不合法（离职等）
        List<User> finalUsers = EntityUtils.filter(users, user -> {
            return !new Integer(1).equals(user.getUserState())
                    || new Integer(1).equals(user.getIsDeleted())
//				|| user.getId().equals(getLoginInfo().getUserId())
                    || !PWD.decode(user.getPassword()).equals(PWD.decode(u.getPassword()))
                    || new Integer(User.OWNER_TYPE_STUDENT).equals(user.getOwnerType());
        });
        Map<String, Unit> unitMap = EntityUtils.getMap(units, Unit::getId);
        jsons = Lists.newArrayList();
        for (User user : finalUsers) {
            JSONObject json = createUID(user.getRealName(), user.getUsername()); //json存放要展示的内容
            json.put("unitName", unitMap.get(user.getUnitId()).getUnitName());
            if (user.getOwnerType().equals(User.OWNER_TYPE_TEACHER)
                    && !(user.getUserType().equals(User.USER_TYPE_UNIT_ADMIN) || user.getUserType().equals(User.USER_TYPE_TOP_ADMIN))) {
                json.put("roleName", "教职工");
                json.put("role", User.OWNER_TYPE_TEACHER);
            } else if (user.getOwnerType().equals(User.OWNER_TYPE_FAMILY)) {
                //add(user.getOwnerId());
                json.put("roleName", "家长");
                json.put("role", User.OWNER_TYPE_FAMILY);
        		Family family = SUtils.dt(familyRemoteService.findOneById(user.getOwnerId(), true),
                        new TypeReference<Family>() {});
        		if(family!=null){
                    Student stu = SUtils.dt(studentRemoteService.findOneById(family.getStudentId(), true),
                            new TypeReference<Student>() {});
                    json.put("realName", stu.getStudentName()+"的家长");
                }
            } else if (user.getUserType().equals(User.USER_TYPE_UNIT_ADMIN)) {
                json.put("roleName", "管理员");
                json.put("role", User.OWNER_TYPE_SUPER); //管理员  不是超管
            }
            user.setAvatarUrl(PJHeadUrlUtils.getShowAvatarUrl(getRequest().getContextPath(), user.getAvatarUrl(), getFileURL()));
            json.put("avatarUrl", user.getAvatarUrl());
            json.put("id", user.getId());
            jsons.add(json);
        }
        //Set<String> entityIds = EntityUtils.getSet(users, User::getId);
        RedisUtils.setObject("multiIdentity." + getUserId() + getSession().getId(), jsons, 5 * 60);
        return jsons;
    }

    JSONObject createUID(String name, String userName) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("realName", name);
        jsonObject.put("userName", userName);
        return jsonObject;
    }


    /**
     * 判断是否是文轩登录
     * 判断是否乐智登录
     * @param map
     */
    private void isWXLogin(ModelMap map) {
        String userName = getLoginInfo().getUserName();
        if (StringUtils.startsWithIgnoreCase(userName, WenXunConstant.WENXUN_BEFORE_USERNAME_VALUE)) {
            map.put("isWenXuan", true);
            map.put("isPop", false);
            String wxUserName = StringUtils.substringAfter(userName, WenXunConstant.WENXUN_BEFORE_USERNAME_VALUE);
            map.put("wxUserName", wxUserName);
            String wxWitaccount = RedisUtils.get(WenXunConstant.WENXUN_WAIT_ACCOUNT_REDIS_KEY + wxUserName);
            map.put("wxWitaccount", wxWitaccount);
            map.put("isHide", true);
        }
    }
    
    public static void main(String[] args) {
        String oldPwd = "GRDFNFMF8J23ALYHJ5AFXP6FMBKPNP8FFSVLHLJF552DDQYJF5GWWQNNWLFRF3N5";
        String pwd = PWD.decode(oldPwd);
        System.out.println(pwd);
        String endPwd = new PWD(pwd).encode();
        System.out.println(endPwd);
    }
}
