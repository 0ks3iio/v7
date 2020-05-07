package net.zdsoft.system.service.sms;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

public class ZDUtils implements Serializable {
	private static final long serialVersionUID = 5375453903080008149L;
	private static int BUFFER_SIZE = 1024 * 4;

	public static void close(InputStream in) {
		if (in == null) {
			return;
		}

		try {
			in.close();
		} catch (IOException e) {
		}
	}

	/**
	 * 关闭输出流
	 * 
	 * @param out
	 *            输出流
	 */
	public static void close(OutputStream out) {
		if (out == null) {
			return;
		}

		try {
			out.close();
		} catch (IOException e) {
		}
	}

	/**
	 * 访问页面URL，获得输入流
	 * 
	 * @param pageURL
	 *            页面URL
	 * @return 输入流
	 * @throws IOException
	 */
	public static InputStream visitContent(String pageURL) throws IOException {
		URL url = new URL(pageURL);
		HttpURLConnection client = (HttpURLConnection) url.openConnection();

		HttpURLConnection.setFollowRedirects(false);
		client.setInstanceFollowRedirects(false);

		client.connect();

		return client.getInputStream();
	}

	/**
	 * 访问页面URL，获得页面内容
	 * 
	 * @param pageURL
	 *            页面URL
	 * @return 页面内容
	 * @throws IOException
	 */
	public static String readContent(String pageURL) throws IOException {
		InputStream in = null;
		ByteArrayOutputStream out = null;

		try {
			in = visitContent(pageURL);
			out = new ByteArrayOutputStream();

			byte[] buffer = new byte[BUFFER_SIZE];
			int length;
			while ((length = in.read(buffer)) != -1) {
				out.write(buffer, 0, length);
			}
			return new String(out.toByteArray());
		} finally {
			close(in);
			close(out);
		}
	}

	/**
	 * 读取网站内容
	 * 
	 * @param pageURL
	 *            地址
	 * @param getOrPost
	 *            方法，get 或者 post
	 * @param parameters
	 *            参数，map形式，可以为null
	 * @param sessionId
	 *            sessionid，可以为null
	 * @param charset
	 *            字符集，可以为null
	 * @return
	 * @throws IOException
	 */
	public static String readContent(String pageURL, String getOrPost, Map<String, String> parameters, String sessionId,
			String charset) throws IOException {
		InputStream in = null;
		ByteArrayOutputStream out = null;
		HttpURLConnection conn = null;
		try {
			URL url = new URL(pageURL);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(getOrPost);
			conn.setDoOutput(true);
			conn.setInstanceFollowRedirects(false);
			if (StringUtils.isNotBlank(sessionId))
				conn.setRequestProperty("Cookie", "JSESSIONID=" + sessionId);

			OutputStream httpOutputStream = conn.getOutputStream();
			StringBuffer postParams = new StringBuffer();
			int index = 0;

			if (MapUtils.isNotEmpty(parameters)) {
				for (Map.Entry<String, String> entry : parameters.entrySet()) {
					postParams.append(index != 0 ? "&" : "");
					postParams.append(entry.getKey());
					postParams.append("=");
					postParams.append(entry.getValue());
					index++;
				}
				httpOutputStream.write(postParams.toString().getBytes());
			}

			in = conn.getInputStream();
			out = new ByteArrayOutputStream();

			byte[] buffer = new byte[BUFFER_SIZE];
			int length;
			while ((length = in.read(buffer)) != -1) {
				out.write(buffer, 0, length);
			}
			if (StringUtils.isBlank(charset))
				return new String(out.toByteArray());
			else
				return new String(out.toByteArray(), charset);
		} finally {
			close(in);
			close(out);
			if (conn != null)
				conn.disconnect();
		}
	}

	/**
	 * 把一个字节数组转换为16进制表达的字符串
	 * 
	 * @param bytes
	 * @return
	 */
	public static String toHexString(byte[] bytes) {
		StringBuffer hexString = new StringBuffer();

		for (int i = 0; i < bytes.length; i++) {
			hexString.append(StringUtils.leftPad(Integer.toHexString(bytes[i] & 0xff), 2, "0"));
		}
		return hexString.toString();
	}

	/**
	 * 使用MD5对字符串加密.
	 * 
	 * @param str
	 *            源字符串
	 * @return 加密后字符串
	 */
	public static String encodeByMD5(String str, int times) {
		if (times == 0) {
			times = 1;
		}
		for (int i = 0; i < times; i++) {
			str = DigestUtils.md5Hex(str);
		}
		return str;
	}

	public static void main(String[] args) {
		// try {
		// Map<String, String> map = new HashMap<String, String>();
		// map.put("ticket", "ddd");
		// System.out.println(readContent("http://192.168.0.15/pearl/fpf/homepage/loginForEisOnly.action",
		// "GET", map, "5E978D66D5D5F5FF768ECE53851FCC7E", null));
		// }
		// catch (IOException e) {
		// e.printStackTrace();
		// }
	}
}
