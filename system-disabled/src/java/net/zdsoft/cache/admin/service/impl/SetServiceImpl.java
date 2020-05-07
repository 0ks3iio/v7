package net.zdsoft.cache.admin.service.impl;

import net.zdsoft.cache.admin.RedisApplication;
import net.zdsoft.cache.admin.redisdao.RedisDao;
import net.zdsoft.cache.admin.service.SetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shenke
 * @since 2017.07.17
 */
@Service
public class SetServiceImpl extends RedisApplication implements SetService {

    @Autowired private RedisDao redisDao;

    @Override
    public void update(String key, Object[] value, Object[] oldValue) {
        redisDao.updateSet(getRedis(), getDBIndex(), key, value, oldValue);
    }

    @Override
    public void delete(String key, Object[] value) {
        redisDao.delSetValue(getRedis(), getDBIndex(), key, value);
    }
}
