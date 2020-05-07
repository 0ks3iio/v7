package net.zdsoft.cache.admin;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.zdsoft.cache.admin.core.CKey;
import net.zdsoft.cache.admin.core.CacheTreeNode;
import net.zdsoft.framework.entity.Pagination;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author shenke
 * @since 2017.07.11
 */
public abstract class RedisApplication {

    protected static final String DEFAULT_SEPARATOR            = "_";

    protected static final String REQUEST_PARAMETER_REDIS_HOST = "redis_host";
    protected static final String REQUEST_PARAMETER_REDIS_PORT = "redis_port";
    protected static final String REQUEST_PARAMETER_REDIS_DB_INDEX = "dbIndex";
    protected static final String REQUEST_PARAMETER_REDIS_DATA_TYPE = "dataType";
    protected static final String REQUEST_PARAMETER_REDIS_KEY = "key";

    /**
     * key ex: 192.168.0.155_6379_0  host_port_dbIndex
     */
    public static final Map<String,CopyOnWriteArrayList<CKey>> keyCache = Maps.newHashMap();
    public static final Map<String,RedisTemplate> redisTemplateCache = Maps.newHashMap();
    public static final Map<String,Map<String,String>> redisServerCache = Maps.newHashMap();

    protected static final Set<CacheTreeNode> treeCache = Sets.newTreeSet();

    private static ThreadLocal<Pagination> paginationThreadLocal = new ThreadLocal<Pagination>(){
        @Override
        protected Pagination initialValue() {
            return new Pagination(Pagination.DEFAULT_PAGE_SIZE,Boolean.FALSE);
        }
    };

    private static ThreadLocal<String> redisThreadLocal = new ThreadLocal<String>(){
        @Override
        protected String initialValue() {
            return "NULL";
        }
    };

    private static ThreadLocal<Integer> dbIndexThreadLocal = new ThreadLocal<Integer>(){
        @Override
        protected Integer initialValue() {
            return 1;
        }
    };

    protected static RefreshType MANUALLY = RefreshType.MANUALLY;

    protected static ReentrantLock lock = new ReentrantLock();
    protected static Condition condition = lock.newCondition();

    protected volatile boolean KEY_CACHE_REFRESH_ING = Boolean.FALSE;

    protected static volatile List<String> ERROR_MSG = Lists.newArrayList();

    /**
     * 绑定参数
     * @param redisIpPort ex: 192.168.1.1_6379
     */
    protected void bindRedis(String redisIpPort){
        redisThreadLocal.set(redisIpPort);
    }

    protected void bindDBIndex(Integer dbIndex) {
        dbIndexThreadLocal.set(dbIndex);
    }

    protected void unbindDBIndex() {
        dbIndexThreadLocal.remove();
    }

    protected String getRedis() {
        return redisThreadLocal.get();
    }

    protected Integer getDBIndex(){
        return dbIndexThreadLocal.get();
    }

    protected void unbindRedis() {
        redisThreadLocal.remove();
    }
}
