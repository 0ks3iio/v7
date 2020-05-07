package net.zdsoft.cache.admin.service.impl;

import net.zdsoft.cache.admin.core.CValue;
import net.zdsoft.cache.admin.RedisApplication;
import net.zdsoft.cache.admin.redisdao.RedisDao;
import net.zdsoft.cache.admin.service.HashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shenke
 * @since 2017.07.13
 */
@Service
public class HashServiceImpl extends RedisApplication implements HashService {

    @Autowired private RedisDao redisDao;

    @Override
    public void update(String key, List<CValue> cValueList) {
        redisDao.updateHash(getRedis(), getDBIndex(), key, cValueList);
    }

    @Override
    public void del(String key, String... field) {
        redisDao.delHashField(getRedis(), getDBIndex(), key, field);
    }
}
