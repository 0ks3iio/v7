package net.zdsoft.cache.admin.service;

/**
 * @author shenke
 * @since 2017.07.14
 */
public interface ListService {

    void update(String key, Object value, long index);

    void delete(String key, long index);
}
