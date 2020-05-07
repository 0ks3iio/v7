package net.zdsoft.basedata.remote.service.impl;

import net.zdsoft.basedata.entity.SysProductParam;
import net.zdsoft.basedata.remote.service.SysProductParamRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.SysProductParamService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shenke
 * @since 2017.06.27
 */
@Service("sysProductParamRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class SysProductParamRemoteServiceImpl extends BaseRemoteServiceImpl<SysProductParam, String> implements SysProductParamRemoteService {

    @Autowired private SysProductParamService sysProductParamService;

    @Override
    protected BaseService<SysProductParam, String> getBaseService() {
        return this.sysProductParamService;
    }

    @Override
    public String findProductParamValue(String paramCode) {
        SysProductParam sysProductParam = getBaseService().findOneBy("paramCode",paramCode);
        return sysProductParam==null ? StringUtils.EMPTY : sysProductParam.getParamValue();
    }

    @Override
    public void updateParamValue(String value, String paramCode) {
        sysProductParamService.updateParamValue(value, paramCode);
    }
}
