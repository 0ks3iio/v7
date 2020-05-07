package net.zdsoft.bigdata.daq.data.job;

import net.zdsoft.bigdata.daq.data.service.BizOperationLogService;
import net.zdsoft.framework.config.Evn;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class BizOperationLogJob implements Job {
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		BizOperationLogService bizOperationLogService = Evn
				.getBean("bizOperationLogService");
		bizOperationLogService.saveBizOperationLog();
		bizOperationLogService.saveModuleOperationLog();
		bizOperationLogService.saveSqlAnalyseLog();
	}

}
