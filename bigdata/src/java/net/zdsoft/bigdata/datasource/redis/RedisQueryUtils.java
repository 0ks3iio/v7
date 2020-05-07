package net.zdsoft.bigdata.datasource.redis;

import org.springframework.data.redis.core.script.DefaultRedisScript;

/**
 * @author shenke
 * @since 2018/11/27 上午9:44
 */
final class RedisQueryUtils {

    static String doGetUseStandardKey(String key, RedisDatabaseAdapter databaseKey) {
        return RedisTemplateBuilder.getTemplate(databaseKey).opsForValue().get(key);
    }

    static String doGetUseLuaScript(String script, RedisDatabaseAdapter databaseKey) {
        return doGetUseLuaScriptWithArgs(script, null, databaseKey);
    }

    static String doGetUseLuaScriptWithArgs(String script, Object[] args, RedisDatabaseAdapter databaseKey) {
        return RedisTemplateBuilder.getTemplate(databaseKey).execute(new DefaultRedisScript<>(script, String.class), null, args);
    }
}
