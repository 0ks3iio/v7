package net.zdsoft.careerplan.trade.utils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 使用google gson作为json序列化反序列化工具
 */
public class GsonFactory {

    private static class GsonHolder {
        private static Gson gson = new GsonBuilder().create();
    }

    public static Gson getGson() {
        return GsonHolder.gson;
    }
}
