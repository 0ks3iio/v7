package net.zdsoft.basedata.remote.service.impl;

import net.zdsoft.basedata.entity.CountOnlineTime;
import net.zdsoft.basedata.remote.service.CountOnlineTimeRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.CountOnlineTimeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service("countOnlineTimeRemoteService")
public class CountOnlineTimeRemoteServiceImpl extends BaseRemoteServiceImpl<CountOnlineTime, String> implements CountOnlineTimeRemoteService{
    @Autowired
	private CountOnlineTimeService countOnlineTimeService;
	@Override
	protected BaseService<CountOnlineTime, String> getBaseService() {
		return countOnlineTimeService;
	}
	@Override
	public CountOnlineTime getCountOnlineTimeBySessId(String sessionId) {
		return countOnlineTimeService.getCountOnlineTimeBySessId(sessionId);
	}
	@Override
	public CountOnlineTime getCountOnlineTimeListByLastloginUserId(String userId) {
		return countOnlineTimeService.getCountOnlineTimeListByLastloginUserId(userId);
	}

}
