package net.zdsoft.syncdatamq.action;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.fastjson.JSONObject;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.syncdatamq.service.SymDataModifyTimeService;

@DisallowConcurrentExecution
public class BasedataSyncReceiveDataJob implements Job {
	private static final Logger log = Logger.getLogger(BasedataSyncReceiveDataJob.class);
	private static ResourceBundle evn = ResourceBundle.getBundle("conf/symConf");
	private JdbcTemplate jdbcTemplate;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		if (evn == null)
			return;
		log.info("-----------接收基础数据同步");
		SymDataModifyTimeService symDataModifyTimeService = (SymDataModifyTimeService) Evn
				.getBean("symDataModifyTimeService");
		try {
			if ("true".equals(evn.getString("receiver.activemq.enable"))) {
				
				BasicDataSource dataSource = new BasicDataSource();
				dataSource.setDriverClassName(evn.getString("receiver.connection.driver_class"));
				dataSource.setUrl(evn.getString("receiver.connection.url"));
				dataSource.setUsername(evn.getString("receiver.connection.username"));
				dataSource.setPassword(evn.getString("receiver.connection.password"));
				
				if(jdbcTemplate == null)
					jdbcTemplate = new JdbcTemplate(dataSource);

				JSONObject symDataJson = new JSONObject();
				try {
					ResourceLoader resourceLoader = new DefaultResourceLoader();
					Resource resource = resourceLoader.getResource("conf/symReceiveData.json");
					String symData = IOUtils.toString(resource.getInputStream());
					symDataJson = JSONObject.parseObject(symData);
				} catch (IOException e) {
					e.printStackTrace();
				}

				String symTables = evn.getString("receiver.activemq.tables");
				String clientId = evn.getString("receiver.activemq.clientId");
				CountDownLatch latch = new CountDownLatch(symTables.split(",").length);
				clientId = StringUtils.trim(clientId);
				for (String symTable : symTables.split(",")) {
					symTable = StringUtils.trim(symTable);
					if (StringUtils.contains(symTable, "[")) {
						String clientIdFromReceiver = StringUtils.substringBetween(symTable, "[", "]");
						if (StringUtils.isNotBlank(clientIdFromReceiver))
							clientId = clientIdFromReceiver;
						symTable = StringUtils.substringAfter(symTable, "]");
					}

					String receiveTableName = symTable;
					String dataTableName = symTable;
					JSONObject dataJson = null;
					if (StringUtils.contains(symTable, "(")) {
						receiveTableName = StringUtils.substringBefore(symTable, "(");
						dataTableName = StringUtils.substringBetween(symTable, "(", ")");
					} else {
						dataJson = symDataJson.getJSONObject(symTable);
					}

					if (dataJson != null) {
						if (dataJson.containsKey("send_table_name"))
							dataTableName = dataJson.getString("send_table_name");
					}
					final String receiveTableNameF = receiveTableName;
					final String dataTableNameF = dataTableName;
					final String clientIdF = clientId;
					final JSONObject symDataJsonF = symDataJson;
					new Thread(() -> {
						try {
							boolean rtn = symDataModifyTimeService.saveReceiveSymData(receiveTableNameF, dataTableNameF,
									clientIdF, jdbcTemplate, evn.getString("recevier.driverName"), symDataJsonF);
							while (rtn) {
								rtn = symDataModifyTimeService.saveReceiveSymData(receiveTableNameF, dataTableNameF,
										clientIdF, jdbcTemplate, evn.getString("recevier.driverName"), symDataJsonF);
								Thread.sleep(100);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						latch.countDown();
					}).start();
				}
				
				latch.await();
				dataSource.close();
				log.info("接收数据结束！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info(">>>>接收任务结束！");
	}

}
