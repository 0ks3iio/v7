package net.zdsoft.cache.admin.service.impl;

import net.zdsoft.cache.admin.RedisApplication;
import net.zdsoft.cache.admin.redisdao.RedisDao;
import net.zdsoft.cache.admin.service.StringService;
import net.zdsoft.cache.admin.service.ViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shenke
 * @since 2017.07.12
 */
@Service
public class StringServiceImpl extends RedisApplication implements StringService {

    @Autowired private RedisDao redisDao;
    @Autowired private ViewService viewService;

    @Override
    public void delValue(String ... keys) {
        redisDao.delKV(getRedis(),getDBIndex(),keys);
        viewService.getLeftTree(Boolean.TRUE);
        for (String key : keys) {
            keyCache.remove(getRedis() + DEFAULT_SEPARATOR + getDBIndex() + key);
        }
    }

    @Override
    public void updateValue(String key, String value) {
        redisDao.updateString(getRedis(),getDBIndex(),key,value);
    }
}
