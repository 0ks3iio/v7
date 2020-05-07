package net.zdsoft.framework.utils;


public class CacheKeySyncUtils {
	public static void update(String name) {
		RedisUtils.del(RedisUtils.keys(name + ".ids.*").toArray(new String[0]));
		RedisUtils.del(RedisUtils.keys(name + ".count.*").toArray(new String[0]));
	}
}
