package net.zdsoft.eclasscard.data.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import net.zdsoft.eclasscard.data.service.EccInfoService;
import net.zdsoft.eclasscard.data.websocket.constant.EClassCardConstant;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.StringUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * 
 * @author jiang feng
 *
 */
@Component
@Service("eClassCardWebSocketHandler")
public class EClassCardWebSocketHandler implements WebSocketHandler {
	

	private static final Logger logger = Logger
			.getLogger(EClassCardWebSocketHandler.class);

	private static final List<WebSocketSession> cardList = Collections
			.synchronizedList(new ArrayList<WebSocketSession>());
	

	@Autowired
	private EccInfoService eccInfoService;
	
	/**
	 * 建立websocket连接时调用该方法
	 * @see org.springframework.web.socket.WebSocketHandler#afterConnectionEstablished(org.springframework.web.socket.WebSocketSession)
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session)
			throws Exception {
		logger.debug("connect to the websocket success......");
		// 获取设备号
		String sid = (String) session.getAttributes().get("sid");
		String isApp = (String) session.getAttributes().get("isApp");
		if (StringUtils.isNotBlank(sid))
			session.getAttributes()
					.put(WebSocketConstant.E_CLASS_CARD_SID, sid);
		cardList.add(session);
		if (StringUtils.isNotBlank(sid) && "1".equals(isApp)){
			eccInfoService.updateOnLineStatus(sid, 2);
			RedisUtils.sadd(EClassCardConstant.ECLASSCARD_REDIS_ONLINE_MEMBERS, sid);
		}
	}

	
	/**
	 * 客户端调用websocket.send时候，会调用该方法,进行数据通信
	 * @see org.springframework.web.socket.WebSocketHandler#handleMessage(org.springframework.web.socket.WebSocketSession, org.springframework.web.socket.WebSocketMessage)
	 */
	@Override
	public void handleMessage(WebSocketSession session,
			WebSocketMessage<?> message) throws Exception {
		if (session.isOpen()) {
			Set<String> memsets = RedisUtils.smembers(EClassCardConstant.ECLASSCARD_REDIS_ONLINE_MEMBERS);
			session.sendMessage(message);
			String sid = (String) session.getAttributes().get("sid");
			if(!memsets.contains(sid)){
				String isApp = (String) session.getAttributes().get("isApp");
				if (StringUtils.isNotBlank(sid) && "1".equals(isApp)){
					eccInfoService.updateOnLineStatus(sid, 2);
					RedisUtils.sadd(EClassCardConstant.ECLASSCARD_REDIS_ONLINE_MEMBERS, sid);
				}
			}
		}
	}
	/**
	 * 传输过程出现异常时，调用该方法
	 * @see org.springframework.web.socket.WebSocketHandler#handleTransportError(org.springframework.web.socket.WebSocketSession, java.lang.Throwable)
	 */
	@Override
	public void handleTransportError(WebSocketSession session,
			Throwable exception) throws Exception {
		if (session.isOpen()) {
			session.close();
		}
		logger.debug("websocket connection error......");
		cardList.remove(session);
	}
	
	/**
	 * 建立websocket连接时调用该方法
	 * @see org.springframework.web.socket.WebSocketHandler#afterConnectionEstablished(org.springframework.web.socket.WebSocketSession)
	 */
	@Override
	public void afterConnectionClosed(WebSocketSession session,
			CloseStatus closeStatus) throws Exception {
		logger.debug("websocket connection closed......");
		String sid = (String) session.getAttributes().get("sid");
		String isApp = (String) session.getAttributes().get("isApp");
		cardList.remove(session);
		if (StringUtils.isNotBlank(sid) && "1".equals(isApp)){
			RedisUtils.srem(EClassCardConstant.ECLASSCARD_REDIS_ONLINE_MEMBERS, sid);
			eccInfoService.updateOnLineStatus(sid, 1);
		}
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

	/**
	 * 给电子班牌发送消息
	 *
	 * @param sidList
	 * @param message
	 */
	public void sendMessageToCard(List<String> sidList, TextMessage message) {
		logger.info("准备发送消息至班牌");
		for (WebSocketSession card : cardList) {
			if (CollectionUtils.isNotEmpty(sidList)) {
				if (sidList.contains(card.getAttributes().get(
						WebSocketConstant.E_CLASS_CARD_SID))) {
					try {
						if (card.isOpen()) {
							card.sendMessage(message);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else {
				try {
					if (card.isOpen()) {
						card.sendMessage(message);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
