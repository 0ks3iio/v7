package net.zdsoft.framework.config;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;

import net.zdsoft.framework.delayQueue.constant.DelayQueueConstant;
import net.zdsoft.framework.delayQueue.listener.DelayQueueRedisListener;
import net.zdsoft.framework.utils.FileUtils;
import net.zdsoft.framework.utils.PassportClientUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.StringUtils;

import org.apache.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import redis.clients.jedis.JedisPubSub;

@Component
public class FrameworkEvn implements ApplicationContextAware {
	private static Logger logger = Logger.getLogger(FrameworkEvn.class);

	private static final String EXTERNAL_RESOURCE_PROPERTY_NAME_SUFFIX = ".external";

	private String[] resourceFiles;

	@PostConstruct
	public void postDone() {

		logger.info("init Evn start...");

		for (String resourceFile : resourceFiles) {

			logger.info("load resourceFile: " + resourceFile);

			try {
				ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
				Resource[] resources = resolver.getResources(resourceFile);
				for(Resource resource : resources) {
					Properties properties = FileUtils.readProperties(resource
							.getInputStream());
					String externalResourceFile = properties
							.getProperty(resource.getFilename()
									+ EXTERNAL_RESOURCE_PROPERTY_NAME_SUFFIX);
					if (StringUtils.isNotBlank(externalResourceFile)
							&& new File(externalResourceFile).exists()) {
						logger.info("Found external resource file: "
								+ externalResourceFile);
						Resource externalResource = new FileSystemResource(
								externalResourceFile);
						if (externalResource.isReadable()) {
							logger.info("External resource file will be read: "
									+ externalResourceFile);
							propertiesToMap(FileUtils
									.readProperties(externalResource
											.getInputStream()));
						} else {
							logger.info("External resource file can't be read: "
									+ externalResourceFile);
							propertiesToMap(properties);
						}
					} else {
						propertiesToMap(properties);
					}
				}
			} catch (IOException e) {
				logger.error("Read properties from resource(" + resourceFile
						+ ") error", e);
			}
		}

		logger.info("读取JavaOpt参数信息");
		propertiesToMap(System.getProperties());

		if (Evn.isPassport()) {
			logger.info("连接Passport");
			System.out
					.println("[passportClient]--[Evn初始化passportClient start]");
			PassportClientUtils.init(null, null);
			System.out.println("[passportClient]--[Evn初始化passportClient over]");
		}

		if (Evn.isScheduler()) {
			logger.info("启动任务调度 ");
			try {
				Scheduler s = Evn.getScheduler();
				if (s == null) {
					Evn.setScheduler(Evn.getSchedulerfactory().getScheduler());
					Evn.getScheduler().start();
				}
			} catch (SchedulerException e1) {
				logger.info("任务调度失败");
			}
		}
		
		// 延迟队列通道
		JedisPubSub delayQueuePubSub = new DelayQueueRedisListener();
		new Thread(new Runnable() {
			public void run() {
				logger.info("订阅Redis延迟队列通道");
				RedisUtils.subscribe(delayQueuePubSub,
						DelayQueueConstant.DELAY_QUEUE_REDIS_CHANNEL);
			}
		}).start();
		logger.info("初始化环境支持类结束");
	}

	/**
	 * 将propertis的内容写入Map
	 *
	 * @author dingw
	 * @param ps
	 */
	private void propertiesToMap(Properties ps) {
		for (Object key : ps.keySet()) {
			String value = ps.getProperty(key.toString());
			Evn.getMap().put(key.toString(), value);
			Evn.getMapO().put(key.toString(), value);
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		logger.info("开始加载类 ");
		Evn.setApplication(applicationContext);
	}

	/**
	 * 设置资源文件路径，支持 classpath:、file: 前缀。
	 *
	 * @param resourceFiles
	 *            资源文件数组
	 */
	public void setResourceFiles(String[] resourceFiles) {
		this.resourceFiles = resourceFiles;
	}

}
