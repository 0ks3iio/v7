package net.zdsoft.bigdata.taskScheduler.listener;

import net.zdsoft.bigdata.taskScheduler.task.EtlQuartzJobService;
import net.zdsoft.framework.config.Evn;

import org.apache.log4j.Logger;
import org.quartz.SchedulerException;

import redis.clients.jedis.JedisPubSub;

import com.alibaba.fastjson.JSONObject;

public class EtlRedisListener extends JedisPubSub {

	public static final Logger logger = Logger
			.getLogger(EtlRedisListener.class);

	@Override
	public void onMessage(String channel, String message) {
		JSONObject json = JSONObject.parseObject(message);
		String jobId = json.getString("jobId");
		String operation = json.getString("operation");
		String cron = json.getString("cron");
		if ("add".equals(operation)) {
			logger.info("开始增加etl定时任务" + jobId + "&&&&" + cron);
			try {
				Evn.<EtlQuartzJobService> getBean("etlQuartzJobService").addEtlJob(
						jobId, cron);
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
		} else if ("modify".equals(operation)) {
			logger.info("开始修改etl定时任务" + jobId + "&&&&" + cron);
			try {
				Evn.<EtlQuartzJobService> getBean("etlQuartzJobService")
						.modifyEtlJob(jobId, cron);
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
		} else if ("delete".equals(operation)) {
			logger.info("开始删除etl定时任务" + jobId);
			try {
				Evn.<EtlQuartzJobService> getBean("etlQuartzJobService")
						.deleteEtlJob(jobId);
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
		}
	}
}
