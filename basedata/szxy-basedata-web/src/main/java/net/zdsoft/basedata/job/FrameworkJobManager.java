package net.zdsoft.basedata.job;

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
public class FrameworkJobManager {
	private static final Logger log = Logger.getLogger(FrameworkJobManager.class);

	@PostConstruct
	public void postDone() {
		if (Evn.isScheduler()) {
			JobDetail job = JobBuilder.newJob(TaskJobRun.class).build();
//            Trigger trigger = TriggerBuilder.newTrigger().withSchedule(
//                    SimpleScheduleBuilder.repeatSecondlyForever(Evn.getInt(ConstantBasedata.TASK_START_PERIOD))).startNow().build();
            Trigger trigger = TriggerBuilder.newTrigger().startNow().build();
            Evn.addJob(job, trigger);
            
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
    		int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
            int second = cal.get(Calendar.SECOND);
            //时分秒（毫秒数）
            long millisecond = hour*60*60*1000 + minute*60*1000 + second*1000;
            //凌晨00:00:00
            cal.setTimeInMillis(cal.getTimeInMillis()-millisecond);
            
            job = JobBuilder.newJob(TaskJobDeleteRun.class).build();
            trigger = TriggerBuilder.newTrigger().withSchedule(
                    SimpleScheduleBuilder.repeatHourlyForever(24)).startAt(cal.getTime()).build();
            Evn.addJob(job, trigger);
            log.info("------导入任务调度启动");
		}
	}
}
