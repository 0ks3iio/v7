package net.zdsoft.officework.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.officework.entity.OfficeHealthSleep;


public interface OfficeHealthSleepService extends BaseService<OfficeHealthSleep, String> {

	/**
	 * 查询已存在的数据，避免重复插入数据
	 * @param dateDays
	 * @param ownerIds
	 * @return
	 */
	public List<OfficeHealthSleep> findByDateOwnerIds(String[] dateDays,
			String[] ownerIds);
	
	
}