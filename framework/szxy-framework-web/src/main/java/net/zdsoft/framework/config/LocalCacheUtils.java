package net.zdsoft.framework.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LocalCacheUtils {
	// 默认的缓存容量
	private static int DEFAULT_CAPACITY = 512;
	// 最大容量
	private static int MAX_CAPACITY = 5000;

	private static Map<String, Map<String, CacheEntity>> cacheMap = new HashMap<>(DEFAULT_CAPACITY);

	private static Map<String, List<String>> keyMap = new HashMap<String, List<String>>();

	/**
	 * 将key-value 保存到本地缓存并制定该缓存的过期时间
	 * 
	 * @param key
	 * @param value
	 * @param expireTime
	 *            过期时间，如果是<=0 则表示永不过期
	 * @return
	 */
	public static void putValue(String objectType, String key, Object value) {
//		String cacheEntities = Evn.getString("cache_entities");
//		if (StringUtils.isBlank(cacheEntities))
//			return;
//
//		if (Arrays.asList(cacheEntities.split(",")).stream().noneMatch(
//				x -> StringUtils.equals(x, objectType) || StringUtils.equals(StringUtils.substringBefore(x, "("), objectType))) {
//			return;
//		}
//		List<String> keyList = keyMap.get(objectType);
//		if (keyList == null) {
//			keyList = new LinkedList<>();
//			keyMap.put(objectType, keyList);
//		}
//		int index = keyList.indexOf(key);
//		if (index < 0) {
//			keyList.add(key);
//		} else if (index < keyList.size() - 1) {
//			keyList.remove(key);
//			keyList.add(key);
//		}
//
//		Map<String, CacheEntity> map = cacheMap.get(objectType);
//		if (map == null) {
//			map = new HashMap<>();
//			cacheMap.put(objectType, map);
//		}
//
//		CacheEntity ce = new CacheEntity();
//		ce.setValue(value);
//		// ce.setExpire(expireTime);
//		if (keyList.size() > MAX_CAPACITY) {
//			String removeKey = keyList.remove(0);
//			map.remove(removeKey);
//		}
//		map.put(key, ce);
	}

	/**
	 * 从本地缓存中获取key对应的值，如果该值不存则则返回null
	 * 
	 * @param key
	 * @return
	 */
	public static <T> T getValue(String objectType, String key) {
//		String cacheEntities = Evn.getString("cache_entities");
//		if (StringUtils.isBlank(cacheEntities))
//			return null;
//		if (Arrays.asList(cacheEntities.split(",")).stream().noneMatch(
//				x -> StringUtils.equals(x, objectType) || StringUtils.equals(StringUtils.substringBefore(x, "("), objectType))) {
//			return null;
//		}
//		Map<String, CacheEntity> map = cacheMap.get(objectType);
//		if (map == null)
//			return null;
//		CacheEntity ce = map.get(key);
//		if (ce == null)
//			return null;
//		try {
//			return (T) ce.getValue();
//		} catch (Exception e) {
//			return null;
//		}
		return null;
	}
	
	/**
	 * 从本地缓存中去除key对应的对象
	 * 
	 * @param key
	 * @return
	 */
	public static void remove(String objectType, String key) {
//		if(StringUtils.isBlank(objectType) || StringUtils.isBlank(key)) {
//			return;
//		}
//		Map<String, CacheEntity> map = cacheMap.get(objectType);
//		if (map != null) {
//			map.remove(key);
//		}
	}

	/**
	 * 清空所有
	 */
	public void clear() {
		cacheMap.clear();
	}

	public void clear(String objectType) {
		Map<String, CacheEntity> map = cacheMap.get(objectType);
		if (map != null) {
			map.clear();
		}
	}
}
