package net.zdsoft.eclasscard.data.websocket.listener;

import net.zdsoft.eclasscard.data.websocket.EClassCardWebSocketHandler;
import net.zdsoft.eclasscard.data.websocket.dto.EClassCardDto;
import net.zdsoft.framework.config.Evn;

import org.springframework.web.socket.TextMessage;

import redis.clients.jedis.JedisPubSub;

import com.alibaba.fastjson.JSONObject;

public class EClassCardRedisListener extends JedisPubSub{
	
	@Override
	public void onMessage(String channel, String message) {
		EClassCardDto cardDto = JSONObject.toJavaObject(JSONObject.parseObject(message), EClassCardDto.class);
		((EClassCardWebSocketHandler) Evn.getBean("eClassCardWebSocketHandler")).sendMessageToCard(cardDto.cardList, new TextMessage(cardDto.message));
	}

}
