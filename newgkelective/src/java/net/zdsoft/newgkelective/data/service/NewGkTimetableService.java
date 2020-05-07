package net.zdsoft.newgkelective.data.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.dto.CourseScheduleDto;
import net.zdsoft.basedata.dto.CourseScheduleModifyDto;
import net.zdsoft.basedata.dto.TreeNodeDto;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.dto.ArrayResultSubjectDto;
import net.zdsoft.newgkelective.data.dto.ArraySearchDto;
import net.zdsoft.newgkelective.data.dto.NewGkArrayResultSaveDto;
import net.zdsoft.newgkelective.data.entity.*;

public interface NewGkTimetableService extends BaseService<NewGkTimetable, String>{

	List<NewGkTimetable> findByArrayId(String unitId, String arrayId);
	List<NewGkTimetable> findByArrayIdWithMaster(String unitId, String arrayId);

	void saveAll(String unitId, String divideId, NewGkArrayResultSaveDto dto);

	void saveCopyArray(NewGkArrayResultSaveDto dto);
	
	NewGkTimetable findbySubIdAndClaId(String unitId, String subjectId,String classId);
	/**
	 * 组装时间
	 * @param arrayId 
	 * @param list
	 */
	void makeTime(String unitId, String arrayId, List<NewGkTimetable> list);

	List<NewGkTimetable> findByArrayIdAndClassType(String unitId, String arrayId,
			String[] classTypes);

	List<NewGkTimetable> findByArrayIdAndClassId(String unitId, String arrayId, String classId);
	
	List<ArrayResultSubjectDto> findByMoreConditions(String unitId,String arrayId, ArraySearchDto searchDto);
	/**
	 * 组装教师
	 * @param timetableList
	 */
	void makeTeacher(List<NewGkTimetable> timetableList);

	List<NewGkTimetable> findByArrayIdAndIdIn(String unitId, String arrayId,
			String[] array);

	List<NewGkTimetable> findByArrayIdAndClassIds(String unitId, String arrayId, String[] classIds);

	List<NewGkTimetable> findByArrayIdAndSubjectId(String unitId, String arrayId,String[] subjectIds);
	/**
	 * 相应的扩张表都删除
	 * @param arrayId
	 */
	void deleteByArrayId(String arrayId);

	/**
	 * 获取一个行政班级的课表 以及 课程情况 （课时 单双周等）
	 * @param arrayId
	 * @param classId
	 * @return
	 */
	CourseScheduleModifyDto findClassScheduleInfo(String arrayId, String classId);
	
	CourseScheduleModifyDto findClassScheduleInfoWithMaster(String arrayId, String classId);

	/**
	 * 获取指定教师的课程表
	 * @param arrayId
	 * @param techerIds
	 * @return
	 */
	List<CourseSchedule> makeScheduleByTeacher(String arrayId, Set<String> techerIds);

	/**
	 * 当更改场地时 ，改变对应预排课程中的场地
	 * @param arrayId
	 * @param placeItemList
	 * @param classTypes
	 */
	void updateTimetablePlaces(String arrayId, List<NewGkPlaceItem> placeItemList, String[] classTypes);
	
	/**
	 * 当改变教师安排时，修改对应预排课表中的教师
	 * @param arrayId
	 * @param subjectId
	 * @param teacherPlanExList
	 */
	void updateTimetableTeachers(String arrayId, String subjectId, List<NewGkTeacherPlanEx> teacherPlanExList);

	/**
	 * 重新生成预排课表，目前只在保存排课方案时用到
	 * @param arrayId
	 */
	void updatePreTimetable(String arrayId,List<NewGkDivideClass> allClassList );
	/**
	 * 根据排课特征 更新预排课表中对应的教师
	 * @param arrayId
	 * @param subjectIdList 当更新部分科目的教师时，填写具体科目。当更新所有科目的教师时 留空即可
	 */
	void updateTimetableAllTeachers(String arrayId, List<String> subjectIdList);

	/**
	 * 保存年级特征时更新预排课表
	 * @param arrayId
	 * @param newGradeTimeList
	 */
	void updateTimetableByGradeFea(String arrayId, List<NewGkLessonTime> newGradeTimeList);

	/**
	 * 保存课表设置时 更新预排课表
	 * @param arrayId
	 */
	void updatePreTimetableByBatch(String arrayId);

	/**
	 * 删除课程特征时 同时删除 对应排课方案的 科目课表
	 * @param arrayId
	 * @param subjctIds
	 */
    void deleteScheduleBySubIds(String arrayId, String[] subjctIdTypes);

    /**
	 * 保存课程特征时 更新 在组合班上课的 行政班课程
	 * @param arrayId
	 * @param subjectTimeList
	 */
	void updateByCouseFeature(String arrayId, List<NewGkSubjectTime> subjectTimeList);
	/**
	 * 获取每个行政班  与走班批次时间点冲突的时间点  
	 * @param arrayId
	 * @param teacherMoveTimeMap TODO
	 * @param placeMoveTimeMap TODO
	 * @return key:classId  V:时间点集合 format:1_2_3
	 */
	Map<String, List<String>> findClassMovePeriodMap(String arrayId, Map<String, Set<String>> teacherMoveTimeMap, Map<String, Set<String>> placeMoveTimeMap);

	/**
	 * 获取一个行政班班级的课表信息
	 * @param unitId
	 * @param arrayId
	 * @param classId
	 * @return
	 */
	List<CourseSchedule> findScheduleByClass(String unitId, String arrayId, String[] classId);
	
	/**
	 * 获取一个教师的课程表信息 以及 课程情况 （课时 单双周等）
	 * @param arrayId
	 * @param objId
	 * @return
	 */
	CourseScheduleModifyDto findTeacherScheduleInfo(String arrayId, String objId);
	CourseScheduleModifyDto findTeacherScheduleInfoWithMaster(String arrayId, String objId);

    // Basedata Sync Method
    void deleteBySubjectIds(String... subjectIds);

    // Basedata Sync Method
    void deleteByClassIds(String... classIds);
    /**
     * 获取一个 排课方案的冲突数 并且组装树结构
     * @param arrayId
     * @param dtoList
     * @return
     */
	int getConflictNum(String arrayId, List<TreeNodeDto> dtoList);
	int getConflictNumWithMaster(String arrayId, List<TreeNodeDto> dtoList);
	 /**
     * 获取参数 为getLastMap以及getTree
     * @param arrayId
     * @param classNameMap
     * @param courseNameMap
     * @param timetableMap
     * @param lastPlaceMap
     * @param lastStudentMap
     */
	void getIndex(String arrayId, Map<String, String> classNameMap, Map<String, String> courseNameMap,
			
			Map<String, NewGkTimetable> timetableMap, Map<String, List<NewGkTimetableOther>> lastPlaceMap,
			Map<String, List<NewGkTimetableOther>> lastStudentMap);
	/**
	 * 根据班级id 或者 timetableId 组装课表信息，包括 科目名称 教师 名称，场地名称等<br>
	 * 优先使用timetableIds， 当 timetableIds 为空时 再使用allClazzIds 来获取课表信息
	 * @param unitId
	 * @param arrayId
	 * @param allClazzIds
	 * @param timetableIdes 
	 * @param courseList 传一个空的容器，用于接收课表中用到的课程 可为空
	 * @return
	 */
	List<NewGkTimetableOther> findArrayResultBy(String unitId, String arrayId, Set<String> allClazzIds, Set<String> timetableIds, List<Course> courseList);
	
	List<String> findIdByPlaceIds(String unitId, String arrayId, String[] placeIds);
	/**
	 * 组装场地 课表信息
	 * @param unitId
	 * @param arrayId
	 * @param placeIds
	 * @return
	 */
	List<CourseSchedule> makeScheduleByPlace(String unitId, String arrayId, Set<String> placeIds);
	
	/**
	 * 获取一个场地的 课表和 课时 信息
	 * @param arrayId
	 * @param placeId
	 * @return
	 */
	CourseScheduleModifyDto findPlaceScheduleInfo(String arrayId, String placeId);
	CourseScheduleModifyDto findPlaceScheduleInfoWithMaster(String arrayId, String placeId);

	/**
	 * 找出某次排课中存在冲突的教师 以及教师 冲突的 课程信息
	 * @param unitId
	 * @param arrayId
	 * @return
	 */
    Map<String,List<CourseScheduleDto>> findConflictTeaIds(String unitId, String arrayId);
}
