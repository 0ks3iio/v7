package net.zdsoft.system.service.sms.entity;

import java.io.Serializable;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JSONUtils implements Serializable {
    private static final long serialVersionUID = 8582457927143547809L;

    public static String getString(JSONObject json, int index, String arrayKey, String objectKey) {
        JSONArray array = json.getJSONArray(arrayKey);
        if (array != null && array.size() > index) {
            JSONObject jsonObject = array.getJSONObject(index);
            if (jsonObject != null)
                return get(jsonObject, objectKey);
        }
        return null;
    }

    public static String getString(JSONArray jsonArray, int index, String objectKey) {
        if (jsonArray != null && jsonArray.size() > index) {
            JSONObject jsonObject = jsonArray.getJSONObject(index);
            if (jsonObject != null)
                return get(jsonObject, objectKey);
        }
        return null;
    }

    public static JSONObject getJsonObject(JSONArray jsonArray, int index) {
        if (jsonArray != null && jsonArray.size() > index) {
            return jsonArray.getJSONObject(index);
        }
        return null;
    }

    public static JSONObject getJsonObject(JSONObject json, int index, String arrayKey) {
        JSONArray array = json.getJSONArray(arrayKey);
        if (array != null && array.size() > index) {
            return array.getJSONObject(index);
        }
        return null;
    }

    public static String get(JSONObject json, String key) {
        if (json == null) {
            return null;
        }
        if (json.containsKey(key))
            return json.getString(key);
        else
            return null;
    }
}
