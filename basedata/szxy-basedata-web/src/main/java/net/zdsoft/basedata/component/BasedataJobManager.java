package net.zdsoft.basedata.component;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import net.zdsoft.basedata.entity.ConstantBasedata;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.DateUtils;

/**
 * @author shenke
 * @since 2017/2/7 14:24
 */
@Component
@Lazy(false)
public class BasedataJobManager {

	private static final Logger log = LoggerFactory.getLogger(ClassFlowImportJob.class);

	@PostConstruct
	public void postDoe() {

		// 日志记录，任意一台都可以做
		JobDetail logJob = JobBuilder.newJob(OperationLogDataJob.class).build();
		Trigger logTrigger = TriggerBuilder.newTrigger().withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(10))
				.startAt(DateUtils.addMinute(new Date(), 1)).build();
		// 表示任意一台服务器都能运行此任务
		Evn.addJob(logJob, logTrigger, false);

		String period = Evn.getString(ConstantBasedata.IMPORT_CLASS_FLOW_PERIOD);
		if (StringUtils.isNotBlank(period)) {
			log.info("------转班任务启动");
			int period2 = NumberUtils.toInt(period, 60);
			System.out.println("[转班任务启动]");
			JobDetail job = JobBuilder.newJob(ClassFlowImportJob.class).build();
			Trigger trigger = TriggerBuilder.newTrigger()
					.withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(period2)).startNow().build();
			Evn.addJob(job, trigger, true);
		}

		if (StringUtils.isNotBlank(Evn.getString(ConstantBasedata.IMPORT_STU_FLOW_OUT_PERIOD))) {
			log.info("------学生异动调出任务启动");
			JobDetail job = JobBuilder.newJob(StudentOutImportJob.class).build();
			Trigger trigger = TriggerBuilder.newTrigger()
					.withSchedule(SimpleScheduleBuilder
							.repeatSecondlyForever(Evn.getInt(ConstantBasedata.IMPORT_STU_FLOW_OUT_PERIOD)))
					.startNow().build();
			Evn.addJob(job, trigger, true);
		}
		if (StringUtils.isNotBlank(Evn.getString(ConstantBasedata.IMPORT_STU_FLOW_IN_PERIOD))) {
			log.info("------学生异动调入任务启动");
			JobDetail job = JobBuilder.newJob(StudentInImportJop.class).build();
			Trigger trigger = TriggerBuilder.newTrigger()
					.withSchedule(SimpleScheduleBuilder
							.repeatSecondlyForever(Evn.getInt(ConstantBasedata.IMPORT_STU_FLOW_IN_PERIOD)))
					.startNow().build();
			Evn.addJob(job, trigger, true);
		}
	}
}
