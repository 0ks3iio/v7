package net.zdsoft.basedata.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.entity.AdjustedDetail;

public interface AdjustedDetailService extends BaseService<AdjustedDetail, String> {

	/**
	 * 根据条件查询调课记录
	 * @param schoolId
	 * @param acadyear
	 * @param semester
	 * @param dates
	 * @param teacherIds
	 * @return
	 */
	List<AdjustedDetail> findByCondition(String schoolId, String acadyear, Integer semester, String[] dates, String[] teacherIds);

    List<AdjustedDetail> findListByAdjustedIds(String[] adjustIds);

	/**
	 * 查询调课和被调课id对应map
	 * @param adjustedIds
	 * @return
	 */
	Map<String, String> findMapByAdjustedIdIn(String[] adjustedIds);

    /**
     * 查询调课和被调课courseScheduleId对应map
     * 调课为key，被调为value
     * @param adjustedIds
     * @return
     */
    Map<String, String> findCourseScheduleIdMapByAdjustedIdIn(String[] adjustedIds);

    void deleteByAdjustedIds(String[] adjustedIds);

    /**
     * 删除老师后清空调课表中的teacherId
     * @param teacherId
     */
	void deleteByTeacherIds(String... teacherIds);

	/**
	 * 删除班级后删除相应调课表中的记录
	 * @param classIds
	 */
	void deleteByClassIds(String... classIds);
}
