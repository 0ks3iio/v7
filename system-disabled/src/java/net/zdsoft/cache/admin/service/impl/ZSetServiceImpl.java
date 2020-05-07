package net.zdsoft.cache.admin.service.impl;

import net.zdsoft.cache.admin.RedisApplication;
import net.zdsoft.cache.admin.redisdao.RedisDao;
import net.zdsoft.cache.admin.service.ZSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shenke
 * @since 2017.07.17
 */
@Service
public class ZSetServiceImpl extends RedisApplication implements ZSetService {

    @Autowired private RedisDao redisDao;

    @Override
    public void update(String key, Object value, Double score) {
        redisDao.updateZSet(getRedis(), getDBIndex(), key, value, score);
    }

    @Override
    public void delete(String key, Object value) {
        redisDao.delZSetValue(getRedis(), getDBIndex(), key, value);
    }
}
