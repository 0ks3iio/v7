package net.zdsoft.syncdata.service;

import javax.jms.Destination;

public interface ProducerService {
	
	public void sendMessage(Destination destination, final String message);

}
