package net.zdsoft.api.index.action;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.zdsoft.api.base.entity.eis.ApiDeveloper;
import net.zdsoft.api.base.service.ApiDeveloperService;
import net.zdsoft.api.openapi.remote.openapi.action.ApiBaseAction;
import net.zdsoft.api.openapi.remote.openapi.action.VerifyCodeAction;
import net.zdsoft.api.openapi.remote.openapi.constant.OpenApiConstants;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.UuidUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;

/**
 * 登录开放者功能
 * @author yangsj
 *
 */
@Controller
@RequestMapping(value = {"/bigdata/api", "/bigdata/api/openapiSup/home/"})
public class ApiLoginAction extends ApiBaseAction{
	    @Autowired
	    private ApiDeveloperService developerService;

	    @ResponseBody
	    @ControllerInfo("登入开发者平台")
	    @RequestMapping("/login/page")
	    public Response showLoginUser(String uid, String pwd, HttpServletRequest request,
	    		HttpServletResponse response, HttpSession httpSession, ModelMap map) {
	        ApiDeveloper user = developerService.findByUsername(uid);
	        String message;
	        if (user != null) {
	        	String password = user.getPassword();
	        	if(password.length() == 64){
	        		password = PWD.decode(password);
	        	}
	        	if (StringUtils.equalsIgnoreCase(pwd, password)) {
	        		setDeveloper(user);
	        		return Response.ok().build();
	        	}else {
	        		message = "密码错误！";
		        }
	        }else {
	        	message = "用户不存在！";
	        }
		    return Response.error().message(message).build();
	    }

	    @RequestMapping("/logout/page")
	    @ControllerInfo("登出系统")
	    public String showLogout(HttpServletRequest request,ModelMap map) {
	        HttpSession session = getRequest().getSession();
	        session.removeAttribute(OpenApiConstants.DEVELOPER_SESSION);
	        return "redirect:/bigdata/api/index";
	    }
	    
	    @ControllerInfo("进入开发者注册页")
	    @RequestMapping("/regist/page")
	    public String showRegist(ModelMap map, HttpSession httpSession) {
	        String key = PWD.encodeIfNot(VerifyCodeAction.SALT + UuidUtils.generateUuid().substring(0, 18));
	        map.addAttribute("key", key);
	        return "/api/developer/front/developerFront-register.ftl";
	    }

	    @RequestMapping("/register-agreement")
	    public String registerAgreement() {
	        return "/api/developer/front/developerFront-registerAgreement.ftl";
	    }

	    @ResponseBody
	    @RequestMapping("/validate-username")
	    public Object validateUsername(@RequestParam("username") String username) {
	        ApiDeveloper apiDeveloper = developerService.findByUsername(username);
	        if (Objects.isNull(apiDeveloper)) {
	            return Response.ok().build();
	        } else {
	            return Response.error().build();
	        }
	    }

	    @ResponseBody
	    @ControllerInfo("注册开发者")
	    @RequestMapping("/regist/save/page")
	    public String showRegistSave(ApiDeveloper user, String password, HttpServletRequest request,
	            HttpServletResponse response, HttpSession httpSession, ModelMap map) {
	        try {
	            String id = UuidUtils.generateUuid();
	            user.setId(id);
	            user.setTicketKey(UuidUtils.generateUuid());
	            user.setCreationTime(new Date());
	            user.setIsDeleted(0);
	            user.setModifyTime(user.getCreationTime());
	            //TODO 临时生成ApKey 杨帅杰那边会有接口提供
	            user.setApKey(UuidUtils.generateUuid());
	            developerService.save(user);
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	            return error("操作失败");
	        }
	        return success("注册成功");
	    }
}
