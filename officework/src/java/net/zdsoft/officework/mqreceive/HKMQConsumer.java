package net.zdsoft.officework.mqreceive;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.jms.Connection;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.Topic;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.system.remote.service.SystemIniRemoteService;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
public class HKMQConsumer {
	private static final Logger log = Logger.getLogger(HKMQConsumer.class);

	public static final String BROKER_URL_KEY = "HK.MQ.BROKER.URL";

	public static final String TARGET = "openapi.acs.topic";
	
	public static final Map<String,Connection> connmap = new HashMap<String, Connection>();

	private static SystemIniRemoteService getSystemIniRemoteService() {
		return Evn.getBean("systemIniRemoteService");
	}
	private static MessageListener getMessageListener() {
		return Evn.getBean("hKConsumerMessageListener");
	}

	@PostConstruct
	public static void run() throws Exception {

		
			// 创建链接工厂
			final String brokerUrl = getSystemIniRemoteService().findValue(
					BROKER_URL_KEY);
			log.info("海康broker-url:" + brokerUrl);
			if (StringUtils.isNotBlank(brokerUrl)) {
				Thread hkBrokerUrlConn = new Thread(new Runnable() {
					@Override
					public void run() {
						for(;;){
							connHkMQ();
							try {
								Thread.sleep(1000*60*20);//目前连接MQ隔段时间（约1小时）会中断，处理：隔20分钟重新建立连接
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
					
					public void connHkMQ(){
						try {
							Connection connection = null;
							Connection oldconnection = connmap.get("conn");
							Session session = null;
							ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
									brokerUrl);
							// 通过工厂创建一个连接
							connection = factory.createConnection();
				            connmap.put("conn", connection);
							// factory.createConnection(userName, password)
							// 启动连接
							connection.start();
							// 第一个参数表示是否使用事务，第二个参数指定消息的确认模式
							session = connection.createSession(false,
									Session.AUTO_ACKNOWLEDGE);
							Topic topic = session.createTopic(TARGET);
							MessageConsumer consumer = session.createConsumer(topic);
							// 消费者异步接收topic里的消息
							consumer.setMessageListener(getMessageListener());
							if(oldconnection!=null){
								log.info("关闭老连接");
								oldconnection.close();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				},"hkBrokerUrlConn");
				hkBrokerUrlConn.start();
			} else {
				log.info("海康broker-url为空，不需要启动连接");
			}
		
	}

}
