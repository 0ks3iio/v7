package net.zdsoft.bigdata.extend.data.task;

import net.zdsoft.bigdata.extend.data.biz.BgCalService;
import net.zdsoft.framework.config.Evn;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class BgCalQuartzJob implements Job {

	public static final Logger logger = Logger.getLogger(BgCalQuartzJob.class);

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		String jobId = dataMap.getString("jobId");
		logger.info("执行大数据计算定时任务" + jobId);
		BgCalService bgCalService = Evn
				.<BgCalService> getBean("bgCalService");
		bgCalService.dealWarningCal(jobId);
	}

}
