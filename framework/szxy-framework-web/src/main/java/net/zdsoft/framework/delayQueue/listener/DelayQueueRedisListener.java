package net.zdsoft.framework.delayQueue.listener;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.delayQueue.DelayQueueService;
import redis.clients.jedis.JedisPubSub;

public class DelayQueueRedisListener extends JedisPubSub {

	@Override
	public void onMessage(String channel, String message) {
		((DelayQueueService) Evn.getBean("delayQueueService")).removeTimeoutItem(message);
	}

}
