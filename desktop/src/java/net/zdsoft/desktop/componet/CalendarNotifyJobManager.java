package net.zdsoft.desktop.componet;

import net.zdsoft.framework.config.Evn;
import org.apache.log4j.Logger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 日历提醒定时任务，每分钟扫一次，推送手机app
 * @author shenke
 * @since 2017.10.17
 */
@Component
@Lazy(false)
public class CalendarNotifyJobManager {

    private Logger logger = Logger.getLogger(CalendarNotifyJobManager.class);

    @PostConstruct
    public void execute() {
        if ( Evn.isScheduler() ) {
            JobDetail notifyJob = JobBuilder.newJob(CalendarNotifyJob.class).build();
            Trigger notifyJobTrigger = TriggerBuilder.newTrigger()
                    .withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(1))
                    .startNow().build();
            Evn.addJob(notifyJob, notifyJobTrigger, true);
            logger.info("日历提醒定时任务开启");
        }
    }
}
