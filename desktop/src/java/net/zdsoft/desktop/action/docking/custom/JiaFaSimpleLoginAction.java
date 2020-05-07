package net.zdsoft.desktop.action.docking.custom;

import java.net.URLEncoder;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.constant.custom.JiaFaConstant;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.desktop.action.docking.SimpleLoginAction;
import net.zdsoft.desktop.constant.DeployRegion;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.SUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping(value = "/homepage")
public class JiaFaSimpleLoginAction extends SimpleLoginAction{
    
	@RequestMapping(value = "/remote/openapi/hainan/login")
	public String loginForLYjy(HttpServletRequest request,HttpServletResponse response, ModelMap map) {
		//判断部署地区是否是佳发
//		if(doProvingDeploy(DeployRegion.DEPLOY_JIAFA)){
//			return promptFlt(map,"不能访问地址");
//		}
		//第一步得到当前的username
		String username = null ;
		String token = request.getParameter(JiaFaConstant.JF_TOKEN_NAME);
		if(StringUtils.isNotBlank(token) ){
			username = getUserName(token);
		}
		//开始登陆
		if(StringUtils.isNotBlank(username)){
			try {
				if(!StringUtils.startsWith(username, JiaFaConstant.JF_BEFORE_USERNAME_VALUE)) {
					username = JiaFaConstant.JF_BEFORE_USERNAME_VALUE + username;
				}
				User user = SUtils.dc(userRemoteService.findByUsername(username), User.class);
				if(user != null){
					String backUrl = "/homepage/remote/openapi/login/index?uid=" + Base64.getEncoder().encodeToString(user.getUsername().getBytes());
					String logoutResult = logoutBeforeUsername(user.getUsername(), backUrl);
					if(StringUtils.isNotBlank(logoutResult)){	
						return logoutResult;
					}
					return loginUserName(user, null, "1", map);
				}else{
					return promptFlt(map,"登录的用户不存在");
				}
			}catch (Exception e) {
				return promptFlt(map,"登录出现异常，请稍后登录");
			}
		}
		return promptFlt(map,"登录出现异常，请稍后登录");
	}
	
	/**
     * 获取登陆的账号
     * @param code
     * @param state
     * @return
     */
    private String getUserName(String token) {
    	String username = null;
		String accessToken = getAccessToken();
		if(StringUtils.isNotBlank(accessToken)){
			 username = getUserNameByToken(accessToken, token);
	    }
		return username;
    }
    
	/**
     * 获取到token值
     * @param code
     * @param redirectUri
     * @return
     */
    private  String getAccessToken() {
		String url = JiaFaConstant.JF_GET_ACCESS_TOKEN_URL;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("AppId", JiaFaConstant.JF_APP_ID_VAL);
			jsonObject.put("AppKey", JiaFaConstant.JF_APP_KEY_VAL);
			JSONObject dataJsonObject = new JSONObject();
			dataJsonObject.put("Certification", jsonObject);
			HttpEntity<String> requestEntity = new HttpEntity<String>(dataJsonObject.toJSONString(),headers);
			ResponseEntity<String> responseBody = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			JSONObject json = Json.parseObject(responseBody.getBody());
			String data = json.getString("Data");
			if(StringUtils.isNotBlank(data)){
				JSONObject daObject = Json.parseObject(data);
				return URLEncoder.encode(daObject.getString("AccessToken"), "utf-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    
    /**
     * 根据token获取当前登陆的账号
     * @param accessToken
     * @return
     */
    private  String getUserNameByToken(String accessToken, String token) {
		String url = JiaFaConstant.JF_GET_USER_INFO_URL;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set("accesstoken", accessToken);
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("TokenID", token);
			jsonObject.put("OrgCode", JiaFaConstant.JF_ORG_CODE_VAL);
			JSONObject dataJsonObject = new JSONObject();
			dataJsonObject.put("Data", jsonObject);
			HttpEntity<String> requestEntity = new HttpEntity<String>(dataJsonObject.toJSONString(),headers);
			ResponseEntity<String> responseBody = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			JSONObject json = Json.parseObject(responseBody.getBody());
			String data = json.getString("data");
			if(StringUtils.isNotBlank(data)){
				JSONObject daObject = Json.parseObject(data);
				return URLEncoder.encode(daObject.getString("account_name"), "utf-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
