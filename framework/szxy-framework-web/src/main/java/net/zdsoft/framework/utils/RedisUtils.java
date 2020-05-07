package net.zdsoft.framework.utils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;

import net.zdsoft.framework.exception.BusinessRuntimeException;
import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.exceptions.JedisException;

public class RedisUtils {

	private static Logger logger = Logger.getLogger(RedisUtils.class);

	// private static JedisPool pool;
	private static JedisSentinelPool sentinelPool;
	private static JedisPool pool;
	private static boolean isSentinelPool;// 是否哨兵模式

	public static final int TIME_FOREEVER = 0;
	public static final int TIME_FIVE_SECONDS = 5;
	public static final int TIME_SHORT_CACHE = 10;
	public static final int TIME_HALF_MINUTE = 30;
	public static final int TIME_ONE_MINUTE = 60;
	public static final int TIME_FIVE_MINUTES = 300;
	public static final int TIME_TEN_MINUTES = 600;
	public static final int TIME_HALF_HOUR = 1800;
	public static final int TIME_ONE_HOUR = 3600;
	public static final int TIME_ONE_DAY = 86400;
	public static final int TIME_ONE_WEEK = 604800;
	public static final int TIME_ONE_MONTH = 2592000;
	public static final int TIME_MAX_TIME = 25920000;

	public static final String KEY_PREFIX = "eis.v7.";
	private static final String REDIS_RESOURCE_PROPERTY_NAME = "redis.properties.external";
	private static final String REDIS_RESOURCE_NAME = "conf/redis.properties";
	private static Map<String[], JedisPubSub> pubSubMap = new ConcurrentHashMap<String[], JedisPubSub>();
	private static Map<String[], JedisPubSub> patternSubMap = new ConcurrentHashMap<String[], JedisPubSub>();
	// private static ShardedJedisPool shardedJedisPool;

	// 初始化redis客户端信息
	static {
		logger.info("reids启动");
		// 加载redis配置文件
		Properties properties = createRedisProperties(REDIS_RESOURCE_NAME);

		// 创建jedis池配置实例
		JedisPoolConfig config = new JedisPoolConfig();
		// 设置最大连接数
		config.setMaxTotal(Integer.valueOf(properties
				.getProperty("redis.maxActive")));
		// 获取Jedis连接的最大等待时间
		config.setMaxWaitMillis(Integer.valueOf(properties
				.getProperty("redis.maxWait")));
		// 最大空闲连接数
		config.setMaxIdle(Integer.valueOf(properties
				.getProperty("redis.maxIdle")));
		// 在获取Jedis连接时，自动检验连接是否可用
		config.setTestOnBorrow(Boolean.valueOf(properties
				.getProperty("redis.testOnBorrow")));
		// 在将连接放回池中前，自动检验连接是否有效
		config.setTestOnReturn(true);
		// 自动测试池中的空闲连接是否都是可用连接
		config.setTestWhileIdle(true);
		// 是否启动哨兵模式
		isSentinelPool = Boolean.valueOf(properties
				.getProperty("redis.sentinel.enable"));
		if (isSentinelPool) {
			Set<String> sentinelUrl = new HashSet<String>();
			for (Object obj : properties.keySet()) {
				String key = (String) obj;
				if (key.indexOf("redis.sentinel.url") == 0) {
					sentinelUrl.add(properties.getProperty(key));
				}
			}
			if (StringUtils.isNotBlank(properties.getProperty("redis.pass"))) {
				sentinelPool = new JedisSentinelPool(
						properties.getProperty("redis.masterName"),
						sentinelUrl, config, 2000,
						properties.getProperty("redis.pass"));
			} else {
				sentinelPool = new JedisSentinelPool(
						properties.getProperty("redis.masterName"),
						sentinelUrl, config);
			}
		} else {
			// 根据配置实例化jedis池
			if (StringUtils.isNotBlank(properties.getProperty("redis.pass"))) {
				pool = new JedisPool(config,
						properties.getProperty("redis.ip"),
						Integer.valueOf(properties.getProperty("redis.port")),
						2000, properties.getProperty("redis.pass"));
			} else {
				pool = new JedisPool(config,
						properties.getProperty("redis.ip"),
						Integer.valueOf(properties.getProperty("redis.port")));
			}
		}

		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				check();
			}
		}, 10000, 60000);
	}

	public static synchronized  Jedis getJedis() {
		Jedis jedis = null;
		try {
			if (isSentinelPool) {
				jedis = sentinelPool.getResource();
			} else {
				jedis = pool.getResource();
			}
			if (jedis == null) {
				logger.error(" getJedis retrun null ,init pool error,check it!");
			}
			return jedis;
		} catch (Exception e) {
		}
		return jedis;
	}

	public static synchronized Jedis getJedis(Jedis jedis) {
		if (jedis != null) {
			return jedis;
		}
		try {
			if (isSentinelPool) {
				jedis = sentinelPool.getResource();
			} else {
				jedis = pool.getResource();
			}
			if (jedis == null) {
				logger.error(" getJedis retrun null ,init pool error,check it!");
			}
			return jedis;
		} catch (Exception e) {
		}
		return jedis;
	}

	public static boolean check() {
		Jedis jedis = null;
		boolean success = true;
		try {
			jedis = getJedis();
			jedis.get("");
			reSubscribe();
		} catch (Exception e) {
			logger.warn("redis服务异常……");
			if (!isSentinelPool) {
				try {
					logger.warn("redis服务异常，重新连接……");
					// 加载redis配置文件
					Properties properties = createRedisProperties(REDIS_RESOURCE_NAME);
					// 创建jedis池配置实例
					JedisPoolConfig config = new JedisPoolConfig();
					// 设置池配置项值
					config.setMaxTotal(Integer.valueOf(properties
							.getProperty("redis.maxActive")));
					config.setMaxWaitMillis(Integer.valueOf(properties
							.getProperty("redis.maxWait")));
					config.setMaxIdle(Integer.valueOf(properties
							.getProperty("redis.maxIdle")));
					config.setTestOnBorrow(Boolean.valueOf(properties
							.getProperty("redis.testOnBorrow")));
					// 根据配置实例化jedis池
					if (StringUtils.isNotBlank(properties
							.getProperty("redis.pass"))) {
						pool = new JedisPool(config,
								properties.getProperty("redis.ip"),
								Integer.valueOf(properties
										.getProperty("redis.port")), 2000,
								properties.getProperty("redis.pass"));
					} else {
						pool = new JedisPool(config,
								properties.getProperty("redis.ip"),
								Integer.valueOf(properties
										.getProperty("redis.port")));
					}

				} catch (Exception e1) {
					return false;
				}
			}
			success = false;
		} finally {
			returnResource(jedis, success);
		}
		return true;
	}

	private static void reSubscribe() {
		if ( pubSubMap.isEmpty() ) {
			return;
		}
		for (Map.Entry<String[], JedisPubSub> pubSubEntry : pubSubMap.entrySet()) {
			RedisUtils.subscribe(pubSubEntry.getValue(), pubSubEntry.getKey());
			pubSubMap.remove(pubSubEntry.getKey());
			if (logger.isDebugEnabled()) {
				logger.debug("重新订阅" + Arrays.toString(pubSubEntry.getKey()));
			}
		}

		for (Map.Entry<String[], JedisPubSub> entry : patternSubMap.entrySet()) {
			RedisUtils.pSubscribe(entry.getValue(), entry.getKey());
			patternSubMap.remove(entry.getKey());
			if (logger.isDebugEnabled()) {
				logger.debug("重新订阅" + Arrays.toString(entry.getKey()));
			}
		}
	}

	/**
	 * 获取redis.properties配置信息
	 * 
	 * @author dingw
	 * @param
	 * @return
	 */
	private static Properties createRedisProperties(String resourceFile) {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource(resourceFile);
		Properties properties = null;
		try {
			properties = FileUtils.readProperties(resource.getInputStream());
			// 读取外部文件
			String externalResourceFile = properties
					.getProperty(REDIS_RESOURCE_PROPERTY_NAME);
			if (!Validators.isEmpty(externalResourceFile)) {
				Resource externalResource = new FileSystemResource(
						externalResourceFile);
				if (externalResource.isReadable()) {
					logger.info("Redis External resource file will be read: "
							+ externalResourceFile);
					return FileUtils.readProperties(externalResource
							.getInputStream());
				}
			}
			return properties;
		} catch (Exception e) {
			logger.error("Read Redis properties from resource(" + resourceFile
					+ ") error", e);
		}

		return properties;
	}

	public static String get(String key, int time, RedisInterface<String> ri) {
		return get(null, key, time, ri);
	}

	public static String get(Jedis jedis, String key, int time,
			RedisInterface<String> ri) {
		String value = get(jedis, key);
		if (value == null) {
			String t = ri.queryData();
			if (t == null) {
				return null;
			}

			value = SerializationUtils.serialize(t);
			if (time > 0) {
				set(jedis, key, value, time);
			} else {
				set(jedis, key, value);
			}
			return t;
		} else {
			return SerializationUtils.deserialize(value, String.class);
		}
	}

	/**
	 * 
	 * @param key
	 * @param time
	 * @param ri
	 * @return
	 */
	public static <T> T getObject(String key, int time, Class<T> clazz,
			RedisInterface<T> ri) {
		return getObject(null, key, time, 0, clazz, ri);
	}

	public static <T> T getObject(String key, int time, TypeReference<T> type,
			RedisInterface<T> ri) {
		return getObject(null, key, time, 0, type, ri);
	}

	/**
	 * 
	 * @param jedis
	 * @param key
	 * @param time
	 * @param ri
	 * @return
	 */
	public static <T> T getObject(Jedis jedis, String key, int time,
			Class<T> clazz, RedisInterface<T> ri) {
		return getObject(jedis, key, time, 0, clazz, ri);
	}

	/**
	 * 
	 * @param key
	 * @param time
	 * @param maxCount
	 * @param ri
	 * @return
	 */
	public static <T> T getObject(String key, int time, int maxCount,
			Class<T> clazz, RedisInterface<T> ri) {
		return getObject(null, key, time, maxCount, clazz, ri);
	}

	/**
	 * 
	 * @param jedis
	 * @param key
	 * @param time
	 * @param maxCount
	 * @param ri
	 * @return
	 */
	public static <T> T getObject(Jedis jedis, String key, int time,
			int maxCount, Class<T> clazz, RedisInterface<T> ri) {
		String value = get(jedis, key);
		if (value == null) {
			T t = ri.queryData();
			if (maxCount > 0) {
				if (CollectionUtils.size(t) > maxCount) {
					return t;
				}
			}
			if (t == null) {
				return null;
			}

			value = SerializationUtils.serialize(t);
			if (time > 0) {
				set(jedis, key, value, time);
			} else {
				set(jedis, key, value);
			}
			return t;
		} else {
			return SerializationUtils.deserialize(value, clazz);
		}
	}

	public static <T> T getObject(Jedis jedis, String key, int time,
			int maxCount, TypeReference<T> type, RedisInterface<T> ri) {
		String value = get(jedis, key);
		if (value == null) {
			T t = ri.queryData();
			if (t == null) {
				return null;
			}
			if (maxCount > 0) {
				if (CollectionUtils.size(t) > maxCount) {
					return t;
				}
			}

			value = SerializationUtils.serialize(t);
			if (time > 0) {
				set(jedis, key, value, time);
			} else {
				set(jedis, key, value);
			}
			return t;
		} else {
			return JSON.parseObject(value, type, Feature.IgnoreNotMatch);
		}
	}

	public static List<String> getList(String key,
			RedisInterface<List<String>> ri) {
		return getList(null, key, ri);
	};

	public static List<String> getList(Jedis jedis, String key,
			RedisInterface<List<String>> ri) {
		List<String> ids = lrange(key);
		if (CollectionUtils.isEmpty(ids)) {
			ids = ri.queryData();
			if (ids != null) {
				lpush(key, ids.toArray(new String[0]));
			}
		}
		return ids;
	};

	public static Set<String> getSet(String key, RedisInterface<Set<String>> ri) {
		return getSet(null, key, ri);
	};

	public static Set<String> getSet(Jedis jedis, String key,
			RedisInterface<Set<String>> ri) {
		Set<String> ids = smembers(jedis, key);
		if (CollectionUtils.isEmpty(ids)) {
			ids = ri.queryData();
			if (CollectionUtils.isNotEmpty(ids)) {
				sadd(jedis, key, ids.toArray(new String[0]));
			}
		}
		return ids;
	};

	/**
	 * @param key
	 * @param ri
	 * @return
	 */
	public static <T> T getObject(String key, Class<T> clazz,
			RedisInterface<T> ri) {
		return getObject(key, 0, clazz, ri);
	}

	/**
	 * 
	 * @param jedis
	 * @param key
	 * @param ri
	 * @return
	 */
	public static <T> T getObject(Jedis jedis, String key, Class<T> clazz,
			RedisInterface<T> ri) {
		return getObject(jedis, key, 0, clazz, ri);
	}

	public static String get(String key, RedisInterface<String> ri) {
		return get(key, 0, ri);
	}

	public static String get(Jedis jedis, String key, RedisInterface<String> ri) {
		return get(jedis, key, 0, ri);
	}

	/**
	 * 查找
	 * 
	 * @param key
	 */
	public static Set<String> keys(String key) {
		return keys(null, key);
	}
	
	public static Set<String> hkeys(String key){
		return hkeys(null, key);
	}

	public static Set<String> hkeys(Jedis jedis, String key) {
		final String keyf = KEY_PREFIX + key;
		Jedis jedis2 = getJedis(jedis);
		boolean success = true;
		try {
			return jedis2.hkeys(keyf);
		} catch (JedisException e) {
			logger.error("jedis set error", e);
			success = false;
			return new HashSet<String>();
		} finally {
			returnResource(jedis2, success);
		}
	
	}
	public static Set<String> keys(Jedis jedis, String key) {
		final String keyf = KEY_PREFIX + key;
		Jedis jedis2 = getJedis(jedis);
		boolean success = true;
		try {
			return jedis2.keys(keyf);
		} catch (JedisException e) {
			logger.error("jedis set error", e);
			success = false;
			return new HashSet<String>();
		} finally {
			returnResource(jedis2, success);
		}
	}

	/**
	 * 单次存储
	 * 
	 * @param key
	 * @param value
	 */
	public static void set(String key, String value) {
		set(key, value, 0);
	}

	public static void set(Jedis jedis, String key, String value) {
		set(jedis, key, value, 0);
	}

	/**
	 * 
	 * @param key
	 * @param t
	 * @param seconds
	 */
	public static <T> void setObject(String key, T t, int seconds) {
		setObject(null, key, t, seconds);
	}

	/**
	 * 
	 * @param jedis
	 * @param key
	 * @param t
	 * @param seconds
	 */
	public static <T> void setObject(Jedis jedis, String key, T t, int seconds) {
		String value = SerializationUtils.serialize(t);
		if (seconds > 0) {
			set(jedis, key, value, seconds);
		} else {
			set(jedis, key, value);
		}
	}

	/**
	 * 
	 * @param key
	 * @param t
	 */
	public static <T> void setObject(String key, T t) {
		setObject(null, key, t);
	}

	/**
	 * 
	 * @param jedis
	 * @param key
	 * @param t
	 */
	public static <T> void setObject(Jedis jedis, String key, T t) {
		setObject(jedis, key, t, 0);
	}

	/**
	 * 保存数据
	 * 
	 * @param key
	 * @param value
	 * @param seconds
	 *            保存时间，单位：秒
	 */
	public static void set(String key, String value, int seconds) {
		set(null, key, value, seconds);
	}

	public static void set(Jedis jediso, String key, String value, int seconds) {
		final String keyf = KEY_PREFIX + key;
		boolean needReturnJedis = jediso == null;
		Jedis jedis = getJedis(jediso);
		boolean success = true;
		try {
			if (value == null) {
				del(key);
				logger.error("key:" + key + "-------value不能为null");
				return;
			}
			if (seconds > 0) {
				jedis.setex(keyf, seconds, value);
			} else {
				jedis.set(keyf, value);
			}
		} catch (JedisException e) {
			logger.error("jedis set error", e);
			success = false;
		}
		finally {
			if(needReturnJedis)
				returnResource(jedis, success);
		}
	}

	/**
	 * 添加hash对象
	 * 
	 * @param key
	 * @param hash
	 */
	public static void setHash(String key, Map<String, String> hash) {
		final String keyf = KEY_PREFIX + key;
		Jedis jedis = null;
		boolean success = true;
		try {
			jedis = getJedis();
			jedis.hmset(keyf, hash);
		} catch (JedisException e) {
			logger.error("jedis setHash error", e);
			success = false;
		} finally {
			returnResource(jedis, success);
		}
	}

	/**
	 * 设置hash中某个域的值(已存在的会覆盖)
	 * 
	 * @param key
	 * @param field
	 * @param value
	 */
	public static void hset(String key, String field, String value) {
		final String keyf = KEY_PREFIX + key;
		Jedis jedis = null;
		boolean success = true;
		try {
			if (value == null) {
				hdel(key, field);
				logger.error("key:" + key + "-------value不能为null");
				return;
			}
			jedis = getJedis();
			jedis.hset(keyf, field, value);
		} catch (JedisException e) {
			logger.error("jedis setHashField error", e);
			success = false;
		} finally {
			returnResource(jedis, success);
		}
	}

	public static <T> void hsetObject(String key, String field, T t) {
		final String keyf = KEY_PREFIX + key;
		Jedis jedis = null;
		boolean success = true;
		try {
			if (t == null) {
				hdel(key, field);
				logger.error("key:" + key + "-------value不能为null");
				return;
			}
			jedis = getJedis();
			jedis.hset(keyf, field, SerializationUtils.serialize(t));
		} catch (JedisException e) {
			logger.error("jedis setHashField error", e);
			success = false;
		} finally {
			returnResource(jedis, success);
		}
	}

	/**
	 * 批量删除hash域中多个值
	 * 
	 * @param key
	 * @param fields
	 */
	public static void hdel(String key, String... fields) {
		final String keyf = KEY_PREFIX + key;
		Jedis jedis = null;
		boolean success = true;
		try {
			jedis = getJedis();
			jedis.hdel(keyf, fields);
		} catch (JedisException e) {
			logger.error("jedis batchRemoveHashField error", e);
			success = false;
		} finally {
			returnResource(jedis, success);
		}
	}

	/**
	 * 获取hash中某个域的值
	 * 
	 * @param key
	 * @param field
	 */
	public static String hget(String key, String field) {
		final String keyf = KEY_PREFIX + key;
		Jedis jedis = null;
		boolean success = true;
		try {
			jedis = getJedis();
			return jedis.hget(keyf, field);
		} catch (JedisException e) {
			logger.error("jedis getHashField error", e);
			success = false;
			return null;
		} finally {
			returnResource(jedis, success);
		}
	}

	public static <T> T hgetObject(String key, String field,
			TypeReference<T> type) {

		final String keyf = KEY_PREFIX + key;
		Jedis jedis = null;
		boolean success = true;
		try {
			jedis = getJedis();
			String value = jedis.hget(keyf, field);
			return JSON.parseObject(value, type, Feature.IgnoreNotMatch);
		} catch (JedisException e) {
			logger.error("jedis getHashField error", e);
			success = false;
			return null;
		} finally {
			returnResource(jedis, success);
		}

	}

	public static <T> T hgetObject(String key, String field, Class<T> clazz) {
		final String keyf = KEY_PREFIX + key;
		Jedis jedis = null;
		boolean success = true;
		try {
			jedis = getJedis();
			return SerializationUtils.deserialize(jedis.hget(keyf, field),
					clazz);
		} catch (JedisException e) {
			logger.error("jedis getHashField error", e);
			success = false;
			return null;
		} finally {
			returnResource(jedis, success);
		}
	}

	/**
	 * 判断hash中的某个域是否存在
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	public static boolean hexists(String key, String field) {
		final String keyf = KEY_PREFIX + key;
		Jedis jedis = null;
		boolean success = true;
		try {
			jedis = getJedis();
			return jedis.hexists(keyf, field);
		} catch (JedisException e) {
			logger.error("jedis checkHExists error", e);
			success = false;
			return false;
		} finally {
			returnResource(jedis, success);
		}
	}

	/**
	 * 将名称为key的hash中field的value增加integer
	 * 
	 * @param key
	 * @param field
	 * @param value
	 */
	public static long hincrby(String key, String field, int value) {
		final String keyf = KEY_PREFIX + key;
		Jedis jedis = null;
		boolean success = true;
		try {
			jedis = getJedis();
			return jedis.hincrBy(keyf, field, value);
		} catch (JedisException e) {
			logger.error("jedis hincrby error", e);
			success = false;
			return -1;
		} finally {
			returnResource(jedis, success);
		}
	}

	@SuppressWarnings("deprecation")
	public static void returnResource(Jedis jedis, boolean success) {
		if (jedis == null) {
			return;
		}
		if (!success) {
			if (isSentinelPool) {
				sentinelPool.returnBrokenResource(jedis);
			} else {
				pool.returnBrokenResource(jedis);
			}
		} else {
			if (isSentinelPool) {
				sentinelPool.returnResource(jedis);
			} else {
				pool.returnResource(jedis);
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static void returnResource(Jedis jedis) {
		if (jedis == null) {
			return;
		}
		if (isSentinelPool) {
			sentinelPool.returnResource(jedis);
		} else {
			pool.returnResource(jedis);
		}
	}

	/**
	 * 从redis获取key对应的value值
	 * 
	 * @param key
	 * @return
	 */
	public static String get(String key) {
		return get(null, key);
	}

	public static String get(Jedis jediso, String key) {
		boolean reResource = jediso == null;
		Jedis jedis = getJedis(jediso);
		boolean success = true;
		try {
			return jedis.get(KEY_PREFIX + key);
		} catch (JedisException e) {
			logger.error("jedis set error", e);
			success = false;
		} finally {
			if(reResource)
				returnResource(jedis, success);
		}
		return null;
	}

	public static boolean exists(Jedis jediso, String key) {
		Jedis jedis = getJedis(jediso);
		boolean success = true;
		try {
			return jedis.exists(KEY_PREFIX + key);
		} catch (JedisException e) {
			logger.error("jedis set error", e);
			success = false;
		} finally {
			returnResource(jedis, success);
		}
		return false;
	}

	public static boolean notExists(String key) {
		return !exists(key);
	}

	public static boolean exists(String key) {
		Jedis jedis = getJedis();
		boolean success = true;
		try {
			return jedis.exists(KEY_PREFIX + key);
		} catch (JedisException e) {
			logger.error("jedis set error", e);
			success = false;
		} finally {
			returnResource(jedis, success);
		}
		return false;
	}

	/**
	 * @param key
	 * @return
	 */
	public static <T> T getObject(String key, Class<T> clazz) {
		String value = get(key);
		if(value == null)
			return null;
		return SerializationUtils.deserialize(value, clazz);
	}

	public static <T> T getObject(String key, TypeReference<T> type) {
		String value = get(key);
		return SerializationUtils.deserialize(value, type);
	}

	/**
	 * 
	 * @param jedis
	 * @param key
	 * @return
	 */
	public static <T> T getObject(Jedis jedis, String key, Class<T> clazz) {
		String value = get(jedis, key);
		return SerializationUtils.deserialize(value, clazz);
	}

	/**
	 * 批量删除key对应的value值
	 * 
	 * @param keys
	 * @return
	 */
	public static long del(String... keys) {
		return del(null, keys);
	}

	public static long del(Jedis jediso, String... keys) {
		Jedis jedis = getJedis(jediso);
		boolean success = true;
		try {
			List<String> list = new ArrayList<String>();
			for (String s : keys) {
				if (StringUtils.startsWith(s, KEY_PREFIX)) {
					list.add(s);
				} else {
					list.add(KEY_PREFIX + s);
				}
			}
			if (CollectionUtils.isNotEmpty(list)) {
				return jedis.del(list.toArray(new String[0]));
			} else {
				return 0;
			}
		} catch (JedisException e) {
			logger.error("jedis remove error", e);
			success = false;
			return 0;
		} finally {
			returnResource(jedis, success);
		}
	}

	public static long delBeginWith(String keyMatcher) {
		Set<String> keys = keys(keyMatcher + "*");
		return del(keys.toArray(new String[0]));
	}

	/**
	 * 数值增加value , -1表示失败
	 * 
	 * @param key
	 * @param value
	 * 
	 */
	public static Long incrby(String key, long value) {
		Jedis jedis = null;
		boolean success = true;
		try {
			jedis = getJedis();
			return jedis.incrBy(KEY_PREFIX + key, value);
		} catch (JedisException e) {
			logger.error("jedis incrby error", e);
			success = false;
			return -1L;
		} finally {
			returnResource(jedis, success);
		}
	}

	/**
	 * 数值增加value , -1表示失败
	 *
	 * @param key
	 * @param value
	 *
	 */
	public static Long incrby(String key, int value) {
		return incrby(key, (long) value);
	}

	/**
	 * 设置过期时间
	 * 
	 * @param key
	 * @param seconds
	 */
	public static void expire(String key, int seconds) {
		expire(null, key, seconds);
	}

	public static void expire(Jedis jediso, String key, int seconds) {
		Jedis jedis = getJedis(jediso);
		boolean success = true;
		try {
			jedis.expire(KEY_PREFIX + key, seconds);
		} catch (JedisException e) {
			logger.error("jedis expire error", e);
			success = false;
			return;
		} finally {
			returnResource(jedis, success);
		}
	}

	/**
	 * 数值减少value
	 * 
	 * @param key
	 * @param value
	 */
	public static Long decrby(String key, int value) {
		Jedis jedis = null;
		boolean success = true;
		try {
			jedis = getJedis();
			Assert.notNull(jedis, "获取不到jedis实例");
			return jedis.decrBy(KEY_PREFIX + key, value);
		} catch (JedisException e) {
			logger.error("jedis decrby error", e);
			success = false;
			return -1L;
		} finally {
			returnResource(jedis, success);
		}
	}

	/**
	 * 列表长度
	 * 
	 * @param key
	 * @return
	 */
	public static Long llen(String key) {
		Jedis jedis = null;
		boolean success = true;
		try {
			jedis = getJedis();
			Assert.notNull(jedis, "获取不到jedis实例");
			return jedis.llen(KEY_PREFIX + key);
		} catch (JedisException e) {
			logger.error("jedis lpush error", e);
			success = false;
			return -1L;
		} finally {
			returnResource(jedis, success);
		}
	}

	/**
	 * 在list的头部添加数据
	 * 
	 * @param key
	 * @param values
	 */
	public static void lpush(String key, String... values) {
		lpush(null, key, values);
	}

	public static void lpush(Jedis jediso, String key, String... values) {
		boolean success = true;
		Jedis jedis = getJedis(jediso);
		try {
			Assert.notNull(jedis, "获取不到jedis实例");
			jedis.lpush(KEY_PREFIX + key, values);
		} catch (JedisException e) {
			logger.error("jedis lpush error", e);
			success = false;
		} finally {
			returnResource(jedis, success);
		}
	}

	/**
	 * 在list的尾部添加数据
	 * 
	 * @param key
	 * @param values
	 */
	public static void rpush(String key, String... values) {
		rpush(null, key, values);
	}

	public static String lpop(String key) {
		return lpop(null, key);
	}

	public static String lpop(Jedis jediso, String key) {
		Jedis jedis = getJedis(jediso);
		boolean success = true;
		try {
			Assert.notNull(jedis, "获取不到jedis实例");
			return jedis.lpop(KEY_PREFIX + key);
		} catch (JedisException e) {
			logger.error("jedis rpush error", e);
			success = false;
		} finally {
			returnResource(jedis, success);
		}
		return "";
	}
	
	public static String rpop(String key) {
		return rpop(null, key);
	}

	public static String rpop(Jedis jediso, String key) {
		Jedis jedis = getJedis(jediso);
		boolean success = true;
		try {
			Assert.notNull(jedis, "获取不到jedis实例");
			return jedis.rpop(KEY_PREFIX + key);
		} catch (JedisException e) {
			logger.error("jedis rpop error", e);
			success = false;
		} finally {
			returnResource(jedis, success);
		}
		return "";
	}

	public static void rpush(Jedis jediso, String key, String... values) {
		Jedis jedis = getJedis(jediso);
		boolean success = true;
		try {
			Assert.notNull(jedis, "获取不到jedis实例");
			jedis.rpush(KEY_PREFIX + key, values);
		} catch (JedisException e) {
			logger.error("jedis rpush error", e);
			success = false;
		} finally {
			returnResource(jedis, success);
		}
	}

	public static List<String> lrange(String key) {
		Jedis jedis = null;
		boolean success = true;
		try {
			jedis = getJedis();
			Assert.notNull(jedis, "获取不到jedis实例");
			return jedis.lrange(KEY_PREFIX + key, 0, -1);
		} catch (JedisException e) {
			logger.error("jedis get error", e);
			success = false;
			return null;
		} finally {
			returnResource(jedis, success);
		}
	}

	public static void eval(String script, String key, List<String> args) {
		Jedis jedis = null;
		boolean success = true;
		try {
			jedis = getJedis();
			Assert.notNull(jedis, "获取不到jedis实例");
			jedis.eval(script, Collections.singletonList(KEY_PREFIX + key), args);
		} catch (JedisException e) {
			logger.error("jedis get error", e);
			success = false;
		} finally {
			returnResource(jedis, success);
		}
	}

	/**
	 * 添加元素member
	 * 
	 * @param key
	 * @param
	 */
	public static void sadd(String key, String... members) {
		sadd(null, key, members);
	}

	public static void sadd(Jedis jediso, String key, String... members) {
		Jedis jedis = getJedis(jediso);
		boolean success = true;
		try {
			Assert.notNull(jedis, "获取不到jedis实例");
			jedis.sadd(KEY_PREFIX + key, members);
		} catch (JedisException e) {
			logger.error("jedis sadd error", e);
			success = false;
			return;
		} finally {
			returnResource(jedis, success);
		}
	}

	public static void srem(String key, String... members) {
		srem(null, key, members);
	}

	public static void srem(Jedis jediso, String key, String... members) {
		Jedis jedis = getJedis(jediso);
		boolean success = true;
		try {
			Assert.notNull(jedis, "获取不到jedis实例");
			jedis.srem(KEY_PREFIX + key, members);
		} catch (JedisException e) {
			logger.error("jedis sadd error", e);
			success = false;
			return;
		} finally {
			returnResource(jedis, success);
		}
	}

	/**
	 * 获取key对应的所有元素
	 * 
	 * @param key
	 * @return
	 */
	public static Set<String> smembers(String key) {
		return smembers(null, key);
	}

	public static Set<String> smembers(Jedis jediso, String key) {
		Jedis jedis = getJedis(jediso);
		boolean success = true;
		try {
			Assert.notNull(jedis, "获取不到jedis实例");
			return jedis.smembers(KEY_PREFIX + key);
		} catch (JedisException e) {
			logger.error("jedis smembers error", e);
			success = false;
			return null;
		} finally {
			returnResource(jedis, success);
		}
	}

	/**
	 * 返回key中所对应的所有键及对应值
	 * 
	 * @param key
	 */
	public static Map<String, String> hgetAll(String key) {
		return hgetAll(null, key);
	}

	public static Map<String, String> hgetAll(Jedis jediso, String key) {
		Jedis jedis = getJedis(jediso);
		boolean success = true;
		try {
			Assert.notNull(jedis, "获取不到jedis实例");
			return jedis.hgetAll(KEY_PREFIX + key);
		} catch (JedisException e) {
			logger.error("jedis hgetall error", e);
			success = false;
			return null;
		} finally {
			returnResource(jedis, success);
		}
	}

	/**
	 * 添加元素到指定数量的list中
	 *  @param redisKey
	 * @param value
	 * @param maxCount
	 */
	public static void addDataToList(String redisKey, String value, int maxCount) {
		Jedis jedis = getJedis();
		boolean success = true;
		try {
			Assert.notNull(jedis, "获取不到jedis实例");
			// 向List头部追加记录
			jedis.lpush(KEY_PREFIX + redisKey, value);
			// 仅保留指定区间内的记录数，删除区间外的记录。下标从 0 开始，即 end 需要最大值 -1
			jedis.ltrim(KEY_PREFIX + redisKey, 0, maxCount -1);
		} catch (JedisException e) {
			logger.error("jedis lpush error", e);
			success = false;
		} finally {
			returnResource(jedis, success);
		}
		
	}
	
	public static String addDataToList(Jedis jediso, String redisKey, String value, int maxCount) {
		Jedis jedis = getJedis(jediso);
		boolean success = true;
		try {
			Assert.notNull(jedis, "获取不到jedis实例");
			jedis.lpush(KEY_PREFIX + redisKey);
			jedis.ltrim(redisKey, 0, maxCount -1);
		} catch (JedisException e) {
			logger.error("jedis rpush error", e);
			success = false;
		} finally {
			returnResource(jedis, success);
		}
		return "";
	}

	/**
	 * 获取指定数量的list
	 * 
	 * @param redisKey
	 * @param isFilterRepeat
	 *            　是否过滤重复数据
	 * @return
	 */
	public static List<String> queryDataFromList(String redisKey,
			boolean isFilterRepeat) {
		List<String> list =lrange(redisKey);
																// 表示到末尾。因为前面插入操作时，限定了存在的记录数
		if (list == null || list.size() == 0) {
			list = new ArrayList<String>();
		}
		Set<String> ids = new HashSet<String>();
		List<String> resultList = new ArrayList<String>();
		for (String s : list) {
			if (isFilterRepeat) {
				if (!ids.contains(s)) {
					resultList.add(s);
					ids.add(s);
				}
			} else {
				resultList.add(s);
			}
		}
		return resultList;
	}
	
	/**
	 * 分布式锁
	 * 获取锁状态
	 * 轮循（每0.1秒）判断锁状态
	 * 返回false说明获取锁状态时发送异常
	 * @return
	 */
	public static boolean hasLocked(String key){
		Jedis jedis = null;
		boolean success = true;
		boolean isPass = true;
		try {
			jedis = getJedis();
			Assert.notNull(jedis, "获取不到jedis实例");
			while(jedis.setnx( KEY_PREFIX +key, key) == 0){
				Thread.sleep(100);//0.1秒减少性能消耗
			}
			//设置5分钟过期时间，防止死锁（这里有个弊端如果一个方法执行了5分钟还没好还是会释放锁）
			jedis.expire( KEY_PREFIX +key, TIME_FIVE_MINUTES);
		} catch (Exception e) {
			logger.error("jedis set error", e);
			success = false;
			isPass = false;
		} finally {
			returnResource(jedis, success);
		}
		if(success && !isPass){
			unLock(key);
		}
		return isPass;
	}
	
	/**
	 * 解锁
	 * 实际调用删除缓存方法
	 */
	public static void unLock(String key){
		del(key);
	}

	/**
	 * 发布消息 </br>
	 * 给某个频道发送消息，订阅该频道的所有客户端将会接收到该消息
	 * @param channel 通道
	 * @param message 消息
	 * @return 返回接收到该消息的客户端数量
	 */
	public static Long publish(String channel, String message){
		Jedis jedis = null;
		try {
			jedis = getJedis();
			Assert.notNull(jedis, "获取不到jedis实例");
			return jedis.publish(channel, message);
		} finally {
			if ( jedis != null ) {
				jedis.close();
			}
		}
	}

	/**
	 * 订阅指定频道，Jedis实现订阅是阻塞式的，但是不需要额外起一个线程</br>
	 * 只需调用该接口，内部会启动一个线程</br>
	 * 若redis服务出现问题或者网络出现波动，RedisUtils会自动重新订阅 </br>
	 * <p>
	 *     {@code
	 *     JedisPubSub jedisPubSub = new JedisPubSubListener(); //JedisPubSubListener 应当实现{@link JedisPubSub}
	 *     new Thread(new Runnable(){
	 *         public void run() {
	 *             RedisUtils.subscribe(jedisPubSub, "channel1", "channel2");
	 *         }
	 *     }).start();
	 *     or
	 *     Executors
	 *     }
	 * </p>
	 * @see #check()
	 * @see #reSubscribe()
	 * @see Jedis#subscribe(JedisPubSub, String...)
	 * @see JedisPubSub#punsubscribe(String...)
	 * @see JedisPubSub#unsubscribe(String...)
	 * @param jedisPubSub 监听发布事件
	 * @param channels 通道
	 */
	public static void subscribe(JedisPubSub jedisPubSub, String... channels) {
		Assert.notNull(channels, "通道不能为空");
		new Thread(() -> {
			Jedis finalJedis = getJedis();
			try {
				finalJedis.subscribe(jedisPubSub, channels);
			} catch (Exception e) {
				pubSubMap.put(channels, jedisPubSub);
				throw new BusinessRuntimeException("订阅失败", e);
			} finally {
				returnResource(finalJedis);
			}
		}).start();

	}

	/**
	 * 根据给定的pattern订阅频道 </br>
	 * 只需调用该接口，内部会启动一个线程</br>
	 * 若redis服务出现问题或者网络出现波动，RedisUtils会自动重新订阅</br>
	 * <p>
	 *     <li>h?llo subscribes hello,hallo,and hxllo...</li>
	 *     <li>h*llo subscribes hllo,heeeeello...</li>
	 *     <li>h[ae]llo subscribes halloand hello but bot hxllo</li>
	 * </p>
	 * @see #check()
	 * @see #reSubscribe()
	 * @see JedisPubSub#punsubscribe(String...)
	 * @see JedisPubSub#unsubscribe(String...)
	 * @param jedisPubSub 监听
	 * @param channelPatterns 模式匹配
	 */
	public static void pSubscribe(final JedisPubSub jedisPubSub, final String ...channelPatterns) {
		final Jedis finalJedis = getJedis();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					finalJedis.psubscribe(jedisPubSub, channelPatterns);
				} catch (Exception e) {
					patternSubMap.put(channelPatterns, jedisPubSub);
					throw new BusinessRuntimeException("订阅失败",e);
				}finally {
					returnResource(finalJedis);
				}
			}
		}).start();
	}

	public static void subscribe(final BinaryJedisPubSub jedisPubSub, final byte[] ...channels) {
		org.springframework.util.Assert.notNull(channels);
		final Jedis finalJedis = getJedis();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					finalJedis.subscribe(jedisPubSub, channels);
				} catch (Exception e) {
					throw new BusinessRuntimeException("订阅失败",e);
				}finally {
					returnResource(finalJedis);
				}
			}
		}).start();

	}

	public static Long publish(byte[] channel, byte[] message){
		Jedis jedis = null;
		try {
			jedis = getJedis();
			Assert.notNull(jedis, "获取不到jedis实例");
			return jedis.publish(channel, message);
		} finally {
			returnResource(jedis);
		}
	}
	
	public static String getKey(Class<?> clazz, String cacheName, String value) {
		return "Entity." + StringUtils.lowerCase(clazz.getSimpleName()) + ".BY." + cacheName + "@" + value; 
		
	}
	
	public static String getKey(Class<?> clazz, String value) {
		return getKey(clazz, "id", value);
	}
}
