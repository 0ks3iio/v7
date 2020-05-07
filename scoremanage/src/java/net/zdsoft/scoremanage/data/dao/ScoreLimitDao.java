package net.zdsoft.scoremanage.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.scoremanage.data.entity.ScoreLimit;

public interface ScoreLimitDao extends BaseJpaRepositoryDao<ScoreLimit, String>{

	@Modifying
	@Query("delete from ScoreLimit where acadyear = ?1 and semester=?2 and unitId= ?4 and examInfoId= ?3  and subjectId= ?5 and classId= ?6 and teacherId= ?7")
	int deleteBySearch(String acadyear, String semester, String examId,
			String unitId, String subjectId, String classId, String teacherId);
	/**
	 * 查出某单位所有有必修课录分权限的老师Id
	 * @param unitId
	 * @param examId
	 * @return
	 */
	@Query("select teacherId From ScoreLimit where unitId = ?1 and examInfoId != ?2 and (classId in "
			+ "(select id from TeachClass where unitId = ?1 and isUsing=1 and isUsingMerge!=1 and isDeleted!=1)"
			+ " or classId in (select id from Clazz where schoolId = ?1 and isDeleted=0 ))")
	List<String> findTeacherIdByUnitIdAndExamInfoIdNot(String unitId, String examId);
	/**
	 * 查出某单位所有有选修课录分权限的老师Id
	 * @param unitId
	 * @param examId
	 * @return
	 */
	@Query("select teacherId From ScoreLimit where unitId = ?1 and examInfoId = ?2 and classId in (select id from TeachClass where unitId = ?1 and isUsing=1 and isUsingMerge!=1 and isDeleted!=1)")
	List<String> findTeacherIdByUnitIdAndExamInfoId(String unitId, String examId);
	
}
