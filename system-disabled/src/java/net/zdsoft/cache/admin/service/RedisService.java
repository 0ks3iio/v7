package net.zdsoft.cache.admin.service;

import net.zdsoft.cache.admin.RedisSystemException;
import net.zdsoft.cache.admin.core.CKey;

/**
 * @author shenke
 * @since 2017.07.11
 */
public interface RedisService {

    Object getCValueByCKey(CKey cKey, int dbIndex);

    void delKV(String ... cKey);

    boolean addServer(String host, int port , String password) throws RedisSystemException;
}
