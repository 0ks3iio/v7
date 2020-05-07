package net.zdsoft.studevelop.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.entity.StuDevelopSubject;

public interface StuDevelopSubjectDao extends BaseJpaRepositoryDao<StuDevelopSubject,String>{
	@Query("From StuDevelopSubject where unitId = ?1 and acadyear = ?2 and semester = ?3 and gradeId = ?4 order by creationTime")
	public List<StuDevelopSubject> stuDevelopSubjectList(String unitId,
			String acadyear, String semester, String gradeId);
	
	@Query("From StuDevelopSubject where unitId = ?1 and acadyear = ?2 and semester = ?3 and gradeId in (?4) order by creationTime")
	public List<StuDevelopSubject> stuDevelopSubjectListByGradeIds(String unitId,
			String acadyear, String semester, String[] gradeIds);
	
	@Modifying
	@Query("delete from StuDevelopSubject where id in (?1)")
	public void deleteByIds(String[] ids);
	
	@Modifying
	@Query("delete from StuDevelopSubject where unitId = ?1 and acadyear = ?2 and semester = ?3")
	public void deleteBySemester(String unitId, String acadyear, String semester);
}
