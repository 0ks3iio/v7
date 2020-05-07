package net.zdsoft.basedata.dao;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.entity.AdjustedDetail;

public interface AdjustedDetailJdbcDao {
	List<AdjustedDetail> findByCondition(String schoolId, String acadyear, Integer semester, String[] dates, String[] teacherIds);

	Map<String, String> findMapByAdjustedIdIn(String[] adjustedIds);

    Map<String, String> findCourseScheduleIdMapByAdjustedIdIn(String[] adjustedIds);
}
