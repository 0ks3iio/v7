package net.zdsoft.system.remote.service.impl;

import net.zdsoft.basedata.remote.service.impl.BaseRemoteServiceImpl;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.system.entity.server.ModelOperation;
import net.zdsoft.system.remote.service.ModelOperationRemoteService;
import net.zdsoft.system.service.server.ModelOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Designed By luf
 *
 * @author luf
 * @date 2019/7/15 11:33
 */
@Service("modelOperationRemoteService")
public class ModelOperationRemoteServiceImpl extends BaseRemoteServiceImpl<ModelOperation,Integer> implements ModelOperationRemoteService {
    @Autowired
    private ModelOperationService modelOperationService;

    @Override
    protected BaseService<ModelOperation, Integer> getBaseService() {
        return modelOperationService;
    }

}
