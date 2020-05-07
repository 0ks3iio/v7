package net.zdsoft.bigdata.extend.data.biz;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.zdsoft.bigdata.extend.data.entity.WarningResult;
import net.zdsoft.bigdata.extend.data.service.WarningResultService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.StringUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

public class BgCalResultPush implements Runnable {

	private Logger log = Logger.getLogger(BgCalResultPush.class);

	private static int BUFFER_SIZE = 1024 * 4;

	private static int pageSize = 500;

	String pushApis;// 推送的apis

	String batchId;// 批次id

	public BgCalResultPush(String pushApis, String batchId) {
		this.pushApis = pushApis;
		this.batchId = batchId;
	}

	@Override
	public void run() {
		// 500条记录发送一次 然后

		WarningResultService warningResultService = (WarningResultService) Evn
				.getBean("warningResultService");
		List<WarningResult> warningResultList = warningResultService
				.getWarningResultByBatchId(batchId);
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		if (CollectionUtils.isEmpty(warningResultList)) {
			return;
		}
		int totalNum = warningResultList.size();
		int beginIndex = 0;
		int endIndex = 0;

		while (beginIndex < totalNum) {
			if (CollectionUtils.isNotEmpty(jsonList))
				jsonList = new ArrayList<JSONObject>();
			if (beginIndex + pageSize < totalNum) {
				endIndex = beginIndex + pageSize;
			} else {
				endIndex = totalNum;
			}
			List<WarningResult> subList = warningResultList.subList(beginIndex,
					endIndex);
			for (WarningResult warn : subList) {
				String result = StringUtils.EMPTY;
				if (StringUtils.isNotBlank(warn.getTips())) {
					result = warn.getTips()
							.replace("@result", warn.getResult());
				}
				String callBackJson = warn.getCallbackJson();
				// callBackJson="unitId=1BB882CE4A7449A2B8B6E69F96868F05&userId=ACC9CFFE120D4AE78D155CC73326445F&type=2";
				JSONObject jsonObject = new JSONObject();
				if (StringUtils.isNotBlank(callBackJson)) {
					String[] paramsArray = callBackJson.split("&");
					for (String param : paramsArray) {
						if (param.contains("=")) {
							String key = param.split("=")[0];
							String value = param.split("=")[1];
							jsonObject.put(key, value);
						}
					}
				}

				jsonObject.put("message", result);
				jsonObject.put("result", warn.getResult());
				jsonObject.put("creationTime", DateUtils.date2String(
						warn.getWarnDate(), "yyyy-MM-dd HH:mm:ss"));
				jsonList.add(jsonObject);
			}
			String callCackResult = JSONObject.toJSONString(jsonList);
			String[] apiList = pushApis.split(";");
			for (String api : apiList) {
				push(api, callCackResult);
			}
			beginIndex = endIndex;
		}
	}

	/**
	 * 发送消息到apis
	 * 
	 * @param url
	 * @param content
	 */
	public String push(final String url, final String content) {
		try {
			return readContentStringParam(url, content);
		} catch (IOException e) {
			
			log.error("大数据推送数据失败"+e.getMessage());
			return null;
		}
	}

	public String readContent(String pageURL) throws IOException {
		URL url = new URL(pageURL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setDoOutput(true);
		conn.connect();
		InputStream in = conn.getInputStream();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		byte[] buffer = new byte[BUFFER_SIZE];
		int length;
		while ((length = in.read(buffer)) != -1) {
			out.write(buffer, 0, length);
		}
		return new String(out.toByteArray(), "UTF-8");
	}

	public String readContentStringParam(String pageURL, String stringParam)
			throws IOException {
		try {
			URL url = new URL(pageURL);// 创建连接
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestMethod("POST"); // 设置请求方式
			connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
			connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
			connection.connect();
			OutputStreamWriter out = new OutputStreamWriter(
					connection.getOutputStream(), "UTF-8"); // utf-8编码
			out.append(stringParam);
			out.flush();
			out.close();
			// 读取响应
			int length = (int) connection.getContentLength();// 获取长度
			InputStream is = connection.getInputStream();
			if (length != -1) {
				byte[] data = new byte[length];
				byte[] temp = new byte[BUFFER_SIZE];
				int readLen = 0;
				int destPos = 0;
				while ((readLen = is.read(temp)) > 0) {
					System.arraycopy(temp, 0, data, destPos, readLen);
					destPos += readLen;
				}
				String result = new String(data, "UTF-8"); // utf-8编码
				return result;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "error"; // 自定义错误信息
	}

}