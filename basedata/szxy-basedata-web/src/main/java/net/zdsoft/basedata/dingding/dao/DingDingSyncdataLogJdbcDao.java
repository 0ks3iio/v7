package net.zdsoft.basedata.dingding.dao;

import net.zdsoft.basedata.dingding.entity.DdSyncdataLog;

public interface DingDingSyncdataLogJdbcDao {

	void insertLogs(DdSyncdataLog... logs);
}
