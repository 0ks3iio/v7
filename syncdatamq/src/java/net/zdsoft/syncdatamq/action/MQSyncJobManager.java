package net.zdsoft.syncdatamq.action;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.syncdatamq.entity.ConstantSyncData;

@Lazy(false)
@Component
public class MQSyncJobManager {
	private Logger log = Logger.getLogger(MQSyncJobManager.class);

	@PostConstruct
	public void postDone() {
		if (Evn.isScheduler()) {
			if (StringUtils.isNotBlank(Evn
					.getString(ConstantSyncData.SYNC_DATA_BASE))) {
				log.error("------增加基础数据发送同步任务");
				JobDetail job = JobBuilder
						.newJob(BasedataSyncSendDataJob.class)
						.withIdentity("BasedataSyncSendDataJob").build();
				Trigger trigger = TriggerBuilder
						.newTrigger()
						.withIdentity("BasedataSyncSendDataJob")
						.withSchedule(
								CronScheduleBuilder.cronSchedule(Evn
										.getString(ConstantSyncData.SYNC_DATA_BASE)))
						.startAt(DateUtils.addSeconds(new Date(), 10)).build();
				Evn.addJob(job, trigger);

				log.error("------增加基础数据接收同步任务");
				JobDetail jobRec = JobBuilder
						.newJob(BasedataSyncReceiveDataJob.class)
						.withIdentity("BasedataSyncReceiveDataJob").build();
				Trigger triggerRec = TriggerBuilder
						.newTrigger()
						.withIdentity("BasedataSyncReceiveDataJob")
						.withSchedule(
								CronScheduleBuilder.cronSchedule(Evn
										.getString(ConstantSyncData.SYNC_DATA_BASE)))
						.startAt(DateUtils.addSeconds(new Date(), 10)).build();
				Evn.addJob(jobRec, triggerRec);
			} else {
				log.error("------没有设置基础数据同步时间，跳过定时任务");
			}
		}
	}
}
