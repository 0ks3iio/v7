package net.zdsoft.basedata.dao;

import java.util.List;

import net.zdsoft.basedata.entity.GradeTeaching;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface GradeTeachingDao extends BaseJpaRepositoryDao<GradeTeaching, String>{

	@Query("From GradeTeaching where unitId = ?1 and acadyear = ?2 and semester = ?3 and gradeId = ?4 and isDeleted = ?5")
	List<GradeTeaching> find(
			String unitId, String acadyear, String semester, String gradeId,
			Integer isDeleted);

	List<GradeTeaching> findByUnitIdAndAcadyearAndSemesterAndGradeIdAndIsDeletedAndSubjectType(
			String unitId, String acadyear, String semester, String gradeId,
			int isDeleted, int parseInt);

	@Modifying
	@Query("update GradeTeaching set isDeleted = 1 where unitId = ?1 and gradeId = ?2 and acadyear = ?3 and semester = ?4 and subjectType like ?5")
	void delGradeTeach(String unitId, String gradeId, String acadyear,
			String semester,String likeSubType);
	
	@Query("From GradeTeaching where gradeId = ?1")
	List<GradeTeaching> findByGrade(String gradeId);

	List<GradeTeaching> findByUnitId(String unitId);

	@Query("From GradeTeaching where unitId = ?1 and acadyear = ?2 and semester = ?3 and gradeId in (?4) and isDeleted = ?5")
	List<GradeTeaching> find(String unitId, String acadyearVal,
			String semesterVal, String[] array, int isDeletedFalse);
	
	List<GradeTeaching> findByUnitIdAndIsDeletedAndAcadyearAndSemesterAndGradeId(String unitId, int isDeletedFalse, String acadyear,
			String semester,String gradeId);

	List<GradeTeaching> findByGradeIdAndIsDeleted(String gradeId, int isDeleted);

	@Query("From GradeTeaching where acadyear = ?1 and semester = ?2 and gradeId = ?3 and unitId = ?4 and isDeleted = 0 and subjectType in (?5)")
	List<GradeTeaching> findGradeTeachingList(String acadyear, String semester, String gradeId, String unitId, String[] subjectTypes);

	@Modifying
	@Query("update GradeTeaching set isDeleted = 1 where isDeleted=0 and gradeId in (?1)")
	void deleteByGradeIds(String... gradeIds);

	@Modifying
	@Query("update GradeTeaching set isDeleted = 1 where isDeleted=0 and subjectId in (?1)")
	void deleteBySubjectIds(String... subjectIds);
}
