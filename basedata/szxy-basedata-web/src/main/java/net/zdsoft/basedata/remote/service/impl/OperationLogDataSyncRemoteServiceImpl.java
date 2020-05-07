package net.zdsoft.basedata.remote.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.OperationLog;
import net.zdsoft.basedata.remote.service.OperationLogDataSyncRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.OperationLogDataSyncService;

@Service("operationLogDataSyncRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class OperationLogDataSyncRemoteServiceImpl extends BaseRemoteServiceImpl<OperationLog, String>
		implements OperationLogDataSyncRemoteService {

	@Autowired
	private OperationLogDataSyncService operationLogDataSyncService;

	@Override
	public int saveLog() {
		return operationLogDataSyncService.saveLog();
	}

	@Override
	public int findAppPopularity(String parameter) {
		return operationLogDataSyncService.findAppPopularity(parameter);
	}

	@Override
	public List<Object[]> findUsageFunctions(String[] userIds, int topN, Date fromDate) {
		return operationLogDataSyncService.findUsageFunctions(userIds, topN, fromDate);
	}

	@Override
	public List<Object[]> findUsageServers(String[] userIds, int topN, Date fromDate) {
		return operationLogDataSyncService.findUsageServers(userIds, topN, fromDate);
	}

	@Override
	public List<OperationLog> findByDescription(String description) {
		return operationLogDataSyncService.findByDescription(description);
	}

	@Override
	public List<OperationLog> findByParameterAndDescription(String ticketKey, String string) {
		return operationLogDataSyncService.findByParameterAndDescription(ticketKey, string);
	}

	@Override
	public List<OperationLog> findAllByType(String ticketKey, String type) {
		return operationLogDataSyncService.findAllByType(ticketKey, type);
	}

	@Override
	public List<OperationLog> findCountBySpace(String ticketKey, String type, Date start, Date end) {
		return operationLogDataSyncService.findCountBySpace(ticketKey, type, start, end);
	}

	@Override
	public List<OperationLog> findAllByType(String openapiDescription) {
		return operationLogDataSyncService.findAllByType(openapiDescription);
	}

	@Override
	protected BaseService<OperationLog, String> getBaseService() {
		return operationLogDataSyncService;
	}
	
	

}
