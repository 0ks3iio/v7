package net.zdsoft.bigdata.frame.data.druid;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.data.service.OptionService;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Lazy(false)
@Service("druidClientService")
public class DruidClientServiceImpl implements DruidClientService {

	private static Logger logger = LoggerFactory
			.getLogger(DruidClientServiceImpl.class);

	private static String druid_url = null;

	@Autowired
	private OptionService optionService;

	public void initClient() throws Exception {
		OptionDto druidDto = optionService.getAllOptionParam("druid");
		if (druidDto == null || druidDto.getStatus() == 0) {
			throw new BigDataBusinessException("druid初始化失败");
		}
		druid_url = druidDto.getFrameParamMap().get("durid_remote_api_url");
	}

	@Override
	public boolean submitDruidJob(String json) {
		// curl -XPOST -H'Content-Type: application/json' -d @data/kafka.json
		// http://hadoop-master:17090/druid/indexer/v1/supervisor
		OptionDto druidDto = optionService.getAllOptionParam("druid");
		if (druidDto == null || druidDto.getStatus() == 0) {
			logger.error("druid初始化失败");
			return false;
		}
		String job_api_url = druidDto.getFrameParamMap().get(
				"druid_job_api_url");
		try {
			String result = httpPostWithJson(job_api_url, json);
			if ("error".equals(result)) {
				return false;
			}
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	@Override
	public String getDruidTaskStatus(String taskId) {
		OptionDto druidDto = optionService.getAllOptionParam("druid");
		if (druidDto == null || druidDto.getStatus() == 0) {
			logger.error("druid初始化失败");
			return "error";
		}
		String job_api_url = druidDto.getFrameParamMap().get(
				"druid_job_api_url");
		job_api_url = job_api_url + "/" + taskId + "/status";
		try {
			String result = httpGet(job_api_url);
			if (!"error".equals(result)) {
				return JSON.parseObject(result).getString("type");
			}
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return "error";
		}
	}

	public List<Json> getDruidQueries(DruidParam druidQueryParam,
			List<Json> resultFieldList) {
		try {
			if (DruidConstants.QUERY_TYPE_GROUP_BY.equals(druidQueryParam
					.getQueryType())) {
				return getGroupByQueries(druidQueryParam, resultFieldList);
			} else if (DruidConstants.QUERY_TYPE_TOP_N.equals(druidQueryParam
					.getQueryType())) {
				return getTopNQueries(druidQueryParam, resultFieldList);
			} else if (DruidConstants.QUERY_TYPE_SELECT.equals(druidQueryParam
					.getQueryType())) {

			} else if (DruidConstants.QUERY_TYPE_TIMESERIES
					.equals(druidQueryParam.getQueryType())) {
				return getTimeseriesQueries(druidQueryParam, resultFieldList);
			}
			return new ArrayList<Json>();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ArrayList<Json>();
		}
	}

	private List<Json> getGroupByQueries(DruidParam druidQueryParam,
			List<Json> resultFieldList) throws Exception {
		initClient();
		List<Json> resultList = new ArrayList<Json>();
		String result4Json = httpPostWithJson(druid_url,
				JSONObject.toJSONString(druidQueryParam));
		if (StringUtils.isEmpty(result4Json)) {
			return resultList;
		}
		if ("error".equals(result4Json)) {
			return resultList;
		}
		JSONArray list = JSONArray.parseArray(result4Json);
		for (int i = 0; i < list.size(); i++) {
			Json result = new Json();
			JSONObject obj = JSONObject.parseObject(list.get(i).toString());
			result.put(
					"date",
					UTCToCST(obj.getString("timestamp"),
							druidQueryParam.getGranularity()));
			String eventJson = obj.getString("event");
			JSONObject event = JSONObject.parseObject(eventJson);
			for (Json field : resultFieldList) {

				JSONArray keyList = field.getJSONArray("keys");
				StringBuffer keys = new StringBuffer();
				for (int j = 0; j < keyList.size(); j++) {
					if (j == 0)
						keys.append(event.getString(keyList.get(j).toString()));
					else {
						keys.append(","
								+ event.getString(keyList.get(j).toString()));
					}
				}
				result.put("key", keys);
				if ("int".equals(field.getString("resultDataType"))) {
					result.put("value",
							event.getIntValue(field.getString("resultField")));
				} else if ("double".equals(field.getString("resultDataType"))) {
					result.put("value",
							event.getDouble(field.getString("resultField")));
				}

				if (StringUtils.isNotBlank(field.getString("serials"))) {
					result.put("serials", field.getString("serials"));
				} else {
					result.put("serials", keys);
				}
			}
			resultList.add(result);
		}
		return resultList;
	}

	private List<Json> getTopNQueries(DruidParam druidQueryParam,
			List<Json> resultFieldList) throws Exception {
		initClient();
		List<Json> resultList = new ArrayList<Json>();
		String result4Json = httpPostWithJson(druid_url,
				JSONObject.toJSONString(druidQueryParam));
		if (StringUtils.isEmpty(result4Json)) {
			return resultList;
		}
		if ("error".equals(result4Json)) {
			return resultList;
		}
		JSONArray list = JSONArray.parseArray(result4Json);
		for (int i = 0; i < list.size(); i++) {
			JSONObject obj = JSONObject.parseObject(list.get(i).toString());
			String resultJson = obj.getString("result");
			JSONArray subList = JSONObject.parseArray(resultJson);
			for (int j = 0; j < subList.size(); j++) {
				Json result = new Json();
				JSONObject subObj = JSONObject.parseObject(subList.get(j)
						.toString());
				for (Json field : resultFieldList) {
					result.put("key", subObj.getString(field.getString("key")));
					result.put("value",
							subObj.getIntValue(field.getString("field")));
					result.put("serials", field.getString("serials"));
				}
				resultList.add(result);
			}
		}
		return resultList;
	}

	private List<Json> getTimeseriesQueries(DruidParam druidQueryParam,
			List<Json> resultFieldList) throws Exception {
		initClient();
		List<Json> resultList = new ArrayList<Json>();
		String result4Json = httpPostWithJson(druid_url,
				JSONObject.toJSONString(druidQueryParam));
		if (StringUtils.isEmpty(result4Json)) {
			return resultList;
		}
		JSONArray list = JSONArray.parseArray(result4Json);
		for (int i = 0; i < list.size(); i++) {
			Json result = new Json();
			JSONObject obj = JSONObject.parseObject(list.get(i).toString());
			result.put(
					"key",
					UTCToCST(obj.getString("timestamp"),
							druidQueryParam.getGranularity()));
			String resultJson = obj.getString("result");
			JSONObject event = JSONObject.parseObject(resultJson);
			for (Json field : resultFieldList) {
				result.put("value", event.getIntValue(field.getString("field")));
				result.put("serials", field.getString("serials"));
			}
			resultList.add(result);
		}
		return resultList;

	}

	public String UTCToCST(String UTCStr, String interval) {
		String format = null;
		switch (interval) {
		case "year":
			format = "yyyy";
			break;
		case "quarter":
			format = "yyyy-MM";
			break;
		case "month":
			format = "yyyy-MM";
			break;
		case "week":
			format = "yyyy-MM-dd";
			break;
		case "day":
			format = "yy-MM-dd";
			break;
		case "hour":
			format = "MM-dd HH";
			break;
		case "fifteen_minute":
			format = "HH:mm";
			break;
		case "thirty_minute":
			format = "HH:mm";
			break;
		case "minute":
			format = "HH:mm";
			break;
		case "second":
			format = "HH:mm:ss";
			break;
		default:
			format = "yyyy-MM-dd";
			break;
		}

		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		try {
			date = sdf.parse(UTCStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return "未知";
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		//calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 8);
		// calendar.getTime() 返回的是Date类型，也可以使用calendar.getTimeInMillis()获取时间戳
		String result = DateUtils.date2String(calendar.getTime(), format);
		switch (interval) {
		case "quarter":
			result = DateUtils.getSeason(calendar.getTime(), true);
			break;
		case "week":
			result = DateUtils.getWeekBeginAndEndDate(calendar.getTime(),
					"MM-dd");
			break;
		default:
			break;
		}
		return result;
	}

	private String httpPostWithJson(String url, String param) {
		HttpPost post = null;
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			post = new HttpPost(url);
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(3000).setConnectTimeout(3000).build();// 设置请求和传输超时时间
			post.setConfig(requestConfig);
			// 构造消息头
			post.setHeader("Content-type", "application/json; charset=utf-8");
			post.setHeader("Connection", "Close");
			// 构建消息实体
			StringEntity entity = new StringEntity(param,
					Charset.forName("UTF-8"));
			entity.setContentEncoding("UTF-8");
			// 发送Json格式的数据请求
			entity.setContentType("application/json");
			post.setEntity(entity);
			HttpResponse response = httpClient.execute(post);
			// 检验返回码
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				return "error";
			} else {
				HttpEntity result = response.getEntity();
				return entityToString(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return "error";
		} finally {
			if (post != null) {
				try {
					post.releaseConnection();
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
					logger.error(e.getMessage());
				}
			}
		}
	}

	private String httpGet(String url) {
		HttpGet get = null;
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			get = new HttpGet(url);
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(3000).setConnectTimeout(3000).build();// 设置请求和传输超时时间
			get.setConfig(requestConfig);
			// 构造消息头
			get.setHeader("Content-type", "application/json; charset=utf-8");
			get.setHeader("Connection", "Close");
			HttpResponse response = httpClient.execute(get);
			// 检验返回码
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				return "error";
			} else {
				HttpEntity result = response.getEntity();
				return entityToString(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return "error";
		} finally {
			if (get != null) {
				try {
					get.releaseConnection();
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
					logger.error(e.getMessage());
				}
			}
		}
	}

	private String entityToString(HttpEntity entity) throws IOException {
		String result = null;
		if (entity != null) {
			long lenth = entity.getContentLength();
			if (lenth != -1 && lenth < 2048) {
				result = EntityUtils.toString(entity, "UTF-8");
			} else {
				InputStreamReader reader1 = new InputStreamReader(
						entity.getContent(), "UTF-8");
				CharArrayBuffer buffer = new CharArrayBuffer(2048);
				char[] tmp = new char[1024];
				int l;
				while ((l = reader1.read(tmp)) != -1) {
					buffer.append(tmp, 0, l);
				}
				result = buffer.toString();
			}
		}
		return result;
	}
}
