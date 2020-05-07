package net.zdsoft.api.openapi.pushjob.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.zdsoft.api.base.entity.eis.ApiPushJob;
import net.zdsoft.api.openapi.pushjob.constant.BaseOpenapiConstant;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.UrlUtils;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

public class BaseUrlUtils {
	
	private static String getUrl(ApiPushJob job){
		String url;
		if("3".equals(job.getDataType())){
			url = job.getRequestUrl();
		}else{
			url = BaseOpenapiConstant.BASE_V2_URL + job.getType();
		}
		return url;
    }
	
	public static String getUrl(ApiPushJob job, Integer page){
    	Map<String,Object> paramMap = new HashMap<>();
    	if(job != null){
    		Date updateDate = job.getUpdateStamp();
    		if(updateDate != null) {
    			paramMap.put(BaseOpenapiConstant.BASE_UPDATE_NAME, job.getUpdateStamp());
    		}
    		if(page != null){
    			paramMap.put(BaseOpenapiConstant.PAGE_PARAM_NAME, page);
    			paramMap.put(BaseOpenapiConstant.LIMIT_PARAM_NAME, BaseOpenapiConstant.MAX_PAGE_SIZE);
    		}
    		paramMap.put(BaseOpenapiConstant.DEFAULT_IS_DELETED, BaseOpenapiConstant.IS_DELETED_ALL);
    	}
		return getUrl(job.getTicketKey(), getUrl(job),paramMap);
    }
    
	public static String getPushUrl(ApiPushJob pushJob) {
		return getUrl(pushJob.getTicketKey(), pushJob.getPushUrl(), null);
	}
	
	
    /**
     * 得到调用的公共地址
     * @param type 类型
     * @param ticketKey 开发者ticketkey
     * @param paramMap  参数
     * @return
     */
    private static String getUrl(String ticketKey,String url,Map<String, Object> paramMap){
    	StringBuffer sBuffer = new StringBuffer();
    	if (!StringUtils.startsWith(url, "http")) {
    		String ipString = Evn.getWebUrl();
    		ipString = UrlUtils.ignoreLastRightSlash(ipString);
    		sBuffer.append(ipString);
		}
    	sBuffer.append(url);
    	sBuffer.append("?");
    	sBuffer.append(BaseOpenapiConstant.BASE_OPENAPI_TICKET_NAME);
    	sBuffer.append("=");
    	sBuffer.append(ticketKey);
    	if(!MapUtils.isEmpty(paramMap)){
    		for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
    			sBuffer.append("&");
    			sBuffer.append(entry.getKey());
    			sBuffer.append("=");
    			if(BaseOpenapiConstant.BASE_UPDATE_NAME.equals(entry.getKey())){
    				sBuffer.append(DateUtils.date2String((Date)entry.getValue(), BaseOpenapiConstant.BASE_UPDATE_SIMPLE_DATE_FORMAT));
    			}else{
    				sBuffer.append(entry.getValue());
    			}
    		}
    	}
		return sBuffer.toString();
	}
}
