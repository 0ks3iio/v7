package net.zdsoft.cache.admin.redisdao;

import net.zdsoft.cache.admin.core.CValue;
import net.zdsoft.cache.admin.RedisApplication;
import net.zdsoft.framework.utils.EntityUtils;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @author shenke
 * @since 2017.07.12
 */
@Service
public class RedisDao {

    private static final String DEL_SIGN = "_del_sign_";

    private RedisTemplate<String, Object> tookDBIndex(String serverName, int dbIndex) {
        RedisTemplate<String, Object> redisTemplate = RedisApplication.redisTemplateCache.get(serverName);
        JedisConnectionFactory connectionFactory = (JedisConnectionFactory) redisTemplate.getConnectionFactory();
        connectionFactory.setDatabase(dbIndex);
        return redisTemplate;
    }

    //GET
    public Object getString(String serverName, int dbIndex, String key){
        Object object = tookDBIndex(serverName,dbIndex).opsForValue().get(key);
        return CValue.createString(object);
    }

    public Object getList(String serverName, int dbIndex, String key) {
        RedisTemplate<String, Object> redisTemplate = tookDBIndex(serverName,dbIndex);
        long size = redisTemplate.opsForList().size(key);
        return CValue.createList(redisTemplate.opsForList().range(key,0, size));
    }

    public Object getHash(String serverName, int dbIndex, String key) {
        RedisTemplate<String,Object> redisTemplate = tookDBIndex(serverName,dbIndex);
        Map<Object,Object> maps = redisTemplate.opsForHash().entries(key);
        return CValue.createHash(maps);
    }

    public Object getSet(String serverName, int dbIndex, String key) {

        return CValue.createSet(tookDBIndex(serverName, dbIndex).opsForSet().members(key));
    }

    public Object getZSet(String serverName, int dbIndex, String key) {
        RedisTemplate<String, Object> redisTemplate = tookDBIndex(serverName, dbIndex);
        long size = redisTemplate.opsForZSet().size(key);
        return CValue.createZSet(redisTemplate.opsForZSet().reverseRangeWithScores(key,0,size));
    }

    //DEL
    public void delKV(String serverName, int dbIndex, String ... keys) {
        tookDBIndex(serverName,dbIndex).delete(Arrays.asList(keys));
    }

    public void delHashField(String serverName, int dbIndex, String cKey, String[] fields) {
        tookDBIndex(serverName, dbIndex).opsForHash().delete(cKey,fields);
    }

    /**
     * same as RPOP
     * @param serverName
     * @param dbIndex
     * @param cKey
     */
    public void delListRightValue(String serverName, int dbIndex, String cKey) {
        tookDBIndex(serverName, dbIndex).opsForList().rightPop(cKey);
    }

    /**
     * same as LPOP
     * @param serverName
     * @param dbIndex
     * @param cKey
     */
    public void delListLeftValue(String serverName, int dbIndex, String cKey) {
        tookDBIndex(serverName, dbIndex).opsForList().leftPop(cKey);
    }

    /**
     *
     * @param serverName
     * @param dbIndex
     * @param cKey
     * @param index
     */
    public void delListByIndex(String serverName, int dbIndex, String cKey, long index) {

        RedisTemplate<String,Object> redisTemplate = tookDBIndex(serverName, dbIndex);
        redisTemplate.opsForList().set(cKey,index,DEL_SIGN);
        redisTemplate.opsForList().remove(cKey,0,DEL_SIGN);
    }

    public void delSetValue(String serverName, int dbIndex, String key, Object[] value) {
        tookDBIndex(serverName, dbIndex).opsForSet().remove(key, value);
    }

    public void delZSetValue(String serverName, int dbIndex, String key, Object value) {
        tookDBIndex(serverName, dbIndex).opsForZSet().remove(key, value);
    }

    //ADD

    public void addString(String serverName, int dbIndex, String cKey, Object value) {
        tookDBIndex(serverName, dbIndex).opsForValue().set(cKey, value);
    }

    public void addList(String serverName, int dbIndex, String key, Object[] values) {
        tookDBIndex(serverName, dbIndex).opsForList().leftPushAll(key,values);
    }

    public void addHashField(String serverName, int dbIndex, String key, List<CValue> cValueList) {
        Map<Object,Object> maps = EntityUtils.getMap(cValueList,"field", "value");
        tookDBIndex(serverName, dbIndex).opsForHash().putAll(key,maps);
    }

    public void addSet(String serverName, int dbIndex, String key, Object[] values) {
        tookDBIndex(serverName, dbIndex).opsForSet().add(key, values);
    }

    public void addZSet(String serverName, int dbIndex, String key, final List<CValue> cValues) {
        tookDBIndex(serverName, dbIndex).opsForZSet().add(key, new HashSet<ZSetOperations.TypedTuple<Object>>(){
            {
                for (final CValue cValue : cValues) {
                    add(new ZSetOperations.TypedTuple<Object>() {
                        private Object v;
                        private double score;
                        {
                            v = cValue.getValue();
                            score = cValue.getScore();
                        }
                        @Override
                        public Object getValue() {
                            return v;
                        }

                        @Override
                        public Double getScore() {
                            return score;
                        }

                        @Override
                        public int compareTo(ZSetOperations.TypedTuple o) {
                            if ( o == null ) {
                                return 1;
                            }
                            return this.getScore().compareTo(o.getScore());
                        }
                    });
                }
            }
        });
    }
    //UPDATE

    public void updateString(String serverName, int dbIndex, String key, Object value) {
        tookDBIndex(serverName, dbIndex).opsForValue().set(key, value);
    }

    public void updateList(String serverName, int dbIndex, String key,long index, Object value){
        tookDBIndex(serverName, dbIndex).opsForList().set(key,index,value);
    }

    public void updateHash(String serverName, int dbIndex, String key,final List<CValue> cValues){
        tookDBIndex(serverName, dbIndex).opsForHash().putAll(key,new HashMap<Object, Object>(){{
            for (CValue cValue : cValues) {
                put(cValue.getField(),cValue.getValue());
            }
        }});
    }

    public void updateZSet(String serverName, int dbIndex, String key , Object value, double score){
        tookDBIndex(serverName, dbIndex).opsForZSet().add(key,value,score);
    }

    public void updateSet(String serverName, int dbIndex, String key, Object[] value, Object[] oldValue) {
        RedisTemplate<String,Object> redisTemplate = tookDBIndex(serverName, dbIndex);
        redisTemplate.opsForSet().add(key,value);
        redisTemplate.opsForSet().remove(key, oldValue);
    }
}
