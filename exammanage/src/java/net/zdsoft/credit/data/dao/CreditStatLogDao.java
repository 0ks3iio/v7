package net.zdsoft.credit.data.dao;

import net.zdsoft.credit.data.entity.CreditStatLog;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CreditStatLogDao extends BaseJpaRepositoryDao<CreditStatLog, String> {
    @Modifying
    @Query("delete from CreditStatLog where acadyear =?1 and semester=?2 and gradeId=?3")
    void deleteByParams(String year ,String semster,String gradeId);

	CreditStatLog findBySetIdAndGradeId(String setId, String gradeId);
	
}
