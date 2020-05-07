package net.zdsoft.bigdata.stat.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import net.zdsoft.basedata.service.BaseJdbcService;
import net.zdsoft.bigdata.base.entity.Node;
import net.zdsoft.bigdata.base.service.BgNodeService;
import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.data.export.Statistical;
import net.zdsoft.bigdata.data.service.OptionService;
import net.zdsoft.bigdata.data.utils.HdfsUtils;
import net.zdsoft.bigdata.frame.data.elastic.EsClientService;
import net.zdsoft.bigdata.frame.data.mysql.MysqlClientService;
import net.zdsoft.bigdata.stat.constant.BgSysStatConstatant;
import net.zdsoft.bigdata.stat.service.BgSysStatService;
import net.zdsoft.echarts.convert.api.JData;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.RedisUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

@Service("bgSysStatService")
public class BgSysStatServiceImpl implements BgSysStatService {

	@Autowired
	MysqlClientService mysqlClientService;

	@Autowired
	OptionService optionService;

	@Autowired
	EsClientService esClientService;

	@Autowired
	BaseJdbcService baseJdbcService;

	@Autowired
	BgNodeService bgNodeService;

	@Resource
	private Statistical statistical;
	
	@Override
	public void summaryStat() {
		// 统计 数据源 api 任务 大屏 图表 报表 事件 模型 总量和当日个数
		Json summary = new Json();
		String beginDate = DateUtils.date2String(new Date(), "yyyy-MM-dd")
				+ " 00:00:00";
		String endDate = DateUtils.date2String(new Date(), "yyyy-MM-dd")
				+ " 23:59:59";
		summary.put("chart_today_num", statistical.countChartByDate(
				DateUtils.string2Date(beginDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.string2Date(endDate, "yyyy-MM-dd HH:mm:ss")));
		summary.put("chart_total_num", statistical.countChart());

		summary.put("screen_today_num", statistical.countScreenByDate(
				DateUtils.string2Date(beginDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.string2Date(endDate, "yyyy-MM-dd HH:mm:ss")));
		summary.put("screen_total_num", statistical.countScreen());

		summary.put("report_today_num", statistical.countReportByDate(
				DateUtils.string2Date(beginDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.string2Date(endDate, "yyyy-MM-dd HH:mm:ss")));
		summary.put("report_total_num", statistical.countReport());

		summary.put("db_today_num", statistical.countDatabaseByDate(
				DateUtils.string2Date(beginDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.string2Date(endDate, "yyyy-MM-dd HH:mm:ss")));
		summary.put("db_total_num", statistical.countDatabase());

		summary.put("api_today_num", statistical.countApiByDate(
				DateUtils.string2Date(beginDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.string2Date(endDate, "yyyy-MM-dd HH:mm:ss")));
		summary.put("api_total_num", statistical.countApi());

		summary.put("event_today_num", statistical.countEventByDate(
				DateUtils.string2Date(beginDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.string2Date(endDate, "yyyy-MM-dd HH:mm:ss")));
		summary.put("event_total_num", statistical.countEvent());

		summary.put("model_today_num", statistical.countModelByDate(
				DateUtils.string2Date(beginDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.string2Date(endDate, "yyyy-MM-dd HH:mm:ss")));
		summary.put("model_total_num", statistical.countModel());

		summary.put("job_today_num", statistical.countJobByDate(
				DateUtils.string2Date(beginDate, "yyyy-MM-dd HH:mm:ss"),
				DateUtils.string2Date(endDate, "yyyy-MM-dd HH:mm:ss")));
		summary.put("job_total_num", statistical.countJob());

		RedisUtils.set(BgSysStatConstatant.KEY_STAT_SUMMARY,
				summary.toJSONString());
	}

	@Override
	public void moduleStat() {
		
		List<JData.Entry> entryList = Lists.newArrayList();
		String beginDate = DateUtils.date2String(
				DateUtils.addDay(new Date(), -6), "yyyy-MM-dd")
				+ " 00:00:00";
		String endDate = DateUtils.date2String(new Date(), "yyyy-MM-dd")
				+ " 23:59:59";
		Json dateRangeParam = new Json();
		dateRangeParam.put("field", "log_time");
		dateRangeParam.put("start_date", beginDate);
		dateRangeParam.put("end_date", endDate);

		List<Json> aggParamList = new ArrayList<Json>();
		Json aggParam = new Json();
		aggParam = new Json();
		aggParam.put("name", "类型");
		aggParam.put("serials", "次数");
		aggParam.put("field", "name");
		aggParamList.add(aggParam);

		List<Json> aggResultList = esClientService.queryAggregation(
				"module_operation_log", "module_operation_log", null, dateRangeParam,
				aggParamList, new ArrayList<Json>());
		aggResultList.forEach(e -> {
			JData.Entry entry = new JData.Entry();
			entry.setX(String.valueOf(e.getString("key")));
			entry.setY(String.valueOf(e.getString("value")));
			entry.setName("点击量");
			entryList.add(entry);
		});
		RedisUtils.setObject(BgSysStatConstatant.KEY_STAT_MODUE_CLICK,
				JSON.toJSONString(entryList));
	}

	@Override
	public void jobStat() {
		String sql = "select state,count(1) as num from BG_ETL_JOB_LOG where log_time>=to_date('@beginDate','yyyy-mm-dd hh24:mi:ss') group by state";
		String beginDate = DateUtils.date2String(
				DateUtils.addDay(new Date(), -6), "yyyy-MM-dd")
				+ " 00:00:00";
		sql = sql.replace("@beginDate", beginDate);
		List<Object[]> objList = baseJdbcService.findBySql(sql, null);
		Json result = new Json();
		for (Object[] obj : objList) {
			if ("1".equals(String.valueOf(obj[0]))) {
				result.put("success", String.valueOf(obj[1]));
			} else if ("2".equals(String.valueOf(obj[0]))) {
				result.put("error", String.valueOf(obj[1]));
			} else if ("3".equals(String.valueOf(obj[0]))) {
				result.put("error", String.valueOf(obj[1]));
			} else if ("-1".equals(String.valueOf(obj[0]))) {
				result.put("stop", String.valueOf(obj[1]));
			}
		}
		RedisUtils.setObject(BgSysStatConstatant.KEY_STAT_JOB_STAT,
				JSON.toJSON(result));
	}

	@Override
	public void logStat() {
		Json result = new Json();
		String beginDate = DateUtils.date2String(
				DateUtils.addDay(new Date(), -6), "yyyy-MM-dd")
				+ " 00:00:00";
		String endDate = DateUtils.date2String(new Date(), "yyyy-MM-dd")
				+ " 23:59:59";
		Json dateRangeParam = new Json();
		dateRangeParam.put("field", "log_time");
		dateRangeParam.put("start_date", beginDate);
		dateRangeParam.put("end_date", endDate);

		List<Json> aggParamList = new ArrayList<Json>();
		Json aggParam = new Json();
		aggParam = new Json();
		aggParam.put("name", "类型");
		aggParam.put("serials", "次数");
		aggParam.put("field", "log_type");
		aggParamList.add(aggParam);

		List<Json> aggResultList = esClientService.queryAggregation(
				"bigdata_log", "bigdata_tomcat_log", null, dateRangeParam,
				aggParamList, new ArrayList<Json>());
		aggResultList.forEach(e -> {
			result.put(e.getString("key"), e.getString("value"));
		});

		RedisUtils.setObject(BgSysStatConstatant.KEY_STAT_LOG_STAT,
				JSON.toJSON(result));
	}

	@Override
	public void usageStat() {
		// mysql
		Json mysqlUsage = mysqlClientService.getDbUsageInfo(null,"bigdata");
		RedisUtils.set(BgSysStatConstatant.KEY_STAT_MYSQL_USAGE,
				mysqlUsage.toJSONString());
		// hdfs
		OptionDto hdfsDto = optionService.getAllOptionParam("hdfs");
		String hdfsApiUrl = null;
		if (hdfsDto != null && hdfsDto.getStatus() != 0) {
			hdfsApiUrl = hdfsDto.getFrameParamMap().get("hdfs_api_url");
		}
		Json hdfsUsage = HdfsUtils.getHdfsUsage(hdfsApiUrl);
		RedisUtils.set(BgSysStatConstatant.KEY_STAT_HDFS_USAGE,
				hdfsUsage.toJSONString());
	}

	@Override
	public void nodeMonitoring() {
		List<Node> nodes = bgNodeService.findAll();
		List<String> correctIds = nodes.stream().filter(x -> bgNodeService.nodeConnection(x))
				.map(x->x.getId()).collect(Collectors.toList());
		correctIds.add("");
		bgNodeService.updateStatusByIds(correctIds);
	}
}
