package net.zdsoft.basedata.service;

import java.util.List;

import net.zdsoft.basedata.entity.Adjusted;
import net.zdsoft.basedata.entity.AdjustedDetail;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.basedata.entity.TipsayEx;

public interface AdjustedService extends BaseService<Adjusted, String> {

    void saveAllAdjust(List<Adjusted> adjustedList, List<AdjustedDetail> adjustedDetailList, List<TipsayEx> tipsayExList);
    /**
     * 
     * @param acadyear
     * @param semester
     * @param teacherId
     * @param week 可为空
     * @return
     */
    List<Adjusted> findListByTeacherIdAndWeek(String acadyear, String semester, String teacherId, String week);

    void deleteById(String adjustedId);
    void deleteByIds(String[] adjustedIds);
    /**
     * 
     * @param acadyear
     * @param semester
     * @param unitId
     * @param week 可为空
     * @return
     */
    List<Adjusted> findListBySchoolIdAndWeek(String acadyear, String semester, String unitId, String week);

    String updateStateById(String adjustedId, String state, String teacherId);

	void saveAll(Adjusted adjusted, AdjustedDetail[] adjustedDetails, TipsayEx tipsayEx, List<CourseSchedule> courseSchedules, List<String> delOtherIds);

    /**
     * 需传入更改后的CourseSchedule
     * 被调课程可为空
     * 在传入前请判断原有教师是否已发生改变
     * @param adjustId
     * @param adjustedCourseSchedule
     * @param beenAdjustedCourseSchedule
     * @return
     */
	void updateRollBack(String adjustId, CourseSchedule adjustedCourseSchedule, CourseSchedule beenAdjustedCourseSchedule);

    void saveAllArrange(List<Adjusted> adjustedList, List<AdjustedDetail> adjustedDetailList, List<TipsayEx> tipsayExList, CourseSchedule[] courseSchedules);
    /**
     * 删除班级后删除相应调课记录
     * @param classId
     */
	void deleteByClassId(String classId);
	/**
	 * 删除班级 在指定周次的调课信息
	 * @param searchAcadyear
	 * @param searchSemester
	 * @param schoolId
	 * @param array
	 * @param classId
	 */
	void deleteByWeeks(String searchAcadyear, Integer searchSemester, String schoolId, Integer[] array, String classId);
	
	
}
