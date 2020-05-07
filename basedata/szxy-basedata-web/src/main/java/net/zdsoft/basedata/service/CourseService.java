package net.zdsoft.basedata.service;

import java.util.LinkedHashMap;
import java.util.List;

import net.zdsoft.basedata.entity.Course;

public interface CourseService extends BaseService<Course, String> {
	/**
	 * 建议调用下面方法  getListByCondition
	 * @param unitIds
	 * @param type
	 * @param subjType
	 * @param subjName
	 * @param section
	 * @param subjectType
	 * @return
	 */
	@Deprecated
    public List<Course> findByUnitIdIn(String[] unitIds,String type, String courseTypeId, String subjName, String section, String subjectType);
	/**
	 * @param unitId 本单位id
	 * @param types
	 * @param courseTypeId
	 * @param subjName
	 * @param section
	 * @param subjectType
	 * @param isUsing
	 * @return 获取自己单位+顶级单位的数据
	 */
	public List<Course> getListByCondition(String unitId, String[] types, String courseTypeId, String subjName,String section,String subjectType,Integer isUsing);
	
	public List<Course> getListByConditionWithMaster(String unitId, String[] types, String courseTypeId, String subjName,String section,String subjectType,Integer isUsing);
	
	/**
	 * @param unitId 本单位id
	 * @param subjectName
	 * @return 获取自己单位和顶级单位数据
	 */
	public List<Course> findByUnitIdInAndSubjectName(String unitId, String subjectName);

    public List<Course> findBySubjectCodes(String[] subjectCodes);
    /**
     * 取得非删除科目中排序号的最大值
     * 
     * @return
     */
    public int findMaxOrderId(String unitId);

    /**
     * 软删
     * 
     * @param ids
     */
    public void updateIsDeleteds(String[] ids);
    
    /**
     * 根据单位和学段获取课程。 改掉用 findByUnitIdAndTypeAndLikeSection 原因规则改成section筛选
     * @param unitIds
     * @param type
     * @param section
     * @return
     */
    @Deprecated 
    public List<Course> findByUnitIdIn(String[] unitIds,String type, String... sections);
    /**
     * @param unitId
     * @param type
     * @param section
     * @return 获取自己单位和顶级单位数据
     */
    public List<Course> findByUnitIdAndTypeAndLikeSection(String unitId,String type, String section);
    /**
     * 常用接口调用 学校获取自己本身+顶级教育局，教育局获取顶级教育局数据
     * 
     * @param isUsing
     *            可以不传，用来获取显示名称；如果使用科目必须传isUsing=1在启用数据
     * @param unitId
     * @return 注意科目名称其他模块调用请取简称 getShortName()
     */
    public List<Course> findByUnitIdAndIsUsingList(String unitId, Integer isUsing);

    public List<Course> saveAllEntitys(Course... course);
    
	String checkUnique(Course course, String AddOrUp, String unitId);
	/**
	 * 根据单位 和 课程类型 获取课程
	 * @param unitId
	 * @param courseTypeIds
	 * @return 获取本单位数据
	 */
	public List<Course> findByUnitIdAndCourseTypeIdIn(String unitId,String[] courseTypeIds);
	/**
	 * @param unitId
	 * @param code
	 * @return  获取本单位+顶级单位数据
	 */
	public List<Course> findByUnitIdInAndCourseCode(String unitId,String code);
	
	/**
	 * 对数据库内置32个0的课程操作
	 * @param topUnitId
	 */
	public void updateInitCourse(String topUnitId);
	
	/**
	 * 查询符合条件的课程 丢弃最好不适用 getListByCondition
	 * @param unitIds
	 * @param type 
	 * @param courseTypeId 学科类型
	 * @param subjName
	 * @param section
	 * @param subjectType 单位类型
	 * @return
	 */
	@Deprecated
	public List<Course> findBySection(String[] unitIds,String type, String courseTypeId, String subjName, String section, Integer isUsing, String subjectType);
	/**
	 * 
	 * @param ids
	 * @return map<id,subjectName>
	 */
	public LinkedHashMap<String, String> findPartCouByIds(String[] ids);
	/**
	 * 获取传入单位id
	 * @param unitId
	 * @return 单位和顶级单位 未删除启用科目信息根据orderid排序
	 */
	public List<Course> findByTopUnitAndUnitId(String unitId);
	/**
	 * 组装其他辅助参数信息
	 * @param courseList
	 */
	public void makeOtherParm(List<Course> courseList);
	
	public List<Course> getCourseByUnitIdIn(String... unitId);
	/**
	 * 查找 某个 学校 学段的 虚拟课程
	 * @param unitId
	 * @param section
	 * @return
	 */
	List<Course> getVirtualCourses(String unitId, String section);
}
