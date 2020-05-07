package net.zdsoft.bigdata.taskScheduler.task;

import net.zdsoft.bigdata.taskScheduler.service.EtlJobService;
import net.zdsoft.framework.config.Evn;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class EtlQuartzJob implements Job {

	public static final Logger logger = Logger.getLogger(EtlQuartzJob.class);

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		String jobId = dataMap.getString("jobId");
		logger.info("执行Etl定时任务" + jobId);
		EtlJobService etlJobService = Evn
				.<EtlJobService> getBean("etlJobService");

		etlJobService.dealEtlQuantzJob(jobId);
	}

}
