package net.zdsoft.framework.listener;

import java.io.*;
import java.util.Calendar;
import java.util.Collections;
import java.util.Set;
import java.util.Timer;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import net.zdsoft.framework.config.Evn;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import net.zdsoft.framework.config.StaticDataCache;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.utils.RedisUtils;

public class SessionUserListener implements HttpSessionListener {

	private static final String SERVER_UUID = StaticDataCache.getServerUuid();

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		// 本服务器的在线数+1
		RedisUtils.incrby(Constant.SESSION_COUNT_PREFIX + SERVER_UUID, 1);
		RedisUtils.expire(Constant.SESSION_COUNT_PREFIX + SERVER_UUID, RedisUtils.TIME_TEN_MINUTES);
		// 本服务器的UUID记录到列表中
		RedisUtils.sadd(Constant.SESSION_COUNT_PREFIX_SET, SERVER_UUID);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		if (Evn.getBoolean("session.logout.log")) {
			try {
				OutputStream stream = new FileOutputStream(new File("/opt/data/session.log"));
				new Exception("").printStackTrace(new PrintStream(stream, true));
			} catch (FileNotFoundException e) {
				//ignore
			}
		}

		String key = Constant.SESSION_COUNT_PREFIX + SERVER_UUID;
		// 本服务器在线数-1
		long count = RedisUtils.decrby(key, 1);
		if (count <= 0) {
			RedisUtils.srem(Constant.SESSION_COUNT_PREFIX_SET, SERVER_UUID);
			RedisUtils.del(key);
		} else {
			RedisUtils.expire(Constant.SESSION_COUNT_PREFIX + SERVER_UUID, RedisUtils.TIME_TEN_MINUTES);
			RedisUtils.sadd(Constant.SESSION_COUNT_PREFIX_SET, SERVER_UUID);
		}
	}

	static {
		/**
		 * 5分钟做一次检查，看看服务器是否有失效的用户数
		 */
		new Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				RedisUtils.expire(Constant.SESSION_COUNT_PREFIX + SERVER_UUID, RedisUtils.TIME_TEN_MINUTES);
				Set<String> set = RedisUtils.getSet(Constant.SESSION_COUNT_PREFIX_SET, () -> Collections.EMPTY_SET);
				int count = 0;
				for (String x : set) {
					String key = Constant.SESSION_COUNT_PREFIX + x;
					if (RedisUtils.notExists(key) || NumberUtils.toInt(RedisUtils.get(key)) == 0) {
						// 将此服务器的UUID从列表中移除
						RedisUtils.srem(Constant.SESSION_COUNT_PREFIX_SET, x);
						RedisUtils.del(key);
					} else {
						String size = RedisUtils.get(key);
						count += NumberUtils.toInt(size, 0);
					}
				}
				Calendar c = Calendar.getInstance();
				// 5分钟记录一次
				int timeMinute = (c.get(Calendar.MINUTE) / 5) * 5;
				String timeKey = DateFormatUtils.format(c.getTime(), "yyyy-MM-dd HH:")
						+ StringUtils.leftPad(String.valueOf(timeMinute), 2, "0");
				String dayOfTimeKey = DateFormatUtils.format(c.getTime(), "yyyy-MM-dd");
				// 记录这个时间的最大记录数
				int preCount = NumberUtils.toInt(RedisUtils.hget(Constant.SESSION_COUNT_PREFIX + "time.key", timeKey));
				//记录这段时间内最大的用户数，如果都没有任何用户数（即为0）也记录下来
				if (count > preCount || (count == 0 && preCount == 0)) {
					RedisUtils.hset(Constant.SESSION_COUNT_PREFIX + "time.key." + dayOfTimeKey, timeKey,
							String.valueOf(count));
					// 一周后，如果没有被取走，则失效
					RedisUtils.expire(Constant.SESSION_COUNT_PREFIX + "time.key." + dayOfTimeKey,
							RedisUtils.TIME_ONE_WEEK);
				}
			}
		}, 0, RedisUtils.TIME_ONE_MINUTE * 5000L);
	}
}
