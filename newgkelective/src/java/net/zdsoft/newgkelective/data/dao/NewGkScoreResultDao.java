package net.zdsoft.newgkelective.data.dao;


import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkScoreResult;

public interface NewGkScoreResultDao extends BaseJpaRepositoryDao<NewGkScoreResult, String>{
    @Query("from NewGkScoreResult where unitId=?1 and referScoreId in (?2)")
	public List<NewGkScoreResult> findByReferScoreIds(String unitId, String[] referScoreIds);
	List<NewGkScoreResult> findByUnitIdAndReferScoreId(String unitId, String referScoreId);
	public List<NewGkScoreResult> findByUnitIdAndReferScoreIdAndSubjectId(String unitId,
			String referScoreId, String subjectId);
	
	public List<NewGkScoreResult> findByUnitIdAndReferScoreIdAndStudentIdIn(String unitId, String referScoreId, String[] studentIds);

//	@Query(value="select count(b.student_id),b.refer_score_id from (select distinct d.student_id,d.refer_score_id from newgkelective_score_result d where d.refer_score_id in (?0) ) as b "
//			+"group by b.refer_score_id" ,nativeQuery = true)
//	@Query("select count(b.studentId),b.referScoreId from (select distinct d.studentId,d.referScoreId from NewGkScoreResult d where d.referScoreId in (?1) ) as b "
//			+"group by b.referScoreId")
//	@Query("select distinct d.studentId,d.referScoreId from NewGkScoreResult d where referScoreId in (?1) ")
//	public List<Object[]> findCountByReferId(String[] referScoreIds);
	
	@Query("select count(d.studentId),sum(d.score),d.referScoreId,d.subjectId from NewGkScoreResult d where unitId=?1 and referScoreId in (?2) group by d.referScoreId,d.subjectId ")
	public List<Object[]> findCountSubjectByReferId(String unitId, String[] referScoreIds);

    // Basedata Sync Method
    void deleteByStudentIdIn(String... stuids);

    // Basedata Sync Method
    void deleteBySubjectIdIn(String... subids);
    
	public void deleteByReferScoreId(String referScoreId);
}
