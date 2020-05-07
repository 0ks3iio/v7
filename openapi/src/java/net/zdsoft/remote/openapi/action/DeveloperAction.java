/* 
 * @(#)DeveloperAction.java    Created on 2017-2-20
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.remote.openapi.action;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.framework.utils.Validators;
import net.zdsoft.remote.openapi.constant.OpenApiConstants;
import net.zdsoft.remote.openapi.entity.Developer;
import net.zdsoft.remote.openapi.enums.ApplyStatusEnum;
import net.zdsoft.remote.openapi.enums.YesNoEnum;
import net.zdsoft.remote.openapi.service.DeveloperService;
import net.zdsoft.remote.openapi.service.OpenApiApplyService;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.remote.service.ServerRemoteService;
import net.zdsoft.system.remote.service.SysOptionRemoteService;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Chicb
 * @version $Revision: 1.0 $, $Date: 2017-2-20 下午05:21:53 $
 */
@Controller
@RequestMapping(value = { "/developer/" })
public class DeveloperAction extends OpenApiBaseAction {
    private static final String USERNAMEVAL = "3-16个字母组成，区分大小写";
    private static final String PASSWORDVAL = "6-16个字符，区分大小写";
    private static final String UNITNAMEVAL = "2-30个中文字符";
    private static final String REALNAMEVAL = "2-20个中文字符";
    private static final String PHONEVAL = "手机号格式不正确";
    private static final String EMAILVAL = "请填写正确的邮箱地址";

    private static final String DEFAULTTICKET = "8A5F449F5A8A3716015AAC13B7EE4041";// 默认ticket
    private static final String DEFAULTKEY = "X1J9BHLLBCA4QQL5F1UP6SACS1E2K32Y";// 默认appKey
    private static final String DEFAULT_INDEX_URL = "http://www.demo.net/index.aspx";
    private static final String DEFAULT_VERIFY_URL = "http://www.demo.net/verify.aspx";
    private static final int DEAFAULT_SERVERID = 000000;// 默认应用编号
    private static final int DEFAULT_ROOT = 1;// 默认部署根目录
    @Resource
    private SysOptionRemoteService sysOptionRemoteService;
    @Resource
    private DeveloperService developerService;
    @Resource
    private ServerRemoteService serverRemoteService;
    @Resource
    private OpenApiApplyService applyService;
    
//    /**
//     * 进入数据管理模块
//     * 
//     * @author chicb
//     * @return
//     */
//    @RequestMapping("/showpage")
//    public String dataManageShow(@RequestParam(value = "applyStatus", defaultValue = "0") int applyStatus, ModelMap map) {
//        Developer developer = getDeveloper();
//        int isLogin = YesNoEnum.NO.getValue();
//        List<String> types = null;
//        if (null != developer) {
//            isLogin = YesNoEnum.YES.getValue();
//            types = applyService.getTypes(ApplyStatusEnum.PASS_VERIFY.getValue(), developer.getId());
//            map.put("developerId", developer.getId());
//            map.put("ticketKey", developer.getTicketKey());
//        }
//        map.put("openApiApplys", getTypes(types));
//        map.put("isLogin", isLogin);
//        return "/openapi/businessData/businessData.ftl";
//    }
    /**
     * 管理页面模版
     * 
     * @author chicb
     * @return
     */
    @RequestMapping("/home")
    public String home(ModelMap model) {
        putLogoInfo(model);
        model.addAttribute("developer", getDeveloper());
        return "/openapi/homepage/home.ftl";
    }

    /**
     * 开发文档页面
     * 
     * @author wulinhao
     * @return
     */
    @RequestMapping("/devDoc")
    public String devDoc(ModelMap model) {
        putLogoInfo(model);
        //添加所有的接口类型
        Developer developer = getDeveloper();
        int islogin = YesNoEnum.NO.getValue();
        List<String> types = null;
        if (null != developer) {
        	islogin = YesNoEnum.YES.getValue();
            types = applyService.getTypes(ApplyStatusEnum.PASS_VERIFY.getValue(), developer.getId());
            model.put("showCountInter", true);
        }
        model.addAttribute("islogin", islogin);
        model.addAttribute("developer",developer);
        model.put("openApiApplys", getTypes(types));
        
        return "/openapi/devDoc/devDoc.ftl";
    }
    
    /**
     * 用户信息管理页面
     * 
     * @author chicb
     * @return
     */
    @RequestMapping("/userInfo")
    public String userInfo(ModelMap map) {
        map.put("user", getDeveloper());
        return "/openapi/userInfo/userInfo.ftl";
    }

    /**
     * 条款页面
     * 
     * @author chicb
     * @param map
     * @return
     */
    @RequestMapping("/showClause")
    public String clausePage(ModelMap map) {
        putLogoInfo(map);
        return "/openapi/homepage/clausePage.ftl";
    }

    /**
     * 开发者主页
     * 
     * @author chicb
     * @param islogin
     * @param httpSession
     * @param map
     * @return
     */
    @RequestMapping("/index")
    public String index(@RequestParam(value = "islogin", defaultValue = "0") int islogin,ModelMap map) {
    	putLogoInfo(map);
        Developer devloper = getDeveloper();
        if (devloper != null) {
            // return "redirect:/developer/home";
            islogin = YesNoEnum.YES.getValue();
            map.put("name", devloper.getUsername());
        }
        map.put("islogin", islogin);
//        putLogoInfo(map);
        return "/openapi/homepage/index.ftl";
    }

    /**
     * 登录开发者平台
     * 
     * @author chicb
     * @param username
     * @param password
     * @param request
     * @param response
     * @param httpSession
     * @return
     */
    @ResponseBody
    @RequestMapping("/login")
    public String login(String username, String password,String developerId) {
    	Developer user;
    	if(StringUtils.isNotBlank(developerId) && StringUtils.isBlank(username)) {
    		user = developerService.findOne(developerId);
    		setDeveloper(user);
			return success("登录成功,跳转中……");
    	}else {
    		user = developerService.findByUsername(username);
    		if (user == null) {
    			return error("用户名错了，再想想！");
    		}
    		/*
    		 * if (!Validators.isEmpty(user.getIps()) && user.getIps().indexOf(ServletUtils.getRemoteAddr(getRequest())) >=
    		 * 0) { return error("此为非法请求"); }
    		 */
    		String pwd = user.getPassword();
    		if (StringUtils.length(pwd) == 64) {
    			pwd = PWD.decode(pwd);
    		}
    		if (StringUtils.equalsIgnoreCase(pwd, password)) {
    			setDeveloper(user);
    			return success("登录成功,跳转中……");
    		}
    		else {
    			return error("密码错了，再想想！");
    		}
    	}
    }

    /**
     * 退出
     * 
     * @author chicb
     * @param request
     * @param response
     * @param httpSession
     * @param map
     * @return
     */
    @RequestMapping("/logout")
    public String showLogout(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession,
            ModelMap map) {
        httpSession.invalidate();
        return "redirect:index";
    }

    /**
     * 进入注册页面
     * 
     * @author chicb
     * @return
     */
    @RequestMapping(value = { "/regist" })
    public String showRegist(ModelMap map) {
        putLogoInfo(map);
        int islogin = YesNoEnum.NO.getValue();
        if (null != getDeveloper()) {
            map.put("name", getDeveloper().getUsername());
            islogin = YesNoEnum.YES.getValue();
        }
        map.put("islogin", islogin);
        return "/openapi/homepage/regist.ftl";
    }

    /**
     * 提交注册
     * 
     * @author chicb
     * @param developer
     * @param httpSession
     * @return
     */
    @ResponseBody
    @RequestMapping(value = { "/regist/save" })
    public String saveRegist(@ModelAttribute("developer") Developer developer, HttpSession httpSession) {
        Map<String, String> result = vatedate(developer, true);
        String msg = result.get("msg");
        String obj = result.get("obj");
        if (!Validators.isBlank(msg)) {
            JSONObject json = new JSONObject();
            json.put("msg", msg);
            json.put("success", false);
            json.put("obj", obj);
            return json.toJSONString();
        }
        developer.setId(UuidUtils.generateUuid());
        developer.setTicketKey(UuidUtils.generateUuid());
        developer.setCreationTime(new Date());
        String pwd = encodePwd(developer.getPassword());
        developer.setPassword(pwd);
        try {
            developerService.save(developer);
        }
        catch (Exception e) {
            return error("注册失败请刷新后重试");
        }
        setDeveloper(developer);
        return success("success");
    }

    @ResponseBody
    @RequestMapping("/modifyUser")
    public String modifyUser(@ModelAttribute("developer") Developer developer) {
        Map<String, String> result = vatedate(developer, false);
        String msg = result.get("msg");
        if (!Validators.isBlank(msg)) {
            return error(msg);
        }
        Developer userInfo = getDeveloper();
        developer.setId(userInfo.getId());
        int reslult = developerService.updateDeveloper(developer);
        if (reslult == YesNoEnum.YES.getValue()) {
            developer.setTicketKey(userInfo.getTicketKey());
            developer.setPassword(userInfo.getPassword());
            developer.setUsername(userInfo.getUsername());
            setDeveloper(developer);
            return success("修改成功");
        }
        return error("修改失败请刷新页面后重试");
    }

    @ResponseBody
    @RequestMapping("/modifyPwd")
    public String modifyPwd(String oldParam, String newParam, HttpSession httpSession) {
        if (pwdValide(oldParam, httpSession)) {
            Developer userInfo = getDeveloper();
            String encodePwd = encodePwd(newParam);
            int i = developerService.updatePwd(encodePwd, userInfo.getId());
            if (i == 1) {
                userInfo.setPassword(encodePwd);
                setDeveloper(userInfo);
                return success("");
            }
        }
        return error("");
    }

    /**
     * 校验
     * 
     * @author chicb
     * @param name
     * @param value
     * @return
     */
    @ResponseBody
    @RequestMapping(value = { "/regist/valid/{name}/{value}" }, method = RequestMethod.GET)
    public String validate(@PathVariable("name") String name, @PathVariable("value") String value,
            HttpSession httpSession) {
        if ("paramName".equals(name)) {
            if (Validators.isName(value, 3, 16)) {
                Developer user = developerService.findByUsername(value);
                if (user == null) {
                    return success("success");
                }
            }
            return error("");
        }
        else if ("verify".equals(name)) {
            String code = RedisUtils.get(OpenApiConstants.VERIFY_CODE_CACHE_KEY + httpSession.getId());
            if (!Validators.isBlank(code) && !Validators.isBlank(value) && StringUtils.equalsIgnoreCase(code, value)) {
                return success("success");
            }
            return error("");
        }
        else if ("oldParamVail".equals(name)) {
            if (pwdValide(value, httpSession)) {
                return success("success");
            }
            return error("");
        }
        return "";
    }

    @ResponseBody
    @RequestMapping("/verifyImage")
    public String execute(@RequestParam(value = "codeLength", required = false, defaultValue = "4") Integer codeLength,
            HttpServletResponse response, HttpSession session) {
        try {
            String code = getCode(codeLength);
            RedisUtils.del(OpenApiConstants.VERIFY_CODE_CACHE_KEY + session.getId());
            RedisUtils.set(OpenApiConstants.VERIFY_CODE_CACHE_KEY + session.getId(), code, 5 * 60);
            VerifyImage verifyImage = new VerifyImage(30, 80);
            verifyImage.setFont(25);
            verifyImage.setBgColor(Color.WHITE);
            OutputStream outputStream = response.getOutputStream();
            byte[] bytes = verifyImage.export(code);
            outputStream.write(bytes);
            outputStream.flush();
        }
        catch (Exception e) {
            log.error("验证码生成失败", e);
        }
        return "";
    }

    /**
     * @auth: zhuzq
     * @date: 2017/3/13 9:23
     * @description: 单点登录
     */
    @RequestMapping("/sso/passportDocument")
    public String passportDocument(ModelMap map) {
        String passportUrl = sysOptionRemoteService.findValue(Constant.PASSPORT_URL);
        String checkTicketUrl = sysOptionRemoteService.findValue(Constant.PASSPORT_CHECKTICKET);
        String clientLoginUrl = sysOptionRemoteService.findValue(Constant.PASSPORT_CLIENTLOGIN);
        String demoDownloadUrl = sysOptionRemoteService.findValue(Constant.PASSPORT_DEMO_LINK);
        String docDownloadUrl = sysOptionRemoteService.findValue(Constant.PASSPORT_DOC_LINK);
        String activeLogoutUrl = sysOptionRemoteService.findValue(Constant.PASSPORT_ACTIVE_LOGOUT);// 主动退出请求url地址
        String verifyUrl = sysOptionRemoteService.findValue(Constant.PASSPORT_VERIFY_RUL);

        Developer developer = getDeveloper();
        List<Server> serverList = new ArrayList<Server>();
        String clearPassportUrl = UrlUtils.ignoreLastRightSlash(passportUrl);
        map.put("passportUrl", passportUrl);
        map.put("checkTicketUrl", clearPassportUrl + "/api/checkTicket.htm");
        map.put("clientLoginUrl", clearPassportUrl + "/api/clientLogin.htm");
        map.put("demoDownloadUrl", clearPassportUrl + "/passport-java-demo.zip");
        map.put("docDownloadUrl", docDownloadUrl);
        map.put("activeLogoutUrl", clearPassportUrl + "/logout.htm");
        map.put("verifyUrl", clearPassportUrl + "/login");
        if (developer != null) {
            serverList = SUtils.dt(serverRemoteService.getAppsByDevId(developer.getId()), new TypeReference<List<Server>>(){});
        }
        map.put("serverList", serverList);
        return "/openapi/passportpage/passportDocument.ftl";
    }

    @RequestMapping("/sso/resource")
    public String passportSource(ModelMap modelMap) {
        String demoDownloadUrl = sysOptionRemoteService.findValue(Constant.PASSPORT_DEMO_LINK);
        String docDownloadUrl = sysOptionRemoteService.findValue(Constant.PASSPORT_DOC_LINK);
        modelMap.put("demoDownloadUrl", demoDownloadUrl);
        modelMap.put("docDownloadUrl", UrlUtils.getPrefix(getRequest()) + docDownloadUrl);
        return "/openapi/passportpage/resources.ftl";
    }

    @ResponseBody
    @RequestMapping("/sso/changeServerOfLogin")
    public String changeServerOfLogin(String passport, String serverId) {

        JSONObject json = new JSONObject();
        if (!StringUtils.isEmpty(serverId) && NumberUtils.isNumber(serverId)) {
            Server server = serverRemoteService.getAppByAppId(NumberUtils.toInt(serverId));
            String auth = "";
            String decodeAuth = "";
            String verifyEncodeAuth = "";
            String verifyDecodeAuth = "";
            StringBuffer buffer = new StringBuffer(passport);
            if (server != null) {
                String appKey = server.getServerKey();
                Integer root = 0;
                String context = server.getContext();
                String serverIndexUrl = server.getUrlIndex(Boolean.FALSE);
                if (StringUtils.isEmpty(context) || "/".equals(context)) {
                    root = 1;
                    decodeAuth = appKey + serverId + serverIndexUrl + root;
                }else{
                	decodeAuth = appKey + serverId + serverIndexUrl + context + root;
                }
                auth = DigestUtils.md5Hex(decodeAuth);
                buffer.append("?server=").append(serverId).append("&url=").append(UrlUtils.encode(serverIndexUrl, "UTF-8")).append("&root=").append(root)
                        .append("&auth=").append(auth);
                if(0 == root){
                	buffer.append("&context=").append(context);
                }
                json.put("root", root);
                json.put("context", context);
                json.put("auth", auth);
                json.put("url", buffer.toString());
                json.put("decodeAuth", decodeAuth);
                json.put("indexUrlOfLogin", serverIndexUrl);
                
                //得到 verifyDecodeAuth  verifyEncodeAuth
                verifyDecodeAuth = DEFAULTTICKET + serverIndexUrl + serverId + appKey;
                verifyEncodeAuth = DigestUtils.md5Hex(verifyDecodeAuth);
                json.put("verifyUrl", server.getVerifyUrl());
                json.put("verifyEncodeAuth", verifyEncodeAuth);
                json.put("verifyDecodeAuth", verifyDecodeAuth);
                StringBuffer urlOfVerify = new StringBuffer().append(server.getVerifyUrl()).append("?ticket=").append(DEFAULTTICKET)
                		.append("&url=").append(UrlUtils.encode(serverIndexUrl, "UTF-8")).append("&auth=").append(verifyEncodeAuth);
                json.put("urlOfVerify", urlOfVerify);
            }
            else {
                decodeAuth = DEFAULTKEY + serverId + DEFAULT_INDEX_URL + DEFAULT_ROOT;
                auth = DigestUtils.md5Hex(decodeAuth);
                buffer.append("?server=").append(serverId).append("&url=").append(DEFAULT_INDEX_URL).append("&root=")
                        .append(DEFAULT_ROOT).append("&auth=").append(auth);
                int root = 0;
                String context = "/";
                json.put("root", root);
                json.put("context", context);
                json.put("auth", auth);
                json.put("url", buffer.toString());
                json.put("decodeAuth", decodeAuth);
                json.put("indexUrlOfLogin", DEFAULT_INDEX_URL);
                
              //得到 verifyDecodeAuth  verifyEncodeAuth
                verifyDecodeAuth = DEFAULTTICKET + DEFAULT_INDEX_URL + serverId + DEFAULTKEY;
                verifyEncodeAuth = DigestUtils.md5Hex(verifyDecodeAuth);
                json.put("verifyUrl", DEFAULT_INDEX_URL);
                json.put("verifyEncodeAuth", verifyEncodeAuth);
                json.put("verifyDecodeAuth", verifyDecodeAuth);
                StringBuffer urlOfVerify = new StringBuffer().append(DEFAULT_VERIFY_URL).append("?ticket=").append(DEFAULTTICKET)
                		.append("&url=").append(DEFAULT_INDEX_URL).append("&auth=").append(verifyEncodeAuth);
                json.put("urlOfVerify", urlOfVerify);
            }
        }
        return json.toJSONString();
    }

    @ResponseBody
    @RequestMapping("/sso/modifyUrlOfLogin")
    public String modifyUrlOfLogin(String passport, String serverId, String url, String root) {
        JSONObject json = new JSONObject();
        if (!StringUtils.isEmpty(serverId) && NumberUtils.isNumber(serverId)) {
            Server server = serverRemoteService.getAppByAppId(NumberUtils.toInt(serverId));
            String endodeUrl = UrlUtils.encode(url, "utf-8");
            if (server != null) {
                String decodeAuth = server.getServerKey() + serverId + url + root;
                String authOfLogin = DigestUtils.md5Hex(decodeAuth);
                StringBuffer urlOfLogin = new StringBuffer(passport).append("?server=").append(serverId)
                        .append("&url=").append(endodeUrl).append("&root=").append(root).append("&auth=").append(authOfLogin);
                json.put("auth", authOfLogin);
                json.put("url", urlOfLogin.toString());
                json.put("decodeAuth", decodeAuth);
            }
            else {
                String decodeAuth = DEFAULTKEY + serverId + url + root;
                String authOfLogin = DigestUtils.md5Hex(decodeAuth);
                StringBuffer urlOfLogin = new StringBuffer(passport).append("?server=").append(serverId)
                        .append("&url=").append(endodeUrl).append("&root=").append(root).append("&auth=").append(authOfLogin);
                json.put("auth", authOfLogin);
                json.put("url", urlOfLogin.toString());
                json.put("decodeAuth", decodeAuth);
            }
        }
        return json.toJSONString();
    }

    @ResponseBody
    @RequestMapping("/sso/changeTicketOfCheckTicket")
    public String changeTicketOfCheckTicket(String ticket, String serverId, String checkTicket) {

        JSONObject json = new JSONObject();
        if (!StringUtils.isEmpty(serverId) && NumberUtils.isNumber(serverId)) {
            Server server = serverRemoteService.getAppByAppId(NumberUtils.toInt(serverId));
            if (server != null) {
                String appKey = server.getServerKey();
                String decodeAuth = appKey + serverId + ticket;
                String auth = DigestUtils.md5Hex(decodeAuth);
                StringBuffer url = new StringBuffer(checkTicket).append("?serverId=").append(serverId)
                        .append("&ticket=").append(ticket).append("&auth=").append(auth);
                json.put("auth", auth);
                json.put("url", url.toString());
                json.put("decodeAuth", decodeAuth);
            }
            else {
                String decodeAuth = DEFAULTKEY + serverId + ticket;
                String auth = DigestUtils.md5Hex(DEFAULTKEY + serverId + ticket);
                StringBuffer url = new StringBuffer(checkTicket).append("?serverId=").append(serverId)
                        .append("&ticket=").append(ticket).append("&auth=").append(auth);
                json.put("auth", auth);
                json.put("url", url.toString());
                json.put("decodeAuth", decodeAuth);
            }
        }
        return json.toJSONString();
    }

    @ResponseBody
    @RequestMapping("/sso/changeServerOfCheckTicket")
    public String changeServerOfCheckTicket(String ticket, String serverId, String checkTicket) {

        JSONObject json = new JSONObject();
        if (!StringUtils.isEmpty(serverId) && NumberUtils.isNumber(serverId)) {
            Server server = serverRemoteService.getAppByAppId(NumberUtils.toInt(serverId));
            if (server != null) {
                String appKey = server.getServerKey();
                String decodeAuth = appKey + serverId + ticket;
                String auth = DigestUtils.md5Hex(decodeAuth);
                StringBuffer url = new StringBuffer(checkTicket).append("?serverId=").append(serverId)
                        .append("&ticket=").append(ticket).append("&auth=").append(auth);
                json.put("auth", auth);
                json.put("url", url.toString());
                json.put("decodeAuth", decodeAuth);
            }
            else {
                String decodeAuth = DEFAULTKEY + serverId + ticket;
                String auth = DigestUtils.md5Hex(decodeAuth);
                StringBuffer url = new StringBuffer(checkTicket).append("?serverId=").append(serverId)
                        .append("&ticket=").append(ticket).append("&auth=").append(auth);
                json.put("auth", auth);
                json.put("url", url.toString());
                json.put("decodeAuth", decodeAuth);
            }
        }
        return json.toJSONString();
    }

    @ResponseBody
    @RequestMapping("/sso/changeServerOfActiveExit")
    public String changeServerOfActiveExit(String serverId, String ticket, String urlOfActiveExit) {

        JSONObject json = new JSONObject();
        if (!StringUtils.isEmpty(serverId) && NumberUtils.isNumber(serverId)) {
            Server server = serverRemoteService.getAppByAppId(NumberUtils.toInt(serverId));
            if (server != null) {
                String decodeAuth = server.getServerKey() + serverId + ticket;
                String auth = DigestUtils.md5Hex(decodeAuth);
                StringBuffer url = new StringBuffer(urlOfActiveExit).append("?server=").append(serverId)
                        .append("&ticket=").append(ticket).append("&auth=").append(auth);
                json.put("auth", auth);
                json.put("url", url.toString());
                json.put("serverId", serverId);
                json.put("decodeAuth", decodeAuth);
            }
            else {
                String decodeAuth = DEFAULTKEY + serverId + ticket;
                String auth = DigestUtils.md5Hex(decodeAuth);
                StringBuffer url = new StringBuffer(urlOfActiveExit).append("?server=").append(serverId)
                        .append("&ticket=").append(ticket).append("&auth=").append(auth);
                json.put("auth", auth);
                json.put("url", url.toString());
                json.put("serverId", serverId);
                json.put("decodeAuth", decodeAuth);
            }
        }
        return json.toJSONString();
    }

    @ResponseBody
    @RequestMapping("/sso/modifyTicketOfActiveExit")
    public String modifyTicketOfActiveExit(String serverId, String ticket, String urlOfActiveExit) {
        JSONObject json = new JSONObject();
        if (!StringUtils.isEmpty(serverId) && NumberUtils.isNumber(serverId)) {
            Server server = serverRemoteService.getAppByAppId(NumberUtils.toInt(serverId));
            if (server != null) {
                String decodeAuth = server.getServerKey() + serverId + ticket;
                String auth = DigestUtils.md5Hex(decodeAuth);
                StringBuffer url = new StringBuffer(urlOfActiveExit).append("?server=").append(serverId)
                        .append("&tikcet=").append(ticket).append("&auth=").append(auth);
                json.put("auth", auth);
                json.put("url", url.toString());
                json.put("decodeAuth", decodeAuth);

            }
            else {
                String decodeAuth = DEFAULTKEY + serverId + ticket;
                String auth = DigestUtils.md5Hex(decodeAuth);
                StringBuffer url = new StringBuffer(urlOfActiveExit).append("?server=").append(serverId)
                        .append("&tikcet=").append(ticket).append("&auth=").append(auth);
                json.put("auth", auth);
                json.put("url", url.toString());
                json.put("decodeAuth", decodeAuth);
            }
        }
        return json.toJSONString();
    }

    @ResponseBody
    @RequestMapping("/sso/modifyOfClientLoign")
    public String modifyOfClientLoign(String serverId, String username, String password, String clientLoginUrl) {
        JSONObject json = new JSONObject();
        if (!StringUtils.isEmpty(serverId) && NumberUtils.isNumber(serverId)) {
            Server server = serverRemoteService.getAppByAppId(NumberUtils.toInt(serverId));
            clientLoginUrl = StringUtils.trimToEmpty(clientLoginUrl);
            if (server != null) {
                String encodePassword = DigestUtils.md5Hex(server.getServerKey() + serverId + username + password);
                StringBuilder url = new StringBuilder(clientLoginUrl).append("?username=").append(username)
                        .append("&password=").append(encodePassword).append("&serverId=").append(serverId);
                json.put("encodePassword", encodePassword);
                json.put("url", url.toString());
            }
            else {
                String encodePassword = DigestUtils.md5Hex(DEFAULTKEY + serverId + username + password);
                StringBuilder url = new StringBuilder(clientLoginUrl).append("?username=").append(username)
                        .append("&password=").append(encodePassword).append("&serverId=").append(serverId);
                json.put("encodePassword", encodePassword);
                json.put("url", url.toString());
            }
        }
        return json.toJSONString();
    }

    @ResponseBody
    @RequestMapping("/sso/download")
    public void download(String type, HttpServletRequest request, HttpServletResponse response) {
        String path = "";
        if ("doc".equals(type)) {
            path = sysOptionRemoteService.findValue(Constant.PASSPORT_DOC_LINK);
        }
        else if ("demo".equals(type)) {
            path = sysOptionRemoteService.findValue(Constant.PASSPORT_DEMO_LINK);
        }
        String fileName = path.substring(path.lastIndexOf("\\") + 1);
        InputStream in = null;
        OutputStream out = null;
        try {
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            in = new FileInputStream(path);
            int len = 0;
            byte[] bt = new byte[1024];
            out = response.getOutputStream();
            while ((len = in.read(bt)) > 0) {
                out.write(bt, 0, len);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }

        }

    }

    @RequestMapping("/devDoc/accessSpecification")
    public String accessGuide() {
        return "/openapi/devDoc/accessSpecification.ftl";
    }

    @RequestMapping("/devDoc/accessProcess")
    public String accessProcess() {
        return "/openapi/devDoc/accessProcess.ftl";
    }

    @RequestMapping("/devDoc/developerRegist")
    public String developerRegist() {
        return "/openapi/devDoc/developerRegist.ftl";
    }

    @RequestMapping("/devDoc/removeApFlow")
    public String offAssemblyLineAp () {

        return "/openapi/devDoc/removeApFlow.ftl";
    }

    @RequestMapping("/devDoc/summary")
    public String summary() {
        return "/openapi/devDoc/summary.ftl";
    }

    @RequestMapping("/devDoc/showClause")
    public String showClause() {
        return "/openapi/devDoc/showClause.ftl";
    }

    /** ----------私有方法区----------- **/
    /**
     * @author chicb
     * @param developer
     * @return
     */
    private String encodePwd(String password) {
        PWD pwd = new PWD(password);
        return pwd.encode();
    }

    @SuppressWarnings("unchecked")
    private void putLogoInfo(ModelMap map) {
        Map<String, String> logoInfoMap = (HashMap<String, String>) getRequest().getSession().getAttribute(
                OpenApiConstants.OPEN_LOGO_INFO_SESSION);
        logoInfoMap = judgeLogoInfoMap(logoInfoMap);
        map.put("logoUrl", logoInfoMap.get(OpenApiConstants.OPEN_API_LOGO_RUL));
        map.put("logoName", logoInfoMap.get(OpenApiConstants.OPEN_API_LOGO_NAME));
        map.put("foot", logoInfoMap.get(OpenApiConstants.OPEN_API_FOOT_VALUE));
        // map.put("islogin", 0);
    }

    /**
     * @author chicb
     * @param value
     * @param httpSession
     */
    private String getCode(int length) {
        if (length <= 0) {
            log.error("验证码长度非负");
            return "验证码长度非负".toString();
        }

        StringBuilder code = new StringBuilder();

        for (int i = 0; i < length; i++) {
            code.append(OpenApiConstants.VERIFY_CODE.charAt(RandomUtils.nextInt(0,
                    OpenApiConstants.VERIFY_CODE.length() - 1)));
        }

        return code.toString();
    }

    private boolean pwdValide(String password, HttpSession httpSession) {
        String pwd = getDeveloper().getPassword();
        if (StringUtils.length(pwd) == 64) {
            pwd = PWD.decode(pwd);
        }
        if (StringUtils.equalsIgnoreCase(pwd, password)) {
            return true;
        }
        return false;
    }

    private Map<String, String> judgeLogoInfoMap(Map<String, String> logoInfoMap) {
        if (MapUtils.isEmpty(logoInfoMap)) {
            logoInfoMap = new HashMap<String, String>();

            // String url = sysOptionRemoteService.findValue(Constant.PLAT_LOGO_PATH);
            String url = sysOptionRemoteService.findValue(OpenApiConstants.OPEN_API_LOGO_RUL);
            String name = sysOptionRemoteService.findValue(OpenApiConstants.OPEN_API_LOGO_NAME);
            logoInfoMap.put(OpenApiConstants.OPEN_API_LOGO_RUL, url);
            logoInfoMap.put(OpenApiConstants.OPEN_API_LOGO_NAME, name);
            logoInfoMap.put(OpenApiConstants.OPEN_API_FOOT_VALUE,
                    sysOptionRemoteService.findValue(OpenApiConstants.OPEN_API_FOOT_VALUE));
            getRequest().getSession().setAttribute(OpenApiConstants.OPEN_LOGO_INFO_SESSION, logoInfoMap);
        }
        return logoInfoMap;
    }

    /**
     * 校验
     * 
     * @author chicb
     * @param developer
     * @return
     */
    private Map<String, String> vatedate(Developer developer, boolean isAdd) {
        Map<String, String> result = new HashMap<String, String>();
        if (isAdd) {
            if (!Validators.isString(developer.getUsername(), 3, 16)) {
                result.put("msg", USERNAMEVAL);
                result.put("obj", "username");
                return result;
            }
            if (Validators.isAlphanumeric(developer.getPassword())) {
                if (!Validators.isString(developer.getPassword(), 6, 16)) {
                    result.put("msg", PASSWORDVAL);
                    result.put("obj", "getPassword");
                    return result;
                }
            }
            else {
                result.put("msg", PASSWORDVAL);
                result.put("obj", "getPassword");
                return result;
            }
        }
        if (!Validators.isName(developer.getUnitName(), 4, 60)) {
            result.put("msg", UNITNAMEVAL);
            result.put("obj", "unitname");
            return result;
        }
        if (!Validators.isName(developer.getRealName(), 4, 40)) {
            result.put("msg", REALNAMEVAL);
            result.put("obj", "realname");
            return result;
        }
        if (!Validators.isEmail(developer.getEmail())) {
            result.put("msg", EMAILVAL);
            result.put("obj", "email");
            return result;
        }
//        if (!MobileUtils.isMobile(developer.getMobilePhone(),
//                sysOptionRemoteService.findValue(OpenApiConstants.MOBILE_REGEX),
//                sysOptionRemoteService.findValue(OpenApiConstants.UNICOM_REGEX),
//                sysOptionRemoteService.findValue(OpenApiConstants.TELCOM_REGEX))) {
//            result.put("msg", PHONEVAL);
//            result.put("obj", "phone");
//            return result;
//        }
        return result;
    }
}

class VerifyImage {
    private static final Logger LOG = LoggerFactory.getLogger(VerifyImage.class);
    private Integer height;
    private Integer width;

    private Font font;
    private Color fontColor = Color.PINK;
    private Color bgColor = Color.LIGHT_GRAY;

    private Integer randomPointNum = 30;

    public VerifyImage(Integer height, Integer width) {
        this.height = height;
        this.width = width;
    }

    public VerifyImage(Integer height, Integer width, Color fontColor, Color bgColor) {
        this(height, width);
        this.fontColor = fontColor;
        this.bgColor = bgColor;
    }

    public VerifyImage(Integer height, Integer width, Font font, Color fontColor, Color bgColor) {
        this(height, width, fontColor, bgColor);
        this.font = font;
    }

    public byte[] export(String code) {
        Assert.hasLength(code);
        try {
            BufferedImage verifyImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            Graphics graphics = verifyImage.getGraphics();
            graphics.setColor(bgColor);
            graphics.fillRect(0, 0, width, height);

            graphics.setColor(fontColor);

            int maxFontSize = font.getSize();
            for (int i = 0; i < code.length(); i++) {
                Font newFont = new Font("", RandomUtils.nextInt(0, 5), maxFontSize - RandomUtils.nextInt(0, 8));
                graphics.setFont(newFont);
                graphics.drawString(String.valueOf(code.charAt(i)),
                        10 + (i * (maxFontSize / 2)) + (i > 0 ? RandomUtils.nextInt(0, 5) : 0),
                        16 + RandomUtils.nextInt(0, 6));
                graphics.setColor(getRandomColor());
            }

            // 干扰线
            for (int i = 0; i < randomPointNum; i++) {
                graphics.setColor(getRandomColor());
                int randomX = RandomUtils.nextInt(0, width);
                int randomY = RandomUtils.nextInt(0, height);
                graphics.drawLine(randomX, randomY, randomX, randomY);
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(verifyImage, "png", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
        catch (Exception e) {
            LOG.error("无法创建验证码", e);

        }

        return null;
    }

    private Color getRandomColor() {
        return new Color(RandomUtils.nextInt(0, 256), RandomUtils.nextInt(0, 256), RandomUtils.nextInt(0, 256));
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void setFont(int size) {
        this.font = new Font("", 0, size);
    }

    public Color getFontColor() {
        return fontColor;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    public Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    public void setRandomPointNum(Integer randomPointNum) {
        this.randomPointNum = randomPointNum;
    }

    public static void main(String[] args) {
        String auth = DigestUtils
                .md5Hex("50534014SHTAPKVA15GS6BRY627ZA49PNPJXZWhttp://10.30.2.203/qboss/AdminFrame.aspx1");
        System.out.println(auth);
        System.out.println("sss");
        Date date = new Date();
        date.setTime(559285857);
        System.out.println(date.getYear());
        System.out.println(date.getMonth());
        System.out.println(date.getDay());
    }
}
