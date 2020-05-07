package net.zdsoft.framework.utils;

import com.alibaba.fastjson.JSONObject;

import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.JsonArray;

public class JsonUtils {
	
	public static Json createJson(String name, String value){
		Json json = new Json();
		json.put("name", name);
		json.put("value", value);
		return json;
	}
	
	public static JsonArray createArray(){
		JsonArray array = new JsonArray();
		return array;
	}
	
	public static Json createJson(){
		Json json = new Json();
		return json;
	}
	
	public static String getString(JSONObject json, String key){
		if(json.containsKey(key))
			return json.getString(key);
		else
			return null;
	}
	
	public static String getString(JSONObject json, String key, String defaultValue){
		if(json.containsKey(key))
			return json.getString(key);
		else
			return defaultValue;
	}
	
	public static Integer getInteger(JSONObject json, String key){
		if(json.containsKey(key))
			return json.getInteger(key);
		return null;
	}
	
	public static int getInt(JSONObject json, String key){
		if(json.containsKey(key))
			return json.getIntValue(key);
		return 0;
	}

}
