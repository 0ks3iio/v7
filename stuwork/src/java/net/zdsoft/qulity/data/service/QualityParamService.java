package net.zdsoft.qulity.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.qulity.data.entity.QualityParam;

import java.util.List;
import java.util.Map;

public interface QualityParamService  extends BaseService<QualityParam,String>{

	public List<QualityParam> findByUnitId(String unitId, boolean isAll);

	public List<QualityParam> findByUnitIdAndParamType(String unitId, String paramType);

	public Map<String, Boolean> findIsShowMapByUnitId(String unitId);

    boolean findIsShowByGradeId(String unitId, String gradeId);
}
