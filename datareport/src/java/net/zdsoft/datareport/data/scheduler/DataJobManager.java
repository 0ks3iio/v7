package net.zdsoft.datareport.data.scheduler;

import java.util.Date;

import javax.annotation.PostConstruct;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.DateUtils;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
public class DataJobManager {

	private static final Logger log = LoggerFactory.getLogger(DataJobManager.class);
	
	@PostConstruct
	public void addPeriod() {
		if (Evn.isScheduler()) {
			JobDetail addJob = JobBuilder.newJob(DataAddUndisposedJob.class).build();
			Trigger addTrigger = TriggerBuilder.newTrigger().withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(0))
					.startAt(DateUtils.addMinute(new Date(),1)).build();
			Evn.addJob(addJob, addTrigger,false);
			
			//按周定时的任务
//			JobDetail addWeekJob = JobBuilder.newJob(DataReportAddWeekObjJob.class).build();
//			CronScheduleBuilder weekBuilder = CronScheduleBuilder.cronSchedule("0 0 9 ? * MON");
//		    CronTrigger addWeekTrigger = TriggerBuilder.newTrigger().withIdentity("trigger_2", "tGroup2")
//		                .withSchedule(weekBuilder).startAt(DateUtils.addHour(new Date(),9)).build();
//			Evn.addJob(addWeekJob, addWeekTrigger,false);
			
			//按月定时的任务
//			JobDetail addMonthJob = JobBuilder.newJob(DataReportAddMonthObjJob.class).build();
//			CronScheduleBuilder monthBuilder = CronScheduleBuilder.cronSchedule("0 0 9 1 * *");
//		    CronTrigger addMonthTrigger = TriggerBuilder.newTrigger().withIdentity("trigger_3", "tGroup3")
//		                .withSchedule(monthBuilder).startAt(DateUtils.addHour(new Date(),9)).build();
//			Evn.addJob(addMonthJob, addMonthTrigger,false);
		}
	}
}
