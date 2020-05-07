package net.zdsoft.qulity.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.qulity.data.constant.QualityConstants;
import net.zdsoft.qulity.data.dao.QualityParamDao;
import net.zdsoft.qulity.data.entity.QualityParam;
import net.zdsoft.qulity.data.service.QualityParamService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("qualityParamService")
public class QualityParamServiceImpl extends BaseServiceImpl<QualityParam, String> implements QualityParamService {
	@Autowired
	private QualityParamDao qualityParamDao;
	
	@Override
	protected BaseJpaRepositoryDao<QualityParam, String> getJpaDao() {
		return qualityParamDao;
	}

	@Override
	protected Class<QualityParam> getEntityClass() {
		return QualityParam.class;
	}

	@Override
	public List<QualityParam> findByUnitId(String unitId, boolean isAll) {
		List<QualityParam> paramList = qualityParamDao.findByUnitId(unitId);
		if(CollectionUtils.isNotEmpty(paramList)&&!isAll){
			paramList = paramList.stream().filter(e->!QualityConstants.QULITY_SHOW_JUNIOR_SWITCH.equals(e.getParamType())).collect(Collectors.toList());
		}
		return paramList;
	}

	@Override
	public List<QualityParam> findByUnitIdAndParamType(String unitId, String paramType) {
		return qualityParamDao.findByUnitIdAndParamType(unitId, paramType);
	}

	@Override
	public Map<String, Boolean> findIsShowMapByUnitId(String unitId) {
		Map<String, Boolean> map = new HashMap<>();
		List<QualityParam> qualityParamList = qualityParamDao.findByUnitIdAndParamType(unitId, QualityConstants.QULITY_SHOW_JUNIOR_SWITCH);
		if(CollectionUtils.isNotEmpty(qualityParamList)){
			map = qualityParamList.stream().collect(Collectors.toMap(e -> e.getGradeId(), e -> e.getParam() == 1));
		}
		return map;
	}

	@Override
	public boolean findIsShowByGradeId(String unitId, String gradeId) {
		QualityParam param = qualityParamDao.findByUnitIdAndParamTypeAndGradeId(unitId, QualityConstants.QULITY_SHOW_JUNIOR_SWITCH, gradeId);
		if(param!=null && Constant.IS_TRUE==param.getParam()){
			return true;
		}
		return false;
	}

}
