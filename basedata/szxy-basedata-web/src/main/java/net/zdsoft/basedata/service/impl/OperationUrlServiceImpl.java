package net.zdsoft.basedata.service.impl;

import net.zdsoft.basedata.dao.OperationUrlDao;
import net.zdsoft.basedata.entity.OperationUrl;
import net.zdsoft.basedata.service.OperationUrlService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperationUrlServiceImpl extends BaseServiceImpl<OperationUrl, String> implements OperationUrlService {

	@Autowired
	private OperationUrlDao operationUrlDao;
	
	@Override
	protected BaseJpaRepositoryDao<OperationUrl, String> getJpaDao() {
		return operationUrlDao;
	}

	@Override
	protected Class<OperationUrl> getEntityClass() {
		return OperationUrl.class;
	}
	
}
