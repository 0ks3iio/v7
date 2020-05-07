package net.zdsoft.basedata.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.CountOnlineTimeDao;
import net.zdsoft.basedata.entity.CountOnlineTime;
import net.zdsoft.basedata.service.CountOnlineTimeService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import java.util.List;

@Service("countOnlineTimeService")
public class CountOnlineTimeServiceImpl extends BaseServiceImpl<CountOnlineTime, String> implements CountOnlineTimeService{
	@Autowired
	private CountOnlineTimeDao countOnlineTimeDao;

	@Override
	protected BaseJpaRepositoryDao<CountOnlineTime, String> getJpaDao() {
		return countOnlineTimeDao;
	}

	@Override
	protected Class<CountOnlineTime> getEntityClass() {
		return CountOnlineTime.class;
	}

	@Override
	public CountOnlineTime getCountOnlineTimeBySessId(String sessionId) {
		List<CountOnlineTime> times =  countOnlineTimeDao.getCountOnlineTimeBySessId(sessionId);
		if(CollectionUtils.isNotEmpty(times)){
			return times.get(0);
		}
		return null;
	}

	@Override
	public CountOnlineTime getCountOnlineTimeListByLastloginUserId(String userId) {
		List<CountOnlineTime> times = countOnlineTimeDao.getCountOnlineTimeListByLastloginUserId(userId);
		if(CollectionUtils.isNotEmpty(times)){
			return times.get(0);
		}
		return null;
	}

}
