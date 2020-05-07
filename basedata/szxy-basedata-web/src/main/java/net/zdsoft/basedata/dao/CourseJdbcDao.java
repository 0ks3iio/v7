package net.zdsoft.basedata.dao;

import java.util.List;

import net.zdsoft.basedata.entity.Course;

public interface CourseJdbcDao {
	/**
	 * 建议调用下面方法 getListByCondition
	 * @param unitIds
	 * @param type
	 * @param subjName
	 * @param courseTypeId
	 * @param section
	 * @param isUsing
	 * @param subjectType
	 * @return
	 */
	@Deprecated
	public List<Course> getCourseBySection(String[] unitIds,String type,String subjName, String courseTypeId,String section,Integer isUsing,String subjectType);
	public List<Course> getListByCondition(String[] unitIds,String[] type,String subjName,String courseTypeId,String section,Integer isUsing, String subjectType);
	public void updateIdAndUnitId(List<String[]> courseList);
}
