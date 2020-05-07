package net.zdsoft.framework.delayQueue;

import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import net.zdsoft.framework.delayQueue.constant.DelayQueueConstant;
import net.zdsoft.framework.utils.RedisUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 延迟队列处理service
 * 
 * @author jiang feng
 *
 */
@Service("delayQueueService")
public class DelayQueryServiceImpl implements DelayQueueService {
	private static final Logger logger = Logger
			.getLogger(DelayQueryServiceImpl.class);

	private static DelayQueue<DelayItem<?>> timeoutQueue;
	@Autowired(required = false)
	private ExecutorService executorService;
	// private Map<TimeoutTypeEnum, TimeoutHandler> handlers;
	private transient boolean running;

	private boolean isLoadTimeoutTask = true;

	@PostConstruct
	protected void init() {
		if (isLoadTimeoutTask) {
			logger.info("初始化超时队列");
			running = true;
			// 初始化一个阻塞队列
			timeoutQueue = new DelayQueue<DelayItem<?>>();
			startHandler();
		}
	}

	// 添加任务放在DelayQueue中。
	@Override
	public void addRecentlyTimeoutItems(List<DelayItem<?>> itemList) {
		if (isLoadTimeoutTask) {
			logger.info("加载即将超时的信息...");
			if (CollectionUtils.isNotEmpty(itemList)) {
				timeoutQueue.addAll(itemList);
			}
		}
	}

	@Override
	public void addRemoveItem2Queue(String key) {
		RedisUtils.publish(DelayQueueConstant.DELAY_QUEUE_REDIS_CHANNEL, key);
	}

	// 删除待处理的任务
	@Override
	public void removeTimeoutItem(String key) {
		if (isLoadTimeoutTask) {
			logger.info("根据key删除信息...");
			for (DelayItem<?> t : timeoutQueue) {
				if (t.getKey().equals(key))
					timeoutQueue.remove(t);
			}
		}
	}

	@PreDestroy
	protected void destroy() {
		running = false;
	}

	private void startHandler() {
		logger.info("启动超时监听线程");

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (running) {
					try {
						// 阻塞队列执行take()方法，删除队列顶部的一个对象，并返回删除的对象。这个方法是阻塞方法，如果队列中没有对象，这个线程将被阻塞，知道队列中有对象。
						DelayItem<?> t1 = timeoutQueue.take();
						// TimeoutModel timeoutModel = timeoutQueue.take();
						// 对象交给ExecutorService处理，ExecutorService是一个异步处理机制，相当于一个线程池，意思就把这个take()的对象委托给ExecutorService处理。
						if (t1 != null) {
							Runnable task = t1.getTask();
							if (task == null) {
								continue;
							}
							getExecutorService().execute(task);
						}
					} catch (InterruptedException e) {
						logger.warn("处理超时失败", e);
						e.printStackTrace();
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public ExecutorService getExecutorService() {
		if (executorService == null) {
			// 得到executorService
			// 对象，其中Runtime.getRuntime()相当于new一个对象，、、Executors.newFixedThreadPool(xxx）最大执行多少个线程的executorService
			// 。
			executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
					.availableProcessors() * 2);
		}

		return executorService;
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	// public Map<TimeoutTypeEnum, TimeoutHandler> getHandlers() {
	// return handlers;
	// }

	// public void setHandlers(Map<TimeoutTypeEnum, TimeoutHandler> handlers) {
	// this.handlers = handlers;
	// }

	public void setLoadTimeoutTask(boolean isLoadTimeoutTask) {
		this.isLoadTimeoutTask = isLoadTimeoutTask;
	}

}
