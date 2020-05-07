package net.zdsoft.basedata.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.CourseScheduleRevert;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface CourseScheduleRevertDao extends BaseJpaRepositoryDao<CourseScheduleRevert, String> {

	@Modifying
	@Query("delete from CourseScheduleRevert where schoolId =?1 and acadyear=?2 and semester=?3 and gradeId =?4")
	void deleteByGradeId(String unitId, String acadyear, Integer semester, String gradeId);

	@Modifying
	@Query("delete from CourseScheduleRevert where schoolId =?1 and acadyear=?2 and semester=?3")
	void deleteByUnitId(String unitId, String acadyear, Integer semester);

	@Query("from CourseScheduleRevert where schoolId =?1 and acadyear=?2 and semester=?3 and classId in (?4)")
	List<CourseScheduleRevert> findbyClassIds(String schoolId, String acadyear, Integer semeseter, String[] classIds);

	@Query(nativeQuery=true,value="select 1 from dual where exists(select 1 from BASE_COURSE_SCHEDULE_REVERT where "
			+ "school_Id =?1 and acadyear=?2 and semester=?3 and grade_Id =?4)")
	Integer checkBackupExists(String unitId, String acadyear, Integer semester, String gradeId);
}
