package net.zdsoft.basedata.service.impl;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.MQMessageProducerService;

@Service
@Lazy
public class MQMessageProducerServiceImpl implements MQMessageProducerService {

	@Autowired
	@Lazy
	private JmsTemplate jmsTemplate;

	@Override
	public void sendMessage(Destination destination, String message) {
		jmsTemplate.send(destination, new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(message);
			}
		});
	}

}
