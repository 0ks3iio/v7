package net.zdsoft.cache.admin.service.impl;

import com.google.common.collect.Lists;
import net.zdsoft.cache.admin.CacheAdminConstant;
import net.zdsoft.cache.admin.InitCacheAdmin;
import net.zdsoft.cache.admin.Pair;
import net.zdsoft.cache.admin.RedisSystemException;
import net.zdsoft.cache.admin.core.CKey;
import net.zdsoft.cache.admin.RedisApplication;
import net.zdsoft.cache.admin.redisdao.RedisDao;
import net.zdsoft.cache.admin.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author shenke
 * @since 2017.07.12
 */
@Service
public class RedisServiceImpl extends RedisApplication implements RedisService {

    @Autowired private RedisDao redisDao;
    @Autowired private InitCacheAdmin initCacheAdmin;

    @Override
    public Object getCValueByCKey(CKey cKey,  int dbIndex) {
        Object values = null;
        String serverName = getRedis();
        switch (cKey.getDataType()) {
            case STRING:
                values = redisDao.getString(serverName,dbIndex, cKey.getKey());
                break;
            case HASH:
                values = redisDao.getHash(serverName,dbIndex, cKey.getKey());
                break;
            case LIST:
                values = redisDao.getList(serverName,dbIndex, cKey.getKey());
                break;
            case SET:
                values = redisDao.getSet(serverName,dbIndex, cKey.getKey());
                break;
            case ZSET:
                values = redisDao.getZSet(serverName,dbIndex, cKey.getKey());
                break;
            case NONE:
                break;
        }
        return values;
    }

    @Override
    public void delKV(String ... key) {
        redisDao.delKV(getRedis(),getDBIndex(),key);
        CopyOnWriteArrayList<CKey> cKeyList = keyCache.get(getRedis() + DEFAULT_SEPARATOR + getDBIndex());
        CopyOnWriteArrayList<CKey> cKeys = Lists.newCopyOnWriteArrayList(cKeyList);
        for (CKey cKey : cKeyList) {
            for (String s : key) {
                if ( cKey.getKey().equals(s) ) {
                    cKeyList.remove(cKey);
                }
            }
        }

    }

    @Override
    public boolean addServer(String host, int port, String password) throws RedisSystemException {
        // init redis Server
        try {
            RedisTemplate redisTemplate = initCacheAdmin.initRedisTemplate(host, port, password);
            Pair<Map<String,CopyOnWriteArrayList<CKey>>,String> pair = initCacheAdmin.initCacheKey(redisTemplate);
            final String key = host + DEFAULT_SEPARATOR + port;
            redisTemplateCache.put(key, redisTemplate);
            keyCache.putAll(pair.getT());
            Map<String, String> serverMap = new HashMap<String,String>(3);
            serverMap.put(CacheAdminConstant.PROPERTIES_REDIS_HOST,host);
            serverMap.put(CacheAdminConstant.PROPERTIES_REDIS_PORT, String.valueOf(port));
            serverMap.put(CacheAdminConstant.SERVER_CACHE_KEY_DB_NUMBER,pair.getO());
            serverMap.put(CacheAdminConstant.PROPERTIES_REDIS_PASSWORD,password);
            redisServerCache.put(key , serverMap);
        } catch (Exception e){
            throw new RedisSystemException("添加失败" + e.getMessage());
        }
        return Boolean.TRUE;
    }
}
