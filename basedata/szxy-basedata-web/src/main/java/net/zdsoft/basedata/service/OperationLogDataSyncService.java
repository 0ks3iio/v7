package net.zdsoft.basedata.service;

import java.util.Date;
import java.util.List;

import net.zdsoft.basedata.entity.OperationLog;
import net.zdsoft.framework.entity.Pagination;

public interface OperationLogDataSyncService extends BaseService<OperationLog, String> {
	
	void insertLog(OperationLog... logs);
	
	List<OperationLog> findByJsonIsNullTopN(int top);
	
	void updateJsonStr(String[] ids, String[] jsonStrs);

	int saveLog();

	int findAppPopularity(String parameter);

	List<Object[]> findUsageFunctions(String[] userIds, int topN, Date fromDate);

	List<Object[]> findUsageServers(String[] userIds, int topN, Date fromDate);

	List<OperationLog> findByDescription(String description);

	/**
	 * @param ticketKey
	 * @param string
	 * @return
	 */
	List<OperationLog> findByParameterAndDescription(String ticketKey, String string);

	/**
	 * @param ticketKey
	 * @param type
	 * @return
	 */
	List<OperationLog> findAllByType(String ticketKey, String type);

	/**
	 * @param ticketKey
	 * @param type
	 * @param start
	 * @param end
	 * @return
	 */
	List<OperationLog> findCountBySpace(String ticketKey, String type, Date start, Date end);

	/**
	 * @param openapiDescription
	 * @return
	 */
	List<OperationLog> findAllByType(String openapiDescription);
	
	List<OperationLog> findListByUrl(String url, Date beginDate,Date endDate);
	
	List<OperationLog> findListByDate(Date beginDate,Date endDate);

}
