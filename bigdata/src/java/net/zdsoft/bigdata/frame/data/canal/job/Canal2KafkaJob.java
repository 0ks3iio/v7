package net.zdsoft.bigdata.frame.data.canal.job;

import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.List;

import net.zdsoft.bigdata.frame.data.canal.entity.CanalJob;
import net.zdsoft.bigdata.frame.data.canal.service.CanalJobService;
import net.zdsoft.bigdata.frame.data.kafka.KafkaProducerAdapter;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Json;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import com.alibaba.otter.canal.protocol.Message;

public class Canal2KafkaJob {
	private static final Logger logger = LoggerFactory
			.getLogger(Canal2KafkaJob.class);

	public void initJobs() {
		List<CanalJob> jobList = Evn.<CanalJobService> getBean(
				"canalJobService").findAll();
		for (CanalJob job : jobList) {
			if (0 == job.getStatus()) {
				continue;
			}
			new Thread(new Canal2KafkaScheduledExecutor(job)).start();
		}
	}

	class Canal2KafkaScheduledExecutor implements Runnable {
		private CanalJob job;

		Canal2KafkaScheduledExecutor(CanalJob job) {
			this.job = job;
		}

		@Override
		public void run() {
			startJob(job);
		}
	}

	public void startJob(CanalJob canalJob) {

		// String hostname = "192.168.0.201";
		// String destination = "test";
		// String topicName = "test";
		// int port = 11111;
		// int batchSize = 1000;
		// 创建链接
		CanalConnector connector = CanalConnectors.newSingleConnector(
				new InetSocketAddress(canalJob.getHostName(), canalJob
						.getPort().intValue()), canalJob.getDestination(), "",
				"");
		try {
			connector.connect();
			// connector.subscribe(".*\\..*");
			connector.subscribe(canalJob.getSubscribeRule());
			BigDecimal totalSize = canalJob.getTotalSize();
			while (true) {
				Message message = connector.getWithoutAck(canalJob
						.getBatchSize().intValue()); // 获取指定数量的数据
				long batchId = message.getId();
				int size = message.getEntries().size();
				if (batchId == -1 || size == 0) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					totalSize = dealWithEntry(canalJob.getId(),
							canalJob.getTopicName(), totalSize,
							message.getEntries());
				}
				connector.ack(batchId); // 提交确认
				// connector.rollback(batchId); // 处理失败, 回滚数据
			}
		} finally {
			connector.disconnect();
		}
	}

	private BigDecimal dealWithEntry(String id, String topicName,
			BigDecimal totalSize, List<Entry> entrys) {
		for (Entry entry : entrys) {
			if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN
					|| entry.getEntryType() == EntryType.TRANSACTIONEND) {
				continue;
			}
			RowChange rowChage = null;
			try {
				rowChage = RowChange.parseFrom(entry.getStoreValue());
			} catch (Exception e) {
				throw new RuntimeException(
						"ERROR ## parser of eromanga-event has an error , data:"
								+ entry.toString(), e);
			}
			EventType eventType = rowChage.getEventType();
			logger.debug(String
					.format("================> binlog[%s:%s] , name[%s,%s] , eventType : %s",
							entry.getHeader().getLogfileName(), entry
									.getHeader().getLogfileOffset(), entry
									.getHeader().getSchemaName(), entry
									.getHeader().getTableName(), eventType));

			for (RowData rowData : rowChage.getRowDatasList()) {
				totalSize = totalSize.add(BigDecimal.valueOf(1));
				if (eventType == EventType.DELETE) {
					send2KafKa(topicName, "DELETE",
							rowData.getBeforeColumnsList());
				} else if (eventType == EventType.INSERT) {
					send2KafKa(topicName, "INSERT",
							rowData.getAfterColumnsList());
				} else {
					send2KafKa(topicName, "UPSERT",
							rowData.getAfterColumnsList());
				}
			}
		}
		// 更新数据到表中
		Evn.<CanalJobService> getBean("canalJobService").updateJobById(id,
				totalSize, new Date());
		return totalSize;
	}

	private void send2KafKa(String topicName, String operation,
			List<Column> columns) {
		Json result = new Json();
		result.put("operation", operation);
		result.put("Type", operation);
		for (Column column : columns) {
			result.put(column.getName(), column.getValue());
		}
		try {
			KafkaProducerAdapter producer = KafkaProducerAdapter.getInstance();
			System.out.println(result.toString());
			producer.send(topicName, result.toString());
			Thread.sleep(100);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
