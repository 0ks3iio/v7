package net.zdsoft.system.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.system.entity.Ware;

public interface WareDao extends BaseJpaRepositoryDao<Ware, String> {
    Ware findByServerId(int serverId);
}
