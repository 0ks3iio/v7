package net.zdsoft.syncdatamq.action;

import java.io.IOException;
import java.text.ParseException;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
import net.zdsoft.framework.enums.BooleanEnum;
import net.zdsoft.syncdatamq.service.SymDataModifyTimeService;

@DisallowConcurrentExecution
public class BasedataSyncSendDataJob implements Job {
	private static final Logger log = Logger.getLogger(BasedataSyncSendDataJob.class);
	private static ResourceBundle evn = ResourceBundle.getBundle("conf/symConf");
	private static ResourceLoader resourceLoader;
	private static Resource resource;
	private JdbcTemplate jdbcTemplate;

	static {
		resourceLoader = new DefaultResourceLoader();
		resource = resourceLoader.getResource("conf/symSendData.json");
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		if (evn == null)
			return;
		log.info("任务启动！");
		log.info("-----------发送基础数据同步");
		SymDataModifyTimeService symDataModifyTimeService = (SymDataModifyTimeService) Evn
				.getBean("symDataModifyTimeService");
		try {

			if ("true".equals(evn.getString("sender.activemq.enable"))) {
				BasicDataSource dataSource = new BasicDataSource();
				dataSource.setDriverClassName(evn.getString("sender.connection.driver_class"));
				dataSource.setUrl(evn.getString("sender.connection.url"));
				dataSource.setUsername(evn.getString("sender.connection.username"));
				dataSource.setPassword(evn.getString("sender.connection.password"));
				
				if(jdbcTemplate == null)
					jdbcTemplate = new JdbcTemplate(dataSource);

				JSONObject symDataJson = new JSONObject();
				try {
					String symData = IOUtils.toString(resource.getInputStream());
					symDataJson = JSONObject.parseObject(symData);
				} catch (IOException e) {
					e.printStackTrace();
				}

				String symTables = evn.getString("sender.activemq.tables");
				String clientIds = evn.getString("sender.activemq.clientIds");
				String sendSync = evn.getString("sender.activemq.thread.sync");
				CountDownLatch latch = new CountDownLatch(clientIds.split(",").length);
				for (String clientId : clientIds.split(",")) {
					clientId = StringUtils.trim(clientId);
					final JSONObject symDataJsonFinal = symDataJson;
					String clientIdFinal = clientId;
					new Thread(() -> {
						CountDownLatch latchTable = new CountDownLatch(symTables.split(",").length);
						for (String symTable : symTables.split(",")) {
							if (BooleanEnum.TRUE.intValue() == NumberUtils.toInt(sendSync)) {
								new Thread(() -> {
									try {
										deal4Sender(symDataModifyTimeService, jdbcTemplate, symDataJsonFinal,
												clientIdFinal, symTable);
									} catch (Exception e) {
										e.printStackTrace();
									}
									latchTable.countDown();
								}).start();
							} else {
								try {
									deal4Sender(symDataModifyTimeService, jdbcTemplate, symDataJsonFinal, clientIdFinal,
											symTable);
								} catch (Exception e) {
									e.printStackTrace();
								}
								latchTable.countDown();
								log.info(clientIdFinal + "," + symTable + "完成，同步中symTable数：" + latchTable.getCount());
							}
						}
						
						try {
							latchTable.await();
							log.info(clientIdFinal + "所有的symTable完成，同步中client数：" + latch.getCount());
							latch.countDown();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}).start();
				}
				latch.await();
				dataSource.close();
				log.info("本次发送同步完成");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		log.info(">>>>发送任务结束！");
	}

	private void deal4Sender(SymDataModifyTimeService symDataModifyTimeService, JdbcTemplate jdbcTemplate,
			JSONObject symDataJson, String clientId, String symTable) throws ParseException, InterruptedException {
		symTable = StringUtils.trim(symTable);
		String entityName = symTable;
		String timeColumnName = "modify_time";
		String orderByColumnName = "creation_time";
		if (StringUtils.contains(symTable, "(")) {
			entityName = StringUtils.substringBefore(symTable, "(");
			timeColumnName = StringUtils.substringBetween(symTable, "(", ")");
			if (StringUtils.contains(timeColumnName, ";")) {
				orderByColumnName = timeColumnName.split(";")[1];
				timeColumnName = timeColumnName.split(";")[0];
			}
		}
		
		boolean rtn = symDataModifyTimeService.saveSendSymData(entityName, clientId, timeColumnName, orderByColumnName,
				jdbcTemplate, symDataJson);
		while (rtn) {
			rtn = symDataModifyTimeService.saveSendSymData(entityName, clientId, timeColumnName, orderByColumnName,
					jdbcTemplate, symDataJson);
			Thread.sleep(100);
		}
	}
}
