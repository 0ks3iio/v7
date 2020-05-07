package net.zdsoft.openapi.pushjob.utils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.openapi.pushjob.constant.BaseOpenapiConstant;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * 1:获取双方验证的特征值,进去传输
 * 
 * @author yangsj
 *
 */
public class PushUrlUtils {
	
	
	/**
	 * 获取特定的值
	 * @param redisKey
	 * @param time
	 * @param url
	 * @param method
	 * @return
	 */
	private String getAccessToken(String redisKey, int time, String url, String method) {
		String accessToken = null;
		try {
			accessToken = RedisUtils.get(redisKey);
			if (StringUtils.isBlank(accessToken)) {
				String data;
				if(BaseOpenapiConstant.GET_METHOD.equalsIgnoreCase(method)){
					data = UrlUtils.get(url, new String());
				}else{
					data = UrlUtils.post(url);
				}
				if(StringUtils.isNotBlank(data)){
					JSONObject jsonObject = JSON.parseObject(data);
					accessToken = jsonObject.getString("token");
				}
				//提前一分钟过期
				if(StringUtils.isNotBlank(accessToken))
				    RedisUtils.set(redisKey, accessToken, (int)TimeUnit.MINUTES.toSeconds(time));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return accessToken;
	}
	
	private String getPushUrl(String url, Map<String,Object> paramMap){
		StringBuffer sBuffer = new StringBuffer();
    	sBuffer.append(url);
    	boolean isIndex = url.indexOf(UrlUtils.QUESTION_MARK) == -1 ;
    	if(!MapUtils.isEmpty(paramMap)){
    		for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
    			if (isIndex) {
    				sBuffer.append(UrlUtils.QUESTION_MARK);
    				isIndex = Boolean.FALSE;
    			}else{
    				sBuffer.append("&");
    			}
    			sBuffer.append(entry.getKey());
    			sBuffer.append("=");
    			sBuffer.append(entry.getValue());
    		}
    	}
		return sBuffer.toString();
	}
	
	private String getDate(){
		return null;
		
		
		
	}
}
