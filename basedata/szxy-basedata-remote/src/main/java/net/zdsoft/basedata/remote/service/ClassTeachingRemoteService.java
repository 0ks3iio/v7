package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.ClassTeaching;

public interface ClassTeachingRemoteService extends BaseRemoteService<ClassTeaching,String> {

	/**
	 * 获取班级任课信息，并组装成Map
	 * 
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param subjectIds
	 * @return Map&lt;&lt;subjectId, Map&lt;classId, ClassTeaching&gt;&gt;
	 */
	public String findByCourseIdsMap(String unitId, String acadyear, String semester, String... subjectIds);

	/**
	 * 获取教师所教的所有课程
	 * @param unitId
	 * @param teaherIds
	 * @return
	 */
	public String findClassTeachingListByTeacherId(String unitId, String... teaherIds);
	/**
	 * 获取教师所教的行政班课程班级信息
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param teaherIds
	 * @return
	 */
	public String findClassTeachingList(String unitId,String acadyear,String semester, String... teaherIds);
	
	public String findBySearchMap(String unitId,String acadyear,String semester,String[] classIds);
	
	/**
	 * 获取班级的开课信息
	 * @param acadyear
	 * @param semester
	 * @param classIds
	 * @return List<ClassTeaching>
	 */
	public String findClassTeachingListByClassIds(String acadyear, String semester, String... classIds);
	/**
	 * 
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param classIds
	 * @param subjectIds
	 * @param flag 是否组装助教教师Set
	 * @return
	 */
	public String findByClassIdsSubjectIds(String unitId,
			String acadyear, String semester, String[] classIds, String[] subjectIds,
			boolean flag);
	/**
	 * 获取学年学期下的开课信息
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param classIds
	 * @param isDeleted
	 * @param isTeaCls
	 * @return
	 */
	public String findBySearch(String unitId,String acadyear,String semester,String[] classIds,Integer isDeleted,Integer isTeaCls);
	
	/**
	 * 根据科目类型查询
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param subjectType  提供三放调用没有调整类型
	 * @return
	 */
	public String findByUnitIdAndAcadyearAndSemesterAndSubjectType(String unitId, String acadyear, String semester, Integer subjectType);

	/**
	 * 返回该单位下行政班某科目任课主老师+扩展老师
	 * @param acadyear not null
	 * @param semester not null
	 * @param unitId not null
	 * @param gradeId
	 * @param subjectId
	 * @return List<String>
	 */
	public String findBySubidTeacherList(String acadyear, String semester,String unitId,String gradeId, String subjectId);
	/**
	 * 软删除
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param classIds
	 */
	public void deleteCurrentClassIds(String unitId, String acadyear, String semester, String[] classIds);
	
	public String findListByGradeId(String acadyear,String semester, String unitId,String gradeId);
	
	public String findListByGradeIdAndSubId(String acadyear,String semester, String unitId,String gradeId,String subjectId);

	public String findClassTeachingListHistoryByTeacherId(String unitId, String[] teaherIds);

	/**
	 * 返回相应 行政班和教学班 +科目
	 * @param unitId
	 * @param teaherIds
	 * @return  list<ClassTeaching>
	 */
	public String findClassTeachingListByBlendTeacherIds(String unitId, String[] teaherIds);

	/**
	 * 返回相应 行政班和教学班 +科目
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param teaherIds
	 * @return  list<ClassTeaching>
	 */
	public String findClassTeachingListByBlendTeacherIds(String unitId,String acadyear,String semester, String[] teaherIds);
}
