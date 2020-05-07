package net.zdsoft.basedata.service.impl;

import net.zdsoft.basedata.dao.SysProductParamDao;
import net.zdsoft.basedata.entity.SysProductParam;
import net.zdsoft.basedata.service.SysProductParamService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shenke
 * @since 2017.06.27
 */
@Service("sysProductParamService")
public class SysProductParamServiceImpl extends BaseServiceImpl<SysProductParam,String> implements SysProductParamService {

    @Autowired private SysProductParamDao sysProductParamDao;

    @Override
    protected BaseJpaRepositoryDao<SysProductParam, String> getJpaDao() {
        return this.sysProductParamDao;
    }

    @Override
    protected Class<SysProductParam> getEntityClass() {
        return getTClass();
    }

    @Override
    public void updateParamValue(String value, String paramCode) {
        sysProductParamDao.updateParamValueByCode(value, paramCode);
    }
}
