package net.zdsoft.basedata.remote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.EduInfo;
import net.zdsoft.basedata.remote.service.EduInfoRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.EduInfoService;

@Service("eduInfoRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class EduInfoRemoteServiceImpl extends BaseRemoteServiceImpl<EduInfo,String> implements EduInfoRemoteService {

    @Autowired
    private EduInfoService eduInfoService;

    @Override
    protected BaseService<EduInfo, String> getBaseService() {
        return eduInfoService;
    }

}
