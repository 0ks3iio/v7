package net.zdsoft.framework.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.StringUtils;

public class SerializationUtils {

    public static <T> String serialize(T t) {
        if (t == null)
            return null;
        if (t instanceof String) {
            return String.valueOf(t);
        }
        return JSON.toJSONString(t, SerializerFeature.EMPTY);
    }

    public static <T> T deserialize(String data, Class<T> clazz) {
        if (clazz == String.class)
            return (T) data;
        if (StringUtils.isBlank(data) || (!StringUtils.contains(data, "{") && !StringUtils.contains(data, "[")))
            return null;
        return JSON.parseObject(data, clazz, Feature.IgnoreNotMatch);
    }

    public static <T> T deserialize(String data, TypeReference<T> type) {
        if (StringUtils.isBlank(data) || (!StringUtils.contains(data, "{") && !StringUtils.contains(data, "[")))
            return null;
        return JSON.parseObject(data, type, Feature.IgnoreNotMatch);
    }

    public static void main(String[] args) {
    }

}
