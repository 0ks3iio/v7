package net.zdsoft.system.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.system.entity.SysPlatformModel;

public interface SysPlatformModelDao extends BaseJpaRepositoryDao<SysPlatformModel, Integer> {

    @Query("FROM SysPlatformModel WHERE platform = ?1 AND PARM IS NULL AND MARK = 1")
    List<SysPlatformModel> findByPlatform(Integer platform);
}
