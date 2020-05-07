package net.zdsoft.basedata.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.SchoolBuildingDao;
import net.zdsoft.basedata.entity.SchoolBuilding;
import net.zdsoft.basedata.service.SchoolBuildingService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

/**
 * @author yangsj 2017-1-24下午5:20:52
 */
@Service
public class SchoolBuildingServiceImpl extends BaseServiceImpl<SchoolBuilding, String> implements SchoolBuildingService {
    @Autowired
    private SchoolBuildingDao schoolBuildingDao;

    @Override
    protected BaseJpaRepositoryDao<SchoolBuilding, String> getJpaDao() {
        // TODO Auto-generated method stub
        return schoolBuildingDao;
    }

    @Override
    protected Class<SchoolBuilding> getEntityClass() {
        // TODO Auto-generated method stub
        return SchoolBuilding.class;
    }

}
