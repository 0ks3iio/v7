package net.zdsoft.basedata.dao;

import java.util.Date;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.SyncTriggerData;

public interface SyncTriggerDataDao extends BaseJpaRepositoryDao<SyncTriggerData, String> {
	
	public void deleteByCreationTimeBefore(Date creationTime);

}
