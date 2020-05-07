package net.zdsoft.cache.admin.service;

import net.zdsoft.cache.admin.core.CValue;

import java.util.List;

/**
 * @author shenke
 * @since 2017.07.13
 */
public interface HashService {

    void update(String key, List<CValue> cValueList);

    void del(String key, String... field);
}
