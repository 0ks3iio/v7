package net.zdsoft.basedata.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.SchoolBuildingAreaDao;
import net.zdsoft.basedata.entity.SchoolBuildingArea;
import net.zdsoft.basedata.service.SchoolBuildingAreaService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

/**
 * @author yangsj 2017-1-24下午5:23:07
 */
@Service
public class SchoolBuildingAreaServiceImpl extends BaseServiceImpl<SchoolBuildingArea, String> implements
        SchoolBuildingAreaService {
    @Autowired
    private SchoolBuildingAreaDao schoolBuildingAreaDao;

    @Override
    protected BaseJpaRepositoryDao<SchoolBuildingArea, String> getJpaDao() {
        // TODO Auto-generated method stub
        return schoolBuildingAreaDao;
    }

    @Override
    protected Class<SchoolBuildingArea> getEntityClass() {
        // TODO Auto-generated method stub
        return SchoolBuildingArea.class;
    }

    @Override
    public void deleteAllByIds(String... id) {
        if (id != null && id.length > 0)
            schoolBuildingAreaDao.deleteAllByIds(id);
    }

}
