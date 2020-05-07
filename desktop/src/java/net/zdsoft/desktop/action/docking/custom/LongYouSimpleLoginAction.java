package net.zdsoft.desktop.action.docking.custom;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.constant.custom.LyConstant;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.desktop.action.docking.SimpleLoginAction;
import net.zdsoft.desktop.constant.DeployRegion;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UrlUtils;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping(value = "/homepage")
public class LongYouSimpleLoginAction extends SimpleLoginAction {

	@RequestMapping(value = "/remote/openapi/longyou/login")
	public String loginForLYjy(HttpServletRequest request,HttpServletResponse response, ModelMap map) {
		//判断部署地区是否是龙游
		if(doProvingDeploy(DeployRegion.DEPLOY_LONGYOU)){
			return promptFlt(map,"不能访问地址");
		}
		boolean isTrueBoolean = false;
		//第一步得到当前的username
		String username = null ;
		String twoParam = request.getParameter("twoParam");
		String url = request.getParameter("url");
		if(StringUtils.isNotBlank(twoParam)){
			twoParam = StringEscapeUtils.unescapeHtml(twoParam);
			JSONObject jsonObject = JSON.parseObject(twoParam);
			String uid = jsonObject.getString("uid");
			try {
				username =  new String(Base64.getDecoder().decode(uid), "UTF-8");
			} catch (Exception e) {
				return promptFlt(map,"uid参数解析失败");
			}
			url = jsonObject.getString("url");
		}else{
			//走鼎永那边的平台认证
			String code = request.getParameter(LyConstant.LY_CODE_NAME);
			String state = request.getParameter(LyConstant.LY_STATE_NAME);
			if(StringUtils.isNotBlank(code) && StringUtils.isNotBlank(state)){
				if(LyConstant.LY_STATE_VALUE.equals(state)){
					username = getUserName(code);
					isTrueBoolean = Boolean.TRUE;
				}
			}else{
				try {
					isTrueBoolean = attemptAuthen(request, response);
				} catch (ServletException | IOException e) {
					e.printStackTrace();
					return promptFlt(map,"登录出现异常，请稍后登录");
				}
			}
		}
		//开始登陆
		if( isTrueBoolean && StringUtils.isNotBlank(username)){
			try {
				User user = SUtils.dc(userRemoteService.findByUsername(username), User.class);
				if(user != null){
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("uid", Base64.getEncoder().encodeToString(user.getUsername().getBytes()));
					jsonObject.put("url", url);
					String twoParam1;
					try {
						twoParam1 = URLEncoder.encode(jsonObject.toJSONString(),"UTF-8");
					} catch (UnsupportedEncodingException e) {
						twoParam1 = null;
					}
					String backUrl = getBackUrl(twoParam1, "twoParam", "/homepage/remote/openapi/longyou/login");
					String logoutResult = logoutBeforeUsername(user.getUsername(), backUrl);
					if(StringUtils.isNotBlank(logoutResult)){	
						return logoutResult;
					}
					return loginUserName(user, url, "3", map);
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
    private String getUserName(String code) {
    	String username = null;
    	String redirectUri = getRedirectUrl(LyConstant.LY_REDIRECT_URI);
		String accessToken = getAccessToken(code,redirectUri);
		if(StringUtils.isNotBlank(accessToken)){
			 username = getUserNameByToken(accessToken);
	    }
		return username;
    }
    
	/**
     * 获取到token值
     * @param code
     * @param redirectUri
     * @return
     */
    private  String getAccessToken(String code, String redirectUri) {
		String url = LyConstant.LY_GET_ACCESS_TOKEN_URL;
		String authorization = LyConstant.LY_CLIENT_ID_VAL + ":" + LyConstant.LY_CLIENT_SECRET_VAL;
		try {
			 authorization = Base64.getEncoder().encodeToString(authorization.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			authorization = null;
			log.error("authorization参数进行base64编码失败----------" + e.getMessage());
			return null;
		}
		authorization = LyConstant.LY_GET_TOKEN_PREFIX + authorization;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.add("Authorization", authorization);
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("grant_type", "authorization_code");
			map.add("code", code);
			map.add("redirect_uri", redirectUri);
			HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
			ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
			String responseBody = responseEntity.getBody();
			JSONObject json = Json.parseObject(responseBody);
			return URLEncoder.encode(json.getString("access_token"), "utf-8");
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
    private  String getUserNameByToken(String accessToken) {
		String url = LyConstant.LY_GET_USER_INFO_URL;
		String authorization = LyConstant.LY_GET_USER_TOKEN_PREFIX + accessToken;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", authorization);
			HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
			ResponseEntity<String> responseBody = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
			JSONObject json = Json.parseObject(responseBody.getBody());
			return URLEncoder.encode(json.getString("account"), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    
    /**
     * 得到返回的地址
     * @return
     */
    private String getRedirectUrl(String url){
    	return new StringBuilder().append(UrlUtils.getPrefix(getRequest()))
				.append(url)
				.toString();
    }
    
    /**
    * 请求鼎永那边的认证地址
    * @param request
    * @param response
    * @return
    * @throws ServletException
    * @throws IOException
    */
   private boolean attemptAuthen(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    boolean isTrue = false;
       //获得id_token
		final String code = request.getParameter(LyConstant.LY_CODE_NAME);
	   	if(code == null || code.trim().length() == 0) {
	   		String redirectUri = getRedirectUrl(LyConstant.LY_REDIRECT_URI);
	   		String loginUrl = String.format("%s?client_id=%s&redirect_uri=%s&response_type=%s&scope=%s&state=%s", 
	   				LyConstant.LY_GET_CODE_URL,LyConstant.LY_CLIENT_ID_VAL, redirectUri, LyConstant.LY_CODE_NAME, 
	   				LyConstant.LY_SCODE_VALUE, LyConstant.LY_STATE_VALUE);
	   		response.sendRedirect(loginUrl);
	   	} else {
	   		isTrue = Boolean.TRUE;
	   	}
	   	return isTrue;
   }
   
   /**
	 * 通用的 得到 backUrl
	 * @param param 请求地址后带的参数值  
	 * @param paramName 请求地址后带的参数名称
	 * @param url   跳转的地址
	 * @return
	 */
	private String getBackUrl(String param, String paramName, String url) {
		if(StringUtils.isNotBlank(url)){
			url = url + "?" + paramName + "=" + param;
		}
		return url;
	}
}
