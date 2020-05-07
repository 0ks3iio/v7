package net.zdsoft.bigdata.taskScheduler.task;

import net.zdsoft.bigdata.taskScheduler.EtlType;
import net.zdsoft.bigdata.taskScheduler.entity.EtlJob;
import net.zdsoft.bigdata.taskScheduler.listener.EtlChannelConstant;
import net.zdsoft.bigdata.taskScheduler.service.EtlJobService;
import net.zdsoft.bigdata.datax.entity.DataxJob;
import net.zdsoft.bigdata.datax.service.DataxJobService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.RedisUtils;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("etlQuartzJobService")
public class EtlQuartzJobServiceImpl implements EtlQuartzJobService {

    public static final Logger logger = Logger
            .getLogger(EtlQuartzJobServiceImpl.class);

    private static final String GROUP_ETL = "group-etl";

    @Autowired
    private EtlJobService etlJobService;
    @Autowired
    private DataxJobService dataxJobService;

    @Override
    public void initEtlJob() throws SchedulerException {
        List<EtlJob> jobList = etlJobService.findScheduledEtlJobs();
        for (EtlJob job : jobList) {
            Json json = new Json();
            json.put("jobId", job.getId());
            json.put("cron", job.getScheduleParam());
            json.put("operation", "add");
            if (job.getEtlType() == EtlType.KETTLE.getValue()) {
                RedisUtils.publish(EtlChannelConstant.ETL_KETTLE_REDIS_CHANNEL,
                        json.toJSONString());
            } else if (job.getEtlType() == EtlType.KYLIN.getValue()) {
                RedisUtils.publish(EtlChannelConstant.ETL_KYLIN_REDIS_CHANNEL,
                        json.toJSONString());
            } else if (job.getEtlType() == EtlType.SHELL.getValue()) {
                RedisUtils.publish(EtlChannelConstant.ETL_SHELL_REDIS_CHANNEL,
                        json.toJSONString());
            } else if (job.getEtlType() == EtlType.SPARK.getValue()) {
                RedisUtils.publish(EtlChannelConstant.ETL_SPARK_REDIS_CHANNEL,
                        json.toJSONString());
            } else if (job.getEtlType() == EtlType.FLINK.getValue()) {
				RedisUtils.publish(EtlChannelConstant.ETL_FLINK_REDIS_CHANNEL,
						json.toJSONString());
			} else if (job.getEtlType() == EtlType.PYTHON.getValue()) {
				RedisUtils.publish(EtlChannelConstant.ETL_PYTHON_REDIS_CHANNEL,
						json.toJSONString());
			} else if (job.getEtlType() == EtlType.GROUP.getValue()) {
                RedisUtils.publish(EtlChannelConstant.ETL_GROUP_REDIS_CHANNEL,
                        json.toJSONString());
            }
        }
        // datax job
        List<DataxJob> dataxJobs = dataxJobService.findScheduledDataxJobs();
        for (DataxJob job : dataxJobs) {
            Json json = new Json();
            json.put("jobId", job.getId());
            json.put("cron", job.getScheduleParam());
            json.put("operation", "add");
            RedisUtils.publish(EtlChannelConstant.ETL_DATAX_REDIS_CHANNEL,
                    json.toJSONString());
        }
    }

    /**
     * @param jobId 业务id
     * @param cron  时间设置，参考quartz说明文档
     * @Description: 添加一个定时任务
     */
    @Override
    public void addEtlJob(String jobId, String cron) throws SchedulerException {
        // 创建任务
        JobDetail jobDetail = JobBuilder.newJob(EtlQuartzJob.class)
                .withIdentity(jobId, GROUP_ETL).build();
        // 传入参数
        jobDetail.getJobDataMap().put("jobId", jobId);

        // 创建触发器 按照页面传来的cron执行
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger_" + jobId, GROUP_ETL)
                .withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
        Scheduler scheduler = Evn.getScheduler();
        // 将任务及其触发器放入调度器
        scheduler.scheduleJob(jobDetail, trigger);
        // 调度器开始调度任务
        scheduler.start();
        logger.info("增加etl定时任务成功" + jobId + "&&&&" + cron);
    }


	/**
	 * @Description: 修改一个任务的触发时间
	 *
	 * @param cron
	 *            时间设置，参考quartz说明文档
	 */
	@Override
	public void modifyEtlJob(String jobId, String cron)
			throws SchedulerException {
		Scheduler scheduler = Evn.getScheduler();
		TriggerKey triggerKey = TriggerKey.triggerKey("trigger_" + jobId,
				GROUP_ETL);
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		if (trigger == null) {
			addEtlJob(jobId, cron);
			return;
		}
		String oldTime = trigger.getCronExpression();
		if (!oldTime.equalsIgnoreCase(cron)) {
			// 触发器
			TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder
					.newTrigger();
			// 触发器名,触发器组
			triggerBuilder.withIdentity("trigger_" + jobId, GROUP_ETL);
			triggerBuilder.startNow();
			// 触发器时间设定
			triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
			// 创建Trigger对象
			trigger = (CronTrigger) triggerBuilder.build();
			// 方式一 ：修改一个任务的触发时间
			scheduler.rescheduleJob(triggerKey, trigger);
			logger.info("修改etl定时任务成功" + jobId + "&&&&" + cron);
		}
	}

    /**
     * @param jobId 业务id
     * @Description: 移除一个任务
     */
    @Override
    public void deleteEtlJob(String jobId) throws SchedulerException {
        Scheduler scheduler = Evn.getScheduler();

        TriggerKey triggerKey = TriggerKey.triggerKey("trigger_" + jobId,
                GROUP_ETL);

        scheduler.pauseTrigger(triggerKey);// 停止触发器
        scheduler.unscheduleJob(triggerKey);// 移除触发器
        scheduler.deleteJob(JobKey.jobKey(jobId, GROUP_ETL));// 删除任务
        logger.info("删除etl定时任务成功" + jobId);
    }
}
