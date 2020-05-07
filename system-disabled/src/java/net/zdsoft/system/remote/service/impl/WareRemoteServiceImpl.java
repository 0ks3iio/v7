package net.zdsoft.system.remote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.remote.service.impl.BaseRemoteServiceImpl;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.system.entity.Ware;
import net.zdsoft.system.remote.service.WareRemoteService;
import net.zdsoft.system.service.WareService;

@Service("wareRemoteService")
public class WareRemoteServiceImpl extends BaseRemoteServiceImpl<Ware,String> implements WareRemoteService {

    @Autowired
    private WareService wareService;

    @Override
    protected BaseService<Ware, String> getBaseService() {
        return wareService;
    }

}
