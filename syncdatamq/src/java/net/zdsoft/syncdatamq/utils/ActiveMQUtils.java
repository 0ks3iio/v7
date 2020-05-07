package net.zdsoft.syncdatamq.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.IllegalStateException;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.lang3.StringUtils;

public class ActiveMQUtils {

	private static Session session; // Destination ：消息的目的地;消息发送给谁.
	private static MessageProducer producer; // TextMessage message;
	private static Connection connection = null; // Session： 一个发送或接收消息的线程
	private static ConnectionFactory connectionFactory = null; // Connection
																// ：JMS 客户端到JMS
	private static Map<String, Connection> connectionMap = new HashMap<String, Connection>();
	private static Map<String, Session> sessionMap = new HashMap<String, Session>();
	private static Map<String, Destination> destinationMap = new HashMap<String, Destination>();
	private static Map<String, MessageConsumer> messageConsumerMap = new HashMap<String, MessageConsumer>();
	
	private static ResourceBundle con = ResourceBundle.getBundle("conf/conf");

	public static final String ACTIVE_MQ_AES_ENCRYPT_KEY = "8AC49C9C5BB02C33015BB8049B55292F";

	public static final String ACTIVE_MQ_URL_PARAMETER = "activeMqParam";

	public static final String ACTIVE_MQ_QUEUE_PREFIX_NAME = "szxy.";

	public static final int ACTIVE_MQ_QUEUE_TIME_TO_LIVE = 3600 * 1000 * 12 * 30;

	public static String createEncryptKey(String clientId, String entityName) {
		return ACTIVE_MQ_AES_ENCRYPT_KEY + "." + clientId + "." + entityName;
	}

	public static void commitSession(String queueName) {
		Session receiveSession = sessionMap.get(queueName);
		if (receiveSession != null)
			try {
				receiveSession.commit();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		return;
	}

	public static void rollbackSession(String queueName) {
		Session receiveSession = sessionMap.get(queueName);
		if (receiveSession != null)
			try {
				receiveSession.rollback();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		return;
	}

	/**
	 * 持久化接收消息（走事务，后续需调用commitSession确认事务结束消息才会被消费），注册消费者id为参数queueName
	 * 
	 * @param queueName
	 * @return
	 */
	public static String receiveMessageQueue(String queueName) {
		try {
			initReceiveSession(queueName, true);
			return returnMessageQueue(queueName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 非持久化接收消息（走事务，后续需调用commitSession确认事务结束消息才会被消费）
	 * 
	 * @param queueName
	 * @return
	 */
	public static String receiveMessageQueueNoPersistent(String queueName) {
		try {
			initReceiveSession(queueName, false);
			return returnMessageQueue(queueName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 关闭接收连接
	 * 
	 * @param queueName
	 * @throws JMSException
	 */
	public static void closeReceiveConnection(String queueName) throws JMSException {
		messageConsumerMap.remove(queueName);
		destinationMap.remove(queueName);
		sessionMap.remove(queueName);
		Connection receiveConnection = connectionMap.get(queueName);
		receiveConnection.close();
		connectionMap.remove(queueName);
	}

	private static String returnMessageQueue(String queueName) throws JMSException {
		Session receiveSession = sessionMap.get(queueName);
		Destination destination = destinationMap.get(queueName);
		if (destination == null && receiveSession != null) {
			destination = receiveSession.createQueue(ACTIVE_MQ_QUEUE_PREFIX_NAME + queueName);
			destinationMap.put(queueName, destination);
		}
		MessageConsumer messageConsumer = messageConsumerMap.get(queueName);
		if (destination != null && messageConsumer == null) {
			messageConsumer = receiveSession.createConsumer(destination);
			messageConsumerMap.put(queueName, messageConsumer);
		}

		if (messageConsumer != null) {
			try {
				TextMessage textMessage = (TextMessage) messageConsumer.receive(1000);// 阻塞
				if (textMessage == null)
					return null;
				return textMessage.getText();
			} catch (IllegalStateException e) {
				closeReceiveConnection(queueName);
				throw e;
			}
		}
		return null;
	}

	public static void main(String[] args) {
		ConnectionFactory conn = new ActiveMQConnectionFactory("admin", "zdsoft@123.com", "tcp://60.12.69.9:61616");
		try {
			Connection receiveConnection = conn.createConnection();
			receiveConnection.setClientID("szxy");
			receiveConnection.start();
		} catch (JMSException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param topicName
	 * @param isPersistent
	 *            是否持久化接收
	 * @throws Exception
	 */
	private static synchronized void initReceiveSession(String topicName, boolean isPersistent) throws Exception {
		Connection receiveConnection = connectionMap.get(topicName);
		if (receiveConnection == null) {
			synchronized (ActiveMQUtils.class) {
				receiveConnection = connectionMap.get(topicName);
				if (receiveConnection == null) {
					String activeMqUser = con.getString("activemq.username");
					if (StringUtils.isBlank(activeMqUser))
						activeMqUser = ActiveMQConnection.DEFAULT_USER;
					String activeMqPwd = con.getString("activemq.password");
					if (StringUtils.isBlank(activeMqPwd))
						activeMqPwd = ActiveMQConnection.DEFAULT_PASSWORD;
					connectionFactory = new ActiveMQConnectionFactory(activeMqUser, activeMqPwd,
							con.getString("activemq.brokerURL"));
					receiveConnection = connectionFactory.createConnection();
					if (isPersistent)
						receiveConnection.setClientID(ACTIVE_MQ_QUEUE_PREFIX_NAME + topicName);
					receiveConnection.start();
					connectionMap.put(topicName, receiveConnection);
				}
			}
		}
		Session receiveSession = sessionMap.get(topicName);
		if (receiveSession == null) {
			synchronized (ActiveMQUtils.class) {
				receiveSession = sessionMap.get(topicName);
				if (receiveSession == null) {
					receiveSession = receiveConnection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
					sessionMap.put(topicName, receiveSession);
				}
			}
		}
	}

	private static void initSendSession() throws Exception {
		if (connection == null) {
			synchronized (ActiveMQUtils.class) {
				if (connection == null) {
					String activeMqUser = con.getString("activemq.username");
					if (StringUtils.isBlank(activeMqUser))
						activeMqUser = ActiveMQConnection.DEFAULT_USER;
					String activeMqPwd = con.getString("activemq.password");
					if (StringUtils.isBlank(activeMqPwd))
						activeMqPwd = ActiveMQConnection.DEFAULT_PASSWORD;
					connectionFactory = new ActiveMQConnectionFactory(activeMqUser,
							activeMqPwd, con.getString("activemq.brokerURL"));
					connection = connectionFactory.createConnection();
					connection.start();
				}
			}

		}
		if (session == null) {
			synchronized (ActiveMQUtils.class) {
				if (session == null) {
					session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
				}
			}
		}
	}

	public static void sendMessageQueue(String queueName, String data) {
		try {
			initSendSession();
			if (connection != null) {
				Destination queue = session.createQueue(ACTIVE_MQ_QUEUE_PREFIX_NAME + queueName);
				producer = session.createProducer(queue);
				producer.setDeliveryMode(DeliveryMode.PERSISTENT);// 持久化
				TextMessage message = session.createTextMessage(data);
				producer.setTimeToLive(ACTIVE_MQ_QUEUE_TIME_TO_LIVE);
				producer.send(message);
				session.commit();
			}
		} catch (Exception e) {
			try {
				if (session != null)
					session.close();
				if (connection != null)
					connection.close();
			} catch (JMSException e1) {
				e1.printStackTrace();
			}
			session = null;
			connection = null;
			throw new RuntimeException(e);
		}
	}
}
