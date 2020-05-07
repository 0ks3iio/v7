package net.zdsoft.basedata.remote.service;

import java.util.Date;
import java.util.List;

import net.zdsoft.basedata.entity.OperationLog;

public interface OperationLogDataSyncRemoteService extends BaseRemoteService<OperationLog, String> {

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

}
