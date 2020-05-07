package net.zdsoft.bigdata.daq.data.job;

import net.zdsoft.bigdata.daq.data.service.ApiDataDaqService;
import net.zdsoft.bigdata.daq.data.service.BizOperationLogService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.DateUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

@DisallowConcurrentExecution
public class ApiDataDaqJob implements Job {
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		ApiDataDaqService apiDataDaqService = Evn
				.getBean("apiDataDaqService");
		apiDataDaqService.saveApiData();
	}

}
