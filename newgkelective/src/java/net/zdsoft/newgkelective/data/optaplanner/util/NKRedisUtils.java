package net.zdsoft.newgkelective.data.optaplanner.util;

import net.zdsoft.framework.utils.RedisUtils;

public class NKRedisUtils{
	
	public static void set(String key, String value) {
		RedisUtils.set(key, value);
	}
	
	public static String get(String key) {
		return RedisUtils.get(key);
	}
	
	public static long del(String... keys) {
		return RedisUtils.del(keys);
	}
	
}
