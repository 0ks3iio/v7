package net.zdsoft.eclasscard.data.websocket.service.impl;

import java.util.List;

import net.zdsoft.eclasscard.data.websocket.constant.EClassCardConstant;
import net.zdsoft.eclasscard.data.websocket.dto.EClassCardDto;
import net.zdsoft.eclasscard.data.websocket.service.EClassCardWebSocketService;
import net.zdsoft.framework.utils.RedisUtils;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

@Service("eClassCardWebSocketService")
public class EClassCardWebSocketServiceImpl implements
		EClassCardWebSocketService {

	// 根据班牌号推送消息
	public void sendToCard(List<String> cardList, String jsonMessage) {
		EClassCardDto cardDto = new EClassCardDto();
		cardDto.setCardList(cardList);
		cardDto.setMessage(jsonMessage);
		RedisUtils.publish(EClassCardConstant.ECLASSCARD_REDIS_CHANNEL,
				JSONObject.toJSONString(cardDto));
		// eClassCardWebSocketHandler.sendMessageToCard(cardList,new
		// TextMessage(jsonMessage));
	}

}
