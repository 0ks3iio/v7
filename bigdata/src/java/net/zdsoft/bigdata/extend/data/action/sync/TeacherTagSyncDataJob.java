package net.zdsoft.bigdata.extend.data.action.sync;

import net.zdsoft.bigdata.extend.data.biz.BgCalService;
import net.zdsoft.framework.config.Evn;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class TeacherTagSyncDataJob implements Job {
	private Logger log = Logger.getLogger(TeacherTagSyncDataJob.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		BgCalService bgCalService = (BgCalService) Evn
				.getBean("bgCalService");
		try {
			bgCalService.dealUserTagCal("teacher");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
