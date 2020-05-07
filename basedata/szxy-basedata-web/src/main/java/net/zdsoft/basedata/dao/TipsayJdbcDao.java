package net.zdsoft.basedata.dao;

import java.util.List;

import net.zdsoft.basedata.entity.Tipsay;

public interface TipsayJdbcDao {
	List<Tipsay> findByCondition(String schoolId, String acadyear, Integer semester, String[] dates, String type, String[] teacherIds);
}
