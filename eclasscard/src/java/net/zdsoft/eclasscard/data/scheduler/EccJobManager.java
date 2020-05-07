package net.zdsoft.eclasscard.data.scheduler;

import java.util.Date;

import javax.annotation.PostConstruct;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.DateUtils;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
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
public class EccJobManager {
	private static final Logger log = LoggerFactory.getLogger(EccJobManager.class);
	@PostConstruct
	public void addPeriod() {
		//只可以在一台上跑
//		if (Evn.isScheduler()) {//使用单台定时器参数，原参数"eclasscrad.scheduler.start"去掉
//			if ("true".equalsIgnoreCase(Evn.getString("eclasscrad.scheduler.start"))) {
			//启动服务1分钟，加入未考勤时间段，未显示的通知公告
			JobDetail addJob = JobBuilder.newJob(EccAddUndisposedJob.class).build();
			Trigger addTrigger = TriggerBuilder.newTrigger().withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(0))
					.startAt(DateUtils.addMinute(new Date(),1)).build();
			Evn.addJob(addJob, addTrigger,false);
			
			//每天早上4：00加入当天的上课，寝室考勤
			JobDetail addTodayJob = JobBuilder.newJob(EccAddAttPeriodEverydayJob.class).build();
			CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 4 * * ? *");
		    CronTrigger addTodayTrigger = TriggerBuilder.newTrigger().withIdentity("addToday", "eclasscard-timer")
		                .withSchedule(cronScheduleBuilder).startAt(DateUtils.addHour(new Date(),4)).build();//防止当天启动，4点又执行一次
			Evn.addJob(addTodayJob, addTodayTrigger,false);
			//每20分钟检查是否要下发人脸
			JobDetail addFacelowerJob = JobBuilder.newJob(EccFaceModifyLowerJob.class).build();
			CronScheduleBuilder faceLowerBuilder = CronScheduleBuilder.cronSchedule("0 0/20 * * * ? *");
			CronTrigger addFacelowerTrigger = TriggerBuilder.newTrigger().withIdentity("addFacelower", "eclasscard-timer")
					.withSchedule(faceLowerBuilder).startAt(DateUtils.addMinute(new Date(),2)).build();
			Evn.addJob(addFacelowerJob, addFacelowerTrigger,false);
			
			//每个半小时检查下开关机数据
			JobDetail addOpenCloseJob = JobBuilder.newJob(EccOpenCloseTimeJob.class).build();
			CronScheduleBuilder openCloseBuilder = CronScheduleBuilder.cronSchedule("0 0/30 * * * ? *");
			CronTrigger openCloseTrigger = TriggerBuilder.newTrigger().withIdentity("addOpenClose", "eclasscard-timer")
					.withSchedule(openCloseBuilder).startAt(DateUtils.addMinute(new Date(),2)).build();
			Evn.addJob(addOpenCloseJob, openCloseTrigger,false);
			
			//每天早上1：00 清除2周前的日志
			JobDetail clearLogJob = JobBuilder.newJob(EccClearLogJob.class).build();
			CronScheduleBuilder conClearLogBuilder = CronScheduleBuilder.cronSchedule("0 0 1 * * ? *");
		    CronTrigger clearLogTrigger = TriggerBuilder.newTrigger().withIdentity("addClearLog", "eclasscard-timer")
		                .withSchedule(conClearLogBuilder).startAt(DateUtils.addHour(new Date(),3)).build();//防止当天启动
			Evn.addJob(clearLogJob, clearLogTrigger,false);
			

//		}
	}
	

}
