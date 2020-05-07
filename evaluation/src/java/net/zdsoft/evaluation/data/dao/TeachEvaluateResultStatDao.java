package net.zdsoft.evaluation.data.dao;

import java.util.List;

import net.zdsoft.evaluation.data.entity.TeachEvaluateResultStat;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface TeachEvaluateResultStatDao extends BaseJpaRepositoryDao<TeachEvaluateResultStat,String>{
	
	@Modifying@Transactional
	@Query("Delete From TeachEvaluateResultStat where projectId = ?1")
	public void deleteByProjectId(String projectId);
	
	@Query("From TeachEvaluateResultStat where projectId in ?1 ")
	public List<TeachEvaluateResultStat> findByProjectIds(String[] projectIds);

	public List<TeachEvaluateResultStat> findByProjectIdAndDimensionAndStatSubjectIdAndStatClassId(
			String projectId, String statDimension, String subjectId,
			String classId);

	public List<TeachEvaluateResultStat> findByProjectIdAndDimensionAndStatSubjectId(
			String projectId, String statDimension, String subjectId);

	public List<TeachEvaluateResultStat> findByProjectIdAndDimensionAndStatClassId(
			String projectId, String statDimension, String classId);

	public List<TeachEvaluateResultStat> findByProjectIdAndDimension(
			String projectId, String statDimension);

	public List<TeachEvaluateResultStat> findByProjectIdAndDimensionAndStatSubjectIdAndStatTeacherId(
			String projectId, String statDimension, String subId,
			String teacherId);

	public List<TeachEvaluateResultStat> findByProjectIdAndDimensionAndStatTeacherId(
			String projectId, String statDimension, String teaId);

}
