package net.zdsoft.credit.data.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.credit.data.entity.CreditDailyInfo;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

	

public interface CreditDailyInfoDao extends BaseJpaRepositoryDao<CreditDailyInfo, String> {
    @Query("from CreditDailyInfo where subDailyId in (?1)")
    List<CreditDailyInfo> findListBySubDailySetIds(String[] setIds);
	@Query("from CreditDailyInfo where acadyear = ?1 and semester = ?2 and subjectId = ?3 and studentId in (?4)")
	List<CreditDailyInfo> findListByStudentIds(String acadyear, String semester, String subjectId, String... stuIds);
	
	@Transactional
	@Modifying
	@Query("delete From CreditDailyInfo where acadyear = ?1 and semester = ?2 and setId = ?3 and subjectId = ?4 and studentId in (?5)")
	void deleteByStuIds(String acadyear, String semester, String setId, String subjectId, String[] stuIds);
	
	@Query("select count(distinct studentId) from CreditDailyInfo where acadyear = ?1 and semester = ?2 and gradeId = ?3 and subjectId = ?4 ")
	int countStuNum(String acadyear, String semester, String gradeId, String subId);
	@Query("from CreditDailyInfo where acadyear = ?1 and unitId=?2 and semester = ?3 and gradeId = ?4 and dailyId is null")
	List<CreditDailyInfo> findListByNullDailyId(String acadyear, String unitId, String semester,String gradeId);
	@Query("from CreditDailyInfo where acadyear = ?1 and unitId=?2 and semester = ?3 and studentId = ?4 and dailyId is null")
	List<CreditDailyInfo> findListByNullDailyIdByStuId(String acadyear, String unitId, String semester,String studentId);

	@Query("from CreditDailyInfo where acadyear = ?1 and semester = ?2 and studentId = ?3 and dailyId is not null and subDailyId is not null")
	List<CreditDailyInfo> findListByNotNullDailyId(String acadyear, String semester,String studentId);
	@Query("from CreditDailyInfo where acadyear = ?1 and semester = ?2 and classId = ?3 and subjectId=?4 and dailyId is not null and subDailyId is not null")
	List<CreditDailyInfo> findListByClassIdAndNotNullDailyId(String acadyear, String semester,String classId,String subjectId);
}
