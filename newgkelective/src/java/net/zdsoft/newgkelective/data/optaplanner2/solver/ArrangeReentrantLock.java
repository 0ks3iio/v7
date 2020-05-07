package net.zdsoft.newgkelective.data.optaplanner2.solver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.newgkelective.data.optaplanner2.constants.SelectionConstants;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

/**
 * 默认redis锁
 * redis没开启，则用java锁
 * @author shensiping
 *
 */
public class ArrangeReentrantLock {
    private static final String ARRANGE_REENTRANT_LOCK = "ArrangeReentrantLock_";
    private Lock javaLock = new ReentrantLock();
    private RedisLock redisLock;
    private static boolean lockByRedis;
    private static Map<String, Object> keyLockMap = new ConcurrentHashMap<String, Object>();
    
    private ArrangeReentrantLock() {
        init();
    }
    
    public void init() {
        Jedis jedis = RedisUtils.getJedis();
        if (jedis != null) {
            redisLock = new RedisLock(jedis);
            lockByRedis = true;
        }
    }
    
    public boolean tryLock(String lockKey) {
        if (lockByRedis) {
            return redisLock.tryLock(ARRANGE_REENTRANT_LOCK+lockKey, (int)SelectionConstants.UNIT_HOUR.toSeconds(SelectionConstants.REDIS_LIMIT_HOUR));
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
    
    public boolean tryLockOneDay(String lockKey) {
    	if (lockByRedis) {
    		return redisLock.tryLock(ARRANGE_REENTRANT_LOCK+lockKey, (int)SelectionConstants.UNIT_HOUR.toSeconds(24));
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
            Jedis jedis = RedisUtils.getJedis();
            boolean success = true;
			try {
	            if (jedis != null) {
	                jedis.del(ARRANGE_REENTRANT_LOCK+lockKey);
	            }
			} catch (JedisException e) {
				e.printStackTrace();
				success = false;
			} finally {
				RedisUtils.returnResource(jedis, success);
			}
        } else {
            synchronized (keyLockMap) {
                keyLockMap.remove(lockKey);
            }
        }
    }
    
    
    public boolean isKeyLocked(String lockKey) {
			if (lockByRedis) {
				Jedis jedis = RedisUtils.getJedis();
				boolean success = true;
				try {
					if (jedis != null) {
						String value = jedis.get(ARRANGE_REENTRANT_LOCK+lockKey);
						if (value != null) {
							return true;
						}
					}
				} catch (JedisException e) {
					e.printStackTrace();
					success = false;
				} finally {
					RedisUtils.returnResource(jedis, success);
				}
			} else {
				synchronized (keyLockMap) {
					return keyLockMap.containsKey(lockKey);
				}
			}
			return false;
    }
    
    
    private static class ArrangeReentrantLockHolder {
        private static final ArrangeReentrantLock lock = new ArrangeReentrantLock();
    }
    
    public static ArrangeReentrantLock getLock() {
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
