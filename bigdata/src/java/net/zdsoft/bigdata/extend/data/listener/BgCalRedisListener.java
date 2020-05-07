package net.zdsoft.bigdata.extend.data.listener;

import net.zdsoft.bigdata.extend.data.task.BgCalQuartzJobService;
import net.zdsoft.framework.config.Evn;

import org.apache.log4j.Logger;
import org.quartz.SchedulerException;

import redis.clients.jedis.JedisPubSub;

import com.alibaba.fastjson.JSONObject;

public class BgCalRedisListener extends JedisPubSub {

	public static final Logger logger = Logger
			.getLogger(BgCalRedisListener.class);

	@Override
	public void onMessage(String channel, String message) {
		JSONObject json = JSONObject.parseObject(message);
		String jobId = json.getString("jobId");
		String operation = json.getString("operation");
		String cron = json.getString("cron");
		if ("add".equals(operation)) {
			try {
				Evn.<BgCalQuartzJobService> getBean("bgCalQuartzJobService").addCalJob(
						jobId, cron);
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
		} else if ("modify".equals(operation)) {
			try {
				Evn.<BgCalQuartzJobService> getBean("bgCalQuartzJobService")
						.modifyCalJob(jobId, cron);
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
		} else if ("delete".equals(operation)) {
			try {
				Evn.<BgCalQuartzJobService> getBean("bgCalQuartzJobService")
						.deleteCalJob(jobId);
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
		}
	}
}
