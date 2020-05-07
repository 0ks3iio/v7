package net.zdsoft.basedata.service;

import javax.jms.Destination;

public interface MQMessageProducerService {
	
	public void sendMessage(Destination destination, final String message);
	

}
