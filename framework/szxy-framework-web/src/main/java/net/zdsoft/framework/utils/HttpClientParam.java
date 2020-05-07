package net.zdsoft.framework.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;

public class HttpClientParam {
	
	private Map<String, String> headMap;

	public Map<String, String> getHeadMap() {
		return headMap;
	}

	public HttpClientParam setHeadMap(Map<String, String> headMap) {
		this.headMap = headMap;
		return this;
	}
	
	public HttpClientParam addHeader(String key, String value) {
		if(headMap == null)
			headMap = new HashMap<String, String>();
		headMap.put(key, value);
		return this;
	}
	
	public HttpClientParam addParam(String key, String value) {
		if(paramMap == null)
			paramMap = new HashMap<String, String>();
		paramMap.put(key, value);
		return this;
	}
	
	public HttpClientParam addBody(String key, String value) {
		if(bodyMap == null)
			bodyMap = new HashMap<String, String>();
		bodyMap.put(key, value);
		return this;
	}
	
	/**
	 * 参数Map，组装到url中，可为空
	 */
	private Map<String, String> paramMap;
	/**
	 * 传入body的数据，可为空
	 */
	private Map<String, String> bodyMap;
	/**
	 * 异步调用的回调函数，可为空
	 */
	private FutureCallback<HttpResponse> callback;
	/**
	 * post方式提交，false表示get方法提交
	 */
	private boolean post;
	/**
	 * 转换的字符集
	 */
	private String charset;
	
	private String sessionId;

	public String getSessionId() {
		return sessionId;
	}

	public HttpClientParam setSessionId(String sessionId) {
		this.sessionId = sessionId;
		return this;
	}

	public String getCharset() {
		return charset;
	}

	public HttpClientParam setCharset(String charset) {
		this.charset = charset;
		return this;
	}

	public boolean isPost() {
		return post;
	}

	public HttpClientParam setPost(boolean post) {
		this.post = post;
		return this;
	}

	public Map<String, String> getParamMap() {
		return paramMap;
	}

	public HttpClientParam setParamMap(Map<String, String> paramMap) {
		this.paramMap = paramMap;
		return this;
	}

	public Map<String, String> getBodyMap() {
		return bodyMap;
	}

	public HttpClientParam setBodyMap(Map<String, String> bodyMap) {
		this.bodyMap = bodyMap;
		return this;
	}

	public FutureCallback<HttpResponse> getCallback() {
		return callback;
	}

	public void setCallback(FutureCallback<HttpResponse> callback) {
		this.callback = callback;
	}

}
