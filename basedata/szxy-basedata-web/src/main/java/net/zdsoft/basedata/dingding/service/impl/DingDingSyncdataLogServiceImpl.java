package net.zdsoft.basedata.dingding.service.impl;

import net.zdsoft.basedata.dingding.dao.DingDingSyncdataLogDao;
import net.zdsoft.basedata.dingding.entity.DdSyncdataLog;
import net.zdsoft.basedata.dingding.service.DingDingSyncdataLogService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dingDingSyncdataLogService")
public class DingDingSyncdataLogServiceImpl extends
		BaseServiceImpl<DdSyncdataLog, String> implements DingDingSyncdataLogService {

	@Autowired
	DingDingSyncdataLogDao dingDingSyncdataLogDao;

	@Override
	protected BaseJpaRepositoryDao<DdSyncdataLog, String> getJpaDao() {
		return dingDingSyncdataLogDao;
	}

	@Override
	protected Class<DdSyncdataLog> getEntityClass() {
		return DdSyncdataLog.class;
	}

	@Override
	public void insertLogs(DdSyncdataLog... logs) {
		dingDingSyncdataLogDao.insertLogs(logs);
		
	}
	
	
}
