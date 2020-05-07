package net.zdsoft.bigdata.extend.data.task;

import java.util.List;

import net.zdsoft.bigdata.extend.data.entity.WarningProject;
import net.zdsoft.bigdata.extend.data.listener.BgCalChannelConstant;
import net.zdsoft.bigdata.extend.data.service.WarningProjectService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.RedisUtils;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("bgCalQuartzJobService")
public class BgCalQuartzJobServiceImpl implements BgCalQuartzJobService{

	public static final Logger logger = Logger.getLogger(BgCalQuartzJobServiceImpl.class);

	private static final String GROUP_BG_CAL = "group-bg-cal";

	@Autowired
	private WarningProjectService warningProjectService;
	
	@Override
	public void initCalJob() throws SchedulerException {
		List<WarningProject> warningList = warningProjectService
				.findAvailableProject();
		for (WarningProject project : warningList) {
			Json json = new Json();
			json.put("jobId", project.getId());
			json.put("cron", project.getScheduleParam());
			json.put("operation", "add");
			RedisUtils.publish(BgCalChannelConstant.BG_CAL_REDIS_CHANNEL,
					json.toJSONString());
		}
	}
	
	/**
	 * @Description: 添加一个定时任务
	 * @param jobId
	 *            业务id
	 * @param cron
	 *            时间设置，参考quartz说明文档
	 */
	@Override
	public void addCalJob(String jobId,String cron)
			throws SchedulerException {
		// 创建任务
		JobDetail jobDetail = JobBuilder.newJob(BgCalQuartzJob.class)
				.withIdentity(jobId,GROUP_BG_CAL).build();
		// 传入参数
		jobDetail.getJobDataMap().put("jobId", jobId);

		// 创建触发器 按照页面传来的cron执行
		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity("trigger_" + jobId, GROUP_BG_CAL)
				.withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
		Scheduler scheduler = Evn.getScheduler();
		// 将任务及其触发器放入调度器
		scheduler.scheduleJob(jobDetail, trigger);
		// 调度器开始调度任务
		scheduler.start();
		logger.info("增加大数据计算定时任务成功" + jobId + "&&&&" + cron);
	}

	/**
	 * @Description: 修改一个任务的触发时间
	 * 
	 * @param jobName
	 * @param cron
	 *            时间设置，参考quartz说明文档
	 */
	@Override
	public void modifyCalJob(String jobId, String cron)
			throws SchedulerException {
		Scheduler scheduler = Evn.getScheduler();
		TriggerKey triggerKey = TriggerKey.triggerKey("trigger_" + jobId,
				GROUP_BG_CAL);
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		if (trigger == null) {
			addCalJob(jobId, cron);
			return;
		}
		String oldTime = trigger.getCronExpression();
		if (!oldTime.equalsIgnoreCase(cron)) {
			// 触发器
			TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder
					.newTrigger();
			// 触发器名,触发器组
			triggerBuilder.withIdentity("trigger_" + jobId, GROUP_BG_CAL);
			triggerBuilder.startNow();
			// 触发器时间设定
			triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
			// 创建Trigger对象
			trigger = (CronTrigger) triggerBuilder.build();
			// 方式一 ：修改一个任务的触发时间
			scheduler.rescheduleJob(triggerKey, trigger);
			logger.info("修改大数据计算定时任务成功" + jobId + "&&&&" + cron);
		}
	}

	/**
	 * @Description: 移除一个任务
	 * 
	 * @param jobId
	 *            业务id
	 */
	@Override
	public void deleteCalJob(String jobId)
			throws SchedulerException {
		Scheduler scheduler = Evn.getScheduler();

		TriggerKey triggerKey = TriggerKey.triggerKey("trigger_" + jobId,
				GROUP_BG_CAL);

		scheduler.pauseTrigger(triggerKey);// 停止触发器
		scheduler.unscheduleJob(triggerKey);// 移除触发器
		scheduler.deleteJob(JobKey.jobKey(jobId, GROUP_BG_CAL));// 删除任务
		logger.info("删除大数据计算定时任务成功" + jobId);
	}
}
