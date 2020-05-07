package net.zdsoft.basedata.dingding.dao;

import net.zdsoft.basedata.dingding.entity.DdSyncdataLog;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface DingDingSyncdataLogDao extends
		BaseJpaRepositoryDao<DdSyncdataLog, String>, DingDingSyncdataLogJdbcDao {

}
