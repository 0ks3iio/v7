package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.SysProductParam;

/**
 * @author shenke
 * @since 2017.06.27
 */
public interface SysProductParamRemoteService extends BaseRemoteService<SysProductParam, String> {

    String findProductParamValue(String paramCode);

    void updateParamValue(String value, String paramCode);
}
