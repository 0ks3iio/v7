package net.zdsoft.basedata.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.dto.TimetableChangeDto;
import net.zdsoft.basedata.entity.TipsayEx;

public interface TipsayExService extends BaseService<TipsayEx, String>{

	/**
	 * 代管月统计查询
	 * @param schoolId
	 * @param acadyear
	 * @param semester
	 * @param dates
	 * @param teacherName
	 * @return
	 */
	List<TimetableChangeDto> findDtoListByMonthCondition(String schoolId, String acadyear, Integer semester, String[] dates, String teacherName);

	/**
	 * 调代管统计查询
	 * @param schoolId
	 * @param acadyear
	 * @param semester
	 * @param dates
	 * @param type
	 * @param dateWeekMap
	 * @return
	 */
	List<TimetableChangeDto> findDtoListByDayCondition(String schoolId, String acadyear, Integer semester, String[] dates, String type, Map<String,String> dateWeekMap);

    void deleteByTipsayIds(String[] adjustedIds);

    void updateAuditorIdAndStateByAdjustId(String adjustedId, String state, String teacherId);

    /**
     * @param teacherIds
     */
	void deleteByTeacherIds(String... teacherIds);

	/**
	 * 删除班级后删除相应调代课记录
	 * @param classIds
	 */
	void deleteByClassIds(String... classIds);

	/**
	 * 删除课程后删除相应调代课记录
	 * @param subjectIds
	 */
	void deleteBySubjectIds(String... subjectIds);
	
	/**
	 * 查询教师本周调代课数(定制)
	 * 调课数（鹰硕）=调课数（万朋），代课数（鹰硕）=代课数+代管数（万朋）
	 * @param acadyear
	 * @param semester
	 * @param teacherId
	 * @return courseNum-本周课时数，takeNum-代课数，beTakeNum-被代课数，adjustNum-调课数
	 */
    String findByAcadyearAndSemesterAndTeacherId(String acadyear, Integer semester, String teacherId);

    /**
     * 调课单导出
     * @param unitId
     * @param acadyear
     * @param semester
     * @param adjustedIds
     * @return
     */
	List<TimetableChangeDto> findDtoListByExportCondition(String unitId, String acadyear, Integer semester, String[] adjustedIds);
}
