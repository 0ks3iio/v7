package net.zdsoft.gkelective.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.gkelective.data.dao.GkClassStoreDao;
import net.zdsoft.gkelective.data.entity.GkClassStore;
import net.zdsoft.gkelective.data.service.GkClassStoreService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("gkClassStoreService")
public class GkClassStoreServiceImpl extends BaseServiceImpl<GkClassStore, String> implements GkClassStoreService {

	@Autowired
	private GkClassStoreDao gkClassStoreDao;
	@Override
	protected BaseJpaRepositoryDao<GkClassStore, String> getJpaDao() {
		return gkClassStoreDao;
	}

	@Override
	protected Class<GkClassStore> getEntityClass() {
		return GkClassStore.class;
	}



}
