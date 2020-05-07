package net.zdsoft.officework.job;

import net.zdsoft.eclasscard.remote.service.EclasscardRemoteService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.officework.utils.OfficeHealthUtils;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RingAttanceDemoJob implements Job{
	private static final Logger log = LoggerFactory.getLogger(RingAttanceDemoJob.class);
	
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		EclasscardRemoteService eclasscardRemoteService = Evn.getBean("eclasscardRemoteService");
		try {
			eclasscardRemoteService.attancePerMinute(OfficeHealthUtils.getUnitIdMap());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
