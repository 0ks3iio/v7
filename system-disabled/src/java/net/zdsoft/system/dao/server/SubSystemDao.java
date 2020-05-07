package net.zdsoft.system.dao.server;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.system.entity.server.SubSystem;

public interface SubSystemDao extends SubSystemJdbcDao, BaseJpaRepositoryDao<SubSystem, Integer> {

    @Query("select max(id) from SubSystem")
    public int findMaxIntId();

    @Query("select max(displayOrder) from SubSystem")
    public int findMaxDisplayOrder();
}
