package net.zdsoft.newstusys.dao;

import java.util.List;

import net.zdsoft.newstusys.entity.StudentResume;

/**
 * 
 * @author weixh
 * @since 2018年3月6日 下午8:43:24
 */
public interface StudentResumeJdbcDao {
	public void save(StudentResume sr);
	
	public void update(StudentResume sr);
	
	public void batchSave(List<StudentResume> srs);
	
	public void batchUpdate(List<StudentResume> srs);

	public void deleteByStuIds(String... stuId);
	
	public void deleteByIds(String... ids);
	
	public List<StudentResume> findByStuid(String stuId);
	public List<StudentResume> findByStuids(String... stuIds);
}
