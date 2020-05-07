package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.OperationLog;

import java.util.Date;
import java.util.List;

public interface OperationLogRemoteService extends BaseRemoteService<OperationLog, String>{
	
	int findAppPopularity(String parameter);
	

	/**
	 * 根据用户ID，取出某个日期后的前N次子系统
	 * @param userIds
	 * @param topN
	 * @param fromDate
	 * @return
	 */
	List<Object[]> findUsageFunctions(String[] userIds, int topN, Date fromDate);
	
	List<Object[]> findUsageServers(String[] userIds, int topN, Date fromDate);
	
	List<OperationLog> findListByUrl(String url, Date beginDate,Date endDate);
	
	List<OperationLog> findListByDate(Date beginDate,Date endDate);

	long countUnits(String[] unitIds);

	long countUsersByDate();
	
	long countAllUsers();

	long countSchool();
}
