package net.zdsoft.basedata.remote.service;

import java.util.Date;

import net.zdsoft.basedata.entity.Grade;

public interface GradeRemoteService extends BaseRemoteService<Grade,String> {

	/**
	 * 获取状态正常的年级信息
	 * @param schoolId
	 * @return
	 */
	public String findBySchoolId(String schoolId);
	
	/**
	 * 获取状态正常的年级信息
	 * @param schoolId
	 * @return
	 */
	public String findBySchoolId(String schoolId,Integer[] section);
	
	/**
	 * @deprecated 会列出所有学年学期的年级，不合理
	 * @param schoolId
	 * @param section
	 * @return
	 */
	public String findBySchoolIdAndSection(String schoolId,Integer section);
	/**
	 * 根据教师ID得到该教师是年级组长的年级列表
	 * @param teacherId
	 * @return
	 */
	public String findByTeacherId(String teacherId);

	/**
	 * 设置年级为毕业状态
	 * @param date
	 * @param schId
	 * @param acadYear
	 * @param section
	 * @param schoolingLength
	 */
	public void updateGraduate(Date date, String schId, String acadYear,
			Integer section, Integer schoolingLength);

	/**
	 * 
	 * @param schoolId
	 * @param curAcadyear
	 * @param section
	 * @return
	 */
	public String findBySchidSectionAcadyear(String schoolId,String curAcadyear, Integer[] section);

	/**
	 * 数组形式entitys参数，返回list的json数据
	 * @param entitys
	 * @return
	 */
	public String saveAllEntitys(String entitys);

	/**
	 * 获取年级信息（不包括删除的）
	 * @param schoolId
	 * @param section
	 * @param openAcadyear
	 * @param isOnlyNotGraduate 是否只获取未毕业的
	 * @return
	 */
	public String findBySchoolId(String schoolId, Integer[] section, String openAcadyear,boolean isOnlyNotGraduate);

	/**
	 * 获取各个单位正常的年级信息
	 * @param unitIds
	 * @return Map<String, List<Grade>>
	 */
	public String findBySchoolIdMap(String[] unitIds);
	
	  /**
     * 根据单位、年级code找未删除未毕业的年级 排序：order by displayOrder, section, openAcadyear desc, gradeName
     * 
     * @param unitId
     * @param gradeCodes
     * @return
     */
    public String findByUnitIdAndGradeCode(String unitId, String... gradeCodes);
    /**
     * 获取单位学年下正常的年级
     * @param unitId
     * @param acadyear
     * @return
     */
	public String findByUnitIdAndCurrentAcadyear(String unitId, String acadyear);
	/**
	 * 获取该学年下正常的某学段年纪
	 * @param unitId
	 * @param sections
	 * @param currentAcadyear
	 * @return
	 */
	String findByUnitIdAndGradeCode(String unitId,Integer[] sections, String currentAcadyear);
	/**
     * 获取单位学年下正常的年级
     * @param unitId
     * @param acadyear
     * @return
     */
	public String findByUnitIdsAndCurrentAcadyear(String[] unitIds, String acadyear);

	/**
	 * 获取学年下正常的年级
	 * @param unitId
	 * @param gradeId
	 * @param searchAcadyear
	 * @return
	 */
	public String findByIdAndCurrentAcadyear(String unitId, String gradeId,
			String searchAcadyear);

	/**
	 * 获取单位下的年级
	 * @param unitId
	 * @param graduated
	 * @return
	 */
	public String findBySchoolIdAndIsGraduate(String unitId, Integer graduated);
	
	/**
	 * 获取单位下的年级
	 * @param unitId
	 * @param acadyear
	 * @return
	 */
	public String findBySchoolIdAndOpenAcadyear(String unitId, String acadyear);

	/**
	 * 特殊接口如老师存在跨年级教学情况、课表上下午都是取年级最大值接口调用如下
	 * 根据需求传入，如果gradeIds更精确
	 * @param schoolId 不是必填
	 * @param gradeIds
	 * @return Grade
	 */
	public String findTimetableMaxRangeBySchoolId(String schoolId,String[] gradeIds);
	
	
	public String findByUnitIdsIn(String[] unitIds);

	public String findBySchoolIdsAndOpenAcaday(String[] schoolIds, String openAcadyear);
}
