package net.zdsoft.syncdata.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import net.zdsoft.basedata.constant.custom.LyConstant;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.SecurityUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.syncdata.entity.ConstantSyncData;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

/**
 * @author yangsj  2018年1月19日下午5:04:51
 */
public class DYSynHttpRequestUtils {
   
	private static RestTemplate restTemplate = new RestTemplate();
	
	private static String showAccessToken() {
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("grant_type", "client_credentials");
			map.put("scope", "datacenterScope");
			map.put("client_id", Evn.getString(ConstantSyncData.SYNC_DATA_BJDY_CLIENTID));
			map.put("client_secret", Evn.getString(ConstantSyncData.SYNC_DATA_BJDY_CLIENTSECRET));
			String jsonStr = UrlUtils.readContent(LyConstant.LY_GET_ACCESS_TOKEN_URL,map, true);	
			JSONObject json = Json.parseObject(jsonStr);
			return URLEncoder.encode(json.getString("access_token"), "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	
	
	public static String doInterface(String interfaceName, String apiCode, JSONObject param) {
		return doInterface(interfaceName, apiCode, param, null);
	}

	public static String doInterface(String interfaceName, String apiCode, JSONObject param, Integer pageIndex,String param2) {
		String accessToken = showAccessToken();
		String url = Evn.getString(ConstantSyncData.SYNC_DATA_BJDY_INTERFACE_URL) + "/" + apiCode;
		if(StringUtils.isNotBlank(param2)) {
			url = url + "?JGID="+param2;
		}
		if (pageIndex != null) {
			url += "&pageIndex=" + pageIndex;
		}
		if(param != null && param.containsKey("GXSJ")) {
			String GXSJ = (String) param.get("GXSJ");
			url += "&GXSJ="+GXSJ;
		}
		String authorization = ConstantSyncData.SYNC_DATA_BJDY_TOKEN_PREFIX + accessToken;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", authorization);
			HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
			ResponseEntity<String> responseBody = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
			JSONObject json = Json.parseObject(responseBody.getBody());
			int resultCode = json.getInteger("failure");
			Integer totalCount = json.getInteger("total");
			if (totalCount == null || totalCount == 0) {
				return null;
			}
			if (resultCode != 0) {
				return "ERROR";
			}
			String content;
			content = json.getString("rows");
			return content;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ERROR";
	}
	
	public static String doInterface(String interfaceName, String apiCode, JSONObject param, Integer pageIndex) {
		String accessToken = showAccessToken();
		String url = Evn.getString(ConstantSyncData.SYNC_DATA_BJDY_INTERFACE_URL) + "/" + apiCode;
		//对starttime进行名字的转化
		if(param != null  && pageIndex == null) {
			if(param.containsKey("GXSJ")){
				String GXSJ = (String) param.get("GXSJ");
				url += "?GXSJ="+GXSJ;
			}
			if(param.containsKey("XX_JBXX_ID")){
				String XX_JBXX_ID = (String) param.get("XX_JBXX_ID");
				url += "&XX_JBXX_ID="+XX_JBXX_ID;
			}
		}
		String authorization = ConstantSyncData.SYNC_DATA_BJDY_TOKEN_PREFIX + accessToken;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", authorization);
			HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
			ResponseEntity<String> responseBody = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
			JSONObject json = Json.parseObject(responseBody.getBody());
			int resultCode = json.getInteger("failure");
			Integer totalCount = json.getInteger("total");
			if (totalCount == null || totalCount == 0) {
				return null;
			}
			if (resultCode != 0) {
				return "ERROR";
			}
			String content;
			content = json.getString("rows");
			return content;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ERROR";
	}
	
	
	public static String doInterface(String interfaceName, String apiCode, JSONObject param, Integer pageIndex,Integer pageSize) {
		String accessToken = showAccessToken();
		String url = Evn.getString(ConstantSyncData.SYNC_DATA_BJDY_INTERFACE_URL) + "/" + apiCode;
		if (pageIndex != null && pageSize != null) {
			url += "?pageIndex=" + pageIndex +"&pageSize=" + pageSize;
		}
		if(param != null) {
			if(param.containsKey("GXSJ")){
				String GXSJ = (String) param.get("GXSJ");
				url += "&GXSJ="+GXSJ;
			}
			if(param.containsKey("XX_JBXX_ID")){
				String XX_JBXX_ID = (String) param.get("XX_JBXX_ID");
				url += "&XX_JBXX_ID="+XX_JBXX_ID;
			}
		}
		String authorization = ConstantSyncData.SYNC_DATA_BJDY_TOKEN_PREFIX + accessToken;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", authorization);
			HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
			ResponseEntity<String> responseBody = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
			JSONObject json = Json.parseObject(responseBody.getBody());
			int resultCode = json.getInteger("failure");
			Integer totalCount = json.getInteger("total");
			if (totalCount == null || totalCount == 0) {
				return null;
			}
			if (resultCode != 0) {
				return "ERROR";
			}
			String content;
			content = json.getString("rows");
			return content;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ERROR";
	}
	
	public static String doPostInterface(String interfaceName, String apiCode, JSONObject param,String jsonString) {
		String accessToken = showAccessToken();
		String url = Evn.getString(ConstantSyncData.SYNC_DATA_BJDY_INTERFACE_URL) + "/" + apiCode;
		String authorization = ConstantSyncData.SYNC_DATA_BJDY_TOKEN_PREFIX + accessToken;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			headers.set("Authorization", authorization);
			HttpEntity<String> requestEntity = new HttpEntity<String>(jsonString, headers);
			String responseBody = restTemplate.postForObject(url, requestEntity, String.class);
			JSONObject json = Json.parseObject(responseBody);
			int resultCode = json.getInteger("failure");
			Integer totalCount = json.getInteger("total");
			if (totalCount == null || totalCount == 0) {
				return null;
			}
			if (resultCode != 0) {
				return "ERROR";
			}
			String content;
			content = json.getString("rows");
			return content;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ERROR";
	}
	
	private String decode(String encodeStr) {
		if (StringUtils.isBlank(encodeStr))
			return null;
		byte[] bs = SecurityUtils.decryptToBytes(encodeStr.getBytes(),
				Evn.getString(ConstantSyncData.SYNC_DATA_BJDY_CLIENTSECRET).getBytes());
		try {
			return new String(bs, "utf8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new JSONObject().toJSONString();
	}

	private String showAppIdAndSecret() {
		return "client_id=" + Evn.getString(ConstantSyncData.SYNC_DATA_BJDY_CLIENTID) + "&client_secret="
				+ Evn.getString(ConstantSyncData.SYNC_DATA_BJDY_CLIENTSECRET);
	}
	
}
