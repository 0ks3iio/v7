package net.zdsoft.basedata.remote.service;


import java.util.Date;

import net.zdsoft.basedata.dto.CourseScheduleDto;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.framework.entity.Pagination;

public interface CourseScheduleRemoteService extends BaseRemoteService<CourseSchedule, String>{
	/**
	 * 找教师某一周的课表
	 * @param schoolId
	 * @param searchAcadyear
	 * @param searchSemester
	 * @param teacherId
	 * @param week
	 * @return List<CourseSchedule>
	 */
	public String findByTeacherId(String schoolId,String searchAcadyear, Integer searchSemester, String teacherId,Integer week);
	
	/**
	 * 找当前学年学期下，某一门课程的所有相关课表信息 ，若当前时间不在学年学期内返回空集合
	 * @return List<CourseSchedule>
	 */
	public String findBySubjectId(String schoolId,String subjectId);
	 /**
	  * 找班级某一周的课表
	 * @param schoolId
     * @param searchAcadyear
     * @param searchSemester
     * @param classId
     * @param week(为空:则查询某学年学期下班级课程)
     * @return List<CourseSchedule>
     */
	public String findByClassId(String schoolId,String searchAcadyear, Integer searchSemester,String classId, Integer week);
	
	/**
	  * 部分班级某一周的课表
	 * @param schoolId
    * @param searchAcadyear
    * @param searchSemester
    * @param classId
    * @return List<CourseSchedule>
    */
	public String findByClassIdes(String schoolId,String searchAcadyear, Integer searchSemester,String[] classId, Integer week);
	
	/**
	 * 找教师当前周的课表，若当前时间不在学年学期内返回空集合
	 * @param schoolId
	 * @param teacherId
	 * @return List<CourseSchedule>
	 */
	public String findByTeacherId(String schoolId, String teacherId);
	/**
	 * 找班级当前周的课表，若当前时间不在学年学期内返回空集合
	 * @param schoolId
	 * @param classId
	 * @return List<CourseSchedule>
	 */
	public String findByClassId(String schoolId, String classId);
	
	/**
     * school_id   acadyear  semester 必填
     * @param dto 
     * @return List<CourseSchedule>
     */
    public String getByCourseScheduleDto(CourseScheduleDto dto);
    
    public void deleteCourseByIds(String[] ids);

    /**
     * 批量保存
     * @param courseSchedule
     */
	public void saveAll(String courseSchedule);
	/**
	 * 
	 * @param acadyear 学年
	 * @param semester  学期
	 * @param week 周课表
	 * @param ownerId 教师 或者学生 id
	 * @param s  1:教师 2:学生
	 * @return List<CourseSchedule>   className,subjectName,placeName已经填充内容
	 */
	String findCourseScheduleListByPerId(String acadyear, Integer semester, Integer week, String ownerId, String s);
	
	
	String findCourseScheduleListByPerId(String acadyear, Integer semester, String startDate, String endDate, String ownerId, String s);
	
	/**
	 * 获取学年，学期下，教师（学生）的课程信息
	 * @param acadyear
	 * @param semester
	 * @param type 1教师2学生
	 * @param ownerId 对象id(teacherId/studentId)
	 * @return Map<String,Set<String>> 
	 * 				学生：key: subjectId,value:Set<TeacherId>  
	 * 				教师：key: classId+","+classType,value:Set<subjectId>
	 * 调用下面方法findCourseScheduleMapByObjId(String acadyear,Integer semester,String type,Integer week,String ownerId);
	 */
	@Deprecated
	public String findCourseScheduleMapByObjId(String acadyear,Integer semester,String type,String ownerId);
	public String findCourseScheduleMapByObjId(String acadyear,Integer semester,String type,Integer week,String ownerId);
	
	/**
	 * 获取学年学期下，学生的课程信息
	 * @return Map<stuId,Set<subId,teacherId>>
	 */
	public String findCourseScheduleMap(String schoolId,String acadyear,Integer semester);
	/**
	 * 电子班排接口
	 * @param searchAcadyear
	 * @param searchSemester
	 * @param schoolId
	 * @param weekOfWorktime
	 * @param dayOfWeek
	 * @param periodInterval
	 * @param punchCard
	 * @return
	 */
	public String findCourseScheduleList(String searchAcadyear, Integer searchSemester,
            String schoolId, Integer weekOfWorktime,Integer dayOfWeek,String periodInterval,Integer punchCard,Integer period);
	
	/**
	 * 删除某段时间内的班级课表  暂时周次不为0时 星期参数才起作用
	 * @param deleteDto
	 */
	public void deleteByClassId(CourseScheduleDto deleteDto);
	
	
	/**
	 * 查询场地某一周的课表
	 * @param schoolId
	 * @param searchAcadyear
	 * @param searchSemester
	 * @param placeId
	 * @param week
	 * @param ismakeTeacher是否辅助教师数据
	 * @return List<CourseSchedule>
	 */
	public String findByPlaceId(String schoolId,String searchAcadyear, Integer searchSemester, String placeId,Integer week,boolean ismakeTeacher);
	
	public String findByPlaceId(String schoolId,String searchAcadyear, Integer searchSemester, String placeId,String startDate, String endDate,boolean ismakeTeacher);
	
	/**
	 * 根据年级 学科(2者可选可不选)，查询老师
	 * @param gradeId
	 * @param subjectId
	 * @return
	 */
	public String findTeachersByGradeIdAndSubjectId(String schoolId,String acadyear,
			Integer semester,String gradeId, String subjectId);
	
	/**
	 * 找部分教师某一周的课表
	 * @param searchAcadyear
	 * @param searchSemester
	 * @param week
	 * @param teacherIds
	 * @return
	 */
	public String findCourseScheduleListByTeacherIdIn(String searchAcadyear, Integer searchSemester,Integer week,String[] teacherIds);
	
	/**
	 * 查询多个老师的课程表map
	 * @param acadyear
	 * @param semester
	 * @param teacherIds
	 * @param week
	 * @return
	 */
	public String findCourseScheduleMapByTeacherId(String acadyear, String semester, String[] teacherIds, Integer week);
	 /**
     * 获取 教师在 某一周 之后的课程
     * @param schoolId
     * @param acadyear
     * @param semester
     * @param teacherIds
     * @return
     */
	public String findByTeacherIdsInWeekGte(String schoolId, String acadyear, Integer semester,
			String[] teacherIds, Integer week);

	public String findByModifyTime(String[] schoolId,String acadyear, Integer semester, Date modifyTime, Pagination page);
	
}
