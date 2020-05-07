package net.zdsoft.bigdata.daq.data.biz;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.Properties;

import net.zdsoft.bigdata.frame.data.kafka.KafkaProducerAdapter;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 实时读取数据
 *
 */
public class DaqLogRead4File {

	private static final Logger logger = LoggerFactory
			.getLogger(DaqLogRead4File.class);

	public void readLog() throws Exception {
//		List<Event> eventList = Evn.<EventService> getBean("eventService")
//				.findAll();
//		String filePath = Evn.<SysOptionRemoteService> getBean(
//				"sysOptionRemoteService").findValue(Constant.FILE_PATH);
//		for (Event event : eventList) {
//			String fullPath = filePath
//					+ event.getFilePath().replace("/", File.separator);
//			fullPath = filePath
//					+ event.getFilePath().replace("/", File.separator)
//							.replace("daq", "druid");
//			File logFileDir = new File(fullPath);
//			if (!logFileDir.exists()) {
//				logFileDir.mkdirs();
//			}
//			String fileName = event.getFileName();
//			// fileName = "login_log";
//			String topicName = event.getTopicName();
//			topicName="kafka_login_log";
//			long lastPointer = dealHistoryData(event.getId(), topicName,
//					fullPath, fileName);
//			ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
//			exec.scheduleWithFixedDelay(new MyScheduledExecutor(event.getId(),
//					topicName, fullPath, fileName, lastPointer), 0, 30,
//					TimeUnit.SECONDS);
//		}
	}

	class MyScheduledExecutor implements Runnable {
		private String eventId;
		private String topicName;
		private String filePath;
		private String fileName;
		private long pointer;

		MyScheduledExecutor(String eventId, String topicName, String filePath,
				String fileName, long pointer) {
			this.eventId = eventId;
			this.topicName = topicName;
			this.filePath = filePath;
			this.fileName = fileName;
			this.pointer = pointer;
		}

		@Override
		public void run() {
			// 如果已经有记录的话
			String today = DateUtils.date2String(new Date(), "yyy-MM-dd");
			String fullFileName = filePath + fileName + "." + today;
			final File logFile = new File(fullFileName);
			RandomAccessFile randomFile = null;
			// 获得变化部分
			try {
				long len = logFile.length();
				if (len < pointer) {
					logger.info("Log file was reset. Restarting logging from start of file.");
					pointer = 0;
				} else {
					// 指定文件可读可写
					randomFile = new RandomAccessFile(logFile, "rw");
					// 获取RandomAccessFile对象文件指针的位置，初始位置是0
					randomFile.seek(pointer);// 移动文件指针位置
					String tmp = "";
					while ((tmp = randomFile.readLine()) != null) {
						String jsonData = new String(
								tmp.getBytes("ISO-8859-1"), "utf-8");
						String data = DaqLogBiz.dealDataFromFile(eventId, jsonData);
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
						pointer = randomFile.getFilePointer();
					}
				}
			} catch (Exception e) {
				// 实时读取日志异常，需要记录时间和lastTimeFileSize 以便后期手动补充
				e.printStackTrace();
				logger.error(today + " File read error, pointer: " + pointer);
			} finally {
				saveProperties(filePath, topicName, today, pointer);
				try {
					randomFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

//	private Properties getProperties(String filePath, String topicName) {
//		File propertyFile = new File(filePath + topicName + ".properties");
//		Properties props = new Properties();// 使用Properties类来加载属性文件
//		if (propertyFile.exists()) {
//			FileInputStream iFile = null;
//			try {
//				iFile = new FileInputStream(propertyFile);
//				props.load(iFile);
//
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			} finally {
//				try {
//					iFile.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//			return props;
//		} else {
//			return null;
//		}
//	}

	private void saveProperties(String filePath, String topicName, String date,
			long pointer) {
		// 将pointer 落地以便下次启动的时候，直接从指定位置获取
		File propertyFile = new File(filePath + topicName + ".properties");
		// 是否存在
		if (!propertyFile.exists()) {
			try {
				// 创建文件
				propertyFile.createNewFile();
			} catch (IOException e) {
			}
		}
		Properties props = new Properties();
		FileOutputStream oFile = null;
		try {
			oFile = new FileOutputStream(propertyFile, false);
			props.setProperty("date", date);
			props.setProperty("pointer", String.valueOf(pointer));
			props.store(oFile, "创建属性文件");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				oFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

//	private long dealHistoryData(String eventId, String topicName,
//			String filePath, String fileName) throws Exception {
//		// 读取属性文件 看看上次执行到哪里
//		Properties props = getProperties(filePath, topicName);
//		long lastPointer = 0;
//		if (props != null) {
//			String lastDate4str = props.getProperty("date");
//			String lastPointer4str = props.getProperty("pointer");
//			lastPointer = Long.valueOf(lastPointer4str).longValue();
//			Date lastDate = DateUtils.string2Date(lastDate4str, "yyy-MM-dd");
//			while (DateUtils.compareForDay(lastDate, new Date()) < 0) {
//				readLogByDate(eventId, topicName, filePath, fileName,
//						DateUtils.date2String(lastDate, "yyy-MM-dd"),
//						lastPointer);
//				lastDate = DateUtils.addDay(lastDate, 1);
//				lastPointer = 0;
//			}
//		}
//		return lastPointer;
//	}

//	private void readLogByDate(String eventId, String topicName,
//			String filePath, String fileName, String logDate, long pointer)
//			throws Exception {
//		String fullFileName = filePath + fileName + "." + logDate;
//		final File logFile = new File(fullFileName);
//		RandomAccessFile randomFile = new RandomAccessFile(logFile, "rw");
//		// 获取RandomAccessFile对象文件指针的位置，初始位置是0
//		System.out.println("RandomAccessFile文件指针的初始位置:" + pointer);
//		randomFile.seek(pointer);// 移动文件指针位置
//		String tmp = "";
//		while ((tmp = randomFile.readLine()) != null) {
//			String jsonData = new String(tmp.getBytes("ISO-8859-1"), "utf-8");
//			System.out.println(topicName + " : " + jsonData);
//			String loginLog = DaqLogBiz.dealDataFromFile(eventId, jsonData);
//			try {
//				KafkaProducerAdapter producer = KafkaProducerAdapter
//						.getInstance();
//				producer.send(topicName, loginLog);
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				saveProperties(filePath, topicName,
//						DateUtils.date2String(
//								DateUtils.addDay(DateUtils.string2Date(logDate,
//										"yyy-MM-dd"), 1), "yyy-MM-dd"), pointer);
//			}
//		}
//		randomFile.close();
//	}

	public static void main(String[] args) throws Exception {
		DaqLogRead4File view = new DaqLogRead4File();
		view.readLog();
	}

}
