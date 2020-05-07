package net.zdsoft.basedata.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.dto.CourseScheduleDto;
import net.zdsoft.basedata.entity.CourseSchedule;

public interface CourseScheduleJdbcDao {
	public List<CourseSchedule> getByCourseScheduleDto(CourseScheduleDto dto);

	public List<CourseSchedule> findCourseScheduleListByTeacherId(
			String searchAcadyear, Integer searchSemester, String teacherId,
			Integer week);
	
	public List<CourseSchedule> findCourseScheduleListByTeacherId(
			String searchAcadyear, Integer searchSemester, String teacherId,
			String startDate, String endDate);
	/**
	 * 查询作为辅助教师
	 * @param searchAcadyear
	 * @param searchSemester
	 * @param perId
	 * @param week
	 * @return
	 */
	public List<CourseSchedule> findCourseScheduleListByFuTeacherId(
			String searchAcadyear, Integer searchSemester, String teacherId,
			Integer week);
	/**
	 * 获取课表信息
	 * @param searchAcadyear
	 * @param searchSemester
	 * @param teacherId
	 * @param startDate，开始日期（yyyyMMdd）
	 * @param endDate，结束日期（yyyyMMdd）
	 * @return
	 */
	public List<CourseSchedule> findCourseScheduleListByFuTeacherId(
			String searchAcadyear, Integer searchSemester, String teacherId, String startDate, String endDate);
	/**
	 * 
	 * @param dto
	 * @param timeStr 不能为空
	 * @return 
	 */
	public List<CourseSchedule> findByTimes(CourseScheduleDto dto,
			String[] timeStr);
	/**
	 * 
	 * @param courseScheduleId
	 * @return key:courseScheduleId value:辅助教师
	 */
	public Map<String,Set<String>> findTeacherIds(String[] courseScheduleId);

	public void deleteByClassId(CourseScheduleDto deleteDto);

	public List<CourseSchedule> findBySchoolIdAndGradeIdAndSubjectId(String schoolId,String acadyear,
			Integer semester,String gradeId, String subjectId);

	public List<CourseSchedule> findCourseScheduleListByTeacherIdIn(String searchAcadyear, Integer searchSemester, Integer week, String[] teacherIds);

	public Map<String, List<CourseSchedule>> findCourseScheduleMapByFuTeacherId(String acadyear, String semester, String[] teacherIds, Integer week);

	public List<CourseSchedule> findCourseScheduleListByClassIdes(String acadyear, Integer semester,
			String[] classId, Integer week, String showXN);


	public List<CourseSchedule> findByWeekTimes(CourseScheduleDto dto, String[] timeStr);

	/**
	 * 获取年级用到的 所有场地
	 * @param schoolId
	 * @param acadyear
	 * @param semester
	 * @param week
	 * @param gradeId
	 * @return
	 */
	List<String> findAllPlaceIds(String schoolId, String acadyear, Integer semester, Integer week, String gradeId);

}
