package net.zdsoft.framework.action;

import com.alibaba.fastjson.JSONObject;

public class JsonResult {

	public static JSONObject error() {
		JSONObject json = new JSONObject();
		json.put("result", -1);
		json.put("resultCode", -1);
		json.put("message", "操作异常！");
		json.put("resultMsg", "操作异常！");
		return json;
	}

	public static JSONObject ok() {
		JSONObject json = new JSONObject();
		json.put("result", 1);
		json.put("resultCode", 1);
		json.put("message", "操作成功！");
		json.put("resultMsg", "操作成功！");
		return json;
	}

	public static JSONObject msg(boolean ok, String msg) {
		JSONObject json = new JSONObject();
		json.put("message", msg);
		json.put("resultMsg", msg);
		if (ok) {
			json.put("result", 1);
			json.put("resultCode", 1);
		} else {
			json.put("result", -1);
			json.put("resultCode", -1);
		}
		return json;
	}
}
