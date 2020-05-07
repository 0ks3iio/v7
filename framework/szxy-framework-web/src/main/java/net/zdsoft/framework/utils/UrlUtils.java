/*
 * @(#)URLUtils.java Created on 2004-10-13
 * $Header: /project/keel/src/net/zdsoft/keel/util/URLUtils.java,v 1.15
 * 2007/10/16 03:25:24 liangxiao Exp $
 */
package net.zdsoft.framework.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

/**
 * URL工具类
 * 
 * @author liangxiao
 * @version $Revision: 1.15 $, $Date: 2007/10/16 03:25:24 $
 */
public final class UrlUtils {

	private static Logger logger = LoggerFactory.getLogger(UrlUtils.class);

	private static final char AND_SIGN = '&';
	private static final char EQUALS_SIGN = '=';
	private static final char POINT_SIGN = '.';
	public static final char QUESTION_MARK = '?';
	public static final char SEPARATOR_SIGN = '/';

	private static int BUFFER_SIZE = 1024 * 4;

	private static String charSet = "UTF-8";

//	public static CloseableHttpClient httpClient;
//	public static HttpClientContext context;
//	public static CookieStore cookieStore;
//	public static RequestConfig requestConfig;

//	static {
//		init();
//	}

//	private static void init() {
//		context = HttpClientContext.create();
//		cookieStore = new BasicCookieStore();
//		// requestConfig =
//		// RequestConfig.custom().setConnectTimeout(120000).setSocketTimeout(60000)
//		// .setConnectionRequestTimeout(60000).build();
//		// 设置默认跳转以及存储cookie
//		httpClient = HttpClientBuilder.create().setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
//				.setRedirectStrategy(new DefaultRedirectStrategy()).setDefaultCookieStore(cookieStore).build();
//	}

	/**
	 * 设置字母集
	 * 
	 * @param charSet
	 */
	public static void setCharSet(String charSet) {
		UrlUtils.charSet = charSet;
	}

	/**
	 * 拼接URL
	 * 
	 * @param url         URL
	 * @param queryString 查询字符串，比如：id=1
	 * @return 拼接后的URL
	 */
	public static String addQueryString(String url, String queryString) {
		if (Validators.isEmpty(queryString)) {
			return url;
		}

		if (url.indexOf(QUESTION_MARK) == -1) {
			url = url + QUESTION_MARK + queryString;
		} else {
			url = url + AND_SIGN + queryString;
		}
		return url;
	}

	public static StringBuilder addQueryString(StringBuilder url, String queryString) {
		if (Validators.isEmpty(queryString)) {
			return url;
		}
		if (url.indexOf(String.valueOf(QUESTION_MARK)) == -1) {
			url.append(QUESTION_MARK).append(queryString);
		} else {
			url.append(String.valueOf(AND_SIGN)).append(queryString);
		}
		return url;
	}

	/**
	 * 拼接URL
	 * 
	 * @param url   URL
	 * @param name  参数的名称
	 * @param value 参数的值
	 * @return 拼接后的URL
	 */
	public static String addQueryString(String url, String name, Object value) {
		return addQueryString(url, new String[] { name }, new Object[] { value });
	}

	/**
	 * 拼接URL
	 * 
	 * @param url    URL
	 * @param names  参数的名称数组
	 * @param values 参数的值数组
	 * @return 拼接后的URL
	 */
	public static String addQueryString(String url, String[] names, Object[] values) {
		if (names.length != values.length) {
			throw new IllegalArgumentException("Length of array must be equal");
		}

		StringBuilder queryString = new StringBuilder();
		boolean isFirst = true;
		for (int i = 0; i < names.length; i++) {
			Object value = values[i];
			if (value != null) {
				if (!isFirst) {
					queryString.append(AND_SIGN);
				} else {
					isFirst = false;
				}

				if (value instanceof Object[]) {
					Object[] array = (Object[]) value;
					for (int j = 0; j < array.length; j++) {
						if (j > 0) {
							queryString.append(AND_SIGN);
						}
						appendParameter(queryString, names[i], array[j]);
					}
				} else if (value instanceof Collection) {
					@SuppressWarnings("unchecked")
					Iterator iterator = ((Collection) value).iterator();
					int j = 0;
					while (iterator.hasNext()) {
						if (j++ > 0) {
							queryString.append(AND_SIGN);
						}
						appendParameter(queryString, names[i], iterator.next());
					}
				} else {
					appendParameter(queryString, names[i], value);
				}
			}
		}

		return addQueryString(url, queryString.toString());
	}

	/**
	 * 查询字符串后面增加参数
	 * 
	 * @param queryString 查询字符串，比如：id=1&type=1
	 * @param name        参数的名称
	 * @param value       参数的值
	 * @return 拼接后的查询字符串
	 */
	private static StringBuilder appendParameter(StringBuilder queryString, String name, Object value) {
		queryString.append(name);
		queryString.append(EQUALS_SIGN);

		if (value instanceof Boolean) {
			value = ((Boolean) value).booleanValue() ? "1" : "0";
		} else if (value instanceof Date) {
			value = DateUtils.date2StringByDay((Date) value);
		}

		try {
			queryString.append(URLEncoder.encode(String.valueOf(value), charSet));
		} catch (UnsupportedEncodingException e) {
			// ignore
		}
		return queryString;
	}

	/**
	 * 对url按照指定编码方式解码
	 * 
	 * @param url
	 * @param encoding
	 * @return
	 */
	public static String decode(String url, String encoding) {
		try {
			return URLDecoder.decode(url, encoding);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.toString());
			return null;
		}
	}

	/**
	 * 使URL成为动态URL，如果没有问号就在最后添加问号
	 * 
	 * @param url
	 * @return
	 */
	public static String dynamicURL(String url) {
		if (url.indexOf(QUESTION_MARK) == -1) {
			url = url + QUESTION_MARK;
		}
		return url;
	}

	/**
	 * 对url按照指定编码方式编码
	 * 
	 * @param url
	 * @param encoding
	 * @return
	 */
	public static String encode(String url, String encoding) {
		try {
			return URLEncoder.encode(url, encoding);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.toString());
			return null;
		}
	}

	/**
	 * 通过servletPath取得action的名称
	 * 
	 * @param servletPath
	 * @return
	 */
	public static String getActionName(String servletPath) {
		return servletPath.substring(servletPath.lastIndexOf(SEPARATOR_SIGN) + 1, servletPath.lastIndexOf(POINT_SIGN));
	}

	/**
	 * 取得url的后缀名
	 * 
	 * @param url
	 * @return
	 */
	public static String getExtension(String url) {
		int pointIndex = url.indexOf(POINT_SIGN);

		if (pointIndex == -1) {
			return null;
		}

		int interrogationIndex = url.indexOf(QUESTION_MARK);

		return interrogationIndex == -1 ? url.substring(pointIndex + 1)
				: url.substring(pointIndex + 1, interrogationIndex);
	}

	/**
	 * 通过servletPath获得namespace
	 * 
	 * @param servletPath
	 * @return
	 */
	public static String getNamespace(String servletPath) {
		return servletPath.substring(0, servletPath.lastIndexOf(SEPARATOR_SIGN));
	}

	/**
	 * 忽略URL中的末尾的'/'符号.
	 * 
	 * @param url url地址字符串
	 * @return 忽略末尾'/'符号后的url地址.
	 */
	public static String ignoreLastRightSlash(String url) {
		if (Validators.isEmpty(url)) {
			return url;
		}

		// 末尾字符是否为'/', 若是则去除
		if (url.charAt(url.length() - 1) == SEPARATOR_SIGN) {
			return url.substring(0, url.length() - 1);
		}

		return url;
	}

	/**
	 * 忽略URL中的开始的'/'符号.
	 *
	 * @param url url地址字符串
	 * @return 忽略开始'/'符号后的url地址.
	 */
	public static String ignoreFirstLeftSlash(String url) {
		if (Validators.isEmpty(url)) {
			return url;
		}
		if (url.charAt(0) == SEPARATOR_SIGN) {
			return url.substring(1);
		}
		return url;
	}

	/**
	 * 忽略URL中的两端的'/'符号.
	 *
	 * @param url url地址字符串
	 * @return 忽略两端'/'符号后的url地址.
	 */
	public static String ignoreBothSideSlash(String url) {
		if (Validators.isEmpty(url)) {
			return url;
		}
		url = ignoreFirstLeftSlash(url);
		url = ignoreLastRightSlash(url);

		return url;
	}

	/**
	 * @deprecated 改用 visitUrl方法
	 */
	public static InputStream visitContent(String pageURL) throws IOException {
		URL url = new URL(pageURL);
		HttpURLConnection client = (HttpURLConnection) url.openConnection();
		client.setRequestMethod("POST");// 提交模式
		HttpURLConnection.setFollowRedirects(false);
		client.setInstanceFollowRedirects(false);

		client.connect();

		return client.getInputStream();
	}

	/**
	 * @deprecated 改用 visitUrl方法
	 */
	public static String readContent(String pageURL, String sessionId) throws IOException {
		URL url = new URL(pageURL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setDoOutput(true);
		conn.setRequestProperty("Cookie", "JSESSIONID=" + sessionId);
		conn.connect();
		InputStream in = conn.getInputStream();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		byte[] buffer = new byte[BUFFER_SIZE];
		int length;
		while ((length = in.read(buffer)) != -1) {
			out.write(buffer, 0, length);
		}
		return new String(out.toByteArray());
	}

	/**
	 * @deprecated 改用 visitUrl方法
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
			if (in != null)
				in.close();
			if (out != null)
				out.close();
		}
	}

	/**
	 * @deprecated 改用 visitUrl方法
	 */
	public static String readContent(String pageURL, String attName, String attVal, boolean isPost) throws IOException {
		InputStream in = null;
		ByteArrayOutputStream out = null;

		try {
			in = visitContent(pageURL, attName, attVal, isPost);
			out = new ByteArrayOutputStream();

			byte[] buffer = new byte[BUFFER_SIZE];
			int length;
			while ((length = in.read(buffer)) != -1) {
				out.write(buffer, 0, length);
			}
			return new String(out.toByteArray());
		} finally {
			if (in != null)
				in.close();
			if (out != null)
				out.close();
		}
	}

	/**
	 * @deprecated 改用 visitUrl方法
	 */
	public static String readContent(String pageURL, Map<String, String> map, boolean isPost) throws IOException {
		InputStream in = null;
		ByteArrayOutputStream out = null;

		try {
			in = visitContent(pageURL, map, isPost);
			out = new ByteArrayOutputStream();

			byte[] buffer = new byte[BUFFER_SIZE];
			int length;
			while ((length = in.read(buffer)) != -1) {
				out.write(buffer, 0, length);
			}
			return new String(out.toByteArray());
		} finally {
			if (in != null)
				in.close();
			if (out != null)
				out.close();
		}
	}

	private static Map<String, String> toNameValuePairMap(String parameters) {
		if (StringUtils.isBlank(parameters))
			return Maps.newHashMap();
		Map<String, String> map = new HashMap<String, String>();
		String[] paramList = parameters.split("&");
		for (String parm : paramList) {
			int index = -1;
			for (int i = 0; i < parm.length(); i++) {
				index = parm.indexOf("=");
				break;
			}
			String key = parm.substring(0, index);
			String value = parm.substring(++index, parm.length());
			map.put(key, value);
		}
		return map;
	}

	public static String post(String url) throws IOException {
		return HttpClientUtils.postSync(url, null);
	}

	public static String post(String url, String params) throws IOException {
		return visitUrl(url, params, true, null);
	}

	public static String post(String url, Map<String, String> paramMap) throws IOException {
		return visitUrl(url, paramMap, true, null);
	}

	public static String postWithSession(String url, String sessionId) throws IOException {
		return visitUrl(url, "", true, sessionId);
	}

	public static String postWithSession(String url, String params, String sessionId) throws IOException {
		return visitUrl(url, params, true, sessionId);
	}

	public static String post(String url, Map<String, String> paramMap, String sessionId) throws IOException {
		return visitUrl(url, paramMap, true, sessionId);
	}

	public static String get(String url, String params) throws IOException {
		return visitUrl(url, params, false, null);
	}

	public static String get(String url, Map<String, String> paramMap) throws IOException {
		return visitUrl(url, paramMap, false, null);
	}

	public static String get(String url, String params, String sessionId) throws IOException {
		return visitUrl(url, params, false, sessionId);
	}

	public static String get(String url, Map<String, String> paramMap, String sessionId) throws IOException {
		return visitUrl(url, paramMap, false, sessionId);
	}

	private static String visitUrl(String url, String params, boolean isPost, String sessionId) throws IOException {
		return visitUrl(url, toNameValuePairMap(params), isPost, sessionId);
	}

	private static String visitUrl(String url, Map<String, String> paramMap, boolean isPost, String sessionId)
			throws IOException {
		HttpClientParam param = new HttpClientParam();
		param.setPost(isPost);
		param.setParamMap(paramMap);
		param.setSessionId(sessionId);
		return HttpClientUtils.exeUrlSync(url, param);
	}

	/**
	 * @deprecated 改用 visitUrl方法
	 */
	public static String readContentStringParam(String pageURL, String param, boolean isPost) throws IOException {

		InputStream in = null;
		ByteArrayOutputStream out = null;

		try {
			in = visitContentStringParam(pageURL, param, isPost);
			out = new ByteArrayOutputStream();

			byte[] buffer = new byte[BUFFER_SIZE];
			int length;
			while ((length = in.read(buffer)) != -1) {
				out.write(buffer, 0, length);
			}
			return new String(out.toByteArray());
		} finally {
			if (in != null)
				in.close();
			if (out != null)
				out.close();
		}
	}

	/**
	 * @deprecated 改用 visitUrl方法
	 */
	public static InputStream visitContent(String pageURL, String attName, String attVal, boolean isPost)
			throws IOException {
		URL url = new URL(pageURL);
		HttpURLConnection client = (HttpURLConnection) url.openConnection();
		if (isPost) {
			client.setRequestMethod("POST");// 提交模式
		}
		HttpURLConnection.setFollowRedirects(false);
		client.setInstanceFollowRedirects(false);
		// 表单参数与get形式一样
		StringBuffer params = new StringBuffer();
		client.setDoOutput(true);// 是否输入参数
		params.append(attName).append("=").append(attVal);
		byte[] bypes = params.toString().getBytes();
		client.getOutputStream().write(bypes);// 输入参数

		client.connect();

		return client.getInputStream();
	}

	/**
	 * @deprecated 改用 visitUrl方法
	 */
	public static InputStream visitContent(String pageURL, Map<String, String> paramMap, boolean isPost)
			throws IOException {

		URL url = new URL(pageURL);
		HttpURLConnection client = (HttpURLConnection) url.openConnection();
		if (isPost) {
			client.setRequestMethod("POST");// 提交模式
			if (paramMap == null) {
				paramMap = new HashMap<String, String>();
			}
		}
		HttpURLConnection.setFollowRedirects(false);
		client.setInstanceFollowRedirects(false);
		// 表单参数与get形式一样
		StringBuffer params = new StringBuffer();
		client.setDoOutput(true);// 是否输入参数
		boolean first = true;
		if (paramMap != null) {
			for (String key : paramMap.keySet()) {
				if (first)
					params.append(key).append("=").append(paramMap.get(key));
				else
					params.append("&").append(key).append("=").append(paramMap.get(key));
				first = false;
			}
			byte[] bypes = params.toString().getBytes();
			client.getOutputStream().write(bypes);// 输入参数
		}
		client.connect();

		return client.getInputStream();
	}

	/**
	 * @deprecated 改用 visitUrl方法
	 */
	public static InputStream visitContentStringParam(String pageURL, String stringParam, boolean isPost)
			throws IOException {
		URL url = new URL(pageURL);
		HttpURLConnection client = (HttpURLConnection) url.openConnection();
		if (isPost) {
			client.setRequestMethod("POST");// 提交模式
		}
		client.setConnectTimeout(30000);
		client.setReadTimeout(30000);
		HttpURLConnection.setFollowRedirects(false);
		client.setInstanceFollowRedirects(false);
		// 表单参数与get形式一样
		client.setDoOutput(true);// 是否输入参数
		byte[] bypes = stringParam.toString().getBytes();
		client.getOutputStream().write(bypes);// 输入参数
		client.connect();
		return client.getInputStream();
	}

	/**
	 * @throws Exception
	 * @deprecated 改用 visitUrl方法
	 */
	public static String readContent(String pageURL, String sessionId, String charset) throws IOException {
		HttpClientParam param = new HttpClientParam();
		param.setCharset(charset);
		param.setSessionId(sessionId);
		return HttpClientUtils.exeUrlSync(pageURL, param);
	}

	/**
	 * 从URL中分析字符串参数，放到一个map里
	 * 
	 * @param url URL
	 * @return map，存放的都是字符串的键值对
	 */
	public static Map<String, String> getParameters(String url) {
		HashMap<String, String> parameters = new HashMap<String, String>();
		if (Validators.isEmpty(url)) {
			return parameters;
		}

		int questionMarkIndex = url.indexOf(QUESTION_MARK);
		if (questionMarkIndex == -1 || questionMarkIndex == url.length() - 1) {
			return parameters;
		}

		String queryString = url.substring(questionMarkIndex + 1);
		String[] paramArray = queryString.split(String.valueOf(AND_SIGN));

		for (int i = 0; i < paramArray.length; i++) {
			int equalsSignIndex = paramArray[i].indexOf(EQUALS_SIGN);
			if (equalsSignIndex == -1) {
				continue;
			}

			String paramName = paramArray[i].substring(0, equalsSignIndex);
			String paramValue = paramArray[i].substring(equalsSignIndex + 1);
			parameters.put(paramName, paramValue);
		}

		return parameters;
	}

	/**
	 * 缩短url，把baseURL开头的部分去掉，缩短的url都是以"/"开头的
	 * 
	 * @param url
	 * @param baseURL
	 * @return
	 */
	public static String shortenURL(String url, String baseURL) {
		url = StringUtils.trim(url);
		baseURL = StringUtils.trim(baseURL);

		if (baseURL != null && baseURL.endsWith(String.valueOf(SEPARATOR_SIGN))) {
			baseURL = baseURL.substring(0, baseURL.length() - 1);
		}

		return !Validators.isEmpty(url) && !Validators.isEmpty(baseURL) && url.startsWith(baseURL)
				? url.substring(baseURL.length())
				: url;
	}

	public static URL toURL(String urlString) {
		URL url = null;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {

		}
		return url;
	}

	/**
	 * 检查指定url的文件是否存在
	 * 
	 * @param iconUrlString
	 * @return
	 */
	public static boolean checkFileUrl(String iconUrlString) {
		try {
			return readContent(iconUrlString) != null;
		} catch (IOException e) {
			return false;
		} finally {

		}
	}

	public static String getPrefix(HttpServletRequest request) {
		return request.getScheme() // 当前链接使用的协议
				+ "://" + request.getServerName()// 服务器地址
				+ ":" + request.getServerPort() // 端口号
				+ request.getContextPath();
	}
}
