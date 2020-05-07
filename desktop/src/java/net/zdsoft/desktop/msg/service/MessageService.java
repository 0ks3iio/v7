package net.zdsoft.desktop.msg.service;

import net.zdsoft.desktop.msg.entity.Message;
import net.zdsoft.desktop.msg.enums.MessageType;

import java.util.List;

/**
 * Created by shenke on 2017/3/20.
 */
public interface MessageService {

	/**
	 * 根据消息类型和用户Id获取前num条
	 * @param messageType
	 * @param userId
	 * @param num
	 * @return
	 */
	List<Message> findByTypeAndUserId(MessageType messageType, String userId, int num);

	List<Message> findByUserId(String userId, int num);

}
