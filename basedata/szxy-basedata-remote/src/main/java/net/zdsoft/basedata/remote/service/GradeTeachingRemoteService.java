package net.zdsoft.basedata.remote.service;


import net.zdsoft.basedata.entity.GradeTeaching;

public interface GradeTeachingRemoteService extends BaseRemoteService<GradeTeaching,String>{
	/**
	 * 
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param gradeId
	 * @return  Map<String, GradeTeaching>
	 */
	public String findBySearchMap(String unitId, String acadyear, String semester,String gradeId);
	/**
	 * 
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param gradeId
	 * @param subjectType 选修 必修
	 * @return List<GradeTeaching>
	 */
	public String findBySearchList(String unitId, String acadyear, String semester,String gradeId,Integer subjectType);
	
	/**
	 * 根据年级选择，查询全部科目
	 * @param gradeId
	 * @return
	 */
	public String findCourseListByGradeId(String unitId,String acadyear,String semester,String... gradeId);
}
