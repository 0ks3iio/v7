package net.zdsoft.bigdata.daq.data.sdk;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DaqDemo {

	public static void main(String[] args) throws Exception {
		// 采集事件的 URL
		// final String SA_SERVER_URL =
		// "http://192.168.0.20:8099/openapi/da?project=default&token=bbb";
		final String SA_SERVER_DEBUG_URL = "http://192.168.0.20:8099/openapi/dac/v10/debug";
		// DebugConsumer
		final DataAcquisition sa = new DataAcquisition(
				new DataAcquisition.BatchConsumer(SA_SERVER_DEBUG_URL, 5));

		// BatchConsumer
		// final SensorsAnalytics sa =
		// new SensorsAnalytics(new
		// SensorsAnalytics.BatchConsumer(SA_SERVER_URL, 10));

		// LoggingConsumer
		// final SensorsAnalytics sa = new SensorsAnalytics(new
		// SensorsAnalytics.LoggingConsumer("/data/file.log"));

		// 1. 用户匿名访问网站
		Map<String, Object> properties = new HashMap<String, Object>();
		// 1.1 访问首页
		// 前面有$开头的property字段，是系统预置字段
		// 对于预置字段，已经确定好了字段类型和字段的显示名
		properties.clear();
		properties.put("$time", new Date()); // 这条event发生的时间，如果不设置的话，则默认是当前时间
		properties.put("$os_name", "Windows"); // 通过请求中的UA，可以解析出用户使用设备的操作系统是windows的
		properties.put("$os_version", "8.1"); // 操作系统的具体版本
		properties.put("$browser", "IE9"); // 浏览器的具体版本
		properties.put("$ip", "123.123.123.123"); // 请求中能够拿到用户的IP，自动根据这个解析省份、城市
		sa.track("00000000000000000000000000000000", false, "ViewHomePage", properties); // 记录访问首页这个event
		// user_id,os_name,os_version,browse_name,browse_version,ip,ip_province,ip_country,ip_city,visit_time
		sa.flush();
	}

}
