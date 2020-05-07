package net.zdsoft.cache.admin.service.impl;

import net.zdsoft.cache.admin.RedisApplication;
import net.zdsoft.cache.admin.redisdao.RedisDao;
import net.zdsoft.cache.admin.service.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shenke
 * @since 2017.07.14
 */
@Service
public class ListServiceImpl extends RedisApplication implements ListService {

    @Autowired private RedisDao redisDao;

    @Override
    public void update(String key, Object value, long index) {
        redisDao.updateList(getRedis(), getDBIndex(), key, index, value);
    }

    @Override
    public void delete(String key, long index) {
        redisDao.delListByIndex(getRedis(), getDBIndex(), key, index);
    }
}
