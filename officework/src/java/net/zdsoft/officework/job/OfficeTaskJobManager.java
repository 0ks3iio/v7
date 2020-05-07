package net.zdsoft.officework.job;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;

import net.zdsoft.framework.config.Evn;

import org.apache.log4j.Logger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
public class OfficeTaskJobManager {
	private static final Logger log = Logger.getLogger(OfficeTaskJobManager.class);

	@PostConstruct
	public void run() {
		if (Evn.isScheduler()){
			//华三健康数据获取
			JobDetail h3cjob = JobBuilder.newJob(TaskJobHealthInfoRun.class).build();
			Calendar h3cCalendar = Calendar.getInstance();
			h3cCalendar.set(Calendar.MINUTE, 0);
			h3cCalendar.set(Calendar.SECOND, 0);
			h3cCalendar.add(Calendar.HOUR, 1);
			
			Trigger h3cTrigger = TriggerBuilder.newTrigger().withSchedule(
					SimpleScheduleBuilder.repeatHourlyForever(1)).startAt(h3cCalendar.getTime()).build();
			
			System.out.println("定时器任务时间 "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(h3cCalendar.getTime()));
			Evn.addJob(h3cjob, h3cTrigger,false);
		}
//		if (OfficeHealthUtils.isDemo()){
//			 JobDetail h3Democjob = JobBuilder.newJob(RingAttanceDemoJob.class).build();
//            Trigger h3cDemoTrigger = TriggerBuilder.newTrigger().withSchedule(
//                    SimpleScheduleBuilder.repeatMinutelyForever(1)).startAt(new Date()).build();
//            
//            Evn.addJob(h3Democjob, h3cDemoTrigger);
//		}
	}
}
