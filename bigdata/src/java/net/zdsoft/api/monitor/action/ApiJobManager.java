package net.zdsoft.api.monitor.action;

import java.util.Date;

import javax.annotation.PostConstruct;

import net.zdsoft.api.monitor.constant.ApiConstant;
import net.zdsoft.framework.config.Evn;

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

@Lazy(false)
@Component
public class ApiJobManager {
	private Logger log = Logger.getLogger(ApiJobManager.class);
	@PostConstruct
	public void postDone() {
		if (Evn.isScheduler()) {
			if (StringUtils.isNotBlank(Evn
					.getString(ApiConstant.API_MONITOR_TIME))) {
				log.error("------增加统计接口调用次数任务");
				JobDetail job = JobBuilder
						.newJob(ApiCensusJob.class)
						.withIdentity("ApiCensusJob").build();
				Trigger trigger = TriggerBuilder
						.newTrigger()
						.withIdentity("ApiCensusJob")
						.withSchedule(
								CronScheduleBuilder.cronSchedule(Evn
										.getString(ApiConstant.API_MONITOR_TIME)))
						.startAt(DateUtils.addSeconds(new Date(), 10)).build();
				Evn.addJob(job, trigger);
			} else {
				log.error("------没有设置接口监控 时间，跳过定时任务");
			}
		}
	}
}
