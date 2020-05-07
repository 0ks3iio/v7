package net.zdsoft.basedata.dao;

import java.util.Map;

public interface ClassJdbcDao {
	Map<String, Integer> countByGradeIds(String[] gradeIds);
}
