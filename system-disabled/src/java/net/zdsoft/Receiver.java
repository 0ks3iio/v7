package net.zdsoft;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.io.FileUtils;


public class Receiver {

	public static void main(String[] args) {
		 int i = 0;
	        
	        // ConnectionFactory ：连接工厂，JMS 用它创建连接
	        ConnectionFactory connectionFactory;
	        // Connection ：JMS 客户端到JMS Provider 的连接
	        Connection connection = null;
	        // Session： 一个发送或接收消息的线程
	        Session session;
	        // Destination ：消息的目的地;消息发送给谁.
	        Destination destination;
	        // 消费者，消息接收者
	        MessageConsumer consumer;
	        // connectionFactory = new ActiveMQConnectionFactory(
	        // ActiveMQConnection.DEFAULT_USER,
	        // ActiveMQConnection.DEFAULT_PASSWORD, "tcp://192.168.0.104:61616");
	        connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.58.101:61616");
	        try {
	            // 构造从工厂得到连接对象
	            connection = connectionFactory.createConnection();
	            // 启动
	            connection.start();
	            // 获取操作连接
	            session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
	            // 获取session注意参数值 foo.bar 是一个服务器的queue，须在在ActiveMq的console配置
	            destination = session.createQueue("Consumer.yaohaishujuduijie.VirtualTopic.SCOPE_AREA_CLIENT_2");
	            consumer = session.createConsumer(destination);
	            File f = new File("message111.txt");
	            List<String> list = new ArrayList<>();
	            while (true) {
	                // 设置接收者接收消息的时间，为了便于测试，这里谁定为100s
	                TextMessage message = (TextMessage) consumer.receive();
	                if (null != message) {
	                    i++;
	                    list.add(message.getText());
	                    System.out.println("收到消息" + i +":"+ message.getText());
	                } else {
	                    break;
	                }
	            }
	            FileUtils.writeLines(f, list);
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                if (null != connection)
	                    connection.close();
	            } catch (Throwable ignore) {
	            }
	        }
	}
}
