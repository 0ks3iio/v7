package net.zdsoft.credit.data.dao;

import net.zdsoft.credit.data.entity.CreditDailySet;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CreditDailySetDao extends BaseJpaRepositoryDao<CreditDailySet, String> {

    List<CreditDailySet> findBySetId(String setId);

    @Modifying
    @Query("delete from CreditDailySet where id in (?1)")
    void deleteByIds(String... ids);

}
