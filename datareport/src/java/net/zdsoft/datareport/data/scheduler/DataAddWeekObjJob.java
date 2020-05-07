package net.zdsoft.datareport.data.scheduler;

import net.zdsoft.datareport.data.service.DataReportInfoService;
import net.zdsoft.framework.config.Evn;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataAddWeekObjJob implements Job{

	private static final Logger log = LoggerFactory.getLogger(DataAddWeekObjJob.class);

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		DataReportInfoService dataReportInfoService = Evn.getBean("dataReportInfoService");
		
	}
}
