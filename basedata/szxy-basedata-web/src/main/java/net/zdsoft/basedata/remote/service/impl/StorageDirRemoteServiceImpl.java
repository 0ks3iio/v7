package net.zdsoft.basedata.remote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.StorageDir;
import net.zdsoft.basedata.remote.service.StorageDirRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.StorageDirService;
import net.zdsoft.framework.utils.SUtils;

@Service("storageDirRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class StorageDirRemoteServiceImpl extends BaseRemoteServiceImpl<StorageDir,String> implements StorageDirRemoteService {

    @Autowired
    private StorageDirService storageDirService;

    @Override
    protected BaseService<StorageDir, String> getBaseService() {
        return storageDirService;
    }

    @Override
    public String findByTypeAndActiove(Integer type, String actiove) {
        return SUtils.s(storageDirService.findByTypeAndActiove(type, actiove));
    }

    @Override
    public String findByTypeAndPreset(Integer type, String preset) {
        return SUtils.s(storageDirService.findByTypeAndPreset(type, preset));
    }

}
