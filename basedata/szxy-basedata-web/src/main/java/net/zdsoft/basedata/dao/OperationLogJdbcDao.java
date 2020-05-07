package net.zdsoft.basedata.dao;

import java.util.Date;
import java.util.List;

import net.zdsoft.basedata.entity.OperationLog;
import net.zdsoft.framework.entity.Pagination;

public interface OperationLogJdbcDao {
	void insertLog(OperationLog... logs);
	
	void updateJsonStr(String[] ids, String[] jsonStrs);
	
	List<OperationLog> findByJsonIsNullTopN(int top);

	List<Object[]> findUsageFunctions(String[] userIds, int topN, Date fromData);

	List<Object[]> findUsageServers(String[] userIds, int topN, Date fromData);

}
