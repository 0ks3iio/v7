package net.zdsoft.api.monitor.constant;

import net.zdsoft.framework.entity.Constant;

public class ApiConstant extends Constant{

	private static final long serialVersionUID = 1L;
	// api接口统计次数调用
	public static final String API_MONITOR_TIME = "api_monitor_time";
	
	// api统计key值
	public static final String API_CENSUS_REDIS_KEY = "api.census.redis.key";

	//响应超时
	public static final String API_CALL_WARNING_KEY = "bigdata.api.realtime.warning";
	//实时调用
	public static final String API_CALL_DETAIL_KEY = "bigdata.api.realtime.detail";
	//实时监控
	public static final String API_CALL_REALTIME_MONITOR_KEY = "bigdata.api.stat.data.by.minute";
}
