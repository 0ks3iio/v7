package net.zdsoft.evaluation.data.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.evaluation.data.entity.TeachEvaluateRelation;

public interface TeachEvaluateRelationService extends BaseService<TeachEvaluateRelation, String>{

	public List<TeachEvaluateRelation> getRelationList(String unitId,String projectId,String acadyear,
			String semester,String evaluateType,String gradeId);
	
	public void saveRelations(String valueIds,String noCheckIds,String projectId);
	
	public List<TeachEvaluateRelation> findByProjectIds(String[] projectIds);
	
	public Map<String,Set<String>> findMapByProjectIds(String[] projectIds);
}
