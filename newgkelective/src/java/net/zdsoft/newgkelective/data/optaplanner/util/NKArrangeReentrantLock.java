package net.zdsoft.newgkelective.data.optaplanner.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.newgkelective.data.optaplanner.constants.NKSelectionConstants;
import redis.clients.jedis.Jedis;

/**
 * 默认redis锁
 * redis没开启，则用java锁
 * @author shensiping
 *
 */
public class NKArrangeReentrantLock {
    private static final String ARRANGE_REENTRANT_LOCK = "ArrangeReentrantLock_";
    private Lock javaLock = new ReentrantLock();
    private NKRedisLock redisLock;
    private static boolean lockByRedis;
    private static Map<String, Object> keyLockMap = new ConcurrentHashMap<String, Object>();
    
    private NKArrangeReentrantLock() {
        init();
    }
    
    public void init() {
        Jedis jedis = RedisUtils.getJedis();
        if (jedis != null) {
            redisLock = new NKRedisLock();
            lockByRedis = true;
        }
        RedisUtils.returnResource(jedis);
    }
    
    public boolean tryLock(String lockKey) {
        if (lockByRedis) {
            return redisLock.tryLock(ARRANGE_REENTRANT_LOCK+lockKey, (int)NKSelectionConstants.UNIT_MINUTE.toSeconds(NKSelectionConstants.SPENT_LIMIT_MIN));
        } else {
            synchronized (keyLockMap) {
                if (keyLockMap.containsKey(lockKey)) {
                    return false;
                }
                boolean locked = javaLock.tryLock();
                if (locked) {
                    keyLockMap.put(lockKey, new Object());
                }
                return locked;
            }
        }
    }
    
    public boolean resetTTL(String key) {
    	return redisLock.resetTTL(ARRANGE_REENTRANT_LOCK+key, 
    			(int)NKSelectionConstants.UNIT_MINUTE.toSeconds(NKSelectionConstants.REDIS_LIMIT_MINUTE));
    }
    
    public boolean tryLockOneDay(String lockKey) {
    	if (lockByRedis) {
    		return redisLock.tryLock(ARRANGE_REENTRANT_LOCK+lockKey, (int)NKSelectionConstants.UNIT_HOUR.toSeconds(24));
    	} else {
    		synchronized (keyLockMap) {
    			if (keyLockMap.containsKey(lockKey)) {
    				return false;
    			}
    			boolean locked = javaLock.tryLock();
    			if (locked) {
    				keyLockMap.put(lockKey, new Object());
    			}
    			return locked;
    		}
    	}
    }
    
    public void unLock(String lockKey) {
        if (lockByRedis) {
            redisLock.unlock(ARRANGE_REENTRANT_LOCK+lockKey);
        } else {
            synchronized (keyLockMap) {
                // 非Reentrant
                keyLockMap.remove(lockKey);
                javaLock.unlock();
            }
        }
    }
    
    public void removeKey(String lockKey) {
        if (lockByRedis) {
            RedisUtils.del(ARRANGE_REENTRANT_LOCK+lockKey);
          
        } else {
            synchronized (keyLockMap) {
                keyLockMap.remove(lockKey);
            }
        }
    }
    
    
    public boolean isKeyLocked(String lockKey) {
			if (lockByRedis) {
				String value = RedisUtils.get(ARRANGE_REENTRANT_LOCK+lockKey);
				if (value != null) {
					return true;
				}
			} else {
				synchronized (keyLockMap) {
					return keyLockMap.containsKey(lockKey);
				}
			}
			return false;
    }
    
    
    private static class ArrangeReentrantLockHolder {
        private static final NKArrangeReentrantLock lock = new NKArrangeReentrantLock();
    }
    
    public static NKArrangeReentrantLock getLock() {
        return ArrangeReentrantLockHolder.lock;
    }

    /**
     * 获取lockByRedis
     * @return lockByRedis
     */
    public boolean isLockByRedis() {
        return lockByRedis;
    }
    
}
