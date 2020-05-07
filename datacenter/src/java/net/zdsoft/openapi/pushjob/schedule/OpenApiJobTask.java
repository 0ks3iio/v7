package net.zdsoft.openapi.pushjob.schedule;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.zdsoft.base.entity.eis.OpenApiPushJob;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.schedule.entity.BaseJobTask;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.openapi.pushjob.constant.BaseOpenapiConstant;
import net.zdsoft.openapi.pushjob.service.BasePushJobService;
import net.zdsoft.openapi.pushjob.utils.BaseUrlUtils;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class OpenApiJobTask implements BaseJobTask{
	private  final Logger log = Logger.getLogger(OpenApiJobTask.class);
	
	BasePushJobService basePushJobService = Evn.getBean("basePushJobService");
	private static RestTemplate restTemplate = new RestTemplate();
	
	@Override
	public void excute(String jobGroup, String jobName) {
		//jobGroup=ticketKey;jobName=BasePushJob.id
		OpenApiPushJob pushJob = basePushJobService.findOne(jobName);
//		BasePushLogger pushLogger = new BasePushLogger();
//		pushLogger.setId(UuidUtils.generateUuid());
//		pushLogger.setStartTime(new Date());
        log.error("定时任务开始的时间是-----------------" + new Date());
		if(pushJob!=null){
			String lastModifyTimeCache= DateUtils.date2String(pushJob.getUpdateStamp(),BaseOpenapiConstant.BASE_UPDATE_SIMPLE_DATE_FORMAT);//分页循环推送时，每次推送成功时缓存最大时间
			String msg = "";
			String code = "1";
			String pushUrl = BaseUrlUtils.getPushUrl(pushJob);
//			pushLogger.setPushUrl(pushUrl);
			//此处需分页获取推送
			for (int i = 0; i < 1000; i++) {
				//1.获取数据解析出的结果，要直接作为推送参数
				String pushDate = "";
				try {
					String getUrl = BaseUrlUtils.getUrl(pushJob, i+1);
					pushDate = getPushDate(getUrl,i+1,lastModifyTimeCache, pushJob.getJsonType());
					if(StringUtils.isBlank(pushDate)){
						msg = "查找本地数据为空";
						break;
					}
					//获取到当前推送的最大时间
				} catch (IOException e) {
					msg = "获取数据异常";
					code = "-1";
					e.printStackTrace();
					break;
				}
				//2.推送参数是上面直接解析的结果，返回参数需要确定好，用于判断记录推送日志和更新时间戳
				if(StringUtils.isNotBlank(pushDate)&& StringUtils.isNotBlank(pushJob.getPushUrl())){
					try {
						String jsonData = getResponseEntity(pushUrl,pushDate);
//						解析获取msg
						if(StringUtils.isNotBlank(jsonData)){
							JSONObject jsonObject = JSON.parseObject(jsonData);
							msg = jsonObject.getString(BaseOpenapiConstant.RETURN_RESULT_MESSAGE_NAME);
							code = jsonObject.getString(BaseOpenapiConstant.RETURN_RESULT_CODE_NAME);
							if("1".equals(code)){
								System.out.println("推送数据返回的code---------" +  code);
								JSONObject json = Json.parseObject(pushDate);
								lastModifyTimeCache = json.getString(BaseOpenapiConstant.PUSH_RESULT_UPDATE_STAMP_NAME);
							}
						}
					} catch (Exception e) {
						msg = "推送数据异常";
						code = "-1";
						e.printStackTrace();
						lastModifyTimeCache = RedisUtils.get("redis.push.updateStamp.key" + jobName);
						break;
					}
					RedisUtils.set("redis.push.updateStamp.key" + jobName, lastModifyTimeCache);
				}
			}
			pushJob.setUpdateStamp(DateUtils.string2Date(lastModifyTimeCache, BaseOpenapiConstant.BASE_UPDATE_SIMPLE_DATE_FORMAT));
//			pushLogger.setCode(code);
//			pushLogger.setMessage(msg);
//			pushLogger.setEndTime(new Date());
			log.error("定时任务结束的时间是-----------------" + new Date());
//			pushLogger.setPushUrl(pushUrl); 
//			pushLogger.setType("1");
			basePushJobService.save(pushJob);
			
//			basePushLoggerService.save(pushLogger);
		}
	}
	
	/**
	 * 进行数据的重新封装
	 * @param url
	 * @return
	 * @throws IOException
	 */
	private String getPushDate(String url,Integer page, String lastModifyTimeCache, Integer jsonType) throws IOException{
		String jsonData = UrlUtils.get(url, new String());
		if(StringUtils.isNotBlank(jsonData)){
			JSONObject jsonObject = JSON.parseObject(jsonData);
			//判断是否成功取得数据
			Integer code = jsonObject.getInteger("result");
			if(1 == code){
				//判断是否已经超过最大的页数
				if(getIsNotOutMaxPage(jsonObject.getString("pagination"),page)){
					Integer count = jsonObject.getInteger("dataCount");
					if(count != null && count > 0){
						if(jsonType != null && BaseOpenapiConstant.SF_JSON_TYPE == jsonType){
							net.sf.json.JSONObject result = new net.sf.json.JSONObject();
							result.put(BaseOpenapiConstant.PUSH_RESULT_COUNT_NAME, count);
							result.put(BaseOpenapiConstant.PUSH_RESULT_DATA_NAME, jsonObject.getString("data"));
							result.put(BaseOpenapiConstant.PUSH_RESULT_UPDATE_STAMP_NAME, getMaxModifyTime(jsonObject.getString("data"),lastModifyTimeCache));
							if(StringUtils.isBlank(lastModifyTimeCache)){
								result.put(BaseOpenapiConstant.PUSH_RESULT_IS_FIRST_NAME, Boolean.TRUE);
							}else{
								result.put(BaseOpenapiConstant.PUSH_RESULT_IS_FIRST_NAME, Boolean.FALSE);
							}
							return result.toString();
						}else{
							JSONObject result = new JSONObject();
							result.put(BaseOpenapiConstant.PUSH_RESULT_COUNT_NAME, count);
							result.put(BaseOpenapiConstant.PUSH_RESULT_DATA_NAME, jsonObject.getString("data"));
							result.put(BaseOpenapiConstant.PUSH_RESULT_UPDATE_STAMP_NAME, getMaxModifyTime(jsonObject.getString("data"),lastModifyTimeCache));
							if(StringUtils.isBlank(lastModifyTimeCache)){
								result.put(BaseOpenapiConstant.PUSH_RESULT_IS_FIRST_NAME, Boolean.TRUE);
							}else{
								result.put(BaseOpenapiConstant.PUSH_RESULT_IS_FIRST_NAME, Boolean.FALSE);
							}
							return result.toJSONString();
						}
					}
				}
			}
		}
		return StringUtils.EMPTY;
	}
	
	private boolean getIsNotOutMaxPage(String paginaTion,Integer page) {
		if(StringUtils.isNotBlank(paginaTion)){
			JSONObject jsonObject = JSON.parseObject(paginaTion);
			int maxPage = Integer.valueOf(jsonObject.getString("maxPage"));
			return maxPage >= page;
		}
		return Boolean.TRUE;
	}

	/**
	 * 获取最大的更新时间,放进缓存中
	 * @param data
	 * @return
	 */
	private String getMaxModifyTime(String data, String lastModifyTimeCache) {
		if(StringUtils.isNotBlank(data)){
			JSONArray array = Json.parseArray(data);
			for (int i = 0; i < array.size(); i++) {
				JSONObject js = array.getJSONObject(i);
				//暂定数据中都会返回 modifyTime 
				String modifyTime = getModifyTime(js);
				if (StringUtils.isBlank(lastModifyTimeCache) || (StringUtils.isNotBlank(modifyTime) && lastModifyTimeCache.compareTo(modifyTime) < 0)) {
					lastModifyTimeCache = modifyTime;
				}
			}
		}
		return lastModifyTimeCache;
	}

	private String getModifyTime(JSONObject js) {
		String modifyTime = null;
		Object value = js.get("modifyTime");
		if ((value instanceof Timestamp)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(BaseOpenapiConstant.BASE_UPDATE_SIMPLE_DATE_FORMAT);
                modifyTime = sdf.format(value);
            }
            catch (Exception e) {
            }
		}
		if((value instanceof String)) {
			modifyTime = js.getString("modifyTime");
		}
		if((value instanceof Long)) {
			try {
                SimpleDateFormat sdf = new SimpleDateFormat(BaseOpenapiConstant.BASE_UPDATE_SIMPLE_DATE_FORMAT);
                modifyTime = sdf.format(value);
            }
            catch (Exception e) {
            }
		}
		return modifyTime;
	}

	//获取count
	private Integer getCount(String pagination){
		if(StringUtils.isNotBlank(pagination)){
			JSONObject jsonObject = JSON.parseObject(pagination);
			String count = jsonObject.getString("totalDataCount");
			return Integer.parseInt(count);
		}
		return null;
	}
	
	private String getResponseEntity(String url, String data) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<String> requestEntity = new HttpEntity<String>(data, headers);
		ResponseEntity<String> responseBody = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
		return responseBody.getBody();
	}
	
	
	public static void main(String[] args) {
		String ssString = "20180313174137000";//yyyy-MM-dd HH:mm:ss
		Date date = DateUtils.string2Date(ssString,"yyyyMMddHHmmss");
		System.out.println(date);
		
	}
}
