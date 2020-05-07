/* 
 * @(#)OpsAction.java    Created on 2017-2-27
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;

import net.zdsoft.basedata.dto.UnitDto;
import net.zdsoft.basedata.entity.Region;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.RegionRemoteService;
import net.zdsoft.basedata.remote.service.SysProductParamRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.PassportClientUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.framework.utils.Validators;
import net.zdsoft.license.LicenseInfo;
import net.zdsoft.license.service.LicenseService;
import net.zdsoft.passport.entity.Account;
import net.zdsoft.passport.exception.PassportException;
import net.zdsoft.passport.remoting.PassportRemotingException;
import net.zdsoft.passport.remoting.system.CooperatorDto;
import net.zdsoft.passport.service.client.PassportClient;
import net.zdsoft.passport.service.client.PassportClientParam;
import net.zdsoft.system.config.PassportServerClient;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.dto.common.TabMenuDto;
import net.zdsoft.system.dto.ops.OptionVo;
import net.zdsoft.system.entity.config.SysOption;
import net.zdsoft.system.entity.config.SystemIni;
import net.zdsoft.system.entity.config.UnitIni;
import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.enums.server.AppStatusEnum;
import net.zdsoft.system.enums.server.ServerClassEnum;
import net.zdsoft.system.service.config.SysOptionService;
import net.zdsoft.system.service.config.SystemIniService;
import net.zdsoft.system.service.config.UnitIniService;
import net.zdsoft.system.service.ops.OpsService;
import net.zdsoft.system.service.server.ServerService;
import redis.clients.jedis.Jedis;

@Controller
@RequestMapping("/system/ops")
public class OpsAction extends BaseAction {

    private static final Logger logger = LoggerFactory.getLogger(OpsAction.class);

    @Autowired
    private LicenseService licenseService;
    @Autowired
    private UserRemoteService userRemoteService;
    @Autowired
    private ServerService serverService;
    @Autowired
    private SysOptionService sysOptionService;
    @Autowired
    private SystemIniService systemIniService;
    @Autowired
    private UnitIniService unitIniService;
    @Autowired
    private UnitRemoteService unitRemoteService;
    @Autowired
    private RegionRemoteService regionRemoteService;
    @Autowired
    private SysProductParamRemoteService sysProductParamRemoteService;
    @Autowired
    private OpsService opsService;
    @Autowired
    private PassportServerClient passportServerClient;

    @RequestMapping("/login")
    @ControllerInfo("访问登录页")
    public String login() {
        return "/system/ops/login.ftl";
    }

    @RequestMapping("/index")
    public Object index(String code) {
        ModelAndView mv = new ModelAndView("/system/ops/index.ftl");
        mv.addObject("code", code);
        return mv;
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

        httpSession.setAttribute(Constant.KEY_OPS_USER, user);

        // 根据修改时间判断运维是否重置了超管密码
        String code = user.getModifyTime().after(user.getCreationTime()) ? "1" : "0";
        // 校验密码
        return returnSuccess(code, "");
    }

    @RequestMapping("/resetPassword")
    @ControllerInfo("访问重置密码页面")
    public Object resetPassword(HttpSession httpSession,boolean fromDesktop) {
        // TODO 校验是否已登录，后续改为拦截器处理
        if (httpSession.getAttribute(Constant.KEY_OPS_USER) == null) {
            return "redirect:/system/ops/login";
        }
        return new ModelAndView("/system/ops/resetPassword.ftl").addObject("fromDesktop",fromDesktop);
    }

    @ResponseBody
    @RequestMapping("/saveNewPassword")
    @ControllerInfo("重置超管密码为{newPassword}")
    public String saveNewPassword(String oldPassword, String newPassword, HttpSession httpSession)
            throws PassportException {
        User user = (User) httpSession.getAttribute(Constant.KEY_OPS_USER);
        if (user == null) {
            return "redirect:/system/ops/login";
        }
        // 调用basedata-remote修改
        if (Validators.isEmpty(oldPassword)) {
            return returnError("oldPassword", "请输入旧密码");
        }
        if (Validators.isEmpty(newPassword)) {
            return returnError("newPassword", "请输入新密码");
        }
        if (oldPassword.equals(newPassword)) {
            return returnError("newPassword", "新旧密码不能一致");
        }
        if (checkWeakPassword(newPassword)) {
            String msg = systemIniService.findValue(Constant.SYSTEM_PASSWORD_ALERT);
            return returnError("newPassword", org.apache.commons.lang3.StringUtils.isBlank(msg)?"新密码强度不够，请修改":msg);
        }
        if (!(PWD.decode(user.getPassword()).equals(oldPassword))) {
            return returnError("oldPassword", "旧密码不正确");
        }

        userRemoteService.updatePasswordByUsername("super", newPassword);
        // 更新会话里的密码信息
        user.setPassword(newPassword);
        // 是否已激活
        Integer state = user.getUserState();
        return returnSuccess((state == null || state == 0) ? "0" : "1", "重置成功");
    }

    @ResponseBody
    @RequestMapping(value = "refresh/{command}", method = RequestMethod.GET)
    public Object refreshRedis(@PathVariable("command") String command){

        try {
            Jedis jedis = RedisUtils.getJedis();
            if ("flushall".equalsIgnoreCase(command)) {
                jedis.flushAll();
            }
            else if ("flushdb".equalsIgnoreCase(command)){
                jedis.flushDB();
            }
            else {
                return error("not support command");
            }
            return success("flush success");
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    /**
     * super 系统参数tab页面
     */
    @RequestMapping(value = "sysOption/tab")
    public Object sysOptionTab() {
        ModelAndView mv = new ModelAndView("/system/common/tabHome.ftl");
        List<TabMenuDto> sysOptionMenuDtoList = new ArrayList<>();
        sysOptionMenuDtoList.add(new TabMenuDto("deploy必填参数",
                "/system/ops/sysOption/deployParams", "deployParam"));
        sysOptionMenuDtoList.add(new TabMenuDto("baseSysOption参数",
                "/system/ops/sysOption?fromDesktop=true", "sysOption"));
        sysOptionMenuDtoList.add(new TabMenuDto("sysOption参数(eis相关)",
                "/system/ops/sysOption/systemIni?fromDesktop=true", "systemIni"));
        sysOptionMenuDtoList.add(new TabMenuDto("单位参数",
        		"/system/ops/sysOption/unitIni", "unitIni"));

        mv.addObject("tabList", sysOptionMenuDtoList);
        mv.addObject("tabCode", "deployParam");
        return mv;
    }

    /**
     * 运维部署必填参数
     */
    @RequestMapping(value = "sysOption/deployParams")
    public ModelAndView deployParameters() {
        //base_sys_option
        String[] sysOptionCodes = new String[] {"PASSPORT.URL", "MEMBER.URL", "INDEX.URL", "EIS.URL", "FILE.URL",
                "FILE.PATH", "FILE.URL.RES", "FILE.PATH.RES", "SMS.PORT"
                , "PASSPORT.SECOND.URL", "MEMBER.SECOND.URL", "INDEX.SECOND.URL",
                "EIS.SECOND.URL", "FILE.SECOND.URL", "FILE.SECOND.URL.RES"};

        List<SysOption> sysOptionList = sysOptionService.findSysOptionByCodes(sysOptionCodes);

        String[] systemInis = new String[]{"EIS6.DOMAIN", "EIS5.DOMAIN", "SYSTEM.PASSPORT.SWITCH",
                "SYSTEM.OFFICE.SWITCH", "SYSTEM.USE.EISLOGIN.FOR.PASSPORT"};
        List<SystemIni> systemIniList = systemIniService.findListByIn("iniid", systemInis);

        List<OptionVo> optionVos = new ArrayList<>();
        for (SysOption optionCode : sysOptionList) {
            OptionVo optionVo = OptionVo.newBuilder().code(optionCode.getOptionCode())
                    .description(optionCode.getDescription()).name(optionCode.getName())
                    .value(optionCode.getNowValue()).setIni(0).setValueType(optionCode.getValueType()).build();
            optionVos.add(optionVos.size(), optionVo);
        }
        OptionVo connectPassportVo = null;
        for (SystemIni systemIni : systemIniList) {
            OptionVo vo = OptionVo.newBuilder().code(systemIni.getIniid())
                    .description(systemIni.getDescription()).setValueType(systemIni.getValueType() == null ? 1 : systemIni.getValueType())
                    .name(systemIni.getName()).value(systemIni.getNowvalue()).setIni(1).build();
            if (systemIni.getIniid().equals("SYSTEM.PASSPORT.SWITCH")) {
                connectPassportVo = vo;
            }
            else {
                optionVos.add(optionVos.size(), vo);
            }
        }
        optionVos.add(0, connectPassportVo);

        return new ModelAndView("/system/ops/deployParam.ftl")
                .addObject("optionVos", optionVos);

    }

    /**
     * sys_option 参数
     */
    @RequestMapping(value = "sysOption/systemIni")
    public ModelAndView systemIni(){
        List<SystemIni> systemIniList = systemIniService.findAll();
        List<OptionVo> optionVos = new ArrayList<>();
        for (SystemIni systemIni : systemIniList) {
            OptionVo vo = OptionVo.newBuilder().code(systemIni.getIniid())
                    .description(systemIni.getDescription())
                    .setValueType(systemIni.getValueType() == null ? 0 : systemIni.getValueType())
                    .name(systemIni.getName()).value(systemIni.getNowvalue()).setIni(1).build();
            optionVos.add(vo);
        }
        return new ModelAndView("/system/ops/deployParam.ftl")
                .addObject("optionVos", optionVos)
                .addObject("fromSystemIni",true);
    }

    /**
     * sys_systemini_unit 参数
     */
    @RequestMapping(value = "sysOption/unitIni")
    public ModelAndView unitIni(String region,String regionName, String unitName){
    	  // 查找单位列表
        if (StringUtils.isNotEmpty(unitName)) {
            try {
                unitName = URLDecoder.decode(unitName, "utf-8");
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.isNotEmpty(regionName)) {
            try {
                regionName = URLDecoder.decode(regionName, "utf-8");
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    	 Pagination page = createPagination();
         String unitResult = unitRemoteService.findByRegionAndUnitName(region, unitName, SUtils.s(page));
         JSONObject unitResultJson = JSONObject.parseObject(unitResult);
         page.setMaxRowCount(unitResultJson.getIntValue("count"));
         List<UnitDto> unitList = SUtils.dt(unitResultJson.getString("data"), new TR<List<UnitDto>>() {
         });
        return new ModelAndView("/system/ops/unitIniList.ftl")
                .addObject("unitList", unitList)
                .addObject("regionCode", region == null ? "" : region)
                .addObject("regionName", regionName == null ? "" : regionName)
                .addObject("unitName", unitName == null ? "" : unitName)
                .addObject("pagination",page);
    }
    
	private  String getEClassCardVerison(String unitId) {
		UnitIni unitIni = unitIniService.getUnitIni(unitId, UnitIni.ECC_USE_VERSION);
		if(unitIni!=null&&StringUtils.isNotBlank(unitIni.getNowvalue())){
			return unitIni.getNowvalue();
		}
		String eccSysVersion = systemIniService.findValue(UnitIni.ECC_USE_VERSION);
		return eccSysVersion;
		
	}

    @RequestMapping(value = "sysOption/unitIni/edit")
    public ModelAndView unitIniEdit(String unitId){
    	int useVersion = 1; 
		if(UnitIni.ECC_USE_VERSION_STANDARD.equals(getEClassCardVerison(unitId))){
			useVersion = 1;
		}else if(UnitIni.ECC_USE_VERSION_HW.equals(getEClassCardVerison(unitId))){
			useVersion = 2;
		}
    	return new ModelAndView("/system/ops/unitIniEdit.ftl")
        .addObject("useVersion", useVersion)
    	.addObject("unitId", unitId);
    }
    
    @ResponseBody
    @RequestMapping(value = "sysOption/unitIni/save")
    public String unitIniSave(String unitId,int useVersion){
	    try{
	    	UnitIni unitIni = unitIniService.getUnitIni(unitId, UnitIni.ECC_USE_VERSION);
	    	if(unitIni==null){
	    		unitIni = new UnitIni();
	    		unitIni.setIniid(UnitIni.ECC_USE_VERSION);
	    		unitIni.setName("班牌版本");
	    		unitIni.setNowvalue(getVersion(useVersion));
	    		unitIni.setUnitid(unitId);
	    		unitIni.setId(Long.toString(unitIniService.findMaxId()+1));
	    		unitIniService.save(unitIni);
	    	}else{
	    		if(useVersion==1){
	    			if(!UnitIni.ECC_USE_VERSION_STANDARD.equals(unitIni.getNowvalue())){
	    				unitIniService.updateNowvalue(UnitIni.ECC_USE_VERSION_STANDARD, UnitIni.ECC_USE_VERSION, unitId);
	    			}
	    		}else if(useVersion==2){
	    			if(!UnitIni.ECC_USE_VERSION_HW.equals(unitIni.getNowvalue())){
	    				unitIniService.updateNowvalue(UnitIni.ECC_USE_VERSION_HW, UnitIni.ECC_USE_VERSION, unitId);
	    			}
	    		}
	    	}
    	}catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
    	return success("保存成功");
    }

    private String getVersion(int num){
    	switch (num) {
		case 1:
			return UnitIni.ECC_USE_VERSION_STANDARD;
		case 2:
			return UnitIni.ECC_USE_VERSION_HW;
		default:
			return "";
		}
    }
    @RequestMapping("/sysOption")
    @ControllerInfo("访问平台参数列表")
    public Object sysOption(ModelMap map, HttpSession httpSession, boolean fromDesktop) {
        // TODO 校验是否已登录，后续改为拦截器处理
        if (httpSession.getAttribute(Constant.KEY_OPS_USER) == null) {
            return "redirect:/system/ops/login";
        }

        // 加载平台参数
        map.addAttribute("options", sysOptionService.findAll());
        return new ModelAndView("/system/ops/sysOption.ftl")
                .addObject("fromDesktop",fromDesktop);
    }

    @ResponseBody
    @RequestMapping("/modifyOption")
    @ControllerInfo("修改系统参数{code}值为{value}")
    public String modifyOption(int ini, String value, String code, HttpSession httpSession) {
        if (httpSession.getAttribute(Constant.KEY_OPS_USER) == null) {
            return "redirect:/system/ops/login";
        }

        if (Constant.SYSTEM_PASSPORT_SWITCH.equals(code)) {
            System.setProperty(Constant.SYSTEM_PASSPORT_SWITCH, value);
            System.setProperty("connection_passport", value);
        }

        PassportClient client = PassportClientUtils.getPassportClient();
        // 如果修改的是passport地址，需要重新初始化passportClient
        if (Evn.isPassport()
                && Constant.PASSPORT_URL.equals(code)
                && org.apache.commons.lang3.StringUtils.isNotBlank(value)) {
            Server server = serverService.findOneBy("code", Constant.EIS_RUN_CODE);
            int serverId = Evn.getInt(net.zdsoft.framework.entity.Constant.PASSPORT_SERVER_ID);
            String verifyKey = Evn.getString(net.zdsoft.framework.entity.Constant.PASSPORT_VERIFYKEY);
            if (server != null) {
                serverId = server.getId();
                verifyKey = server.getServerKey();
            }
            PassportClientParam p0 = new PassportClientParam(value, serverId, verifyKey);
            client.init(p0);
            System.setProperty(net.zdsoft.framework.entity.Constant.PASSPORT_URL, value);
            String printMsg = "[passportClient]--[passportClient 重新初始化 passportUrl:{0}, serverId:{1}, verifyKey:{2}]";
            System.out.println(MessageFormat.format(printMsg, value, serverId, verifyKey));
            System.setProperty(Constant.PASSPORT_URL, value);
        }
        if (Evn.isPassport()
                && Constant.PASSPORT_SECOND_URL.equals(code)
                && org.apache.commons.lang3.StringUtils.isNotBlank(value)) {
            System.setProperty(Constant.PASSPORT_SECOND_URL, value);
        }

        if (Evn.isPassport()
                && Constant.MEMBER_URL.equals(code)
                && org.apache.commons.lang3.StringUtils.isNotBlank(value)) {
            net.zdsoft.passport.remoting.system.ServerService serverService = passportServerClient.getPassportServerService();
            String passportUrl = sysOptionService.findValueByOptionCode(Constant.PASSPORT_URL);
            if (org.apache.commons.lang3.StringUtils.isBlank(passportUrl)) {
                return error("初始化passport member失败，请先维护passport地址");
            }
            CooperatorDto cooperatorDto = new CooperatorDto();
			cooperatorDto.setFootPage("");
			cooperatorDto.setHeadPage("<a href=\"\" target=\"_blank\"><img src=\"images/logo_shenma.png\" border=\"0\"></a>");
			cooperatorDto.setName("oa7Deploy_init");
			cooperatorDto.setId(0);
			cooperatorDto.setRegionId("00000");
			cooperatorDto.setPassportURL(passportUrl);
			cooperatorDto.setPortalURL("http://www.edu88.com");
			cooperatorDto.setUsable(true);
			cooperatorDto.setType(6);
			CooperatorDto dto = serverService.deployInitialize(false, passportUrl, value, cooperatorDto);
			System.setProperty(Constant.COOPERATOR_ID, String.valueOf(dto.getId()));
			sysOptionService.updateNowValueByCode(String.valueOf(dto.getId()), Constant.COOPERATOR_ID);
        }

        if (ini == 0) {
            sysOptionService.updateNowValueByCode(Objects.toString(value, ""), code);
            if (logger.isDebugEnabled()) {
                logger.debug("update base_sys_option optionCode:{}, value:{}", code, value);
            }
        }
        else {
            systemIniService.updateNowvalue(value, code);
            if (logger.isDebugEnabled()) {
                logger.debug("update sys_option iniId:{}, value:{}", code, value);
            }
        }
        return success("更新成功" + (Constant.MEMBER_URL.equals(code) ? ("，若base_sys_option->COOPERATOR.ID没有数据，" +
				"请手动填入【"+System.getProperty(Constant.COOPERATOR_ID) + "】") : ""));
    }

    @RequestMapping("/init")
    @ControllerInfo("访问激活序列号页面")
    public Object init(ModelMap map, HttpSession httpSession, boolean fromDesktop) {
        // 加载行政区划数据
        if (httpSession.getAttribute(Constant.KEY_OPS_USER) == null) {
            return "redirect:/system/ops/login";
        }

        String regionCode = sysOptionService.findValueByOptionCode(Constant.REGION_CODE);
        Region region = SUtils.dc(regionRemoteService.findByFullCode(regionCode), Region.class);
        map.put("regionCode", regionCode);
        map.put("regionName", region != null ? region.getFullName() : "未设置");
        LicenseInfo licenseInfo = licenseService.getLicense();
        if (licenseInfo != null) {
            map.put("licenseInfo", licenseInfo);
            map.put("licenseTxt", licenseService.getEncryptedLicenseStr());
            // 未过期
            map.put("hasExpire", licenseInfo.getExpireDate().before(DateUtils.getStartDate(new Date())) ? 1 : 0);
            map.put("expireDateFormat", DateUtils.date2String(licenseInfo.getExpireDate(), "yyyy年MM月dd日"));
        }
        map.put("provinceList", SUtils.dt(regionRemoteService.findProviceRegionByType("1"),Region.class));
        if (StringUtils.isNotEmpty(regionCode) && !"000000".equals(regionCode)) {

            List<Region> subRegion = SUtils.dt(regionRemoteService.findSubRegionByFullCode(region.getRegionCode()), Region.class);
            if ( (subRegion == null || subRegion.isEmpty()) ) {
                map.put("country", region);
                String cityCode = regionCode.substring(0,4) + "00";
                List<Region> countryList = SUtils.dt(regionRemoteService.findSubRegionByFullCode(regionCode.substring(0,4)), Region.class);
                countryList = EntityUtils.filter(countryList, new EntityUtils.Filter<Region>() {
                    @Override
                    public boolean doFilter(Region region) {
                        return region.getRegionCode().length() != 6;
                    }
                });
                map.put("countryList",countryList);

                map.put("city", SUtils.dc(regionRemoteService.findByFullCode(cityCode), Region.class));
                String provinceCode = regionCode.substring(0,2) + "0000";
                List<Region> cityList = SUtils.dt(regionRemoteService.findSubRegionByFullCode(regionCode.substring(0,2)), Region.class);
                cityList = EntityUtils.filter(cityList, new EntityUtils.Filter<Region>() {
                    @Override
                    public boolean doFilter(Region region) {
                        return region.getRegionCode().length()!=4;
                    }
                });
                map.put("cityList", cityList);
                map.put("province", SUtils.dc(regionRemoteService.findByFullCode(provinceCode), Region.class));
            }
            else if (!regionCode.endsWith("0000")) {

                String provinceCode = regionCode.substring(0,2) + "0000";
                map.put("province", SUtils.dc(regionRemoteService.findByFullCode(provinceCode), Region.class));
                map.put("city", region);
                List<Region> cityList = SUtils.dt(regionRemoteService.findSubRegionByFullCode(regionCode.substring(0,2)), Region.class);
                cityList = EntityUtils.filter(cityList, new EntityUtils.Filter<Region>() {
                    @Override
                    public boolean doFilter(Region region) {
                        return 4 != region.getRegionCode().length();
                    }
                });
                map.put("cityList", cityList);
                List<Region> countryList = SUtils.dt(regionRemoteService.findSubRegionByFullCode(regionCode.substring(0,4)), Region.class);
                countryList = EntityUtils.filter(countryList, new EntityUtils.Filter<Region>() {
                    @Override
                    public boolean doFilter(Region region) {
                        return 6!=region.getRegionCode().length();
                    }
                });
                map.put("countryList",countryList);
            } else {
                map.put("province", region);
                map.put("cityList", SUtils.dt(regionRemoteService.findSubRegionByFullCode(regionCode.substring(0,2)), Region.class));
            }
        }
        map.put("topAdmin",SUtils.dc(userRemoteService.findTopAdmin(), User.class));
        return new ModelAndView("/system/ops/init.ftl").addObject("fromDesktop", fromDesktop).addAllObjects(map);
    }

    /**
     * 加载已启用的应用列表
     *
     * @author dingw
     * @param map
     * @return
     */
    @RequestMapping("/serverList")
    public Object serverList(ModelMap map, boolean fromDesktop) {
        User topAdmin = SUtils.dc(userRemoteService.findTopAdmin(), User.class);
        if ( topAdmin != null ) {
            List<Server> serverList = serverService.findListBy(new String[]{"status", "serverClass"},
                    new Integer[]{AppStatusEnum.ONLINE.getValue(), ServerClassEnum.INNER_PRODUCT.getValue()});
            String fileUrl = sysOptionService.getFileUrl(getRequest().getServerName());
            for (Server server : serverList) {
                if (StringUtils.isNotBlank(server.getIconUrl())) {
                    server.setIconUrl(UrlUtils.ignoreLastRightSlash(fileUrl) + "/" + server.getIconUrl());
                }
            }
            map.put("servers", serverList);
        }
        String regionCode = sysOptionService.findValueByOptionCode(Constant.REGION_CODE);
        Region region = SUtils.dc(regionRemoteService.findByFullCode(regionCode), Region.class);
        map.put("regionCode", regionCode);
        map.put("regionName", region != null ? region.getFullName() : "未设置");
        map.put("initLicense", topAdmin != null);

        return new ModelAndView("/system/ops/serverList.ftl").addAllObjects(map).addObject("fromDesktop",fromDesktop);
    }

    @ResponseBody
    @RequestMapping("/modifyServer")
    @ControllerInfo("修改应用信息")
    public String modifyServer(Server server, HttpSession httpSession) {
        if (httpSession.getAttribute(Constant.KEY_OPS_USER) == null) {
            return "redirect:/system/ops/login";
        }

        // 先查出来再修改相关字段
        Server existServer = serverService.findOne(server.getId());
        existServer.setProtocol(server.getProtocol());
        existServer.setDomain(server.getDomain());
        existServer.setSecondDomain(server.getSecondDomain());
        existServer.setPort(server.getPort());
        existServer.setContext(server.getContext());
        existServer.setIndexUrl(server.getIndexUrl());
        if (org.apache.commons.lang3.StringUtils.isNotBlank(server.getVerifyUrl())) {
            existServer.setVerifyUrl(server.getVerifyUrl());
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(server.getInvalidateUrl())) {
            existServer.setInvalidateUrl(server.getInvalidateUrl());
        }
        //初始化系统参数
        if (Constant.EIS_RUN_CODE.equals(org.apache.commons.lang3.StringUtils.trim(existServer.getCode()))) {
            String serverId = Objects.getVaule(existServer.getId(), "");
            String serverKey = existServer.getServerKey();
            if (org.apache.commons.lang3.StringUtils.isBlank(serverId)
                    || org.apache.commons.lang3.StringUtils.isBlank(serverKey)) {
                return error("desktop7 serverId或者serverKey 为空update失败，这将导致整个oa7无法访问！");
            }
            System.setProperty("passport_verifyKey", serverKey);
            System.setProperty("passport_server_id", serverId);
            PassportClientUtils.init(server.getSecondDomain(), sysOptionService.findValueByOptionCode(Constant.PASSPORT_SECOND_URL));
        }
        try {
            serverService.updateInnerAp(existServer);
        } catch (Exception e) {
            if ( e instanceof PassportException ) {
                return error("passport sys_server 更新失败" + e.getMessage());
            }
            return error("更新失败" + e.getMessage());
        }
        return success("更新成功");
    }

    @ResponseBody
    @RequestMapping("/verifyLicense")
    @ControllerInfo("激活序列号")
    public String verifyLicense(String unitName, String licenseTxt, HttpSession httpSession, String regionCode) {
        if (httpSession.getAttribute(Constant.KEY_OPS_USER) == null) {
            return "redirect:/system/ops/login";
        }
        if (Validators.isEmpty(unitName)) {
            return error("请输入顶级单位名称");
        }
        if (Validators.isEmpty(licenseTxt)) {
            return error("请输入序列号");
        }
        // 校验序列号信息
        String msg = licenseService.verifyLicense(unitName, licenseTxt);
        if (!Validators.isEmpty(msg)) {
             return error(msg);
        }
        // 解析
        LicenseInfo licenseInfo = licenseService.decodeLicense(licenseTxt);
        if (licenseInfo == null) {
            return error("序列号无效，请检查序列号是否输入正确");
        }
        if (Validators.isEmpty(regionCode) || "000000".equals(regionCode)) {
            return error("请在系统参数设置中维护平台所属行政区划信息");
        }
        try {
            opsService.activeLicense(unitName, licenseTxt, regionCode);
        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
        // 返回验证成功，前台页面异步load应用配置列表页
        return success("激活成功");
    }

    @ResponseBody
    @RequestMapping("/addAdmin")
    @ControllerInfo("创建顶级机构管理员")
    public String addAdmin(String username, String password, HttpServletRequest request, HttpSession httpSession)
            throws PassportException {
        if (httpSession.getAttribute(Constant.KEY_OPS_USER) == null) {
            return "redirect:/system/ops/login";
        }
        // 先校验是否已激活序列号（创建完毕顶级单位）
        LicenseInfo licenseInfo = licenseService.getLicense();
        if (licenseInfo == null) {
            return error("请先激活序列号");
        }

        Unit topUnit = SUtils.dc(unitRemoteService.findTopUnit(), net.zdsoft.basedata.entity.Unit.class);
        if (topUnit == null) {
            return error("请先开通顶级单位（激活序列号时同时开通）");
        }
        if (Validators.isEmpty(password) || Validators.isEmpty(password)) {
            return error("请先输入管理员账号和密码");
        }

        if (checkPassword(password)) {
            // 理论上不存在配置为空的情况，此处不做判空校验
            SystemIni ini = systemIniService.findOneByIniid(Constant.SYSTEM_PASSWORD_ALERT);
            return error(ini.getNowvalue());
        }
        User admin = SUtils.dc(userRemoteService.findAdmin(topUnit.getId()), User.class);
        if (admin != null) {
            return error("顶级单位管理员已创建，用户名为" + admin.getUsername());
        }
        else {
            admin = SUtils.dc(userRemoteService.findByUsername(username), User.class);
        }

        if (admin != null) {
            return error("用户名已经被占用！(数据异常，可联系研发人员排查)");
        }

        // 此处不考虑分布式事务的问题
        Account account = null;
        User topUser = null;
        try {
            if (Evn.isPassport()) {
                account = PassportClientUtils.getPassportClient().queryAccountByUsername(username);
                topUser = SUtils.dc(userRemoteService.findByUsername(username), User.class);
                if (account != null || topUser != null) {
                    return error("该用户名已被占用");
                }
                account = null;
                topUser = null;
                account = passportServerClient.getPassportServerService().addAccount(buildAdminAccount(username, password, topUnit.getRegionCode(),
                        topUnit.getSerialNumber()));
            } else {
                account = new Account();
                account.setUsername(username);
                account.setPassword(password);
                account.setRegionId(topUnit.getRegionCode());
            }
        } catch (PassportRemotingException e) {
            return error("passport 添加顶级管理员失败");
        }

        if ( account != null ) {
            topUser = convert(account, topUnit.getId());
        } else {
            return error("passport添加顶级管理员失败");
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(topUser.getRegionCode())) {
            topUser.setRegionCode(topUnit.getRegionCode());
        }
        userRemoteService.initTopAdmin(SUtils.s(topUser));
        return success("创建成功");
    }

    private Account buildAdminAccount(String username, String password, String regionCode, String schoolId) {
        Account account = new Account();
        account.setId(UuidUtils.generateUuid());
        account.setUsername(username);
        account.setPassword(password);
        account.setRealName("管理员");
        account.setFixedType(2);
        account.setType(11);
        account.setRegionId(regionCode);
        return account;
    }

    private boolean checkPassword(String password) {
        // 理论上不存在配置为空的情况，此处不做判空校验
        SystemIni ini = systemIniService.findOneByIniid(Constant.SYSTEM_PASSWORD_EXPRESSION);
        Pattern pattern = Pattern.compile(ini.getNowvalue());
        Matcher matcher = pattern.matcher(password);
        return !matcher.find();
    }

    /**
     * 检测弱密码规则
     *
     * @author dingw
     * @param password
     * @return
     */
    private boolean checkWeakPassword(String password) {
        // 理论上不存在配置为空的情况，此处不做判空校验
        SystemIni ini = systemIniService.findOneByIniid(Constant.SYSTEM_PASSWORD_STRONG);
        Pattern pattern = Pattern.compile(ini.getNowvalue());
        Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }

    public static void main(String[] args){
        Pattern pattern = Pattern.compile("[\\w\\W]{6,18}");
        Matcher matcher = pattern.matcher("12345");
        System.out.println(matcher.find());
    }

    private User convert(Account account, String unitId) {
        User user = new User();
        user.setId(UuidUtils.generateUuid());
        user.setOwnerId(UuidUtils.generateUuid());
        user.setOwnerType(2);
        user.setRealName(account.getRealName());
        if (org.apache.commons.lang3.StringUtils.isBlank(user.getRealName())) {
            user.setRealName("管理员");
        }
        user.setUnitId(unitId);
        user.setUsername(account.getUsername());
        user.setPassword(account.getPassword());
        user.setRegionCode(account.getRegionId());
        user.setUserType(0);
        user.setUserState(1);
        Date date = new Date();
        user.setCreationTime(date);
        user.setModifyTime(date);
        user.setIsDeleted(0);
        user.setEventSource(0);
        user.setIconIndex(0);
        user.setUserRole(2);
        user.setAccountId(account.getId());
        user.setEnrollYear(2);
        return user;
    }

    private void fill(String key, String value, Map<String,Object> paraMap){
        if ( org.apache.commons.lang3.StringUtils.isNotBlank(value) ) {
            paraMap.put(key, value);
        }
    }
}
