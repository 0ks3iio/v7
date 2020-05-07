package net.zdsoft.credit.data.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.credit.data.entity.CreditModuleInfo;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface CreditModuleInfoDao extends BaseJpaRepositoryDao<CreditModuleInfo, String> {
    @Query("from CreditModuleInfo where examSetId in (?1) and scoreType=?2")
    List<CreditModuleInfo> findListByExamSetIds(String[] setIds,String scoreType);
	@Modifying
	@Query("delete From CreditModuleInfo where setId = ?1 and gradeId = ?2 and acadyear = ?3 and semester = ?4 and examType = ?5")
	void deleteByType(String setId, String gradeId, String acadyear, String semester, String type);

	@Modifying
	@Query("delete From CreditModuleInfo where examSetId in (?1)")
	void deleteByExamSetIds(String[] examSetIds);

	@Query("from CreditModuleInfo where acadyear = ?1 and semester = ?2 and subjectId = ?3 and studentId in (?4)")
	List<CreditModuleInfo> findListByStudentIds(String acadyear, String semester, String subjectId, String[] studentIds);
	
	@Transactional
	@Modifying
	@Query("delete From CreditModuleInfo where acadyear = ?1 and semester = ?2 and subjectId = ?3 and studentId in (?4)")
	void deleteByStuIds(String acadyear, String semester, String subjectId, String[] studentIds);
	
	@Query("select count(distinct studentId) from CreditModuleInfo where acadyear = ?1 and semester = ?2 and gradeId = ?3 and subjectId = ?4 ")
	int countStuNum(String acadyear, String semester, String gradeId, String subId);

}
