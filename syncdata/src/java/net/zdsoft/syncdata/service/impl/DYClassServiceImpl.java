package net.zdsoft.syncdata.service.impl;

import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.syncdata.dao.DYClassDao;
import net.zdsoft.syncdata.entity.DYClazz;
import net.zdsoft.syncdata.service.DYClassService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dYClassService")
public class DYClassServiceImpl extends BaseServiceImpl<DYClazz, String> implements DYClassService {

    @Autowired
    private DYClassDao dyClassDao;

    @Override
    protected BaseJpaRepositoryDao<DYClazz, String> getJpaDao() {
        return dyClassDao;
    }

    @Override
    protected Class<DYClazz> getEntityClass() {
        return DYClazz.class;
    }

	@Override
	public List<DYClazz> saveAllEntitys(DYClazz... array) {
		return dyClassDao.saveAll(checkSave(array));
	}

}
