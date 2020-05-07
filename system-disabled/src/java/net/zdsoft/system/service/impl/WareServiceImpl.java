package net.zdsoft.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.system.dao.WareDao;
import net.zdsoft.system.entity.Ware;
import net.zdsoft.system.service.WareService;

@Service("wareService")
public class WareServiceImpl extends BaseServiceImpl<Ware, String> implements WareService {

    @Autowired
    private WareDao wareDao;

    @Override
    protected BaseJpaRepositoryDao<Ware, String> getJpaDao() {
        return wareDao;
    }

    @Override
    protected Class<Ware> getEntityClass() {
        return Ware.class;
    }

}
