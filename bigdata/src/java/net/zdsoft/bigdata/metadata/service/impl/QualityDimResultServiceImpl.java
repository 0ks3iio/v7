package net.zdsoft.bigdata.metadata.service.impl;

import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.metadata.dao.QualityDimResultDao;
import net.zdsoft.bigdata.metadata.entity.QualityDimResult;
import net.zdsoft.bigdata.metadata.service.QualityDimResultService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("qualityDimResultService")
public class QualityDimResultServiceImpl extends BaseServiceImpl<QualityDimResult, String>
		implements QualityDimResultService {

	@Autowired
	private QualityDimResultDao qualityDimResultDao;

	@Override
	public List<QualityDimResult> findQualityDimResultsByType(Integer type) {
		return qualityDimResultDao.findQualityDimResultsByType(type);
	}
	
	@Override
	public List<QualityDimResult> findQualityDimResultsByTypeDesc(Integer type) {
		return qualityDimResultDao.findQualityDimResultsByTypeDesc(type);
	}
	
	@Override
	public List<QualityDimResult> findQualityDimResultsByTypeAndStatus(
			Integer type, Integer status) {
		return qualityDimResultDao.findQualityDimResultsByTypeAndStatus(type, status);
	}
	
	@Override
	public List<QualityDimResult> findQualityDimResultsByTypeAndDimName(
			Integer type, String dimName) {
		return qualityDimResultDao.findQualityDimResultsByTypeAndDimName( type, dimName);
	}

	@Override
	public void updateHistoryDataStatus() {
		qualityDimResultDao.updateHistoryDataStatus();
	}
	
	@Override
	public void deleteAll() {
		qualityDimResultDao.deleteAllInBatch();
	}

	@Override
	protected BaseJpaRepositoryDao<QualityDimResult, String> getJpaDao() {
		return qualityDimResultDao;
	}

	@Override
	protected Class<QualityDimResult> getEntityClass() {
		return QualityDimResult.class;
	}

}
