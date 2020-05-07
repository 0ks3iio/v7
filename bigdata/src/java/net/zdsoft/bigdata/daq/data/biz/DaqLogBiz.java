package net.zdsoft.bigdata.daq.data.biz;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.OperationLog;
import net.zdsoft.basedata.entity.OperationUrl;
import net.zdsoft.basedata.entity.Region;
import net.zdsoft.basedata.remote.service.OperationUrlRemoteService;
import net.zdsoft.basedata.remote.service.RegionRemoteService;
import net.zdsoft.bigdata.extend.data.entity.Event;
import net.zdsoft.bigdata.extend.data.entity.EventProperty;
import net.zdsoft.bigdata.extend.data.service.EventPropertyService;
import net.zdsoft.bigdata.extend.data.service.EventService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.OSUtil;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class DaqLogBiz {

	private static final Logger logger = LoggerFactory
			.getLogger(DaqLogRead4File.class);

	private static Map<String, Region> regionMap = null;
	private static Map<String, OperationUrl> urlMap = null;

	public static String dealLoginData(String eventId, OperationLog log) {
		Event event = Evn.<EventService> getBean("eventService").findOne(
				eventId);
		boolean isContainTimeField = BooleanUtils.toBoolean(event
				.getTimeProperty());
		boolean isContainEnvField = BooleanUtils.toBoolean(event
				.getEnvProperty());
		boolean isContainUserField = BooleanUtils.toBoolean(event
				.getUserProperty());
		Json result = new Json();
		Json properties = new Json();

		JSONObject params = null;
		if (StringUtils.isBlank(log.getJsonStr())) {
			logger.warn("操作日志参数为空,忽律本条记录(登录用户ID：" + log.getUserId() + ")");
			return null;
		}
		if ("{}".equals(log.getJsonStr())) {
			logger.warn("操作日志参数为{},忽律本条记录(登录用户ID：" + log.getUserId() + ")");
			return null;
		}
		try {
			params = JSONObject.parseObject(log.getJsonStr());
		} catch (Exception e) {
			logger.error("操作日志参数解析失败,忽律本条记录( 登录用户ID：" + log.getUserId() + ")"
					+ e.getMessage());
			return null;
		}
		if (params == null)
			params = new JSONObject();
		params.put("user_id", log.getUserId());
		params.put("owner_type", log.getOwnerType());

		result.put("id", UuidUtils.generateUuid());
		if (isContainTimeField) {
			DateTimeFormatter dtFormatter = DateTimeFormat
					.forPattern("yyyy-MM-dd HH:mm:ss");
			String timestamp = dtFormatter.parseDateTime(
					DateUtils.date2String(DateUtils.addHour(log.getLogTime(),8),
							"yyyy-MM-dd HH:mm:ss")).toString();
			result.put("timestamp", timestamp);

			properties.put("operation_date",
					DateUtils.date2String(log.getLogTime(), "yyyy-MM-dd"));
			properties.put("operation_section",
					DateUtils.getDuringDay(log.getLogTime()));
			properties.put("operation_hour",
					DateUtils.getHours(log.getLogTime()));

			List<EventProperty> timePropertiesList = Evn
					.<EventPropertyService> getBean("eventPropertyService")
					.findAllByEventId(EventProperty.EVENT_TIME);
			for (EventProperty field : timePropertiesList) {
				if (properties.containsKey(field.getFieldName())) {
					result.put(field.getFieldName(),
							properties.get(field.getFieldName()));
				} else {
					result.put(field.getFieldName(), "未知");
				}
			}
		}

		if (isContainEnvField) {
			String clientVersion = log.getClientVersion();
			if (StringUtils.isNotBlank(clientVersion)
					&& clientVersion.split("\\|").length == 2) {
				String platform = clientVersion.split("\\|")[0];
				String header = clientVersion.split("\\|")[1];
				properties.put("platform", platform);
				Map<String, String> osMap = OSUtil.getOsInfo(header);
				Map<String, String> browseMap = OSUtil.getBrowserInfo(header);

				properties.put("os_name", osMap.get("type"));
				properties.put("os_version",
						osMap.get("type") + " " + osMap.get("version"));
				properties.put("browser_name", browseMap.get("type"));
				properties.put("browser_version", browseMap.get("version"));

				List<EventProperty> envPropertiesList = Evn
						.<EventPropertyService> getBean("eventPropertyService")
						.findAllByEventId(EventProperty.EVENT_ENV);
				for (EventProperty field : envPropertiesList) {
					if (properties.containsKey(field.getFieldName())) {
						result.put(field.getFieldName(),
								properties.get(field.getFieldName()));
					} else {
						result.put(field.getFieldName(), "未知");
					}
				}
			}
		}

		if (isContainUserField) {
			List<EventProperty> userPropertiesList = Evn
					.<EventPropertyService> getBean("eventPropertyService")
					.findAllByEventId(EventProperty.EVENT_USER);
			for (EventProperty field : userPropertiesList) {
				// 年龄和性别处理
				if ("sex".equals(field.getFieldName())) {
					if (params.containsKey(field.getFieldName())) {
						if (BaseConstants.MALE == params.getIntValue(field
								.getFieldName())) {
							result.put(field.getFieldName(), "男");
						} else {
							result.put(field.getFieldName(), "女");
						}
					} else {
						result.put(field.getFieldName(), "未知");
					}

				} else if ("age".equals(field.getFieldName())) {
					if (params.containsKey("birthday")) {
						try {
							result.put(field.getFieldName(), DateUtils
									.getAge(DateUtils.string2Date(params
											.getString("birthday"))));
						} catch (Exception e) {
							e.printStackTrace();
							result.put(field.getFieldName(), "未知");
						}
					} else {
						result.put(field.getFieldName(), "未知");
					}
				} else if ("owner_type".equals(field.getFieldName())) {
					if (params.getIntValue(field.getFieldName()) == 1) {
						result.put(field.getFieldName(), "学生");
					} else if (params.getIntValue(field.getFieldName()) == 2) {
						result.put(field.getFieldName(), "教师");
					} else if (params.getIntValue(field.getFieldName()) == 3) {
						result.put(field.getFieldName(), "家长");
					} else if (params.getIntValue(field.getFieldName()) == 9) {
						result.put(field.getFieldName(), "管理员");
					} else {
						result.put(field.getFieldName(), "未知");
					}
				} else {
					if (params.containsKey(field.getFieldName())) {
						result.put(field.getFieldName(),
								params.get(field.getFieldName()));
					} else {
						result.put(field.getFieldName(), "未知");
					}
				}
			}
		}
		List<EventProperty> eventPropertyList = Evn
				.<EventPropertyService> getBean("eventPropertyService")
				.findAllByEventId(eventId);
		for (EventProperty field : eventPropertyList) {
			if (params.containsKey(field.getFieldName())) {
				result.put(field.getFieldName(),
						params.get(field.getFieldName()));
			} else {
				result.put(field.getFieldName(), "未知");
			}
		}
		return result.toString();
	}

	public static String dealModuleData(String eventId, OperationLog log) {
		Event event = Evn.<EventService> getBean("eventService").findOne(
				eventId);
		boolean isContainTimeField = BooleanUtils.toBoolean(event
				.getTimeProperty());
		boolean isContainEnvField = BooleanUtils.toBoolean(event
				.getEnvProperty());
		boolean isContainUserField = BooleanUtils.toBoolean(event
				.getUserProperty());
		Json result = new Json();
		Json properties = new Json();

		JSONObject params = null;
		if (StringUtils.isBlank(log.getJsonStr())) {
			logger.warn("操作日志参数为空,忽律本条记录(登录用户ID：" + log.getUserId() + ")");
			return null;
		}
		if ("{}".equals(log.getJsonStr())) {
			logger.warn("操作日志参数为{},忽律本条记录(登录用户ID：" + log.getUserId() + ")");
			return null;
		}
		try {
			params = JSONObject.parseObject(log.getJsonStr());
		} catch (Exception e) {
			logger.error("操作日志参数解析失败,忽律本条记录( 登录用户ID：" + log.getUserId() + ")"
					+ e.getMessage());
			return null;
		}
		if (params == null)
			params = new JSONObject();

		result.put("id", UuidUtils.generateUuid());
		if (isContainTimeField) {
			DateTimeFormatter dtFormatter = DateTimeFormat
					.forPattern("yyyy-MM-dd HH:mm:ss");
			String timestamp = dtFormatter.parseDateTime(
					DateUtils.date2String(DateUtils.addHour(log.getLogTime(),8),
							"yyyy-MM-dd HH:mm:ss")).toString();
			result.put("timestamp", timestamp);
			properties.put("operation_date",
					DateUtils.date2String(log.getLogTime(), "yyyy-MM-dd"));
			properties.put("operation_section",
					DateUtils.getDuringDay(log.getLogTime()));
			properties.put("operation_hour",
					DateUtils.getHours(log.getLogTime()));

			List<EventProperty> timePropertiesList = Evn
					.<EventPropertyService> getBean("eventPropertyService")
					.findAllByEventId(EventProperty.EVENT_TIME);
			for (EventProperty field : timePropertiesList) {
				if (properties.containsKey(field.getFieldName())) {
					result.put(field.getFieldName(),
							properties.get(field.getFieldName()));
				} else {
					result.put(field.getFieldName(), "未知");
				}
			}
		}

		if (isContainEnvField) {
			String clientVersion = log.getClientVersion();
			if (StringUtils.isNotBlank(clientVersion)
					&& clientVersion.split("\\|").length == 2) {
				String platform = clientVersion.split("\\|")[0];
				String header = clientVersion.split("\\|")[1];
				properties.put("platform", platform);
				Map<String, String> osMap = OSUtil.getOsInfo(header);
				Map<String, String> browseMap = OSUtil.getBrowserInfo(header);

				properties.put("os_name", osMap.get("type"));
				properties.put("os_version",
						osMap.get("type") + " " + osMap.get("version"));
				properties.put("browser_name", browseMap.get("type"));
				properties.put("browser_version", browseMap.get("version"));

				List<EventProperty> envPropertiesList = Evn
						.<EventPropertyService> getBean("eventPropertyService")
						.findAllByEventId(EventProperty.EVENT_ENV);
				for (EventProperty field : envPropertiesList) {
					if (properties.containsKey(field.getFieldName())) {
						result.put(field.getFieldName(),
								properties.get(field.getFieldName()));
					} else {
						result.put(field.getFieldName(), "未知");
					}
				}
			}
		}

		if (isContainUserField) {
			List<EventProperty> userPropertiesList = Evn
					.<EventPropertyService> getBean("eventPropertyService")
					.findAllByEventId(EventProperty.EVENT_USER);
			for (EventProperty field : userPropertiesList) {
				// 年龄和性别处理
				if ("sex".equals(field.getFieldName())) {
					if (params.containsKey(field.getFieldName())) {
						if (BaseConstants.MALE == params.getIntValue(field
								.getFieldName())) {
							result.put(field.getFieldName(), "男");
						} else {
							result.put(field.getFieldName(), "女");
						}
					} else {
						result.put(field.getFieldName(), "未知");
					}

				} else if ("age".equals(field.getFieldName())) {
					if (params.containsKey("birthday")) {
						try {
							result.put(field.getFieldName(), DateUtils
									.getAge(DateUtils.string2Date(params
											.getString("birthday"))));
						} catch (Exception e) {
							e.printStackTrace();
							result.put(field.getFieldName(), "未知");
						}
					} else {
						result.put(field.getFieldName(), "未知");
					}
				} else if ("owner_type".equals(field.getFieldName())) {
					if (params.getIntValue(field.getFieldName()) == 1) {
						result.put(field.getFieldName(), "学生");
					} else if (params.getIntValue(field.getFieldName()) == 2) {
						result.put(field.getFieldName(), "教师");
					} else if (params.getIntValue(field.getFieldName()) == 3) {
						result.put(field.getFieldName(), "家长");
					} else if (params.getIntValue(field.getFieldName()) == 9) {
						result.put(field.getFieldName(), "管理员");
					} else {
						result.put(field.getFieldName(), "未知");
					}
				} else {
					if (params.containsKey(field.getFieldName())) {
						result.put(field.getFieldName(),
								params.get(field.getFieldName()));
					} else {
						result.put(field.getFieldName(), "未知");
					}
				}
			}
		}

		OperationUrlRemoteService operationUrlRemoteService = Evn
				.<OperationUrlRemoteService> getBean("operationUrlRemoteService");
		if (urlMap == null) {
			List<OperationUrl> urlList = operationUrlRemoteService
					.findAllObject();
			if (CollectionUtils.isNotEmpty(urlList)) {
				urlMap = new HashMap<String, OperationUrl>();
				for (OperationUrl url : urlList) {
					urlMap.put(url.getUrl(), url);
				}
			}
		}
		if (urlMap.containsKey(log.getUrl())) {
			if (StringUtils.isNotBlank(urlMap.get(log.getUrl()).getModelName())) {
				result.put("server_name", urlMap.get(log.getUrl())
						.getServerName());
				result.put("model_name", urlMap.get(log.getUrl())
						.getModelName());
			} else {
				logger.warn("模块名称为空");
				return null;
			}
		} else {
			logger.warn("没有找到对应的模块");
			return null;
		}
		return result.toString();
	}

	public static String dealDataFromFile(String eventId, String logdata) {

		if (regionMap == null) {
			regionMap = new HashMap<String, Region>();
			List<Region> regionList = Evn.<RegionRemoteService> getBean(
					"regionRemoteService").findAllObject();
			for (Region region : regionList) {
				regionMap.put(region.getFullCode(), region);
			}
		}

		Event event = Evn.<EventService> getBean("eventService").findOne(
				eventId);
		boolean isContainTimeField = BooleanUtils.toBoolean(event
				.getTimeProperty());
		boolean isContainEnvField = BooleanUtils.toBoolean(event
				.getEnvProperty());
		boolean isContainUserField = BooleanUtils.toBoolean(event
				.getUserProperty());
		JSONObject data = null;
		try {
			data = JSONObject.parseObject(logdata);
		} catch (Exception e) {
			logger.error("数据解析失败" + e.getMessage());
			return null;
		}

		// 得到 每个对象中的属性值
		JSONObject properties = JSONObject.parseObject(data.get("properties")
				.toString());

		Json result = new Json();

		result.put("id", UuidUtils.generateUuid());
		if (isContainTimeField) {
			Date daqDate = DateUtils.string2Date(
					DateUtils.stampToDate(data.get("time").toString()),
					"yyyy-MM-dd HH:mm:ss");

			DateTimeFormatter dtFormatter = DateTimeFormat
					.forPattern("yyyy-MM-dd HH:mm:ss");
			String timestamp = dtFormatter.parseDateTime(
					DateUtils.date2String(daqDate, "yyyy-MM-dd HH:mm:ss"))
					.toString();
			result.put("timestamp", timestamp);
			properties.put("$operation_date",
					DateUtils.date2String(daqDate, "yyyy-MM-dd"));
			properties.put("$operation_section",
					DateUtils.getDuringDay(daqDate));
			properties.put("$operation_hour", DateUtils.getHours(daqDate));

			List<EventProperty> timePropertiesList = Evn
					.<EventPropertyService> getBean("eventPropertyService")
					.findAllByEventId(EventProperty.EVENT_TIME);
			for (EventProperty field : timePropertiesList) {
				result.put(field.getFieldName(),
						properties.get("$" + field.getFieldName()));
			}
		}

		if (isContainEnvField) {
			List<EventProperty> envPropertiesList = Evn
					.<EventPropertyService> getBean("eventPropertyService")
					.findAllByEventId(EventProperty.EVENT_ENV);
			for (EventProperty field : envPropertiesList) {
				result.put(field.getFieldName(),
						properties.get("$" + field.getFieldName()));
			}
		}

		if (isContainUserField) {
			String regionCode = properties.getString("regionCode");
			properties.put("province", "未知");
			properties.put("city", "未知");
			if (StringUtils.isNotBlank(regionCode) && regionCode.length() >= 4) {
				Region proRegion = regionMap.get(regionCode.substring(0, 2)
						+ "0000");
				Region cityRegion = regionMap.get(regionCode.substring(0, 4)
						+ "00");
				if (proRegion != null
						&& StringUtils.isNotBlank(proRegion.getRegionName())) {
					properties.put("province", proRegion.getRegionName());
				}
				if (cityRegion != null
						&& StringUtils.isNotBlank(cityRegion.getRegionName())) {
					properties.put("city", cityRegion.getRegionName());
				}
			}
			List<EventProperty> userPropertiesList = Evn
					.<EventPropertyService> getBean("eventPropertyService")
					.findAllByEventId(EventProperty.EVENT_USER);
			for (EventProperty field : userPropertiesList) {
				result.put(field.getFieldName(),
						properties.get(field.getFieldName()));
			}
		}
		List<EventProperty> eventPropertyList = Evn
				.<EventPropertyService> getBean("eventPropertyService")
				.findAllByEventId(eventId);

		// 特殊处理登录regionCode
		String loginRegionCode = properties.getString("login_region_code");
		if (StringUtils.isNotBlank(loginRegionCode)
				&& loginRegionCode.length() >= 4
				&& !"000000".equals(loginRegionCode)
				&& !"local".equals(loginRegionCode)) {
			Region loginProRegion = regionMap.get(loginRegionCode.substring(0,
					2) + "0000");
			Region loginCityRegion = regionMap.get(loginRegionCode.substring(0,
					4) + "00");
			if (loginProRegion != null
					&& StringUtils.isNotBlank(loginProRegion.getRegionName())) {
				properties
						.put("login_province", loginProRegion.getRegionName());
			}
			if (loginCityRegion != null
					&& StringUtils.isNotBlank(loginCityRegion.getRegionName())) {
				properties.put("login_city", loginCityRegion.getRegionName());
			}
		} else {
			properties.put("login_province", "未知");
			properties.put("login_city", "未知");
		}

		for (EventProperty field : eventPropertyList) {
			result.put(field.getFieldName(),
					properties.get(field.getFieldName()));
		}
		result.put("user_id", UuidUtils.generateUuid());
		result.put("login_ip", "192.168.0.20");
		return result.toString();
	}

	public static void main(String[] args) throws ParseException {
		// DateTimeFormatter dtFormatter = DateTimeFormat
		// .forPattern("yyyy-MM-dd HH:mm:ssZ");
		// /** 输出结果 2016-08-29T22:58:20.000Z */
		// // yyyy-MM-ddTHH:mm:ssZ
		// String result = dtFormatter.parseDateTime(
		// DateUtils.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"))
		// .toString();
		//
		// String result1 = dtFormatter
		// .parseDateTime(
		// DateUtils
		// .date2String(new Date(), "yyyy-MM-dd HH:mm:ss")+"+0800")
		// .withZone(DateTimeZone.UTC).toString();
		//
		// System.out.println(result);
		// System.out.println(result1);

		// TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// System.out.println(sdf.parse("2018-09-01 08:00:00+0800").getTime());
		//
		//
		// DateTimeFormatter dateTimeFormatter =
		// DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withChronology(ISOChronology.getInstance(DateTimeZone.forID("Asia/Shanghai")));
		// System.out.println(dateTimeFormatter.parseDateTime("2018-09-01 07:00:00"));

		JSONObject params = JSONObject.parseObject("{}");
		System.out.println(params.toString());

	}
}
