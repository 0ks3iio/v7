package net.zdsoft.credit.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.credit.data.entity.CreditExamSet;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface CreditExamSetDao extends BaseJpaRepositoryDao<CreditExamSet, String> {
	
	List<CreditExamSet> findBySetIdAndAcadyearAndSemesterAndGradeIdAndType(String setId, String acadyear, String semester, String gradeId, String type);
	
	@Query("from CreditExamSet where setId = ?1 and acadyear = ?2 and semester = ?3 and subjectId = ?4 and classId = ?5 and classType = ?6 and type = '1' order by name")
	List<CreditExamSet> findByUsualSet(String setId, String acadyear, String semester, String subjectId, String classId,
			String classType);
	
	@Modifying
	@Query("delete from CreditExamSet where id in (?1)")
	void deleteByIds(String[] ids);
}
