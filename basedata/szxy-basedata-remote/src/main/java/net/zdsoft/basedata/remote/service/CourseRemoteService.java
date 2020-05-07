package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.Course;

public interface CourseRemoteService extends BaseRemoteService<Course,String> {

	/**
	 * 科目排序
	 * @param courseJsonStrs 
	 * @return
	 */
	public String orderCourse(String courseJsonStrs);
	
	/**
	 * 获取必修课程信息 unids不能为空 建议调用findByUnitIdAndTypeAndLikeSection(String unitId,String type, String section);
	 * @param unitIds
	 * @param section 学段...，<b>可以为null</b>
	 * @return
	 */
	@Deprecated
	public String findByUnitIdIn(String[] unitIds, String... section);
	/**
	 * 获取课程信息
	 * unids不能为空
	 * @param unitIds
	 * @param type 可为null
	 * @param sections
	 * @return
	 */
	@Deprecated
	public String findByUnitIdIn(String[] unitIds,Integer type, String... sections);
	public String findByUnitIdAndTypeAndLikeSection(String unitId,String type, String section);
	/**
	 * 查询符合条件的课程  建议调用getListByCondition(String unitId,String type,String subjName,String courseTypeId,String section,Integer isUsing, String subjectType);
	 * @param unitIds
	 * @param type 
	 * @param courseTypeId 学科类型
	 * @param subjName
	 * @param section
	 * @param subjectType 单位类型
	 * @return
	 */
	@Deprecated
	public String findBy(String[] unitIds,String type, String courseTypeId, String subjName, String section, Integer isUsing, String subjectType);
	public String getListByCondition(String unitId,String type,String subjName,String courseTypeId,String section,Integer isUsing, String subjectType);
	/**不要再用此接口 
	 * 根据课程码获取教育局内课程。
	 * 课程码并不是系统内唯一的，推荐使用
	 * @param codes
	 * 调用这个findByUnitCourseCodes(String unitId, String... codes)替换
	 * @return
	 */
	@Deprecated
	public String findByBaseCourseCodes(String... codes);
	
	/**
	 * 根据Code，检索多个单位下的科目信息
	 * @param unitIds
	 * @param codes
	 * @return 建议使用下面方法findByUnitCourseCodes(String unitId, String... codes)替换
	 */
	@Deprecated
	public String findByBaseCourseCodes(String[] unitIds, String... codes);
	
	/**
	 * 检索本单位适用的科目信息（学校的话，先检索顶级单位设置的科目+学校自己设置的科目）
	 * @param unitId
	 * @param codes
	 * @return
	 */
	public String findByUnitCourseCodes(String unitId, String... codes);
	
	/**特定7选3科目信息
	 * @param unitId TODO
	 * @param 通过缓存提高性能 TIME_ONE_DAY 缓存一天
	 * @return
	 */
	public String findByCodes73(String unitId);
	
	/**特定7选3科目信息 + 语数英
	 * @param unitId TODO
	 * @param 通过缓存提高性能 TIME_ONE_DAY 缓存一天
	 * @return
	 */
	public String findByCodes73YSY(String unitId);

	/**特定7选3科目信息 + 语数英
	 * @param unitId TODO
	 * @param 通过缓存提高性能 TIME_ONE_DAY 缓存一天
	 * @return
	 */
	public String findByCodesYSY(String unitId);
	
	/**
	 * 本单位内查找 其实不是很精确 建议调用这个方法findBySubjectNameIn(String unitId,String[] subNames)
	 * @param unitId
	 * @param isUsing
	 * @param likeName
	 * @return
	 */
	@Deprecated
	public String findBySubjectUnitIdIsUsingName(String unitId, int isUsing,String likeName);
	/**
	 * 本单位范围内查找   其实不是很精确 建议调用这个方法findBySubjectNameIn(String unitId,String[] subNames)
	 * @param unitId
	 * @param likeName
	 * @return
	 */
	@Deprecated
	public String findBySubjectUnitIdName(String unitId, String likeName);
	/**
	 * @param subids
	 * @return
	 */
	public String findBySubjectIdIn(String[] subids);
	/**
	 * 序列号激活需要调用该接口，初始化课程信息
	 * @param unitId 单位Id
	 */
	void updateInitCourse(String unitId);
	/**
	 * unitId+TopUnitid集合结果
	 * @param unitId
	 * @param subNames
	 * @return 查找未删除 启用的isUsing = 1
	 */
	public String findBySubjectNameIn(String unitId,String[] subNames);
	/**
	 * 
	 * @param ids
	 * @return map<id,{subjectName,shortName}>
	 */
	public String findPartCouByIds(String[] ids);

	public String findByUnitIdIn(String... unitId);
	
	public String getVirtualCourses(String unitId,String section);

	/**
	 * 获取物理历史 科目
	 * @param unitId
	 * @return
	 */
	String findWuliLiShi(String unitId);
}
