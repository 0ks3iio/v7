package net.zdsoft.basedata.remote.service.impl;

import net.zdsoft.basedata.dao.OperationLogDao;
import net.zdsoft.basedata.entity.OperationLog;
import net.zdsoft.basedata.remote.service.OperationLogRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.OperationLogDataSyncService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service("operationLogRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class OperationLogRemoteServiceImpl extends BaseRemoteServiceImpl<OperationLog, String>
		implements OperationLogRemoteService {

	@Autowired
	private OperationLogDataSyncService operationLogDataSyncService;
	@Resource
	private OperationLogDao operationLogDao;

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
	public List<OperationLog> findListByUrl(String url, Date beginDate,Date endDate){
		return operationLogDataSyncService.findListByUrl(url, beginDate, endDate);
	}
	
	@Override
	public List<OperationLog> findListByDate(Date beginDate,Date endDate){
		return operationLogDataSyncService.findListByDate(beginDate, endDate);
	}

	@Override
	protected BaseService<OperationLog, String> getBaseService() {
		return operationLogDataSyncService;
	}

	@Override
	public long countUnits(String[] unitIds) {
		if (ArrayUtils.isEmpty(unitIds)) {
			throw new IllegalArgumentException("unitIds can't empty");
		}
		List<String> unitIdsList = Arrays.asList(unitIds);
		List<String> unitIdList =null;
		int num = 500;
		int len = unitIds.length;
		int size = len % num;
		if (size == 0) {
			size = len / num;
		} else {
			size = (len / num) + 1;
		}
		if (unitIds.length >= 1000){
			long desNum=0L;
			for (int i = 0; i < size; i++) {
				int fromIndex = i * num;
				int toIndex = fromIndex + num;
				if (toIndex > len) {
					toIndex = len;
				}
				unitIdList = unitIdsList.subList(fromIndex, toIndex);
				desNum=operationLogDao.countUnits(unitIdList.toArray(new String[0]))+desNum;
			}
			return desNum;
		}else{
			return operationLogDao.countUnits(unitIds);
		}
	}

	@Override
	public long countUsersByDate() {
		return operationLogDao.countUsersByDate();
	}
	
	public long countAllUsers() {
		return operationLogDao.countAllUsers();
	}

	@Override
	public long countSchool() {
		return operationLogDao.countSchool();
	}
}
