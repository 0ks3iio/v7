package net.zdsoft.cache.admin;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.zdsoft.cache.admin.core.CKey;
import net.zdsoft.framework.config.Evn;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisSentinelConnection;
import org.springframework.data.redis.connection.RedisServer;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author shenke
 * @since 2017.07.11
 */
@Component
public class InitCacheAdmin {

    private static Logger logger = LoggerFactory.getLogger(InitCacheAdmin.class);

    @PostConstruct
    public void initRedisKeys() {
        String hostName = Evn.getString(CacheAdminConstant.PROPERTIES_REDIS_HOST);
        String port     = Evn.getString(CacheAdminConstant.PROPERTIES_REDIS_PORT);
        String password = Evn.getString(CacheAdminConstant.PROPERTIES_REDIS_PASSWORD);

        String sentinelEnable = Evn.getString(CacheAdminConstant.PROPERTIES_REDIS_SENTINEL);
        String masterName = Evn.getString(CacheAdminConstant.PROPERTIES_REDIS_MASTER_NAME);
        String sentinelUrl = Evn.getString(CacheAdminConstant.PROPERTIES_REDIS_SENTINEL_URL);

        RedisApplication.redisTemplateCache.clear();
        RedisApplication.redisServerCache.clear();
        RedisApplication.treeCache.clear();
        RedisApplication.keyCache.clear();
        RedisApplication.ERROR_MSG.clear();

        JedisConnectionFactory jedisConnectionFactory = null;
        //是否配置哨兵
        if ( BooleanUtils.toBoolean(sentinelEnable) ) {
            RedisSentinelConfiguration sentinelConfiguration = new RedisSentinelConfiguration(masterName, Sets.newHashSet(sentinelUrl));
            jedisConnectionFactory = new JedisConnectionFactory(sentinelConfiguration);
        }
        RedisTemplate redisTemplate = initRedisTemplate(hostName,Integer.valueOf(port), password, jedisConnectionFactory);
        RedisApplication.redisTemplateCache.put(hostName + RedisApplication.DEFAULT_SEPARATOR + port, redisTemplate);
        jedisConnectionFactory = (JedisConnectionFactory) redisTemplate.getConnectionFactory();

        //master-slave
        if ( jedisConnectionFactory.isRedisSentinelAware() ){
            //
            RedisSentinelConnection sentinelConnection = redisTemplate.getConnectionFactory().getSentinelConnection();
            Collection<RedisServer> masters = sentinelConnection.masters();
            List<RedisServer> slaves = Lists.newArrayList();
            for (RedisServer master : masters) {

                if ( masterName.equals(master.getName()) ) {
                    slaves = Lists.newArrayList(sentinelConnection.slaves(master));
                }
            }
            //初始化其他从节点缓存
            for (RedisServer slave : slaves) {
                String host = slave.getHost();
                int slavePort = slave.getPort();

                RedisApplication.redisTemplateCache.put(host + RedisApplication.DEFAULT_SEPARATOR + slavePort, initRedisTemplate(host, slavePort,null));
                Map<String,String> slaveServer = Maps.newHashMap();
                slaveServer.put(CacheAdminConstant.PROPERTIES_REDIS_HOST,host);
                slaveServer.put(CacheAdminConstant.PROPERTIES_REDIS_PORT,String.valueOf(slavePort));
                //slaveServer.put(Constant.PROPERTIES_REDIS_PASSWORD, slave.getP)
                RedisApplication.redisServerCache.put(host + RedisApplication.DEFAULT_SEPARATOR + slavePort,slaveServer);
            }
        }
        //init redis server cache
        Map<String, String> masterServer = Maps.newHashMap();
        masterServer.put(CacheAdminConstant.PROPERTIES_REDIS_HOST, hostName);
        masterServer.put(CacheAdminConstant.PROPERTIES_REDIS_PORT, port);
        RedisApplication.redisServerCache.put(hostName + RedisApplication.DEFAULT_SEPARATOR + port,masterServer);
        //init key cache
        initKeyCacheFromServerCache();

        //autoRefresh();
    }


    public void initKeyCacheFromServerCache(){
        RedisApplication.keyCache.clear();
        List<String> errorServer = Lists.newArrayList();
        for (Map.Entry<String, RedisTemplate> entry : RedisApplication.redisTemplateCache.entrySet()) {
            try {
                Pair<Map<String,CopyOnWriteArrayList<CKey>>,String> pair = initCacheKey(entry.getValue());
                RedisApplication.keyCache.putAll(pair.getT());
                RedisApplication.redisServerCache.get(entry.getKey()).put(CacheAdminConstant.SERVER_CACHE_KEY_DB_NUMBER,pair.getO());
            } catch (Exception e){
                errorServer.add(entry.getKey());
                logger.error("初始化redis: {} 失败 message {}",entry.getKey(),e);
            } finally {
                for (String s : errorServer) {
                    RedisApplication.redisTemplateCache.remove(s);
                }
            }

        }
    }

    public Pair<Map<String,CopyOnWriteArrayList<CKey>>,String> initCacheKey(RedisTemplate redisTemplate) throws RedisSystemException {
        Pair<Map<String,CopyOnWriteArrayList<CKey>>,String> pair = null;
        RedisConnection redisConnection = RedisConnectionUtils.getConnection(redisTemplate.getConnectionFactory());
        JedisConnectionFactory jedisConnectionFactory = (JedisConnectionFactory) redisTemplate.getConnectionFactory();
        try {
            Map<String,CopyOnWriteArrayList<CKey>> cacheKeys = Maps.newHashMap();
            int dbSize = jedisConnectionFactory.getShardInfo().getDb();
            dbSize = dbSize == 0 ? 15:dbSize;
            RedisSerializer keySerializer = redisTemplate.getKeySerializer();
            int realDB = -1;
            for ( int db=0; db<=dbSize; db++ ) {
                redisConnection.select(db);
                Set<byte[]> byteKeys = redisConnection.keys("*".getBytes());
                if (byteKeys.isEmpty()) {
                    continue;
                }
                realDB++;
                List<CKey> cKeys = Lists.newCopyOnWriteArrayList();
                for (byte[] byteKey : byteKeys) {
                    CKey cKey = new CKey();
                    cKey.setDataType(redisConnection.type(byteKey));
                    cKey.setKey(keySerializer.deserialize(byteKey).toString());
                    cKeys.add(cKey);
                }
                cacheKeys.put(jedisConnectionFactory.getHostName() + RedisApplication.DEFAULT_SEPARATOR + jedisConnectionFactory.getPort() + RedisApplication.DEFAULT_SEPARATOR + db,
                        (CopyOnWriteArrayList<CKey>) cKeys);
            }
            pair = new Pair<Map<String,CopyOnWriteArrayList<CKey>>,String>(cacheKeys,String.valueOf(realDB));
            redisConnection.close();
        } catch (Exception e){
            throw new RedisSystemException(e.getMessage());
        } finally {
            RedisConnectionUtils.releaseConnection(redisConnection, jedisConnectionFactory);
        }

        return pair;
    }

    public RedisTemplate initRedisTemplate(String hostName, int port, String password, JedisConnectionFactory jedisConnectionFactory){
        jedisConnectionFactory = jedisConnectionFactory == null ? new JedisConnectionFactory() : jedisConnectionFactory;
        jedisConnectionFactory.setHostName(hostName);
        jedisConnectionFactory.setPort(port);
        if (StringUtils.isNotBlank(password) ) {
            jedisConnectionFactory.setPassword(password);
        }
        jedisConnectionFactory.afterPropertiesSet();
        RedisTemplate redisTemplate = new CacheAdminRedisTemplate();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        //redisTemplate.setEnableTransactionSupport(Boolean.TRUE);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    public RedisTemplate initRedisTemplate(String hostName, int port, String password) {
        return initRedisTemplate(hostName,port,password,null);
    }
}
