package net.zdsoft.savedata.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.savedata.dao.CheckWorkDataDao;
import net.zdsoft.savedata.entity.CheckWorkData;
import net.zdsoft.savedata.service.CheckWorkDataService;

/**
 * @author yangsj  2018年8月10日下午2:30:33
 */
@Service("checkWorkDataService")
public class CheckWorkDataServiceImpl extends BaseServiceImpl<CheckWorkData, String> implements CheckWorkDataService {

	@Autowired
	private CheckWorkDataDao checkWorkDataDao;
	
	@Override
	protected BaseJpaRepositoryDao<CheckWorkData, String> getJpaDao() {
		return checkWorkDataDao;
	}

	@Override
	protected Class<CheckWorkData> getEntityClass() {
		return CheckWorkData.class;
	}


}
