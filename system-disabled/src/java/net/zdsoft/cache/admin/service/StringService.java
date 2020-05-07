package net.zdsoft.cache.admin.service;

/**
 * @author shenke
 * @since 2017.07.12
 */
public interface StringService {

    void delValue(String ... keys) ;

    void updateValue(String key, String value) ;
}
