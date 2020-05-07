package net.zdsoft.eclasscard.data.websocket.service;

import java.util.List;

public interface EClassCardWebSocketService {

	// 根据班牌号推送消息
	public void sendToCard(List<String> cardList, String jsonMessage);

}
