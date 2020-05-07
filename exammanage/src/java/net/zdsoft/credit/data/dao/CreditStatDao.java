package net.zdsoft.credit.data.dao;

import net.zdsoft.credit.data.entity.CreditStat;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CreditStatDao extends BaseJpaRepositoryDao<CreditStat, String> {
    @Query("from CreditStat where subDailySetId in (?1)")
    List<CreditStat> findBySubDailySetIds(String[] subDailySetIds);

    @Query("from CreditStat where dailySetId in (?1)")
    List<CreditStat> findBydailySetIds(String[] dailySetIds);

    @Modifying
    @Query("delete from CreditStat where gradeId =?1 and acadyear =?2 and semester=?3")
    void delByGradeId(String gradeId, String year, String semester);
}
