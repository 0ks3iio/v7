package net.zdsoft.qulity.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.qulity.data.entity.QualityScore;

import java.util.List;

public interface QualityScoreService  extends BaseService<QualityScore,String>{

	public QualityScore findByUnitIdOne(String unitId, String type);

	public void deleteByUnitIdAndType(String unitId, String type);

	public List<QualityScore> findByClassIdsAndType(String[] classIds, String type, Pagination page);

    public List<QualityScore> findByUnitIdAndType(String unitId, String type);

	public void saveAndDelete(String unitId, String type, QualityScore[] scoreList);

	public void setRecoverStuScore(String unitId);
}
