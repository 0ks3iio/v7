package net.zdsoft.pushjob.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.pushjob.dao.BasePushJobDao;
import net.zdsoft.pushjob.dao.BasePushLoggerDao;
import net.zdsoft.pushjob.entity.BasePushLogger;
import net.zdsoft.pushjob.service.BasePushLoggerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("basePushLoggerService")
public class BasePushLoggerServiceImpl extends BaseServiceImpl<BasePushLogger, String> implements BasePushLoggerService {

	@Autowired
	private BasePushLoggerDao  basePushLoggerDao;
	
	@Override
	protected BaseJpaRepositoryDao<BasePushLogger, String> getJpaDao() {
		return basePushLoggerDao;
	}

	@Override
	protected Class<BasePushLogger> getEntityClass() {
		return BasePushLogger.class;
	}

}
