package net.zdsoft.bigdata.metadata.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.metadata.dao.QualityResultDetailDao;
import net.zdsoft.bigdata.metadata.entity.QualityResultDetail;
import net.zdsoft.bigdata.metadata.service.QualityResultDetailService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("qualityResultDetailService")
public class QualityResultDetailServiceImpl extends BaseServiceImpl<QualityResultDetail, String>
		implements QualityResultDetailService {

	@Autowired
	private QualityResultDetailDao qualityResultDetailDao;


	@Override
	protected BaseJpaRepositoryDao<QualityResultDetail, String> getJpaDao() {
		return qualityResultDetailDao;
	}

	@Override
	protected Class<QualityResultDetail> getEntityClass() {
		return QualityResultDetail.class;
	}

	@Override
	public void deleteAll() {
		qualityResultDetailDao.deleteAll();
	}

}
