package net.zdsoft.framework.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.MapUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.common.collect.Maps;

public class HttpClientUtils {

	private HttpClientUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static String exeUrlSync(String baseUrl, HttpClientParam param) throws IOException {
		if (baseUrl == null) {
			throw new IOException("请求地址不能为空！");
		}

		Map<String, String> paramMap = param.getParamMap();
		Map<String, String> bodyMap = param.getBodyMap();
		Map<String, String> headMap = param.getHeadMap();
		paramMap = paramMap == null ? Maps.newHashMap() : paramMap;
		bodyMap = bodyMap == null ? Maps.newHashMap() : bodyMap;
		headMap = headMap == null ? Maps.newHashMap() : headMap;

		List<BasicNameValuePair> urlParams = new ArrayList<>();
		for (Map.Entry<String, String> entry : paramMap.entrySet()) {
			urlParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}

		if (StringUtils.containAny(baseUrl, "?")) {
			String[] params = StringUtils.substringAfter(baseUrl, "?").split("&");
			for (String p : params) {
				String[] ps = p.split("=");
				if(ps.length == 2) {
					urlParams.add(new BasicNameValuePair(ps[0], ps[1]));
				}
			}
			baseUrl = StringUtils.substringBefore(baseUrl, "?");
		}

		List<BasicNameValuePair> postBody = new ArrayList<>();
		for (Map.Entry<String, String> entry : bodyMap.entrySet()) {
			postBody.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}

		HttpRequestBase httpMethod;
		CloseableHttpClient hc = null;
		try {
			hc = HttpClientFactory.getInstance().getHttpSyncClientPool().getHttpClient();
			HttpClientContext localContext = HttpClientContext.create();
			BasicCookieStore cookieStore = new BasicCookieStore();

			if (param.isPost()) {
				httpMethod = new HttpPost(baseUrl);

				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postBody);
				((HttpPost) httpMethod).setEntity(entity);
				
				if (StringUtils.isNotBlank(param.getSessionId()))
					httpMethod.setHeader("Cookie", "JSESSIONID=" + param.getSessionId());
				
				for(Entry<String, String> e : headMap.entrySet()) {
					httpMethod.setHeader(e.getKey(), e.getValue());
				}

				String getUrl = EntityUtils.toString(new UrlEncodedFormEntity(urlParams));
				httpMethod.setURI(new URI(httpMethod.getURI().toString() + "?" + getUrl));

			} else {
				httpMethod = new HttpGet(baseUrl);
				if (StringUtils.isNotBlank(param.getSessionId()))
					httpMethod.setHeader("Cookie", "JSESSIONID=" + param.getSessionId());
				
				for(Entry<String, String> e : headMap.entrySet()) {
					httpMethod.setHeader(e.getKey(), e.getValue());
				}

				String getUrl = EntityUtils.toString(new UrlEncodedFormEntity(urlParams));
				httpMethod.setURI(new URI(httpMethod.getURI().toString() + "?" + getUrl));
			}
			localContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
			CloseableHttpResponse resp = hc.execute(httpMethod, localContext);

			int statusCode = resp.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpMethod.abort();
				throw new RuntimeException("HttpClient,error status code :" + statusCode);
			}

			HttpEntity entity = resp.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity,
						StringUtils.isBlank(param.getCharset()) ? "utf8" : param.getCharset());
			}
			EntityUtils.consume(entity);
			resp.close();
			return result;
		} catch (Exception e) {
			throw new IOException("请求异常，错误信息：" + e.getMessage());
		}
	}

	public static void postAsync(String baseUrl, Map<String, String> bodyMap) throws IOException {
		HttpClientParam param = new HttpClientParam();
		param.setBodyMap(bodyMap);
		param.setPost(true);
		exeUrlAsync(baseUrl, param);
	}

	public static void getAsync(String baseUrl) throws IOException {
		HttpClientParam param = new HttpClientParam();
		exeUrlAsync(baseUrl, param);
	}

	public static String postSync(String baseUrl, Map<String, String> bodyMap) throws IOException {
		HttpClientParam param = new HttpClientParam();
		param.setBodyMap(bodyMap);
		param.setPost(true);
		return exeUrlSync(baseUrl, param);
	}

	public static String getSync(String baseUrl) throws IOException {
		HttpClientParam param = new HttpClientParam();
		return exeUrlSync(baseUrl, param);
	}

	public static void exeUrlAsync(String baseUrl, HttpClientParam param) throws IOException {
		if (baseUrl == null) {
			throw new IOException("请求地址不能为空！");
		}

		Map<String, String> paramMap = param.getParamMap();
		if (paramMap == null)
			paramMap = new HashMap<>();

		Map<String, String> bodyMap = param.getBodyMap();
		if (bodyMap == null)
			bodyMap = new HashMap<>();

		FutureCallback<HttpResponse> callback = param.getCallback();
		if (callback == null) {
			callback = new HttpAsyncCallback();
		}

		List<BasicNameValuePair> urlParams = new ArrayList<>();
		for (Map.Entry<String, String> entry : paramMap.entrySet()) {
			urlParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		if (StringUtils.containAny(baseUrl, "?")) {
			String[] params = StringUtils.substringAfter(baseUrl, "?").split("&");
			for (String p : params) {
				String[] ps = p.split("=");
				urlParams.add(new BasicNameValuePair(ps[0], ps[1]));
			}
			baseUrl = StringUtils.substringBefore(baseUrl, "?");
		}
		List<BasicNameValuePair> postBody = new ArrayList<>();
		for (Map.Entry<String, String> entry : bodyMap.entrySet()) {
			postBody.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}

		HttpRequestBase httpMethod;
		CloseableHttpAsyncClient hc = null;
		try {
			hc = HttpClientFactory.getInstance().getHttpAsyncClientPool().getAsyncHttpClient();
			hc.start();
			HttpClientContext localContext = HttpClientContext.create();
			BasicCookieStore cookieStore = new BasicCookieStore();

			if (param.isPost()) {
				httpMethod = new HttpPost(baseUrl);
				if (StringUtils.isNotBlank(param.getSessionId()))
					httpMethod.setHeader("Cookie", "JSESSIONID=" + param.getSessionId());
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postBody);
				((HttpPost) httpMethod).setEntity(entity);
				String getUrl = EntityUtils.toString(new UrlEncodedFormEntity(urlParams));
				try {
					httpMethod.setURI(new URI(httpMethod.getURI().toString() + "?" + getUrl));
				} catch (URISyntaxException e) {
				}

			} else {
				httpMethod = new HttpGet(baseUrl);
				if (StringUtils.isNotBlank(param.getSessionId()))
					httpMethod.setHeader("Cookie", "JSESSIONID=" + param.getSessionId());
				String getUrl = EntityUtils.toString(new UrlEncodedFormEntity(urlParams));
				try {
					httpMethod.setURI(new URI(httpMethod.getURI().toString() + "?" + getUrl));
				} catch (URISyntaxException e) {
				}
			}
			localContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
			hc.execute(httpMethod, localContext, callback);
		} catch (IOException e) {
			throw new IOException("请求异常，错误信息：" + e.getMessage());
		}
	}

}
