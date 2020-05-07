package net.zdsoft.system.action;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.DateUtils;

@Component
@Lazy(false)
public class SystemJobManager {

	private static final Logger log = LoggerFactory.getLogger(SystemJobManager.class);

	@PostConstruct
	public void postDoe() {
		// 日志记录，任意一台都可以做
		JobDetail logJob = JobBuilder.newJob(SessionLogDataJob.class).build();
		Trigger logTrigger = TriggerBuilder.newTrigger().withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(60*5))
				.startAt(DateUtils.addMinute(new Date(), 1)).build();
		// 表示任意一台服务器都能运行此任务
		Evn.addJob(logJob, logTrigger, false);
	}

}
