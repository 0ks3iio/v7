package net.zdsoft.cache.admin;

import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.Charset;

/**
 * @author shenke
 * @since 2017.07.11
 */
public class CacheAdminRedisTemplate extends RedisTemplate<String, String> {

    public CacheAdminRedisTemplate() {
        RedisSerializer<String> stringRedisSerializer = new StringRedisSerializer(Charset.forName("UTF-8"));
        setKeySerializer(stringRedisSerializer);
        setValueSerializer(stringRedisSerializer);
        setValueSerializer(stringRedisSerializer);
        setHashKeySerializer(stringRedisSerializer);
        setHashValueSerializer(stringRedisSerializer);
    }

    @Override
    protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
        return new DefaultStringRedisConnection(connection);
    }

    public static class FastJsonRedisSerializer implements RedisSerializer<Object> {

        @Override
        public byte[] serialize(Object o) throws SerializationException {
            return JSON.toJSONString(o).getBytes();
        }

        @Override
        public Object deserialize(byte[] bytes) throws SerializationException {
            return JSON.parseObject(new String(bytes, Charset.forName("UTF-8")));
        }
    }
}
