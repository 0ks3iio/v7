package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.EmLimitDetail;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmLimitDetailDao extends BaseJpaRepositoryDao<EmLimitDetail, String> {

    List<EmLimitDetail> findByLimitIdIn(String[] limitIds);

    @Modifying
    @Query("delete from EmLimitDetail where id in (?1)")
    void deleteAllByIds(String... id);

    void deleteByLimitIdIn(String[] limitId);
}
