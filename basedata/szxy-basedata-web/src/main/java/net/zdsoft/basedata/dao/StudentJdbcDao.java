package net.zdsoft.basedata.dao;

import java.util.List;
import java.util.Map;


import net.zdsoft.basedata.entity.Student;
import net.zdsoft.framework.entity.Pagination;

public interface StudentJdbcDao {
	public Map<String, Integer> countMapByClassIds(String... classIds);

	/**
	 * 查找学生姓名Map 不包括删除的
	 * 
	 * @param studentIds
	 * @return Map key=>id value=>studentName
	 * 
	 */
	public Map<String, String> getStudentNameMap(String[] studentIds);

	/**
	 * 调入调出用(查询未离校的学生:已离校无验证码和在校的学生)
	 * 
	 * @param studentName
	 * @param identityCard
	 * @return
	 */
	List<Student> getStudentIsLeaveSchool(String studentName,
			String identityCard, String unitId, Pagination page);

	/**
	 * 修改离校学生身份证号
	 * 
	 * @param studentList
	 */
	public void updateIdCard(List<Student> studentList);

	/**
	 * 更新学生一卡通号
	 * 
	 * @param studentList
	 */
	public int[] updateCardNumber(List<String[]> studentList);
	
	public List<Student> findByClaIdsLikeStuCodeNames(String unitId, String gradeId,String[] classIds,Student searchStudent);
	
	/**
	 * 获取年级中在校学生只是显示（学号姓名性别身份证等基础信息）
	 * @param unitId TODO
	 * @param classIds
	 * @return List&lt;Student&gt;
	 */
	public List<Student> findPartStudByClaIds(String unitId, String[] classIds);
	/**
	 * 获取年级中在校学生数
	 * @param classIds
	 * @return List&lt;Student&gt;
	 */
	public long CountStudByClaIds(String[] classIds);
	/**
	 * 根据学生ID 获取学生信息
	 * @param unitId TODO
	 * @param studentIds
	 * @return
	 */
	List<Student> findPartStudByStuIds(String unitId, String[] studentIds);
	
	public void updateClaIds(List<Student> studentList);
	
	List<Student> findPartAllStudentByStuIds(String[] studentIds);
	
	List<Student> findBySchoolIdStudentNameStudentCode(String unitId, String studentName,String studentCode);

	/**
	 * 获取学生信息 包括教学班ids 和行政班ids
	 * @param classIds
	 * @return List<Student>
	 */
	List<Student> findListBlendClassIds(String[] classIds);
}
