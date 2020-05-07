package net.zdsoft.framework.entity;

import java.util.Arrays;

import com.alibaba.fastjson.JSONArray;

public class JsonArray extends JSONArray {
	private static final long serialVersionUID = 1L;

	public JsonArray addEx(Object... os) {
		super.addAll(Arrays.asList(os));
		return this;
	}
}
