package net.zdsoft.newstusys.dao;

import java.util.List;

import net.zdsoft.newstusys.entity.StusysStudentBak;

/**
 * 
 * @author weixh
 * 2018年9月18日	
 */
public interface StusysStudentBakJdbcDao {

	public List<StusysStudentBak> findBySchIdParams(String schId, String stuName, String stuCode, 
			String idCard); 
	
	public List<StusysStudentBak> findStuByStuIdSemester(String acadyear, String semester, String stuId);
	
	public List<StusysStudentBak> findSemesterBySchId(String schId);
	
	public void deleteByStuIds(String acadyear,String semester,String[] studentIds);
}
