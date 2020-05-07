package net.zdsoft.datareport.data.scheduler;

import net.zdsoft.datareport.data.service.DataReportInfoService;
import net.zdsoft.framework.config.Evn;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataAddUndisposedJob implements Job{

	private static final Logger log = LoggerFactory.getLogger(DataAddUndisposedJob.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		DataReportInfoService dataReportInfoService = Evn.getBean("dataReportInfoService");
		log.info("加入发布问卷队列和修改问卷状态");
		dataReportInfoService.addTaskQueue();
	}
}
