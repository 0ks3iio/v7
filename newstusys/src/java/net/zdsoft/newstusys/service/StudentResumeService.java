package net.zdsoft.newstusys.service;

import java.util.List;

import net.zdsoft.newstusys.entity.StudentResume;

/**
 * Created by Administrator on 2018/3/1.
 */
public interface StudentResumeService {
	/**
	 * 保存，新增的记录id需为空
	 * @param t
	 */
	public void save(StudentResume t);
	
	/**
	 * 保存，新增的记录id需为空
	 * @param ts
	 */
	public void saveAll(StudentResume[] ts);
	
	public void deleteResumeByStuId(String... stuIds);
	
	public void delete(String... ids);

	public List<StudentResume> findByStuid(String stuId);
	public List<StudentResume> findByStuids(String... stuIds);
}
