package net.zdsoft.basedata.remote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import net.zdsoft.basedata.entity.SchoolBuildingArea;
import net.zdsoft.basedata.remote.service.SchoolBuildingAreaRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.SchoolBuildingAreaService;
import org.springframework.stereotype.Service;

/**
 * @author yangsj 2017-1-24下午5:31:37

 */
@com.alibaba.dubbo.config.annotation.Service
@Service("schoolBuildingAreaRemoteService")
public class SchoolBuildingAreaRemoteServiceImpl extends BaseRemoteServiceImpl<SchoolBuildingArea,String> implements
        SchoolBuildingAreaRemoteService {

    @Autowired
    private SchoolBuildingAreaService schoolBuildingAreaService;

    @Override
    protected BaseService<SchoolBuildingArea, String> getBaseService() {
        // TODO Auto-generated method stub
        return schoolBuildingAreaService;
    }

    @Override
    public void deleteByInIds(String[] ids) {
        schoolBuildingAreaService.deleteAllByIds(ids);
    }

}
