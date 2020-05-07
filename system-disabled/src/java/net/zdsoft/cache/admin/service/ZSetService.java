package net.zdsoft.cache.admin.service;

/**
 * @author shenke
 * @since 2017.07.17
 */
public interface ZSetService {

    void update(String key, Object value, Double score);

    void delete(String key, Object value);
}
