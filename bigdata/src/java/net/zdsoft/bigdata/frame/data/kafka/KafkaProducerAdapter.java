package net.zdsoft.bigdata.frame.data.kafka;

import java.util.Date;
import java.util.Properties;

import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.data.service.OptionService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.DateUtils;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class KafkaProducerAdapter {
	private static final Logger logger = LogManager
			.getLogger(KafkaProducerAdapter.class);

	private static KafkaProducer<String, String> kafkaProducer = null;

	private static KafkaProducerAdapter kafkaProducerAdapter = null;

	private KafkaProducerAdapter() {
	}

	/**
	 * 单例
	 * 
	 * @return
	 */
	public static KafkaProducerAdapter getInstance() throws Exception {
		if (kafkaProducerAdapter == null) {
			synchronized (KafkaProducerAdapter.class) {
				kafkaProducerAdapter = new KafkaProducerAdapter();
				try {
					OptionDto kafkaDto = Evn.<OptionService> getBean(
							"optionService").getAllOptionParam("kafka");
					if (kafkaDto == null || kafkaDto.getStatus() == 0) {
						throw new BigDataBusinessException("kafka初始化失败");
					}
					String kafkaUrl = kafkaDto.getFrameParamMap().get(
							"kafka_url");
					kafkaProducer = new KafkaProducer<String, String>(
							initProperties(kafkaUrl));
				} catch (Exception e) {
					throw new Exception("init kafka producer exception:"
							+ e.getMessage());
				}
			}
		}
//		else {
//			OptionDto kafkaDto = Evn.<OptionService> getBean("optionService")
//					.getAllOptionParam("kafka");
//			if (kafkaDto == null || kafkaDto.getStatus() == 0) {
//				throw new BigDataBusinessException("kafka初始化失败");
//			}
//			if (kafkaDto.getMobility() == 1) {
//				synchronized (KafkaProducerAdapter.class) {
//					kafkaProducerAdapter = new KafkaProducerAdapter();
//					try {
//						String kafkaUrl = kafkaDto.getFrameParamMap().get(
//								"kafka_url");
//						kafkaProducer = new KafkaProducer<String, String>(
//								initProperties(kafkaUrl));
//					} catch (Exception e) {
//						throw new Exception("init kafka producer exception:"
//								+ e.getMessage());
//					}
//				}
//				Evn.<OptionService> getBean("optionService").updateOptionMobility(
//						kafkaDto.getCode(), 0);
//			}
//		}
		return kafkaProducerAdapter;
	}

	private static Properties initProperties(String kafkaUrl) {
		Properties props = new Properties();
		props.put("bootstrap.servers", kafkaUrl);// 服务器
		props.put("group.id", "kle");
		props.put("acks", "all");// 所有follower都响应了才认为消息提交成功，即"committed"
		props.put("retries", 0);// retries = MAX
								// 无限重试，直到你意识到出现了问题:)
		// props.put("max.block.ms", "10");
		props.put("batch.size", 16384);// 16M 1024
									// producer将试图批处理消息记录，以减少请求次数.默认的批量处理消息字节数
		// batch.size当批量的数据大小达到设定值后，就会立即发送，不顾下面的linger.ms
		props.put("linger.ms", 1000);// 延迟1ms发送，这项设置将通过增加小的延迟来完成--即，不是立即发送一条记录，producer将会等待给定的延迟时间以允许其他消息记录发送，这些消息记录可以批量处理
		props.put("buffer.memory", 33554432);// producer可以用来缓存数据的内存大小。
		props.put("key.serializer",
				"org.apache.kafka.common.serialization.IntegerSerializer");
		props.put("value.serializer",
				"org.apache.kafka.common.serialization.StringSerializer");
		return props;
	}

	/**
	 * 发送消息
	 * 
	 * @param message
	 */
	public void send(String topic, String message) {
		ProducerRecord<String, String> record;
		record = new ProducerRecord<String, String>(topic, message);
		kafkaProducer.send(record, new SendCallback(record, 0));
	}

	/**
	 * 发送消息
	 * 
	 * @param message
	 */
	public void syncSend(String topic, String message) {
		ProducerRecord<String, String> record;
		record = new ProducerRecord<String, String>(topic, message);
		kafkaProducer.send(record);
	}

	/**
	 * producer回调
	 */
	static class SendCallback implements Callback {
		ProducerRecord<String, String> record;
		int sendSeq = 0;

		public SendCallback(ProducerRecord<String, String> record, int sendSeq) {
			this.record = record;
			this.sendSeq = sendSeq;
		}

		@Override
		public void onCompletion(RecordMetadata recordMetadata, Exception e) {
			// send success
			if (null == e) {
				String meta = "topic:" + recordMetadata.topic()
						+ ", partition:" + recordMetadata.topic() + ", offset:"
						+ recordMetadata.offset();
				logger.info("send message success ,meta:" + meta);
				return;
			}
			// send failed
			logger.error("send message failed, seq:" + sendSeq + ", record:"
					+ record.toString() + ", errmsg:" + e.getMessage());
			if (sendSeq < 1) {
				kafkaProducer.send(record, new SendCallback(record, ++sendSeq));
			}
		}
	}

	public static void main(String[] args) {
		for (int i = 11; i <= 15; i++) {
			Json result = new Json();
			result.put("student_id", "111111");
			result.put("student_name", "测试学生" + i);
			// 、后台新增、数字校园同步、微课同步统计
			result.put("student_source", "微课同步统计");
			result.put("org_name", "测试机构");
			result.put("org_province", "浙江省");
			result.put("business_dept", "浙大万朋");
			result.put("operation_hour", "12");
			result.put("operation_section", "下午");
			result.put("course_id", "2");
			// result.put("course_name", "足球");
			result.put("course_times", 20);

			DateTimeFormatter dtFormatter = DateTimeFormat
					.forPattern("yyyy-MM-dd HH:mm:ss");
			String timestamp = dtFormatter.parseDateTime(
					DateUtils.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"))
					.toString();
			result.put("timestamp", timestamp);
			try {
				KafkaProducerAdapter producer = KafkaProducerAdapter
						.getInstance();
				producer.send("kafka_khw_stu_course", result.toString());
				Thread.sleep(500);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}