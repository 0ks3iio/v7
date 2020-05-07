package net.zdsoft.basedata.dao;

import java.util.List;

import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ClassTeachingDao extends BaseJpaRepositoryDao<ClassTeaching, String>,ClassTeachingJdbcDao{

	public List<ClassTeaching> findByUnitIdAndIsDeletedAndAcadyearAndSemesterAndSubjectIdIn(String unitId, int isDeletedFalse, String acadyear,
			String semester, String... subjectIds);
	
	public List<ClassTeaching> findByUnitIdAndAcadyearAndSemesterAndClassIdIn(String unitId,String acadyear,String semester, String[] classIds);

	public List<ClassTeaching> findByIsDeletedAndSubjectIdAndAcadyearAndSemesterAndClassIdIn(int isDeletedFalse,
			String subjectId, String acadyear, String semester, String[] classIds);
	
	@Query("select ct From ClassTeaching ct, Clazz cls where ct.classId=cls.id and cls.isDeleted=0 and cls.isGraduate=0 and " +
			"ct.unitId = ?1 and ct.acadyear = ?2 and ct.semester = ?3 and ct.teacherId in (?4) and ct.isDeleted = 0")
	public List<ClassTeaching> findClassTeachingList(String unitId, String acadyear, String semester,
			String[] teaherIds);

	@Modifying
	@Query("delete from ClassTeaching where unitId = ?1 and acadyear = ?2 and semester = ?3 and subjectType = ?4 and classId in (?5)")
	public void delete(String unitId, String acadyear, String semester,
			Integer subjectType, String[] array);

	@Modifying
	@Query("delete from ClassTeaching where id in (?1)")
	public void deleteByIds(String[] array);

	@Modifying
	@Query("update ClassTeaching set teacherId = ?7 where acadyear = ?1 and semester = ?2 and classId in (?3) and unitId = ?4 and subjectId = ?5 and isDeleted = ?6")
	public void updateTeacherId(String acadyear, String semester,
			String[] classIds, String unitId, String subjectId,
			int isDeletedFalse, String teacherId);

	@Modifying
	@Query("update ClassTeaching set teacherId = ?2 where id = ?1")
	public void updateOneTeacherId(String classTeachingId, String teacherId);

	public List<ClassTeaching> findByIsDeletedAndUnitIdAndAcadyearAndSemesterAndSubjectType(int isDeletedFalse, String unitId, String acadyear, String semester, String subjectType);
	
	@Modifying
	@Query("update ClassTeaching set isDeleted=1 where unitId = ?1 and acadyear = ?2 and semester = ?3 and classId in (?4)")
	public void deleteCurrentClassIds(String unitId, String acadyear, String semester, String[] classIds);

	@Modifying
	@Query("update ClassTeaching set isDeleted=1 where isDeleted=0 and classId in (?1)")
	public void deleteByClassIds(String... classIds);

	@Modifying
	@Query("update ClassTeaching set isDeleted=1 where isDeleted=0 and subjectId in (?1)")
	public void deleteBySubjectIds(String... subjectIds);
	
	/**
	 * 获取 某年级 所有行政班 的课时数，不包含虚拟课程 和 以教学班上课的课程；weekType 只能是 1：单周 2：双周
	 * @param unitId
	 * @param gradeIds
	 * @param acadyear
	 * @param semester
	 * @param weekType
	 * @return
	 */
	@Query(nativeQuery=true,value="select class_id,sum(case when week_type=3 then course_hour when week_type= (3-?5) then course_hour-1 else course_hour end) num2"
			+ " from base_class_teaching where unit_id=?1 and acadyear=?3 and semester=?4 " + 
			"and course_hour >0 and subject_type!=3 and is_deleted=0 and is_tea_cls=0 and class_id in(" + 
			"select id from base_class where grade_id in (?2) and is_deleted=0 and is_Graduate=0) " +
			"group by class_id ")
	public List<Object[]> getClassTeachingHourMap(String unitId, String[] gradeIds, String acadyear, String semester, int weekType);
}
