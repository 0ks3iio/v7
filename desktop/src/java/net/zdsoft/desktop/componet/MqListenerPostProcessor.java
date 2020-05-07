package net.zdsoft.desktop.componet;

import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.stereotype.Service;

@Service("mqListenerPostProcessor")
@Lazy
public class MqListenerPostProcessor implements BeanDefinitionRegistryPostProcessor {

	private DefaultListableBeanFactory beanFactory;

	@Autowired
	private SingleConnectionFactory connectionFactory;

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry arg0) throws BeansException {
	}

	/**
	 * 动态增加mq的topic监听器
	 * 
	 * @param beanName
	 * @param topicDestination
	 * @param messageListener
	 */
	public void addListener(String beanName, ActiveMQTopic topicDestination, MessageListener messageListener) {
		DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
		container.setMessageListener(messageListener);
		container.setConnectionFactory(connectionFactory);
		container.setDestination(topicDestination);
		beanFactory.registerSingleton(beanName, messageListener);
	}

	/**
	 * 动态增加mq的queue监听器
	 * 
	 * @param beanName
	 * @param topicDestination
	 * @param messageListener
	 */
	public void addListener(String beanName, ActiveMQQueue queueDestination, MessageListener messageListener) {
		DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
		container.setMessageListener(messageListener);
		container.setConnectionFactory(connectionFactory);
		container.setDestination(queueDestination);
		beanFactory.registerSingleton(beanName, messageListener);
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		this.beanFactory = (DefaultListableBeanFactory) beanFactory;
	}
}
