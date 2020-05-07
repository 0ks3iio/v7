package net.zdsoft.bigdata.dataimport.listener;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import net.zdsoft.basedata.entity.ImportResult;
import net.zdsoft.basedata.remote.service.ImportResultRemoteService;
import net.zdsoft.bigdata.dataimport.entity.ImportErrorInfo;
import net.zdsoft.bigdata.extend.data.entity.Event;
import net.zdsoft.bigdata.extend.data.entity.EventProperty;
import net.zdsoft.bigdata.extend.data.service.EventPropertyService;
import net.zdsoft.bigdata.extend.data.service.EventService;
import net.zdsoft.bigdata.frame.data.kafka.KafkaProducerAdapter;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

public class EventImportListener extends AbstractImportListener {
	private static Logger logger = LoggerFactory
			.getLogger(EventImportListener.class);

	private String eventId;

	private List<EventProperty> propertyList = new ArrayList<EventProperty>();

	public EventImportListener(String eventId, String businessId,
			String unitId, String userId, String filePath, long startTime) {
		this.eventId = eventId;
		this.businessId = businessId;
		this.unitId = unitId;
		this.userId = userId;
		this.startTime = startTime;
		this.filePath = filePath;
		setColumns();
	}

	@Override
	public void dealBiz() {
		boolean debug = false;
		// 数据加载完成 状态标识
		RedisUtils.set("event-import-status-" + businessId, "dataLoaded");
		ImportResultRemoteService importResultRemoteService = (ImportResultRemoteService) Evn
				.getBean("importResultRemoteService");
		ImportResult importResult = new ImportResult();
		importResult.setId(UuidUtils.generateUuid());
		importResult.setUnitId(unitId);
		importResult.setUserId(userId);
		importResult.setBusinessId(eventId);
		importResult.setObjectName("event-import");
		importResult.setFilePath(filePath);
		try {
			logger.info("-----------------业务处理--------------");
			EventService eventService = (EventService) Evn
					.getBean("eventService");
			Event event = eventService.findOne(eventId);

			int currentRowNum = 0;
			int correctNum = 0;
			int errorNum = 0;
			Json progressData = null;
			DateTimeFormatter dtFormatter = DateTimeFormat
					.forPattern("yyyy-MM-dd HH:mm:ss");
			int repeatTime = 0;
			while (true) {
				String s = RedisUtils.rpop("event-import-data-" + businessId);
				boolean hasSequential = false;
				if (StringUtils.isBlank(s)) {
					Thread.sleep(500);
					repeatTime++;
					if (repeatTime == 5)
						break;
					continue;
				}
				Json data = JSON.parseObject(s, Json.class);
				StringBuffer errorMsg = new StringBuffer();
				currentRowNum = data.getIntValue("rowNum");
				// 处理数据字典的转换
				for (EventProperty property : propertyList) {
					String originalValue = data.getString(property
							.getFieldName());
					String dataDictionary = property.getDataDictionary();
					if (StringUtils.isNotBlank(dataDictionary)) {
						Map<String, String> dataDictionaryMap = JSON
								.parseObject(
										dataDictionary,
										new TypeReference<Map<String, String>>() {
										});
						if (dataDictionaryMap.containsKey(originalValue)) {
							String showValue = dataDictionaryMap
									.get(originalValue);
							data.put(property.getFieldName(), showValue);
						} else {
							errorMsg.append(property.getPropertyName())
									.append(":").append("该属性值(")
									.append(originalValue)
									.append(")不在数据字典范围内;");
						}
					}
					if (property.getIsSequential() != null
							&& property.getIsSequential().intValue() == 1) {
						try {
							String dtFormat = "yyyy-MM-dd HH:mm:ss";
							if (StringUtils.isNotBlank(property.getRemark())) {
								dtFormat = property.getRemark();
							}
							String timestamp = dtFormatter.parseDateTime(
									DateUtils.date2String(
											DateUtils.addHour(DateUtils
													.string2Date(originalValue,
															dtFormat), 8),
											"yyyy-MM-dd HH:mm:ss")).toString();
							data.put("timestamp", timestamp);
							hasSequential = true;
						} catch (Exception e) {
							errorMsg.append(property.getPropertyName())
									.append(":").append("该属性值(")
									.append(originalValue)
									.append(")不能转化为日期格式;");
						}
					}
				}
				if (hasSequential == false) {
					errorMsg.append("不存在时序字段;");
				}
				if (errorMsg.toString().length() > 0) {
					errorNum++;
					ImportErrorInfo errorData = new ImportErrorInfo();
					errorData.setRowNum(currentRowNum);
					errorData.setOriginalData(data.getString("originalData"));
					errorData.setErrorMsg(errorMsg.toString());
					RedisUtils.lpush("event-import-error-" + businessId,
							JSONObject.toJSONString(errorData));
				} else {
					try {
						if (!debug) {// 发送至kafka
							KafkaProducerAdapter producer = KafkaProducerAdapter
									.getInstance();
							producer.send(event.getTopicName(),
									data.toJSONString());
							Thread.sleep(1);
						}
						correctNum++;
					} catch (Exception e) {
						throw e;
					}

				}
				// 每执行100条记录,记录一次进度
				if (currentRowNum % 100 == 0) {
					progressData = new Json();
					progressData.put("totalNum", totalSize);
					progressData.put("currentRowNum", currentRowNum);
					progressData.put("progress",
							getPercent(currentRowNum, totalSize));
					progressData.put("correctNum", correctNum);
					progressData.put("errorNum", errorNum);
					long currentTime = System.currentTimeMillis();
					progressData.put("duration",
							(currentTime - startTime) / 1000);
					RedisUtils.set("event-import-progress-" + businessId,
							progressData.toJSONString());
				}
				// 每执行100条记录,记录一次detail &&100条以上记录 否则全部记录
				if (totalSize >= 100) {
					if (currentRowNum % 100 == 0) {
						StringBuffer detailMsg = new StringBuffer();
						RedisUtils.lpush("event-import-detail-" + businessId,
								detailMsg.append("正在处理第").append(currentRowNum)
										.append("行").append("数据------(")
										.append(data.getString("originalData"))
										.append(")").toString());
					}
				} else {
					StringBuffer detailMsg = new StringBuffer();
					RedisUtils.lpush(
							"event-import-detail-" + businessId,
							detailMsg.append("正在处理第").append(currentRowNum)
									.append("行").append("数据------(")
									.append(data.getString("originalData"))
									.append(")").toString());
				}
			}
			// 全部执行完,记录最后一条进度
			progressData = new Json();
			progressData.put("totalNum", totalSize);
			progressData.put("currentRowNum", currentRowNum);
			progressData.put("progress", getPercent(currentRowNum, totalSize));
			progressData.put("correctNum", correctNum);
			progressData.put("errorNum", errorNum);
			long currentTime = System.currentTimeMillis();
			progressData.put("duration", (currentTime - startTime) / 1000);
			RedisUtils.set("event-import-progress-" + businessId,
					progressData.toJSONString(), 120);
			// 处理错误信息
			String errorFilePath = saveErrorMsg(eventId, ExcelTypeEnum.XLSX);
			// 组装导入结果
			String msg = "一共" + totalSize + "条记录,导入成功" + correctNum
					+ "条记录,导入失败" + errorNum + "条记录";
			importResult.setResultMsg(msg);
			importResult.setErrorFile(errorFilePath);
			if (errorNum > 0)
				importResult.setImportStatus(3);
			else
				importResult.setImportStatus(2);
			long endTime = System.currentTimeMillis();
			importResult.setDuration(endTime - startTime);
			importResult.setCreationTime(new Date());
			importResult.setModifyTime(new Date());
			importResultRemoteService.save(importResult);
		} catch (Exception e) {
			importResult.setResultMsg(e.getMessage());
			importResult.setImportStatus(3);
			long endTime = System.currentTimeMillis();
			importResult.setDuration(endTime - startTime);
			importResult.setCreationTime(new Date());
			importResult.setModifyTime(new Date());
			importResultRemoteService.save(importResult);
			RedisUtils.set("event-import-status-" + businessId, "error");
			RedisUtils.lpush("event-import-progress-" + businessId,
					"导入出错:" + e.getMessage());
			removeBusinessData();
		}
		// 导入任务完成 状态改为完成 保留120秒
		RedisUtils.set("event-import-status-" + businessId, "finished", 120);
		removeBusinessData();
	}

	@Override
	public void setColumns() {
		EventService eventService = (EventService) Evn.getBean("eventService");
		EventPropertyService eventPropertyService = (EventPropertyService) Evn
				.getBean("eventPropertyService");
		Event event = eventService.findOne(eventId);
		List<EventProperty> eventPropertyList = eventPropertyService
				.findPropertiesByEventId(event.getId(),
						event.getTimeProperty(), event.getTimeProperty(),
						event.getEnvProperty());
		for (EventProperty property : eventPropertyList) {
			columnMap.put(property.getPropertyName(), property.getFieldName());
			propertyList.add(property);
		}
	}

	private static String getPercent(int number1, int number2) {
		// 创建一个数值格式化对象
		NumberFormat numberFormat = NumberFormat.getInstance();
		// 设置精确到小数点后2位
		numberFormat.setMaximumFractionDigits(1);
		String result = numberFormat.format((float) number1 / (float) number2
				* 100);
		return result;
	}

//	public static void main(String[] args) {
//		DateTimeFormatter dtFormatter = DateTimeFormat
//				.forPattern("yyyy-MM-dd HH:mm:ss");
//		String dtFormat = "yyyy/MM/dd";
//		String timestamp = dtFormatter.parseDateTime(
//				DateUtils.date2String(DateUtils.addHour(
//						DateUtils.string2Date("2017/09/15", dtFormat), 8),
//						"yyyy-MM-ddThh:mm:ss.SSS+08:00")).toString();
//		System.out.println(timestamp);
//	}

	public static void main(String[] args) throws ParseException {
		System.out.println(new Date());
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddThh:mm:ss.SSS+08:00");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS+08:00");
		// CST(北京时间)在东8区
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		System.out.println(sdf.format(new Date()));
	}


}
