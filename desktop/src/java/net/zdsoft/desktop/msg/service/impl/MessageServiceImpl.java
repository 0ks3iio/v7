package net.zdsoft.desktop.msg.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import net.zdsoft.desktop.msg.entity.Message;
import net.zdsoft.desktop.msg.enums.MessageType;
import net.zdsoft.desktop.msg.service.MessageService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.remote.openapi.service.OpenApiOfficeService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;

/**
 * Created by shenke on 2017/3/20.
 */
@Service("messageService")
public class MessageServiceImpl implements MessageService {

	private static OpenApiOfficeService openApiOfficeService;
	
	private OpenApiOfficeService getOpenApiOfficeService(){
		if(openApiOfficeService == null){
			openApiOfficeService = Evn.getBean("openApiOfficeService");
			if(openApiOfficeService == null){
				System.out.println("openApiOfficeService为null，需开启dubbo服务");
			}
		}
		return openApiOfficeService;
	}
	
	@Override
	public List<Message> findByTypeAndUserId(MessageType messageType, String userId, int num) {

		JSONObject remoteParam = new JSONObject();
		remoteParam.put("userId",userId);
		List<Message> messages = new ArrayList<Message>();
		try{
			messages = SUtils.dt(getOpenApiOfficeService().remoteOfficeMsgDetails(remoteParam.toJSONString()),new TypeReference<List<Message>>(){});
		}catch (NullPointerException e) {
		}catch (Exception e) {
			e.printStackTrace();
		}
		if (CollectionUtils.isEmpty(messages)){
			return messages;
		}
		List<Message> messageList = Lists.newArrayList();
		for (Message message : messages) {
			if (messageType.getType().equals(message.getMessageType())){
				messageList.add(message);
			}
		}
		return messageList.size()>num?messageList.subList(0,num):messageList;
	}


	@Override
	public List<Message> findByUserId(String userId, int num) {
		JSONObject remoteParam = new JSONObject();
		remoteParam.put("userId",userId);
		List<Message> messages = new ArrayList<Message>();
		try{
			messages = SUtils.dt(getOpenApiOfficeService().remoteOfficeMsgDetails(userId),new TypeReference<List<Message>>(){});
		}catch (NullPointerException e) {
		}catch (Exception e) {
			e.printStackTrace();
		}
		if(CollectionUtils.isEmpty(messages)){
			return messages;
		}
		Collections.sort(messages, new Comparator<Message>() {
			@Override
			public int compare(Message o1, Message o2) {
				Date d1 = o1.getSendTime();
				Date d2 = o2.getSendTime();
				if(d1 != null && d1.before(d2)){
					return 1;
				}else{
					return -1;
				}
			}
		});
		return messages.size()>num?messages.subList(0,num):messages;
	}
}
