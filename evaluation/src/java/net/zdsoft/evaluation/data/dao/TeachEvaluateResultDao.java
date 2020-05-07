package net.zdsoft.evaluation.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.evaluation.data.entity.TeachEvaluateResult;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface TeachEvaluateResultDao extends BaseJpaRepositoryDao<TeachEvaluateResult,String>,TeachEvaluateResultJdbcDao{
	
	@Query("From TeachEvaluateResult where state = '2' and projectId in ?1")
	public List<TeachEvaluateResult> findBySubmitAndInProjectId(String[] projectIds);

	
	@Query("From TeachEvaluateResult where unitId =?1 and operatorId =?2 and state =?3 and  projectId in ?4")
	public List<TeachEvaluateResult> findByCon(String unitId,String operatorId,String state,String[] projectIds);
	
	@Query("From TeachEvaluateResult where unitId =?1 and itemId =?2")
	public List<TeachEvaluateResult> findByCon(String unitId,String itemId);
	
	@Query("From TeachEvaluateResult where unitId =?1 and operatorId =?2  and  projectId = ?3 and evaluateType =?4")
	public List<TeachEvaluateResult> findByCon(String unitId,String operatorId,String projectId,String evaluateType);
	
	@Query("From TeachEvaluateResult where unitId =?1 and operatorId =?2  and  projectId = ?3 and evaluateType =?4 and state =?5")
	public List<TeachEvaluateResult> findByCon(String unitId,String operatorId,String projectId,String evaluateType,String state);
	
	@Modifying
	@Query("Delete From TeachEvaluateResult where unitId =?1 and operatorId =?2  and  projectId = ?3 and evaluateType =?4")
	public void deleteByCon(String unitId,String operatorId,String projectId,String evaluateType);
	@Modifying
	@Query("Delete From TeachEvaluateResult where unitId =?1 and operatorId =?2  and  projectId = ?3 and evaluateType =?4 and subjectId =?5")
	public void deleteByCon(String unitId,String operatorId,String projectId,String evaluateType,String subjectId);
	
	@Modifying
	@Query("Delete From TeachEvaluateResult where unitId =?1 and operatorId =?2  and  projectId = ?3 and evaluateType =?4 and subjectId =?5 and teacherId = ?6")
	public void deleteByConTea(String unitId,String operatorId,String projectId,String evaluateType,String subjectId,String teacherId);
	
	@Modifying
	@Query("Update  TeachEvaluateResult set state =?1 where id in ?2")
	public void updateByCon(String state,String[] ids);
	@Modifying
	@Query("Delete From TeachEvaluateResult where projectId = ?1")
	public void deleteByProjectId(String projectId);
	
	@Query("From TeachEvaluateResult where projectId = ?1 and classId = ?2 and subjectId = ?3")
	public List<TeachEvaluateResult> findByProjectIdAndClsIdAndSubId(String projectId, String clsId, String subId);

	@Query("From TeachEvaluateResult where projectId = ?1 and subjectId = ?2 and teacherId = ?3")
	public List<TeachEvaluateResult> findByProjectIdAndSubIdAndTeacherId(String projectId, String subId, String teaId);

	@Query("From TeachEvaluateResult where projectId = ?1 and classId = ?2")
	public List<TeachEvaluateResult> findByProjectIdAndClsId(String projectId,String clsId);

	@Query("From TeachEvaluateResult where projectId = ?1 and teacherId = ?2")
	public List<TeachEvaluateResult> findByProjectIdAndTeaId(String projectId,String teaId);

	@Query("From TeachEvaluateResult where projectId = ?1 and gradeId = ?2")
	public List<TeachEvaluateResult> findByProjectIdAndGradeId(
			String projectId, String gradeId);
	
	@Query("From TeachEvaluateResult where projectId = ?1 and subjectId = ?2")
	public List<TeachEvaluateResult> findByProjectIdAndSubId(String projectId,
			String subId);
	
	@Query("From TeachEvaluateResult where projectId = ?1 and state='2' and itemType = '12' and teacherId=?2 and subjectId = ?3 and classId=?4")
	public List<TeachEvaluateResult> findTxtBySubIdAndClsId(String projectId,String teaId, String subjectId, String classId);
	
	@Query("From TeachEvaluateResult where projectId = ?1 and state='2' and itemType = '12' and teacherId=?2 and subjectId = ?3")
	public List<TeachEvaluateResult> findTxtBySubId(String projectId,String teaId, String subjectId);
	
	@Query("From TeachEvaluateResult where projectId = ?1 and state='2' and itemType = '12' and teacherId=?2 and classId = ?3")
	public List<TeachEvaluateResult> findTxtByClsId(String projectId,String teaId, String classId);

	@Query("From TeachEvaluateResult where projectId = ?1 and state='2' and itemType = '12' and teacherId=?2")
	public List<TeachEvaluateResult> findTxtByTeaId(String projectId,String teaId);
	
	@Query("From TeachEvaluateResult where projectId = ?1 and state='2' and itemType = '12' ")
	public List<TeachEvaluateResult> findTxtByTeaId(String projectId);

	@Modifying
	@Query("Delete From TeachEvaluateResult where unitId =?1 and operatorId =?2  and  projectId = ?3 and evaluateType =?4 and subjectId =?5 and teacherId = ?6  and classId = ?7")
	public void deleteByConTea(String unitId, String studentId, String projectId, String evaluateType, String subjectId,
			String teacherId, String teachOrclassId);

}
