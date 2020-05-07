package net.zdsoft.basedata.remote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.SchoolBuildingDao;
import net.zdsoft.basedata.entity.SchoolBuilding;
import net.zdsoft.basedata.remote.service.SchoolBuildingRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.SchoolBuildingService;

/**
 * @author yangsj 2017-1-24下午5:30:26
 */
@Service("schoolBuildingRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class SchoolBuildingRemoteServiceImpl extends BaseRemoteServiceImpl<SchoolBuilding,String> implements
        SchoolBuildingRemoteService {

    @Autowired
    private SchoolBuildingService schoolBuildingService;
    @Autowired
    private SchoolBuildingDao schoolBuildingDao;

    @Override
    protected BaseService<SchoolBuilding, String> getBaseService() {
        // TODO Auto-generated method stub
        return schoolBuildingService;
    }

    @Override
    public void deleteByInIds(String[] ids) {
        // TODO Auto-generated method stub
        schoolBuildingDao.deleteByInIds(ids);

    }

}
