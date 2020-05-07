package net.zdsoft.bigdata.daq.data.job;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.zdsoft.basedata.entity.OperationLog;
import net.zdsoft.basedata.remote.service.OperationLogRemoteService;
import net.zdsoft.bigdata.daq.data.biz.DaqLogBiz;
import net.zdsoft.bigdata.extend.data.entity.Event;
import net.zdsoft.bigdata.extend.data.service.EventService;
import net.zdsoft.bigdata.frame.data.kafka.KafkaProducerAdapter;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 实时读取操作日志数据
 *
 */
public class DaqOperationLogJob {

	private static final Logger logger = LoggerFactory
			.getLogger(DaqOperationLogJob.class);

	public void initLog() throws Exception {
		List<Event> eventList = Evn.<EventService> getBean("eventService")
				.findAll();
		for (Event event : eventList) {
			if (event.getLastCommitDate() != null) {
				continue;
			}
			if ("login".equals(event.getEventCode())) {
				//new LoginScheduledExecutor(event.getId(), true).run();
			} else if ("module".equals(event.getEventCode())) {
				//new ModuleScheduledExecutor(event.getId(), true).run();
			}
		}
	}

	public void readLog() throws Exception {
		List<Event> eventList = Evn.<EventService> getBean("eventService")
				.findAll();
		for (Event event : eventList) {
			int interval = event.getIntervalTime();
			if ("login".equals(event.getEventCode())) {
				ScheduledExecutorService loginExec = Executors
						.newScheduledThreadPool(1);
				loginExec.scheduleWithFixedDelay(new LoginScheduledExecutor(
						event.getId(), false), 0, interval, TimeUnit.SECONDS);
			} else if ("module".equals(event.getEventCode())) {
				ScheduledExecutorService moduleExec = Executors
						.newScheduledThreadPool(1);
				moduleExec.scheduleWithFixedDelay(new ModuleScheduledExecutor(
						event.getId(), false), 0, interval, TimeUnit.SECONDS);
			}
		}
	}

	class LoginScheduledExecutor implements Runnable {
		private String eventId;
		private String topicName;
		private String urls;
		private Date lastCommitDate;
		private boolean init;

		LoginScheduledExecutor(String eventId, boolean init) {
			this.eventId = eventId;
			this.init = init;
		}

		@Override
		public void run() {
			EventService eventService = Evn
					.<EventService> getBean("eventService");
			Event event = eventService.findOne(eventId);

			topicName = event.getTopicName();
			urls = event.getUrls();
			lastCommitDate = event.getLastCommitDate();

			if (lastCommitDate == null && !init) {
				logger.info("登录日志尚未初始化数据");
				return;
			}

			OperationLogRemoteService operationLogRemoteService = (OperationLogRemoteService) Evn
					.getBean("operationLogRemoteService");

			// 如果已经有记录的话
			int interval = 1;
			if (event.getIntervalTime() >= 60) {
				interval = event.getIntervalTime() / 60;
			}
			Date currentDate = DateUtils.addMinute(new Date(), -interval);
			try {

				if (lastCommitDate == null) {
					// 首次执行
					logger.info("初始化登录日志");
					lastCommitDate = DateUtils.string2Date(
							"2018-09-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
					while (DateUtils.compareForDay(lastCommitDate, currentDate) <= 0) {
						Date endDate = DateUtils.addDay(lastCommitDate, 1);
						if (DateUtils
								.compareForDay(lastCommitDate, currentDate) == 0)
							endDate = currentDate;
						String[] urlArray = urls.split(",");
						for (String url : urlArray) {
							List<OperationLog> resultList = operationLogRemoteService
									.findListByUrl(url, lastCommitDate, endDate);
							for (OperationLog log : resultList) {
								String data = DaqLogBiz.dealLoginData(eventId,
										log);
								if (StringUtils.isBlank(data)) {
									continue;
								}
								try {
									KafkaProducerAdapter producer = KafkaProducerAdapter
											.getInstance();
									producer.send(topicName, data);
									Thread.sleep(5);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						lastCommitDate = DateUtils.addDay(lastCommitDate, 1);
					}
				} else {
					String[] urlArray = urls.split(",");
					for (String url : urlArray) {
						List<OperationLog> resultList = operationLogRemoteService
								.findListByUrl(url, lastCommitDate, currentDate);
						for (OperationLog log : resultList) {
							String data = DaqLogBiz.dealLoginData(eventId, log);
							if (StringUtils.isBlank(data)) {
								continue;
							}
							try {
								KafkaProducerAdapter producer = KafkaProducerAdapter
										.getInstance();
								producer.send(topicName, data);
								Thread.sleep(10);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
				// 把当前时间保存进库
				eventService
						.updateLastCommitDateByEventId(eventId, currentDate);
			} catch (Exception e) {
				// 实时读取日志异常，需要记录时间和lastTimeFileSize 以便后期手动补充
				e.printStackTrace();
			}
		}
	}

	class ModuleScheduledExecutor implements Runnable {
		private String eventId;
		private String topicName;
		private Date lastCommitDate;
		private boolean init;

		ModuleScheduledExecutor(String eventId, boolean init) {
			this.eventId = eventId;
			this.init = init;
		}

		@Override
		public void run() {
			EventService eventService = Evn
					.<EventService> getBean("eventService");
			Event event = eventService.findOne(eventId);
			topicName = event.getTopicName();
			lastCommitDate = event.getLastCommitDate();

			if (lastCommitDate == null && !init) {
				logger.info("模块操作日志尚未初始化数据");
				return;
			}
			OperationLogRemoteService operationLogRemoteService = (OperationLogRemoteService) Evn
					.getBean("operationLogRemoteService");
			int interval = 1;
			if (event.getIntervalTime() >= 60) {
				interval = event.getIntervalTime() / 60;
			}
			Date currentDate = DateUtils.addMinute(new Date(), -interval);
			try {
				if (lastCommitDate == null) {
					// 首次执行
					logger.info("初始化模块操作日志");
					lastCommitDate = DateUtils.string2Date(
							"2019-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
					while (DateUtils.compareForDay(lastCommitDate, currentDate) <= 0) {
						Date endDate = DateUtils.addDay(lastCommitDate, 1);
						if (DateUtils
								.compareForDay(lastCommitDate, currentDate) == 0)
							endDate = currentDate;
						List<OperationLog> resultList = operationLogRemoteService
								.findListByDate(lastCommitDate, endDate);
						for (OperationLog log : resultList) {
							String data = DaqLogBiz
									.dealModuleData(eventId, log);
							if (StringUtils.isBlank(data)) {
								continue;
							}
							try {
								KafkaProducerAdapter producer = KafkaProducerAdapter
										.getInstance();
								producer.send(topicName, data);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						lastCommitDate = DateUtils.addDay(lastCommitDate, 1);
					}
				} else {
					List<OperationLog> resultList = operationLogRemoteService
							.findListByDate(lastCommitDate, currentDate);
					for (OperationLog log : resultList) {
						String data = DaqLogBiz.dealModuleData(eventId, log);
						if (StringUtils.isBlank(data)) {
							continue;
						}
						try {
							KafkaProducerAdapter producer = KafkaProducerAdapter
									.getInstance();
							producer.send(topicName, data);
							Thread.sleep(10);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				// 把当前时间保存进库
				eventService
						.updateLastCommitDateByEventId(eventId, currentDate);
			} catch (Exception e) {
				// 实时读取日志异常，需要记录时间和lastTimeFileSize 以便后期手动补充
				logger.error("发送模块操作日志到kafka失败"+e.getMessage());
			}
		}
	}
}