package net.zdsoft.basedata.remote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.SchtypeSectionDao;
import net.zdsoft.basedata.entity.SchtypeSection;
import net.zdsoft.basedata.remote.service.SchtypeSectionRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.SchtypeSectionService;
import net.zdsoft.framework.utils.SUtils;

@Service("schtypeSectionRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class SchtypeSectionRemoteServiceImpl extends BaseRemoteServiceImpl<SchtypeSection,String> implements
        SchtypeSectionRemoteService {

    @Autowired
    private SchtypeSectionService schtypeSectionService;

    @Autowired
    private SchtypeSectionDao schtypeSectionDao;

    @Override
    protected BaseService<SchtypeSection, String> getBaseService() {
        return schtypeSectionService;
    }

    @Override
    public String findBySchoolType(String schoolType) {
        // TODO Auto-generated method stub
        return SUtils.s(schtypeSectionDao.findBySchoolType(schoolType));
    }

}
