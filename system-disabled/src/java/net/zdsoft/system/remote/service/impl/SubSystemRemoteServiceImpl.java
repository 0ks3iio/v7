package net.zdsoft.system.remote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.remote.service.impl.BaseRemoteServiceImpl;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.system.entity.server.SubSystem;
import net.zdsoft.system.remote.service.SubSystemRemoteService;
import net.zdsoft.system.service.server.SubsystemService;

@Service("subSystemRemoteService")
public class SubSystemRemoteServiceImpl extends BaseRemoteServiceImpl<SubSystem, Integer> implements
        SubSystemRemoteService {

    @Autowired
    private SubsystemService subsystemService;

    @Override
    protected BaseService<SubSystem, Integer> getBaseService() {
        return subsystemService;
    }

}
