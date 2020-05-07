package net.zdsoft.basedata.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.dto.CourseScheduleDto;
import net.zdsoft.basedata.entity.ClassHour;
import net.zdsoft.basedata.entity.ClassHourEx;
import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.entity.ClassTeachingEx;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.GradeTeaching;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.TeachPlan;
import net.zdsoft.basedata.entity.TeachPlanEx;
import net.zdsoft.framework.entity.Pagination;

public interface CourseScheduleService extends
		BaseService<CourseSchedule, String> {
	/**
     * @param searchAcadyear
     * @param searchSemester
     * @param classId
     * @return
     */
    List<CourseSchedule> findCourseScheduleListByClassId(String searchAcadyear, Integer searchSemester,
            String classId, Integer week);
    /**
     * @param searchAcadyear
     * @param searchSemester
     * @param teacherId
     * @return
     */
    List<CourseSchedule> findCourseScheduleListByTeacherId(String searchAcadyear, Integer searchSemester,
            String teacherId, Integer week);
    /**
     * school_id   acadyear  semester bitian
     * @param dto
     * @return
     */
    public List<CourseSchedule> getByCourseScheduleDto(CourseScheduleDto dto);

	public void deleteByIds(String[] ids);

	public List<CourseSchedule> saveAllEntitys(CourseSchedule... cs);

	public List<CourseSchedule> findCourseScheduleListByClassIdes(String searchAcadyear,
			Integer searchSemester, String[] classId, Integer week);
	
	/**
	 * 
	 * @param searchAcadyear
	 * @param searchSemester
	 * @param classId
	 * @param week
	 * @param showXN 不传 就是根据条件全拿 1:虚拟科目，不包括关联虚拟科目的教学班 2：不拿虚拟科目，拿关联虚拟科目的教学班
	 * @return
	 */
	public List<CourseSchedule> findCourseScheduleListByClassIdes(String searchAcadyear,
			Integer searchSemester, String[] classId, Integer week,String showXN);
	
	/**
	 * 根据老师或者学生获取个人课表
	 * @param searchAcadyear 必填
	 * @param searchSemester 必填
	 * @param perId 必填(教师id或学生id)
	 * @param type 1:教师，2：学生
	 * @param week那周课程表，根据节假日推算出来
	 * @return className,subjectName,placeName内容已经填充
	 */
	public List<CourseSchedule> findCourseScheduleListByPerId(String searchAcadyear,Integer searchSemester, Integer week, String perId,String type);
	
	public List<CourseSchedule> findCourseScheduleListByPerId(String searchAcadyear,Integer searchSemester, String startDate, String endDaste, String perId,String type);
	/**
	 * 根据课程Id获取课表信息
	 * @param schoolId
	 * @param acadyear
	 * @param semester
	 * @param subjectId
	 * @return
	 */
	public List<CourseSchedule> findCourseScheduleListBySubjectId(String schoolId, String acadyear,
			Integer semester, String subjectId);
	/**
	 * 调用下面带周次精确写
	 * @param acadyear
	 * @param semester
	 * @param type
	 * @param ownerId
	 * @return
	 */
	@Deprecated
	public Map<String,Set<String>> findCourseScheduleMapByObjId(String acadyear, Integer semester,String type, String ownerId);
	public Map<String,Set<String>> findCourseScheduleMapByObjId(String acadyear,Integer semester,String type,Integer week,String ownerId);
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
	List<CourseSchedule> findCourseScheduleList(String searchAcadyear, Integer searchSemester,
            String schoolId, Integer weekOfWorktime,Integer dayOfWeek,String periodInterval,Integer punchCard,Integer period);
	public Map<String,Set<String>> findCourseScheduleMap(String schoolId, String acadyear,
			Integer semester);
	/**
	 * 
	 * @param dto 学年学期学校周次段
	 * @param timeStr (星期_时间段_节次) 不能为空
	 * @return 
	 */
	List<CourseSchedule> findByTimes(CourseScheduleDto dto, String[] timeStr);
	/**
	 * 组装教师
	 * @param list
	 */
	void makeTeacherSet(List<CourseSchedule> list);
	/**
	 * 删除某段时间内的班级课表  暂时周次不为0时 星期参数才起作用
	 * @param deleteDto
	 */
	void deleteByClassId(CourseScheduleDto deleteDto);
	
	List<CourseSchedule> findCourseScheduleListByPlaceId(String schoolId,String searchAcadyear,
			Integer searchSemester, String placeId, Integer week,boolean ismakeTeacher);
	
	List<CourseSchedule> findCourseScheduleListByPlaceId(String schoolId,String searchAcadyear,
			Integer searchSemester, String placeId, String startDate, String endDate,boolean ismakeTeacher);
	List<CourseSchedule> findBySchoolIdAndGradeIdAndSubjectId(String schoolId,String acadyear,
			Integer semester,String gradeId, String subjectId);
	
	List<CourseSchedule> findCourseScheduleListByTeacherIdIn(String searchAcadyear, Integer searchSemester,Integer week,String[] teacherIds);
	
	List<CourseSchedule> findCourseScheduleListByClassIdsAndSubjectIds(String schoolId, String acadyear, Integer semester, String[] classIds, String[] subjectIds);
	
	/**
	 * 删除当前时间之后该学期内所选班级科目的课表
	 * @param schoolId
	 * @param acadyear
	 * @param semester
	 * @param classIds
	 * @param subjectIds
	 * @return 报错信息
	 */
	String deleteByClassIdAndSubjectId(String schoolId,String acadyear,String semester,String[] classIds,String[] subjectIds);
	
	/**
	 * 查询多个老师的课程表map
	 * @param acadyear
	 * @param semester
	 * @param teacherIds
	 * @param week
	 * @return
	 */
	Map<String, List<CourseSchedule>> findCourseScheduleMapByTeacherId(String acadyear, String semester, String[] teacherIds, Integer week);
	
	/**
	 * 查询老师在某班某门课的课程表
	 * @param schoolId
	 * @param acadyear
	 * @param semester
	 * @param classId
	 * @param subjectId
	 * @param teacherId
	 * @return
	 */
	List<CourseSchedule> findByClassIdAndSubjectIdAndTeacherId(String schoolId, String acadyear, Integer semester, String classId, String subjectId, String teacherId);
	
	void saveClassScheduleImport(CourseScheduleDto dto,List<ClassTeaching>ctlist, List<ClassTeachingEx> ctexlist, 
			List<TeachPlan> tplist,List<TeachPlanEx> tpexlist,List<CourseSchedule> cslist, List<GradeTeaching> gtList,
			List<ClassHour> chList, List<ClassHourEx> cheList, List<TeachClass> tcList);

    List<CourseSchedule> findListByTeacherIdsIn(String schoolId, String acadyear, Integer semester, String[] teacherIds);
    /**
     * 获取 教师在 某一周 之后的课程
     * @param schoolId
     * @param acadyear
     * @param semester
     * @param teacherIds
     * @return
     */
    List<CourseSchedule> findListByTeacherIdsInWeekGte(String schoolId, String acadyear, Integer semester, String[] teacherIds, Integer week);
    
    void makeScheduleInfo( List<CourseSchedule> timetableCourseScheduleList,String type);
    
    /**
     * 删除班级后删除相应课程表
     * @param classIds
     */
	void deleteByClassIds(String... classIds);
	
	/**
	 * 删除老师后删除相应课程表中teacherId
	 * @param teacherIds
	 */
	void deleteByTeacherIds(String... teacherIds);
	
	/**
	 * 删除年级后删除年级下所有教学班的课程表
	 * @param gradeIds
	 */
	void deleteTeachClassScheduleByGradeIds(String... gradeIds);
	
	/**
	 * 删除课程后删除相应课程表
	 * @param subjectIds
	 */
	void deleteBySubjectIds(String... subjectIds);
	
	void saveTeacherScheduleImport(List<ClassTeaching> updateClassTeachingList, List<CourseSchedule> updateCourseScheduleList, List<TeachPlan> updateTeachPlanList,
			List<TeachPlanEx> newTeachPlanExList, List<ClassTeachingEx> newClassTeachingExList, List<TeachPlanEx> deleteTeachPlanExList, List<ClassTeachingEx> deleteClassTeachingExList);
	
	/**
	 * 复制课程表
	 * @param gradeId
	 * @param isCopySchedule
	 * @param srcAcadyear
	 * @param srcSemester
	 * @param srcWeek
	 * @param destAcadyear
	 * @param destSemester
	 * @param isCover
	 * @param operatorId 
	 * @return
	 */
	String saveCopy(String gradeId, String isCopySchedule, String srcAcadyear, String srcSemester, String srcWeek, String destAcadyear, String destSemester, String isCover, String operatorId);

    List<CourseSchedule> findByModifyTime(String[] schoolId,String acadyear, Integer semester, Date modifyTime,
			Pagination page);
    /**
     * 将此行政班级 本周 所有的课 覆盖到后面的周次
     * @param classId
     * @param weekOfWorktime
     * @return
     */
	String saveCoverAll(String searchAcadyear, Integer searchSemester, String schoolId, String classId, int weekOfWorktime);
	
	void updateClassTeaching(String classTeachingId, List<String> delTeacherIds, List<String> timetableIds, List<ClassTeaching> classTeachingList, List<ClassTeachingEx> classTeachingExList,List<CourseSchedule> courseScheduleList, List<TeachPlanEx> teachPlanExList);
	
	void deleteByCourseIdIn(String[] courseIds);
	
	void saveScheduleModify(List<CourseSchedule> insertList, List<String> delIds);
	
	/**
	 * 获取某年级 每个行政班的 课时数；不包含虚拟课程
	 * @param unitId
	 * @param gradeIds
	 * @param acadyear
	 * @param semester
	 * @param weekIndex
	 * @return
	 */
	Map<String, Integer> getClassRealHour(String unitId, String[] gradeIds, String acadyear, String semester,
                                          int weekIndex);
	
	/**
	 * 还原从 临时表获取的备份 课表数据
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param classIdList
	 * @param savedList
	 */
	void saveRevertSchedule(String unitId, String acadyear, String semester, List<String> classIdList,
			List<CourseSchedule> savedList);
	/**
	 * 
	 * @param searchAcadyear
	 * @param searchSemester
	 * @param classId
	 * @param weekList
	 * @param dfTimes 时间 集合 ，表达形式 ： dayOfWeek-periodInterval-period 
	 * @return
	 */
	List<CourseSchedule> findClassScheduleByPeriod(String searchAcadyear, Integer searchSemester, String classId,
			List<Integer> weekList, List<String> dfTimes);

	
	/**
	 * @param dto 学年学期学校
	 * @param timeStr (周次_星期_时间段_节次) 不能为空
	 * @return 
	 */
	List<CourseSchedule> findByWeekTimes(CourseScheduleDto dto, String[] timeStr);

	/**
	 * 获取 年级 使用的所有场地
	 * @param schoolId
	 * @param searchAcadyear
	 * @param searchSemester
	 * @param weekIndex TODO
	 * @return
	 */
	List<String> findAllPlaceIds(String schoolId, String searchAcadyear, Integer searchSemester, Integer weekIndex, String gradeId);

	/**
	 * 按照班级 更新 课表的 考勤状态
	 * @param schoolId
	 * @param acadyear
	 * @param semester
	 * @param classId
	 * @param subStatus k:subId v:punchCard
	 */
	void updatePunchCard(String schoolId, String acadyear, Integer semester,
						 String classId, Map<String, Integer> subStatus);
}
