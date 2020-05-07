package net.zdsoft.basedata.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.SyncTriggerDataDao;
import net.zdsoft.basedata.service.SyncTriggerDataService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.SyncTriggerData;

@Service
public class SyncTriggerDataServiceImpl extends BaseServiceImpl<SyncTriggerData, String> implements SyncTriggerDataService{

	@Autowired
	private SyncTriggerDataDao syncTriggerDataDao;
	
	public void deleteByCreationTimeBefore(Date creationTime) {
		syncTriggerDataDao.deleteByCreationTimeBefore(creationTime);
	}
	
	@Override
	protected BaseJpaRepositoryDao<SyncTriggerData, String> getJpaDao() {
		return syncTriggerDataDao;
	}

	@Override
	protected Class<SyncTriggerData> getEntityClass() {
		return null;
	}

	
}
