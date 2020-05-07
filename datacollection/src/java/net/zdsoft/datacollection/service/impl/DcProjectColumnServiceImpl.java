package net.zdsoft.datacollection.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.datacollection.dao.DcProjectColumnDao;
import net.zdsoft.datacollection.entity.DcProjectColumn;
import net.zdsoft.datacollection.service.DcProjectColumnService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

@Service
public class DcProjectColumnServiceImpl extends BaseServiceImpl<DcProjectColumn, String>
		implements DcProjectColumnService {

	@Autowired
	private DcProjectColumnDao dcProjectColumnDao;

	@Override
	protected BaseJpaRepositoryDao<DcProjectColumn, String> getJpaDao() {
		return dcProjectColumnDao;
	}

	@Override
	protected Class<DcProjectColumn> getEntityClass() {
		return DcProjectColumn.class;
	}

	@Override
	public void delete(String projectId, String unitId) {
		dcProjectColumnDao.delete(projectId, unitId);
	}

}
