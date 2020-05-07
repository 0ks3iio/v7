package net.zdsoft.basedata.remote.service;


import net.zdsoft.basedata.entity.Student;

import java.util.List;

public interface StudentRemoteService extends BaseRemoteService<Student,String> {
	
	/**
	 * 支持id超过1000已经处理
	 */
	public String findListByIds(String... ids);

	/**
	 * 获取行政班中在校学生
	 * 
	 * @param classIds
	 * @return List&lt;Student&gt;
	 */
	public String findByClassIds(String... classIds);

	/**
	 * 获取教学班中在校学生
	 *         最好不要调用 请调用teachclassservice 20180113丢弃
	 * @param classIds
	 * @return 
	 */
	public String findByTeachClassIds(String... classIds);

	/**
	 * 获取行政班中在校学生，分页
	 * 
	 * @param classIds
	 * @param page
	 * @return
	 */
	public String findByClassIds(String[] classIds, String page);

	/**
	 * 获取年级中在校学生只是显示（学号姓名性别身份证等基础信息）
	 * <p>三个参数每次只能传一个，其他需要为空</p>
	 * @param unitId 必须传
	 * @param gradeId
	 * @param classIds
	 * @param studentIds
	 * @return
	 */
	public String findPartStudByGradeId(String unitId, String gradeId, String[] classIds, String[] studentIds);
	
	/**
	 * 获取教学班中在校学生，分页
	 *             最好不要调用 请调用teachclassservice 20180113丢弃
	 * @param classIds
	 * @param page
	 * @return
	 */
	public String findByTeachClassIds(String[] classIds, String page);
	/**
	 * 根据单位id范围根据精确学号查找学生
	 * @param schoolId
	 * @param studentCode
	 * @return
	 */
	public String findBySchIdStudentCode(String schoolId,String studentCode);
	
    /**
     * 根据单位id范围根据学号查找学生
     * @param schoolId
     * @param studentCodes
     * @return
     */
    public String findBySchIdStudentCodes(String schoolId, String[] studentCodes);
    
    /**
     * 
     * @param stuName
     *            学生姓名 可为空 模糊查询
     * @param schoolIds
     * @return
     */
    public String findBySchoolIdIn(String stuName, String[] schoolIds);

	/**
	 * 数组形式entitys参数，返回list的json数据
	 * @param entitys
	 * @return
	 */
	public String saveAllEntitys(String entitys);
	
	/**
     * 根据条件查询未被删除的学生
     * @param unitId必填
     * @param ids 可为null  *id超过1000已经处理
     * @param classIds 可为null  *classId超过1000没有处理
     * @param searchStudent 此为student对象的json字符串，提供性别，姓名，学号
     * @param page 可为null
     * @return List<Student> order by class_id,studentCode
     */
	public String findByIdsClaIdLikeStuCodeNames(String unitId,String[] ids,String[] classIds,String searchStudent,String page);
	/**
     * 根据条件查询未被删除的学生
     * @param unitId必填
     * @param ids 可为null
     * @param classIds 可为null
     * @param searchStudent 此为student对象的json字符串，提供性别，姓名，学号
     * @param page 可为null
     * @return List<Student> order by class_id,studentCode
     */
	public String findByNotIdsClaIdLikeStuCodeNames(String unitId,String[] ids,String[] classIds,String searchStudent,String page);
	
	/**
	 * 
	 * @param unitId
	 * @param gradeId
	 * @param classIds
	 * @param searchStudent
	 * @return
	 */
	public String findByClaIdsLikeStuCodeNames(String unitId, String gradeId,String[] classIds,String searchStudent);
	
	/**
	 * 根据条件查询   valCode like 'val%'    如学号 valCode=studentCode
	 * @param unitId 必填
	 * @param valCode 必填
	 * @param Val 必填
	 * @return 
	 */
	public String findByUnitLikeCode(String unitId,String valCode,String Val);
	/**
	 * 根据条件查询
	 * @param greadId必填
	 * @param studentCode必填
	 * @return
	 */
	public String findByGreadIdStuCode(String greadId, String studentCode);
	/**
	 * 获取年级中在校学生
	 * 
	 * @param classIds
	 * @return List&lt;Student&gt;
	 */
	public String findByGradeIds(String... gradeIds);
	/**
	 * 获取年级中在校学生
	 * @param classIds
	 * @return List&lt;Student&gt;
	 */
	public String findByGradeId(String gradeId);

	/**
	 * 获取年级中未删除的学生
	 * 	  包括 在校的,离校的,毕业的
	 * @param gradeId
	 * @return
	 */
	public String findAllStudentByGradeId(String gradeId);
	/**
	 * 获取年级中在校学生数
	 * @param classIds
	 * @return List&lt;Student&gt;
	 */
	public long CountStudByGradeId(String gradeId);
	/**
	 * 根据班级ids获取正常学生
	 * @param classIds
	 * @return long
	 */
	public String findMapByClassIdIn(String[] classIds);
	
	/**
	 * 班级的学生数
	 * @param classIds
	 * @return map(classId, count)
	 */
	public String countMapByClassIds(String... classIds);
	/**
	 * 根据条件查询
	 * @param cradNumber必填
	 * @return
	 */
	public String findByCardNumber(String unitId,String cardNumber);
	/**
	 * 根据学生姓名和身份证查找学生
	 * @param studentName
	 * @param identityCard
	 * @return
	 */
	public String findByStudentNameAndIdentityCardWithNoUser(String studentName,String identityCard);
	/**
	 * 根据学生姓名和身份证查找学生
	 * @param studentName
	 * @param identityCard
	 * @return
	 */
	public String findByStudentNameAndIdentityCard(String realName,
			String identityCard);
	
	/**
	 * 根据身份证号获取学生
	 * @param identityCards
	 * @return
	 */
	public String findByIdentityCards(String... identityCards);
	
	/**
	 * 更新学生一卡通号
	 * 
	 * @param studentList
	 */
	public int[] updateCardNumber(List<String[]> studentList);
	
	public String findBySchoolId(String schoolId);
	
	public void updateClaIds(List<Student> studentList);
	/**
     * 根据学生id
     * @return List<Student> 提供学生id,性别，姓名，学号，schoolId,班级id
     */
	public String findPartStudentById(String[] studentIds);
	public String findMapByAttr(String[] ids, String attrName);
	
	
	 public String findBySchoolIdIn(String[] schoolIds,String page);
	 
	 
	 /**
	  * 根据姓名或者学号精确查询学生
	  * @param unitId 不能为空
	  * @param studentName 
	  * @param studentCode
	  * @return List<Student> 提供学生id,姓名
	  */
	 public String findBySchoolIdStudentNameStudentCode(String unitId,String studentName,String studentCode);

	 public String findByStudentCode(String studentCode);

	 public String countListBySchoolIds(String[] schoolIds);

	String countMapByGradeIds(String... gradeIds);

	/**
	 * 获取学生信息 包括教学班ids 和行政班ids
	 * @param classids
	 * @return List<Student>
	 */
	public String findListBlendClassIds(String[] classids);
}
