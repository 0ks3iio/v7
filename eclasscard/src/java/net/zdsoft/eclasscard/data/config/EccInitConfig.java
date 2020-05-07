package net.zdsoft.eclasscard.data.config;

import javax.annotation.PostConstruct;

import net.zdsoft.eclasscard.data.websocket.constant.EClassCardConstant;
import net.zdsoft.eclasscard.data.websocket.listener.EClassCardRedisListener;
import net.zdsoft.framework.utils.RedisUtils;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import redis.clients.jedis.JedisPubSub;

@Component
@Lazy(false)
public class EccInitConfig {
	private static Logger logger = Logger.getLogger(EccInitConfig.class);
	@PostConstruct
	public void postDone() {
		// 订阅电子班牌通道
		JedisPubSub eclassCardPubSub = new EClassCardRedisListener();
		new Thread(new Runnable() {
			public void run() {
				logger.info("订阅Redis电子班牌通道");
				RedisUtils.subscribe(eclassCardPubSub,
						EClassCardConstant.ECLASSCARD_REDIS_CHANNEL);
			}
		}).start();
	}
}
