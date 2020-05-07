package net.zdsoft.basedata.dingding.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.StringUtils;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

/**
 * 钉钉消息推送 参数存在framework.properties下 dingding-CorpID dingding-CorpSecret
 * 
 * @author jiang feng
 *
 */
public class DingDingUtils {
	private static Logger log = Logger.getLogger(DingDingUtils.class);

	private static int BUFFER_SIZE = 1024 * 4;

	public static String getAccessToken(final String CorpID,final String CorpSecret) {
		String url = "https://oapi.dingtalk.com/gettoken?corpid=";
		url += CorpID;
		url += "&corpsecret=";
		url += CorpSecret;
		String accessToken = "";
		try {
			String result = readContent(url);
			if (StringUtils.isNotBlank(result)) {
				JSONObject jsonOjbect = Json.parseObject(result);
				if ("0".equals(jsonOjbect.getString("errcode"))) {
					accessToken = jsonOjbect.getString("access_token");
				} else {
					log.error(jsonOjbect.getString("errmsg"));
				}
				return accessToken;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return accessToken;
		}
		return accessToken;
	}

	/**
	 * 发送消息到钉钉
	 * 
	 * @param url
	 * @param content
	 */
	public static String push(final String url, final String content) {
		try {
			return readContentStringParam(url, content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @param url
	 */
	public static String push(final String url) {
		try {
			return readContent(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public static String readContent(String pageURL) throws IOException {
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

	public static String readContentStringParam(String pageURL,
			String stringParam) throws IOException {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "error"; // 自定义错误信息
	}

	public static void main(String[] args) {
		// DingDingUser user = new DingDingUser();
		// user.setUserid("11");
		// user.setName("22");
		// List<String> departments = new ArrayList<String>();
		// departments.add("33");
		// user.setDepartment(departments);
		// String result = SUtils.s(user);
		// System.out.println(result);
		//
		// DingDingDept dept = new DingDingDept();
		// // dept.setId("111");
		// dept.setName("222");
		// dept.setParentid("333");
		// String result1 = SUtils.s(dept);
		// System.out.println(result1);
		//getAccessToken();
	}
}
