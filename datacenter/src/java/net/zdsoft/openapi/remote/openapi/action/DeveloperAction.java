/* 
 * @(#)DeveloperAction.java    Created on 2017-2-20
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.openapi.remote.openapi.action;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.zdsoft.base.entity.eis.Developer;
import net.zdsoft.base.enums.ApplyStatusEnum;
import net.zdsoft.base.enums.YesNoEnum;
import net.zdsoft.base.service.DeveloperService;
import net.zdsoft.base.service.OpenApiApplyService;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.framework.utils.Validators;
import net.zdsoft.openapi.remote.openapi.constant.OpenApiConstants;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
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

import com.alibaba.fastjson.JSONObject;

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
    private static final int DEAFAULT_SERVERID = 000000;// 默认应用编号
    private static final int DEFAULT_ROOT = 1;// 默认部署根目录
    @Resource
    private DeveloperService developerService;
    @Resource
    private OpenApiApplyService applyService;
    
    /**
     * 管理页面模版
     * 
     * @author chicb
     * @return
     */
    @RequestMapping("/home")
    public String home(ModelMap model) {
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
//        putLogoInfo(model);
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
//        putLogoInfo(map);
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
//    	putLogoInfo(map);
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
//        putLogoInfo(map);
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
        developer.setCreationTime(new Timestamp(new Date().getTime()));
        developer.setModifyTime(new Timestamp(new Date().getTime()));
        String pwd = encodePwd(developer.getPassword());
        developer.setPassword(pwd);
        developer.setApKey(UuidUtils.generateUuid());
        developer.setUserType(Developer.IS_COMMON_TYPE);
        developer.setUnitName(developer.getUnitName());
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
    public String modifyPwd(String oldPwd, String newPwd, HttpSession httpSession) {
        if (pwdValide(oldPwd, httpSession)) {
            Developer userInfo = getDeveloper();
            String encodePwd = encodePwd(newPwd);
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
        if ("username".equals(name)) {
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
        else if ("passwordVail".equals(name)) {
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
//        logoInfoMap = judgeLogoInfoMap(logoInfoMap);
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
