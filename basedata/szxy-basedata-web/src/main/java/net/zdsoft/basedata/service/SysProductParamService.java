package net.zdsoft.basedata.service;

import net.zdsoft.basedata.entity.SysProductParam;

/**
 * @author shenke
 * @since 2017.06.27
 */
public interface SysProductParamService extends BaseService<SysProductParam, String> {

    void updateParamValue(String value, String paramCode);
}
