package net.zdsoft.system.service.server.impl;


import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.system.dao.server.ModelOperationDao;
import net.zdsoft.system.entity.server.ModelOperation;
import net.zdsoft.system.service.server.ModelOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Designed By luf
 *
 * @author luf
 * @date 2019/7/15 13:48
 */
@Service
public class ModelOperationServiceImpl extends BaseServiceImpl<ModelOperation,Integer> implements ModelOperationService {
    @Autowired
    private ModelOperationDao modelOperationDao;
    @Override
    protected BaseJpaRepositoryDao<ModelOperation, Integer> getJpaDao() {
        return modelOperationDao;
    }

    @Override
    protected Class<ModelOperation> getEntityClass() {
        return ModelOperation.class;
    }
}
