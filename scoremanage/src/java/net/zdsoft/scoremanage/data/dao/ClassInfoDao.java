package net.zdsoft.scoremanage.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.scoremanage.data.entity.ClassInfo;

public interface ClassInfoDao extends BaseJpaRepositoryDao<ClassInfo, String>{
	

	public List<ClassInfo> findBySchoolIdAndSubjectInfoIdIn(String schoolId,String... subjectInfoId);

	public List<ClassInfo> findBySubjectInfoIdIn(String... subjectInfoIds);

	@Query("From ClassInfo where schoolId = ?1 and classType = ?2 and classId = ?3 and subjectInfoId in (?4)")
	public List<ClassInfo> findList(String unitId, String classType, String classId,
			String[] subjectInfoIds);
	
	@Query("From ClassInfo where schoolId = ?1 and classType = ?2 and subjectInfoId in (?3)")
	public List<ClassInfo> findList(String unitId, String classType, String[] subjectInfoIds);

	@Query("From ClassInfo where schoolId = ?1 and  subjectInfoId in (?2)")
	public List<ClassInfo> findList(String unitId, String... subjectInfoIds);
	
	@Query("From ClassInfo where subjectInfoId in (?1) ")
	public List<ClassInfo> findByExamInfoId(String... subjectInfoIds);

	@Modifying
	@Query("delete from ClassInfo where schoolId=?1 and subjectInfoId in (?2)")
	public void deleteBySchoolId(String schooolId, String... subjectInfoIds);

	@Modifying
	@Query("delete from ClassInfo where id in (?1)")
	public void deleteAllByIds(String... id);
	
	@Query("From ClassInfo where schoolId = ?1 and classType = ?2 and subjectInfoId in (?3) ")
	public List<ClassInfo> findByAll(String unitId, String classType, String[] subjectInfoIds);

	public ClassInfo findOneBySchoolIdAndClassTypeAndClassIdAndSubjectInfoId(
			String schoolId, String classType, String classId,
			String subjectInfoId);

	public List<ClassInfo> findBySchoolIdAndClassIdIn(String unitId, String[] classIds);

	public List<ClassInfo> findBySchoolIdAndClassTypeAndSubjectInfoIdIn(String unitId, String classType, String[] subjectInfoIds);

}
