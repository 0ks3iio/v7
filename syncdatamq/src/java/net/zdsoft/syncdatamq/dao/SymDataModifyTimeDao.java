package net.zdsoft.syncdatamq.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.syncdatamq.entity.SymDataModifyTime;

public interface SymDataModifyTimeDao
		extends BaseJpaRepositoryDao<SymDataModifyTime, String>, SymDataModifyTimeJdbcDao {
}
