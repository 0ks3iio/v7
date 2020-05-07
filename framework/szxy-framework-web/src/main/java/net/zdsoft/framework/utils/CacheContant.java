package net.zdsoft.framework.utils;

public class CacheContant {
	public static final String USER_BY_NAME = "user.by.name";

	public static String getKey(String prefixKey, String varKey) {
		return prefixKey + "@" + varKey;
	}

}
