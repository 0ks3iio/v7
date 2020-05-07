package net.zdsoft.evaluation.data.dao;

import java.util.Map;
import java.util.Set;

public interface TeachEvaluateResultJdbcDao {
	
	public Set<String> getStuIdByProjectId(String projectId, String classId, String subId, String teacherId);
	
	public Set<String> getResultClsSubTeaIds(String projectId);
	public Set<String> getResultGradeClsId(String projectId);
	public Set<String> getResultGradeTeaId(String projectId);
	public Set<String> getResultSubIds(String projectId);
	
	public Map<String, Integer> getCountMapByProjectIds(String[] projectIds);
	public Map<String, Float> findTeaRankBy(String projectId, String evaluateType);
}
