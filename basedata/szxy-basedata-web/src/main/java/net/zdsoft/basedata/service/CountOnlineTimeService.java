package net.zdsoft.basedata.service;

import net.zdsoft.basedata.entity.CountOnlineTime;

public interface CountOnlineTimeService extends BaseService<CountOnlineTime, String> {

	public CountOnlineTime getCountOnlineTimeBySessId(String sessionId);
	
	public CountOnlineTime getCountOnlineTimeListByLastloginUserId(String userId);
}
