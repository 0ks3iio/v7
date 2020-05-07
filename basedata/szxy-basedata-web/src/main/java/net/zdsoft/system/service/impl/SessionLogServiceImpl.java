package net.zdsoft.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.system.dao.SessionLogDao;
import net.zdsoft.system.entity.SessionLog;
import net.zdsoft.system.service.SessionLogService;

@Service
public class SessionLogServiceImpl extends BaseServiceImpl<SessionLog, String> implements SessionLogService {

	@Autowired
	private SessionLogDao sessionLogDao;

	@Override
	protected BaseJpaRepositoryDao<SessionLog, String> getJpaDao() {
		return sessionLogDao;
	}

	@Override
	protected Class<SessionLog> getEntityClass() {
		return null;
	}

}
