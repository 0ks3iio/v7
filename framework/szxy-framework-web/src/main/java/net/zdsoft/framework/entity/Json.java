package net.zdsoft.framework.entity;

import com.alibaba.fastjson.JSONObject;

public class Json extends JSONObject {

	private static final long serialVersionUID = 1L;

	public Json putEx(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
