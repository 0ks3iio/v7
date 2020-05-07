package net.zdsoft.system.action;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.system.entity.SessionLog;
import net.zdsoft.system.service.SessionLogService;

@DisallowConcurrentExecution
public class SessionLogDataJob implements Job {
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		new Thread(() -> {
			final String TIME_KEY = "time.key.";
			SessionLogService sessionLogService = (SessionLogService) Evn.getBean("sessionLogService");
			Calendar c = Calendar.getInstance();
			// 5分钟记录一次
			String dayOfTimeKey = DateFormatUtils.format(c, "yyyy-MM-dd");
			c.add(Calendar.DAY_OF_YEAR, -1);
			String yestodayOfTimeKey = DateFormatUtils.format(c, "yyyy-MM-dd");
			Set<String> keys = RedisUtils.hkeys(Constant.SESSION_COUNT_PREFIX + TIME_KEY + dayOfTimeKey);
			Set<String> keys2 = RedisUtils.hkeys(Constant.SESSION_COUNT_PREFIX + TIME_KEY + yestodayOfTimeKey);
			keys.addAll(keys2);
			try {
				for (String key : keys) {
					String count = RedisUtils.hget(Constant.SESSION_COUNT_PREFIX + TIME_KEY + dayOfTimeKey, key);
					if (count != null) {
						SessionLog log = new SessionLog();
						log.setId(EntityUtils.getCode32(key));
						log.setLogTime(key);
						log.setSessionCount(NumberUtils.toInt(count));
						Date date =new Date();
						log.setCreationTime(date);
						log.setModifyTime(date);
						sessionLogService.save(log);
						RedisUtils.hdel(Constant.SESSION_COUNT_PREFIX + TIME_KEY + dayOfTimeKey, key);
					}
				}
			} catch (Exception e) {
				
			}
		}).start();
	}
}
