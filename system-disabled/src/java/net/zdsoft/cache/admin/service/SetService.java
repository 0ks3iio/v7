package net.zdsoft.cache.admin.service;

/**
 * @author shenke
 * @since 2017.07.14
 */
public interface SetService {

    void update(String key, Object[] value, Object[] oldValue);

    void delete(String key, Object[] value);
}
