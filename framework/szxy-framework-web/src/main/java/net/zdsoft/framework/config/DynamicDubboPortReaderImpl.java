package net.zdsoft.framework.config;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import com.alibaba.dubbo.common.utils.NetUtils;
import com.alibaba.dubbo.config.ProtocolConfig; 

public class DynamicDubboPortReaderImpl implements ApplicationContextAware {
	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * 动态分配端口，直接获取系统空闲的端口
	 */
	@PostConstruct
	public void init() {
		Map<String, ProtocolConfig> beansOfType = applicationContext.getBeansOfType(ProtocolConfig.class);
		for (Entry<String, ProtocolConfig> item : beansOfType.entrySet()) {
			item.getValue().setPort(NetUtils.getAvailablePort());
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = (ConfigurableApplicationContext) applicationContext;
	}
}
