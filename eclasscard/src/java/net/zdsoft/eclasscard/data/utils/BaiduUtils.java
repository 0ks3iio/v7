package net.zdsoft.eclasscard.data.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import net.zdsoft.eclasscard.data.dto.AccessToken;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.StringUtils;

import com.alibaba.fastjson.JSONObject;

public class BaiduUtils {
	
	public final static String CLIENT_ID = "kXCHUirXfQHu0IQgHbeWw96v";
	public final static String CLIENT_SECRET = "00fbb0ad4c2146cc4d326ae2851e61c2";
	public final static String ACCESS_TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token"
			+ "?grant_type=client_credentials&client_id="+CLIENT_ID+"&client_secret="+CLIENT_SECRET;
	/** 
	 * 获取access_token 
	 *  
	 * @return 
	 */  
	public static String getAccessToken() {  
		String access_token = RedisUtils.get("access.token."+CLIENT_ID+"."+CLIENT_SECRET); 
		if(StringUtils.isNotEmpty(access_token)){
			return access_token;
		}
	    JSONObject jsonObject = HttpRequest(ACCESS_TOKEN_URL, "POST", null);  
	    AccessToken accessToken = null;
	    if (null != jsonObject) {  
	        try {  
	            accessToken = new AccessToken();  
	            accessToken.setToken(jsonObject.getString("access_token"));  
	            accessToken.setExpiresIn(jsonObject.getIntValue("expires_in"));
	            RedisUtils.set("access.token."+CLIENT_ID+"."+CLIENT_SECRET, accessToken.getToken(), accessToken.getExpiresIn());
	            System.out.println("获取token成功:"+jsonObject.getString("access_token")+"————"+jsonObject.getIntValue("expires_in"));
	        } catch (Exception e) {  
	            accessToken = null;  
	            // 获取token失败  
	            String error = String.format("获取token失败 error:{} error_description:{}", jsonObject.getString("error"), jsonObject.getString("error_description"));  
	            System.out.println(error);
	        }  
	    }  
	    return accessToken.getToken();  
	}
	
    /** 
     * 发起https请求并获取结果 
     *  
     * @param requestUrl 请求地址 
     * @param requestMethod 请求方式（GET、POST） 
     * @param outputStr 提交的数据 
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值) 
     */  
	public static JSONObject HttpRequest(String request , String RequestMethod , String output ){
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			//建立连接
			URL url = new URL(request);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod(RequestMethod);
			if(output!=null){
				OutputStream out = connection.getOutputStream();
				out.write(output.getBytes("UTF-8"));
				out.close();
			}
			//流处理
			InputStream input = connection.getInputStream();
			InputStreamReader inputReader = new InputStreamReader(input,"UTF-8");
			BufferedReader reader = new BufferedReader(inputReader);
			String line;
			while((line=reader.readLine())!=null){
				buffer.append(line);
			}
			//关闭连接、释放资源
			reader.close();
			inputReader.close();
			input.close();
			input = null;
			connection.disconnect();
			jsonObject = JSONObject.parseObject(buffer.toString());
		} catch (Exception e) {
		}
		return jsonObject;
	} 

	public static void main(String[] args) {
		System.out.println(getAccessToken());
	}
}
