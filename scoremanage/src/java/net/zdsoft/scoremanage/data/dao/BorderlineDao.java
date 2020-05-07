package net.zdsoft.scoremanage.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.scoremanage.data.entity.Borderline;

public interface BorderlineDao extends BaseJpaRepositoryDao<Borderline, String>{

	public static final String SQL_AFTER=" order by statType,statMethodDo";
	
	@Query("From Borderline where examId = ?1 and gradeCode = ?2"+SQL_AFTER)
	List<Borderline> findBorderlineList(String examIds,String gradeCode);
	@Query("From Borderline where examId = ?1"+SQL_AFTER)
	List<Borderline> findBorderlineList(String examIds);
	@Query("From Borderline where examId = ?1 and gradeCode = ?2 and subjectId in (?3)"+SQL_AFTER)
	List<Borderline> findBorderlineList(String examId, String gradeCode, String... subjectIds);
	@Query("From Borderline where examId = ?1 and gradeCode = ?2 and statType = ?3 and subjectId = ?4 "+SQL_AFTER)
	List<Borderline> findBorderlineList(String examId, String gradeCode,String statType, String subjectId);
	@Query("From Borderline where examId = ?1 and gradeCode = ?2 and subjectId='00000000000000000000000000000000' and statType in (?3)"+SQL_AFTER)
	List<Borderline> findSubjectId32(String examId, String gradeCode, String... statType);
	@Modifying
	@Query("delete from Borderline where id in (?1)")
	void deleteAllByIds(String... id);

}
