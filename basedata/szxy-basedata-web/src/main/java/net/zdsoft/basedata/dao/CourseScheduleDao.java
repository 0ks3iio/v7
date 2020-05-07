package net.zdsoft.basedata.dao;

import java.util.List;
  






import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CourseScheduleDao extends BaseJpaRepositoryDao<CourseSchedule, String> {
    @Query("From CourseSchedule where acadyear = ?1 and semester = ?2 and classId = ?3 and weekOfWorktime=?4 and isDeleted=0")
    List<CourseSchedule> findCourseScheduleListByClassId(String searchAcadyear, Integer searchSemester,
            String classId, Integer week);
    @Query("From CourseSchedule where acadyear = ?1 and semester = ?2 and classId = ?3 and isDeleted=0")
    List<CourseSchedule> findCourseScheduleListByClassId(String searchAcadyear, Integer searchSemester,
            String classId);
    
    @Query("From CourseSchedule where acadyear = ?1 and semester = ?2 and weekOfWorktime=?4 and teacherId = ?3  and isDeleted=0")
    List<CourseSchedule> findCourseScheduleListByTeacherId(String searchAcadyear, Integer searchSemester,
            String teacherId, Integer week);

    @Modifying
    @Query("update CourseSchedule set isDeleted=1 where id in (?1) ")
	void deleteByIds(String[] ids);

    @Query("From CourseSchedule where acadyear = ?1 and semester = ?2  and weekOfWorktime=?3  and classId in (?4) and isDeleted=0")
	List<CourseSchedule> findCourseScheduleListByClassIdes(
			String searchAcadyear, Integer searchSemester,Integer week, String[] classId);
    
    @Query("select a from CourseSchedule a, DateInfo b where a.acadyear = ?1 and a.semester = ?2 and b.schoolId = a.schoolId  "
    		+ "and a.dayOfWeek = (b.weekday - 1) and a.weekOfWorktime = b.week and to_char(b.infoDate, 'yyyyMMdd') >= ?3 "
    		+ "and to_char(b.infoDate, 'yyyyMMdd') <= ?4 and a.classId in (?5) and a.isDeleted=0")
    List<CourseSchedule> findCourseScheduleListByClassIdes(
    		String searchAcadyear, Integer searchSemester,String startDate, String endDate, String[] classId);
    
    @Query("From CourseSchedule where acadyear = ?1 and semester = ?2  and classId in (?3) and isDeleted=0")
   	List<CourseSchedule> findCourseScheduleListByClassIdes(String searchAcadyear, Integer searchSemester, String[] classId);
    
    @Query("From CourseSchedule where schoolId=?1 and acadyear = ?2 and semester = ?3  and subjectId=?4 and isDeleted=0")
	List<CourseSchedule> findCourseScheduleListBySubjectId(String schoolId,
			String acadyear, Integer semester, String subjectId);
    
    @Query("From CourseSchedule where schoolId = ?3 and acadyear = ?1 and semester = ?2 and weekOfWorktime=?4 and dayOfWeek=?5 "
    		+ "and periodInterval=?6 and punchCard=?7 and period=?8 and isDeleted=0")
    List<CourseSchedule> findCourseScheduleList(String searchAcadyear, Integer searchSemester,
            String schoolId, Integer weekOfWorktime,Integer dayOfWeek,String periodInterval,Integer punchCard,Integer period);
    
    @Query("From CourseSchedule where schoolId = ?1 and acadyear = ?2 and semester = ?3  and placeId = ?4 and weekOfWorktime=?5 and isDeleted=0")
    List<CourseSchedule> findCourseScheduleListByPlaceId(String schoolId,String searchAcadyear,
			Integer searchSemester, String placeId, Integer week);
     
    @Query("select a from CourseSchedule a, DateInfo b where a.schoolId = ?1 and a.acadyear = ?2 and a.semester = ?3 "
    		+ " and b.schoolId = a.schoolId and a.placeId = ?4 and a.dayOfWeek = (b.weekday - 1) "
    		+ "and a.weekOfWorktime = b.week and to_char(b.infoDate, 'yyyyMMdd') >= ?5 and to_char(b.infoDate, 'yyyyMMdd') <= ?6 and a.isDeleted=0")
//    @Query("From CourseSchedule where acadyear = ?2 and semester = ?3 and schoolId = ?1 and placeId = ?4 and weekOfWorktime=?3")
    List<CourseSchedule> findCourseScheduleListByPlaceId(String schoolId,String searchAcadyear,
    		Integer searchSemester, String placeId, String startDate, String endDate);
    @Query("From CourseSchedule where schoolId=?1 and acadyear = ?2 and semester = ?3 and classId in (?4)"
    		+ " and subjectId in (?5)and isDeleted=0")
	List<CourseSchedule> findCourseScheduleListByClassIdsAndSubjectIds(String schoolId, String acadyear, Integer semester, String[] classIds, String[] subjectIds);
    
    @Query("From CourseSchedule where schoolId=?1 and acadyear = ?2 and semester = ?3 and classId = ?4"
    		+ " and subjectId = ?5 and teacherId=?6 and isDeleted=0")
	List<CourseSchedule> findByClassAndSubjectTeacher(String schoolId, String acadyear,
			Integer semester, String classId, String subjectId, String teacherId);
	
    @Modifying
    @Query("update CourseSchedule set isDeleted=1 where courseId in (?1) ")
	void deleteByCourseIdIn(String[] courseIds);

    @Query("From CourseSchedule where schoolId=?1 and acadyear = ?2 and semester = ?3 and teacherId in (?4) and isDeleted=0")
    List<CourseSchedule> findListByTeacherIdsIn(String schoolId, String acadyear, Integer semester, String[] teacherIds);
    @Query("From CourseSchedule where schoolId=?1 and acadyear = ?2 and semester = ?3 and teacherId in (?4) "
    		+ "and weekOfWorktime>= ?5 and isDeleted=0")
    List<CourseSchedule> findListByTeacherIdsInWeekGte(String schoolId, String acadyear, Integer semester, String[] teacherIds, Integer week);
    
    @Modifying
    @Query("update CourseSchedule set isDeleted=1 where classId in (?1) ")
	void deleteByClassIdIn(String... classIds);
    
    
    @Modifying
    @Query("update CourseSchedule set isDeleted=1 where teacherId in (?1) ")
	void deleteByTeacherIdIn(String... teacherIds);
    
    @Modifying
    @Query("update CourseSchedule set isDeleted=1 where exists(select 1 from TeachClass tc where tc.id=classId and tc.gradeId= (?1))")
	void deleteByGradeIds(String... gradeIds);
    
    @Modifying
    @Query("update CourseSchedule set isDeleted=1 where subjectId in (?1) ")
	void deleteBySubjectIdIn(String... subjectIds);
	
	/**
	 * 本班级所有老师 在指定周次的所有课程,包括老师在 其他班级的课程
	 * @param schoolId
	 * @param weekOfWorktime
	 * @param classId
	 * @return
	 */
	@Query("From CourseSchedule where isDeleted=0 and schoolId=?3 and acadyear = ?1 and semester = ?2 and weekOfWorktime in (?4) and teacherId in (" + 
			"select distinct teacherId from CourseSchedule where schoolId =?3 and acadyear = ?1 and semester = ?2"
			+ " and classId=?5 and weekOfWorktime in (?4) and teacherId is not null and isDeleted=0)")
	List<CourseSchedule> findTeacherScheduleByCondition(String acadyear, Integer semester, String schoolId, Integer[] weeks, String classId);
	/**
	 * 删除某班级指定周次的 课程
	 * @param searchAcadyear
	 * @param searchSemester
	 * @param schoolId
	 * @param array
	 * @param classId
	 */
	@Modifying
	@Query("update CourseSchedule set isDeleted=1 where schoolId =?3 and acadyear = ?1 and semester = ?2" + 
			" and classId=?5 and weekOfWorktime in (?4)")
	void deleteByWeekAndClass(String acadyear, Integer semester, String schoolId, Integer[] weeks,
			String classId);
	/**
	 * 某班级 最小的周次
	 * @param searchAcadyear
	 * @param searchSemester
	 * @param classId
	 * @return
	 */
	@Query("select min(weekOfWorktime) From CourseSchedule where isDeleted=0 and acadyear = ?1 and semester = ?2 and classId=?3")
	Integer findMinWeek(String acadyear, Integer semester, String classId);
	
	@Query("From CourseSchedule where isDeleted=0 and id in ?1")
	List<CourseSchedule> findByIds(String[] ids);
	
	/**
	 * 获取某年级 每个行政班的 课时数；不包含虚拟课程
	 * @param unitId
	 * @param gradeId
	 * @param acadyear
	 * @param semester
	 * @param weekIndex
	 * @return
	 */
	@Query(nativeQuery=true, value="select class_id,count(id) num2 from base_course_schedule where school_Id=?1 and acadyear=?3 "
			+ "and semester=?4 and is_deleted=0 and week_of_worktime=?5 and subject_type!='3' and class_id "
			+ "in(select id from base_class where grade_id in (?2) and is_deleted=0 and is_Graduate=0) "
			+ "group by class_id")
	List<Object[]> getClassRealHour(String unitId, String[] gradeIds, String acadyear, String semester,
			int weekIndex);
	
	@Query("From CourseSchedule where acadyear = ?1 and semester = ?2 and classId = ?3 and weekOfWorktime in (?4) "
			+ "and dayOfWeek||'-'||periodInterval||'-'||period in (?5) and isDeleted=0")
	List<CourseSchedule> findClassScheduleByPeriod(String acadyear, Integer semester, String classId,
			Integer[] weeks, String[] periods);
}
