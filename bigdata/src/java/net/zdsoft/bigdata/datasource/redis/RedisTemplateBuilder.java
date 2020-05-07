package net.zdsoft.bigdata.datasource.redis;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author shenke
 * @since 2018/11/27 上午9:31
 */
final class RedisTemplateBuilder {

    private static Map<RedisDatabaseAdapter, RedisTemplate<String, String>> cache = new HashMap<>();

    static void removeCache(RedisDatabaseAdapter databaseAdapter) {
        cache.remove(databaseAdapter);
    }

    static RedisTemplate<String, String> getTemplate(RedisDatabaseAdapter databaseKey) {
        synchronized (databaseKey) {
            return cache.computeIfAbsent(databaseKey, k-> {
                //这里已经约定好了，redis里面的Value是字符串，所以我们将字符串取出交给上层代码处理就好了
                RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
                redisTemplate.setConnectionFactory(redisConnectionFactory(k));
                StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
                redisTemplate.setKeySerializer(stringRedisSerializer);
                redisTemplate.setValueSerializer(stringRedisSerializer);
                redisTemplate.setHashKeySerializer(stringRedisSerializer);
                redisTemplate.setHashValueSerializer(stringRedisSerializer);
                redisTemplate.afterPropertiesSet();
                return redisTemplate;
            });
        }
    }

    private static RedisConnectionFactory redisConnectionFactory(RedisDatabaseAdapter databaseKey) {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName(databaseKey.getDomain());
        factory.setPort(databaseKey.getPort());
        factory.setDatabase(Optional.ofNullable(databaseKey.getDbNumber()).orElse(0));
        if (StringUtils.isNotBlank(databaseKey.getPassword())) {
            factory.setPassword(databaseKey.getPassword());
        }
        factory.setUsePool(true);
        factory.afterPropertiesSet();
        return factory;
    }
}
