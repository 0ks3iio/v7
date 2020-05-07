package net.zdsoft.newstusys.remote.service;

import net.zdsoft.newstusys.entity.StusysStudentBak;

/**
 * 
 * 
 * @author weixh
 * 2018年9月18日	
 */
public interface StusysStudentBakRemoteService {
	/**
	 * 查询学生备份信息
	 * @param schId 必填
	 * @param stuName 以下3个条件必须要有一个，都是模糊查询
	 * @param stuCode
	 * @param idcard
	 * @return
	 */
	public String findStuBySchId(String schId, String stuName, String stuCode, String idcard);
	
	/**
	 * 查询某学生某学期的备份信息
	 * @param acadyear
	 * @param semester
	 * @param stuId
	 * @return
	 */
	public String findStuByStuIdSemester(String acadyear, String semester, String stuId);
	
	/**
	 * 根据班级从毕业班查询学生
	 * @param classIds
	 * @return
	 */
	public String findByClassIds(String[] classIds);
	
	public void deleteByStuIds(String acadyear,String semester,String[] studentIds);
	
	public void saveAll(StusysStudentBak[] stuBaks);
}
