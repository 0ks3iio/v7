package net.zdsoft.stuwork.data.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.entity.DyCourseRecord;

public interface DyCourseRecordDao extends BaseJpaRepositoryDao<DyCourseRecord, String>{
	
	@Query("From DyCourseRecord where schoolId = ?1 and acadyear = ?2 and semester = ?3 and type = ?4 and teacherId =?5 and" +
			" week = ?6 and day = ?7 and period = ?8")
	public List<DyCourseRecord> findByAll(String schoolId, String acadyear,
			String semester, String type, String teacherId, int week, int day, int period);
	
	@Query("From DyCourseRecord where schoolId = ?1 and acadyear = ?2 and semester = ?3 and type = ?4 and teacherId =?5 and" +
			" week = ?6 and day = ?7")
	public DyCourseRecord findBy(String schoolId, String acadyear,
			String semester, String type, String teacherId, int week, int day);
	
	@Query("From DyCourseRecord where schoolId = ?1 and acadyear = ?2 and semester = ?3 and type = ?4 and teacherId =?5 and" +
			" week = ?6 and day = ?7")
	public List<DyCourseRecord> findListBy(String schoolId, String acadyear,
			String semester, String type, String teacherId, int week, int day);
	
	@Query("From DyCourseRecord where schoolId = ?1 and type = ?2 and recordDate = ?3")
	public List<DyCourseRecord> findListByRecordDate(String schoolId, String type, Date recordDate);
	
	@Query("From DyCourseRecord where schoolId = ?1 and acadyear = ?2 and semester = ?3 and week = ?4 and day = ?5 and type = ?6")
	public List<DyCourseRecord> findListByDay(String schoolId, String acadyear, String semester, int week, int day, String type);
	
	@Query("From DyCourseRecord where schoolId = ?1 and acadyear = ?2 and semester = ?3 and week = ?4 and day = ?5 and type = ?6 and period = ?7")
	public List<DyCourseRecord> findListByPeriod(String schoolId, String acadyear, String semester, int week, int day, String type, int period);
	
	@Query("From DyCourseRecord where schoolId = ?1 and acadyear = ?2 and semester = ?3 and week = ?4 and day = ?5 and type = ?6 and classId like ?7")
	public List<DyCourseRecord> findListByRecordClassId(String schoolId, String acadyear, String semester, int week, int day, String type, String classId);
	
	@Query("From DyCourseRecord where schoolId = ?1 and acadyear = ?2 and semester = ?3 and week = ?4 and type = ?5 and classId like ?6")
	public List<DyCourseRecord> findListByRecordClassId(String schoolId, String acadyear, String semester, int week, String type, String classId);
	
	@Modifying
	@Query("delete from DyCourseRecord where acadyear = ?1 and semester = ?2 and week = ?3 and day = ?4 and type = ?5 and classId in (?6)")
	public void deleteBy(String acadyear, String semester, int week, int day, String type, String[] classIds);
	
	@Modifying
	@Query("delete from DyCourseRecord where acadyear = ?1 and semester = ?2 and week = ?3 and day = ?4 and type = ?5 and recordClass in (?6)")
	public void deleteByRecordClass(String acadyear, String semester, int week, int day, String type, String[] classIds);
	
	@Query("From DyCourseRecord where schoolId = ?1 and acadyear = ?2 and semester = ?3 and week = ?4 and classId in (?5)")
	public List<DyCourseRecord> findListByRecordClassIds(String schoolId, String acadyear, String semester, int week,
			String[] classIds);
}
