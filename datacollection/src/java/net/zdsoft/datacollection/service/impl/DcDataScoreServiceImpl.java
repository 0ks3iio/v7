package net.zdsoft.datacollection.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.datacollection.dao.DcDataScoreDao;
import net.zdsoft.datacollection.entity.DcDataScore;
import net.zdsoft.datacollection.service.DcDataScoreService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

@Service
public class DcDataScoreServiceImpl extends BaseServiceImpl<DcDataScore, String> implements DcDataScoreService {

	@Autowired
	private DcDataScoreDao dcDataScoreDao;

	@Override
	protected BaseJpaRepositoryDao<DcDataScore, String> getJpaDao() {
		return dcDataScoreDao;
	}

	@Override
	protected Class<DcDataScore> getEntityClass() {
		return DcDataScore.class;
	}

}
