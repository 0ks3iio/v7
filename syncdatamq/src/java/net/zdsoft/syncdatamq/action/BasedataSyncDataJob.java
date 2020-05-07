package net.zdsoft.syncdatamq.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.FileUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.syncdatamq.entity.SymDataModifyTime;
import net.zdsoft.syncdatamq.service.BasedataSyncDataService;
import net.zdsoft.syncdatamq.service.SymDataModifyTimeService;
import net.zdsoft.syncdatamq.utils.ActiveMQUtils;

@DisallowConcurrentExecution
public class BasedataSyncDataJob implements Job {
	private static final Logger log = Logger.getLogger(BasedataSyncDataJob.class);
	private static ResourceBundle evn = ResourceBundle.getBundle("conf/symConf");
	private static ResourceLoader reader;
	private static Resource resource;

	static {
		reader = new DefaultResourceLoader();
		resource = reader.getResource("/conf/symData.json");
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.info("任务启动！");
		log.info("-----------基础数据同步");
		BasedataSyncDataService service = Evn.getBean("basedataSyncDataService");
		final SymDataModifyTimeService symDataModifyTimeService = Evn.getBean("symDataModifyTimeService");
		try {
			if ("true".equals(evn.getString("activemq.send"))) {
				String sendClientIds = evn.getString("activemq.send.client.ids");
				if (StringUtils.isNotBlank(sendClientIds)) {
					String[] clientIds = sendClientIds.split(",");
					List<SymDataModifyTime> symDataModifyTimes = symDataModifyTimeService.findAll();
					Map<String, List<SymDataModifyTime>> symDataModifyTimeMap = EntityUtils
							.getListMap(symDataModifyTimes, "clientId", null);

					JSONObject allJson = JSONObject.parseObject(FileUtils.readString(resource.getInputStream()));
					final Map<String, JSONObject> symDatas = new HashMap<>();
					for (String clientId : clientIds) {
						clientId = StringUtils.trim(clientId);
						final Map<String, String> resultMap = new HashMap<>();
						List<SymDataModifyTime> modifyTimes = symDataModifyTimeMap.get(clientId);
						final Map<String, SymDataModifyTime> map = EntityUtils.getMap(modifyTimes, "entityName");
						String sendEntities = evn.getString("activemq.send.entities");
						String[] entities = sendEntities.split(",");
						for (String entity : entities) {
							entity = StringUtils.trim(entity);
							if (StringUtils.equals(resultMap.get(clientId + "." + entity), "1")) {
								log.info(clientId + "." + entity + " 数据正在处理中或出现异常，本次跳过！");
								continue;
							}
							JSONObject json = symDatas.get(entity);
							if (json == null) {
								json = allJson.getJSONObject(entity);
								if (json != null) {
									symDatas.put(entity, json);
								}
							}
							SymDataModifyTime modifyTime = map.get(entity);
							if (modifyTime == null) {
								modifyTime = new SymDataModifyTime();
								modifyTime.setModifyTime(DateUtils.parseDate("1900-01-01", "yyyy-MM-dd"));
								modifyTime.setEntityName(entity);
								modifyTime.setClientId(clientId);
								modifyTime.setId(UuidUtils.generateUuid());
							}
							final SymDataModifyTime dataModifyTime = modifyTime;
							new Thread(new Runnable() {
								@Override
								public void run() {
									resultMap.put(dataModifyTime.getClientId() + "." + dataModifyTime.getEntityName(),
											"1");
									String s = null;
									JSONArray array = null;
									boolean start = true;
									// 一次性将要取的数据取完
									try {
										while (start) {
											start = false;
											Thread.sleep(100);
											String modifyTimeColumn = "modifyTime";
											JSONObject json = symDatas.get(dataModifyTime.getEntityName());
											if (json != null && json.containsKey("modifyTimeColumn")) {
												modifyTimeColumn = json.getString("modifyTimeColumn");
											}
											s = symDataModifyTimeService
													.findData(dataModifyTime.getEntityName(),
															dataModifyTime.getModifyTime(), modifyTimeColumn,
															json != null
																	? (json.containsKey("fetchSize")
																			? json.getIntValue("fetchSize") : 2000)
																	: 2000);
											// 大于2的结果值，
											// 表示存在内容，否则应该是{}或者[]，表示没有内容返回
											if (StringUtils.length(s) > 2) {
												start = true;
												try {
													ActiveMQUtils.sendMessageQueue(dataModifyTime.getClientId() + "."
															+ dataModifyTime.getEntityName(), s);

													array = JSONArray.parseArray(s);
													if (array.size() > 0) {
														JSONObject object = array.getJSONObject(array.size() - 1);
														if (object.containsKey(modifyTimeColumn)) {
															dataModifyTime
																	.setModifyTime(object.getDate(modifyTimeColumn));
															symDataModifyTimeService.save(dataModifyTime);
															log.info("发送变更的" + dataModifyTime.getEntityName() + " - "
																	+ dataModifyTime.getClientId() + "数据，总条数："
																	+ array.size() + ", " + SUtils.s(dataModifyTime));
														}
													}
												} catch (Exception e) {
													log.error(dataModifyTime.getEntityName() + " - "
															+ dataModifyTime.getClientId() + "发送失败！ " + e.getMessage());
												}
											} else {
												log.info(dataModifyTime.getEntityName() + " - "
														+ dataModifyTime.getClientId() + "全部变更数据推送完毕（"
														+ DateFormatUtils.format(dataModifyTime.getModifyTime(),
																"yyyy-MM-dd HH:mm:ss.SSS")
														+ "）," + DateFormatUtils.format(dataModifyTime.getModifyTime(),
																"yyyy-MM-dd HH:mm:ss"));
												break;
											}
										}
										resultMap.put(
												dataModifyTime.getClientId() + "." + dataModifyTime.getEntityName(),
												"0");
									} catch (Exception e) {
										resultMap.put(
												dataModifyTime.getClientId() + "." + dataModifyTime.getEntityName(),
												"1");
										throw new RuntimeException(e);
									}
								}
							}).start();
						}
					}
				}
			}

			if (StringUtils.equals("true", evn.getString("activemq.receive")))
				service.dealReceive();
		} catch (Exception e) {
			e.printStackTrace();
		}

		log.info(">>>>任务结束！");
	}

}
