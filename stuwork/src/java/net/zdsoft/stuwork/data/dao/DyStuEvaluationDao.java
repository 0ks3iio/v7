package net.zdsoft.stuwork.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.entity.DyStuEvaluation;

import org.springframework.data.jpa.repository.Query;

public interface DyStuEvaluationDao extends BaseJpaRepositoryDao<DyStuEvaluation, String>{

	@Query("FROM DyStuEvaluation Where unitId = ?1 and acadyear =?2 and semester =?3 and studentId in (?4)")
	public List<DyStuEvaluation> findListByUidAndDto(String unitId,String acadyear,String semester,String[] stuIds);
	
	@Query("FROM DyStuEvaluation Where unitId = ?1 and acadyear =?2 and semester =?3 ")
	public List<DyStuEvaluation> findListByUidAndAcad(String unitId,String acadyear,String semester);
	
	@Query("FROM DyStuEvaluation Where unitId = ?1 and studentId in (?2) order by acadyear,semester desc")
	public List<DyStuEvaluation> findListByUidAndStuids(String unitId,String[] stuIds);
	
	@Query("FROM DyStuEvaluation Where unitId = ?1 and acadyear =?2 and semester =?3 and studentId = ?4")
	public DyStuEvaluation findOneByUidAndDto(String unitId,String acadyear,String semester,String studentId);
	
	@Query("From DyStuEvaluation where studentId = ?1")
	public List<DyStuEvaluation> findByStudentId(String studentId);
	
	public List<DyStuEvaluation> findByUnitIdAndGradeIdIn(String unitId, String[] gradeIds);
}
