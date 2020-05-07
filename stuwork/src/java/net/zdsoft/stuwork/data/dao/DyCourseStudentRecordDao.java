package net.zdsoft.stuwork.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.entity.DyCourseStudentRecord;

public interface DyCourseStudentRecordDao extends
		BaseJpaRepositoryDao<DyCourseStudentRecord, String> {

	@Query("From DyCourseStudentRecord where recordId = ?1")
	public List<DyCourseStudentRecord> findListByRecordId(String recordId);

/*	@Query("From DyCourseStudentRecord where schoolId = 1? and acadyear = ?2 and semester = ?3 "
			+ "and week = ?4 and day = ?5 and classId = ?6")
	public List<DyCourseStudentRecord> findListByClassId(String schoolId,
			String acadyear, int semester, int week, int day, String classId);*/
	
	@Query("From DyCourseStudentRecord where recordId in (?1)")
	public List<DyCourseStudentRecord> findListByRecordIds(String[] recordIds);
	
	@Modifying
	@Query("delete from DyCourseStudentRecord where recordId in (?1)")
	public void deleteByRecordId(String... recordId);
	
	@Modifying
	@Query("delete from DyCourseStudentRecord where acadyear = ?1 and semester = ?2 and week = ?3 and day = ?4 and type = ?5 and classId in (?6)")
	public void deleteBy(String acadyear, String semester, int week, int day, String type, String[] classIds);
	
	@Query("From DyCourseStudentRecord where schoolId = ?1 and acadyear = ?2 and semester = ?3 and week = ?4 and day = ?5 and classId = ?6")
	public List<DyCourseStudentRecord> findListByClassId(String schoolId,
			String acadyear, String semester, int week, int day, String classId);
	
	@Query("From DyCourseStudentRecord where schoolId = ?1 and acadyear = ?2 and semester = ?3 and week = ?4 and classId in ?5")
	public List<DyCourseStudentRecord> findByWeekAndInClassId(String unitId,
			String acadyear, String semester, int week, String[] classIds);
}
