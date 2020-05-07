package net.zdsoft.basedata.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.EduInfoDao;
import net.zdsoft.basedata.entity.EduInfo;
import net.zdsoft.basedata.service.EduInfoService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

@Service
public class EduInfoServiceImpl extends BaseServiceImpl<EduInfo, String> implements EduInfoService {

    @Autowired
    private EduInfoDao eduInfoDao;

    @Override
    protected BaseJpaRepositoryDao<EduInfo, String> getJpaDao() {
        return eduInfoDao;
    }

    @Override
    protected Class<EduInfo> getEntityClass() {
        return EduInfo.class;
    }

    @Override
    public void deleteEduInfosByUnitId(String unitId) {
        eduInfoDao.deleteEduInfosByUnitId(unitId);
    }
}
