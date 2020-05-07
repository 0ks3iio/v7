package net.zdsoft.basedata.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import net.zdsoft.framework.entity.SyncTriggerData;

public interface SyncTriggerDataService extends BaseService<SyncTriggerData, String> {
	
	void deleteByCreationTimeBefore(Date creationTime);

}
