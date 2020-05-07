package net.zdsoft.datacollection.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.datacollection.dao.DcProjectDao;
import net.zdsoft.datacollection.entity.DcProject;
import net.zdsoft.datacollection.service.DcProjectService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

@Service
public class DcProjectServiceImpl extends BaseServiceImpl<DcProject, String> implements DcProjectService {
	
	@Autowired
	private DcProjectDao dcProjectDao;

	@Override
	protected BaseJpaRepositoryDao<DcProject, String> getJpaDao() {
		return dcProjectDao;
	}

	@Override
	protected Class<DcProject> getEntityClass() {
		return DcProject.class;
	}

}
