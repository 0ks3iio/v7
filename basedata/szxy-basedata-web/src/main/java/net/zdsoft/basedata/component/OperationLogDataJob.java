package net.zdsoft.basedata.component;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import net.zdsoft.basedata.service.OperationLogDataSyncService;
import net.zdsoft.framework.config.Evn;

@DisallowConcurrentExecution
public class OperationLogDataJob implements Job {
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		OperationLogDataSyncService operationLogDataSyncService = Evn.getBean("operationLogDataSyncService");
		operationLogDataSyncService.saveLog();
	}

}
