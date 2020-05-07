package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.CountOnlineTime;

public interface CountOnlineTimeRemoteService extends BaseRemoteService<CountOnlineTime,String>{

    public CountOnlineTime getCountOnlineTimeBySessId(String sessionId);
	
	public CountOnlineTime getCountOnlineTimeListByLastloginUserId(String userId);
}
