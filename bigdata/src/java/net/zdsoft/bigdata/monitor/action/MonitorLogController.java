package net.zdsoft.bigdata.monitor.action;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.data.service.OptionService;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.frame.data.elastic.EsClientService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.echarts.Option;
import net.zdsoft.echarts.convert.JConverter;
import net.zdsoft.echarts.convert.api.JData;
import net.zdsoft.echarts.coords.cartesian2d.Cartesian2dAxis;
import net.zdsoft.echarts.coords.enu.AxisType;
import net.zdsoft.echarts.element.Tooltip;
import net.zdsoft.echarts.enu.AxisPointerType;
import net.zdsoft.echarts.enu.BottomEx;
import net.zdsoft.echarts.enu.CoordinateSystem;
import net.zdsoft.echarts.enu.LeftEnum;
import net.zdsoft.echarts.enu.LeftEx;
import net.zdsoft.echarts.enu.LegendEnum;
import net.zdsoft.echarts.enu.Orient;
import net.zdsoft.echarts.enu.Origin;
import net.zdsoft.echarts.enu.PositionEnum;
import net.zdsoft.echarts.enu.RightEnum;
import net.zdsoft.echarts.enu.RightEx;
import net.zdsoft.echarts.enu.SeriesEnum;
import net.zdsoft.echarts.enu.TopEnum;
import net.zdsoft.echarts.enu.TopEx;
import net.zdsoft.echarts.enu.Trigger;
import net.zdsoft.echarts.series.Line;
import net.zdsoft.echarts.series.Series;
import net.zdsoft.echarts.series.data.BarData;
import net.zdsoft.echarts.style.ItemStyle;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

@Controller
@RequestMapping(value = "/bigdata/monitor/")
public class MonitorLogController extends BigdataBaseAction {

	private static final Logger logger = LoggerFactory
			.getLogger(MonitorLogController.class);

	@Autowired
	EsClientService esClientService;

	@Autowired
	private OptionService optionService;

	/**
	 * 大数据log监控分析
	 */
	@RequestMapping(value = { "/log", "/common/log" })
	public String index(ModelMap map) {
		OptionDto flumeDto = optionService.getAllOptionParam("flume");
		if (flumeDto != null && flumeDto.getStatus() == 1) {
			map.put("logList", getSystemLogList(50));
			return "/bigdata/monitor/log/logMonitor.ftl";
		} else {
			map.put("serverName", "Flume");
			map.put("serverCode", "flume");
			return "/bigdata/noServer.ftl";
		}
	}

	@RequestMapping("/log/component")
	public String getLogComponet(ModelMap map) {
		map.put("logList", getSystemLogList(8));
		return "/bigdata/monitor/log/systemLogListDiv.ftl";
	}

	@RequestMapping("/log/query/list")
	public String getLogList(String type, String logType, String bizName,
			String subsystem, ModelMap map) {
		if ("business".equals(type)) {
			map.put("logList",
					getBusinessLogList(logType, bizName, subsystem, 100));
		} else {
			map.put("logList", getSystemLogList(50));
		}
		map.put("logType", logType);
		map.put("bizName", bizName);
		map.put("subsystem", subsystem);
		return "/bigdata/monitor/log/businessLogList.ftl";
	}

	private List<Json> getSystemLogList(int size) {
		try {
			List<String> resultFieldList = new ArrayList<String>();
			resultFieldList.add("log_time");
			resultFieldList.add("log_type");
			resultFieldList.add("log_class");
			resultFieldList.add("log_line");
			resultFieldList.add("log_message");
			Json page = new Json();
			page.put("pageIndex", 1);
			page.put("pageSize", size);

			Json sortParam = new Json();
			sortParam.put("sort_field", "log_time");
			sortParam.put("sort_type", "desc");

			Json queryParam = new Json();
			queryParam.put("type", "matchAllQuery");// matchAllQuery
													// //prefixQuery前缀
			List<Json> logList = esClientService.query("bigdata_log",
					"bigdata_tomcat_log", queryParam, resultFieldList,
					sortParam, page);
			return logList;
		} catch (Exception e) {
			return new ArrayList<Json>();
		}

	}

	private List<Json> getBusinessLogList(String logType, String bizName,
			String subsystem, int size) {
		try {
			List<String> resultFieldList = new ArrayList<String>();
			resultFieldList.add("bizCode");
			resultFieldList.add("logType");
			resultFieldList.add("bizName");
			resultFieldList.add("subSystem");
			resultFieldList.add("description");
			resultFieldList.add("oldData");
			resultFieldList.add("newData");
			resultFieldList.add("operator");
			resultFieldList.add("operationTime");
			Json page = new Json();
			page.put("pageIndex", 1);
			page.put("pageSize", size);

			List<Json> paramList = new ArrayList<Json>();
			if (StringUtils.isNotBlank(logType) && !"all".equals(logType)) {
				Json queryParam1 = new Json();
				queryParam1.put("type", "must");
				queryParam1.put("field", "logType");
				queryParam1.put("value", logType);
				paramList.add(queryParam1);
			}
			if (StringUtils.isNotBlank(bizName)) {
				Json queryParam2 = new Json();
				queryParam2.put("type", "must");
				queryParam2.put("field", "bizName");
				queryParam2.put("value", bizName);
				paramList.add(queryParam2);
			}
			if (StringUtils.isNotBlank(subsystem)) {
				Json queryParam3 = new Json();
				queryParam3.put("type", "must");
				queryParam3.put("field", "subSystem");
				queryParam3.put("value", subsystem);
				paramList.add(queryParam3);
			}

			Json sortParam = new Json();
			sortParam.put("sort_field", "operationTime");
			sortParam.put("sort_type", "desc");

			List<Json> logList = esClientService.multiGroupQuery(
					"biz_operation_log", "business", paramList,
					resultFieldList, sortParam, page);
			return logList;
		} catch (Exception e) {
			return new ArrayList<Json>();
		}

	}

	@ResponseBody
	@ControllerInfo("30天多维模型的热度统计")
	@RequestMapping("/log/chart/model/hotstat")
	public String modelHotStat() {
		try {
			String beginDate = DateUtils.date2String(
					DateUtils.addDay(new Date(), -29), "yyyy-MM-dd")
					+ " 00:00:00";
			String endDate = DateUtils.date2String(new Date(), "yyyy-MM-dd")
					+ " 23:59:59";
			Json dateRangeParam = new Json();
			dateRangeParam.put("field", "operationTime");
			dateRangeParam.put("start_date", beginDate);
			dateRangeParam.put("end_date", endDate);

			List<Json> aggParamList = new ArrayList<Json>();
			Json aggParam = new Json();
			aggParam = new Json();
			aggParam.put("name", "模型名称");
			aggParam.put("serials", "次数");
			aggParam.put("field", "bizName");
			aggParamList.add(aggParam);

			List<Json> paramList = new ArrayList<Json>();
			Json queryParam1 = new Json();
			queryParam1.put("type", "must");
			queryParam1.put("field", "bizCode");
			queryParam1.put("value", "model-query");
			paramList.add(queryParam1);

			List<Json> aggResultList = esClientService.queryAggregation(
					"biz_operation_log", "business", null, dateRangeParam,
					aggParamList, paramList);
			
			//int huobao=0,re=0,wen=0,len=0,bing=0;
			aggResultList.sort(new Comparator<Json>() {
				@Override
				public int compare(Json o1, Json o2) {
					return o1.getString("value").compareTo(
							o2.getString("value"));
				}
			});
//			List<JSON> entryList = Lists.newArrayList();
			aggResultList.forEach(e -> {
//				entry.setX(e.getString("key"));
//				entry.setY(e.getString("value"));
//				entry.setName(e.getString("serials"));
//				entryList.add(entry);
			});
			return "";
		} catch (Exception e) {
			return "";
		}
	}

	@ResponseBody
	@ControllerInfo("today's chart")
	@RequestMapping("/log/chart/today")
	public Response chart4today() {
		try {
			String beginDate = DateUtils.date2String(new Date(), "yyyy-MM-dd")
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
			List<JData.Entry> entryList = Lists.newArrayList();
			aggResultList.forEach(e -> {
				JData.Entry entry = new JData.Entry();
				entry.setX(e.getString("key"));
				entry.setY(e.getString("value"));
				entry.setName(e.getString("serials"));
				entryList.add(entry);
			});
			return getBarChart(entryList, 1);
		} catch (Exception e) {
			logger.error("出错了:" + e.getMessage());
			return Response.error().data("出错了:" + e.getMessage()).build();
		}
	}

	@ResponseBody
	@ControllerInfo("week's chart")
	@RequestMapping("/log/chart/week")
	public Response chart4week() {
		try {
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
			List<JData.Entry> entryList = Lists.newArrayList();
			aggResultList.forEach(e -> {
				JData.Entry entry = new JData.Entry();
				entry.setX(e.getString("key"));
				entry.setY(e.getString("value"));
				entry.setName(e.getString("serials"));
				entryList.add(entry);
			});
			return getBarChart(entryList, 1);
		} catch (Exception e) {
			logger.error("出错了:" + e.getMessage());
			return Response.error().data("出错了:" + e.getMessage()).build();
		}
	}

	@ResponseBody
	@ControllerInfo("week's chart")
	@RequestMapping("/log/chart/week2")
	public Response chart4week2() {
		try {
			String beginDate = DateUtils.date2String(
					DateUtils.addDay(new Date(), -13), "yyyy-MM-dd")
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
			List<JData.Entry> entryList = Lists.newArrayList();
			aggResultList.forEach(e -> {
				JData.Entry entry = new JData.Entry();
				entry.setX(e.getString("key"));
				entry.setY(e.getString("value"));
				entry.setName(e.getString("serials"));
				entryList.add(entry);
			});
			return getBarChart(entryList, 1);
		} catch (Exception e) {
			logger.error("出错了:" + e.getMessage());
			return Response.error().data("出错了:" + e.getMessage()).build();
		}
	}

	@ResponseBody
	@ControllerInfo("month's chart")
	@RequestMapping("/log/chart/month")
	public Response chart4month() {
		try {
			Json dateAggParam = new Json();
			dateAggParam.put("field", "log_time");
			dateAggParam.put("interval", "day");
			dateAggParam.put("name", "次数");
			//

			String beginDate = DateUtils.date2String(
					DateUtils.addDay(new Date(), -29), "yyyy-MM-dd")
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
					"bigdata_log", "bigdata_tomcat_log", dateAggParam,
					dateRangeParam, aggParamList, new ArrayList<Json>());
			List<JData.Entry> entryList = Lists.newArrayList();
			aggResultList.forEach(e -> {
				JData.Entry entry = new JData.Entry();
				entry.setX(e.getString("key"));
				entry.setY(e.getString("value"));
				entry.setName(e.getString("serials"));
				entryList.add(entry);
			});
			return getLineChart(entryList);
		} catch (Exception e) {
			logger.error("出错了:" + e.getMessage());
			return Response.error().data("出错了:" + e.getMessage()).build();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Response getBarChart(List<JData.Entry> entryList, Integer windowSize) {
		JData data = new JData();
		data.setType(SeriesEnum.bar);
		data.setCoordSys(CoordinateSystem.cartesian2d);
		data.setEntryList(entryList);
		data.setSelfCoordSys(true);
		data.setSelfXAxis(true);
		data.setSelfYAxis(true);

		JData.JCoordSysPosition position = new JData.JCoordSysPosition();
		data.setCoordSysPosition(position);
		data.setXAxisType(AxisType.category);
		JData.JAxisPosition xp = new JData.JAxisPosition();
		JData.JAxisPosition yp = new JData.JAxisPosition();
		data.setXJAxisPosition(xp);
		data.setYJAxisPosition(yp);

		Option option = new Option();

		JConverter.convert(data, option);
		for (Cartesian2dAxis dAxis : option.xAxis()) {
			dAxis.axisLabel().interval(0);
		}
		// 设置标题，提示框等
		option.title().show(true).left(LeftEnum.left);
		Tooltip tooltip = new Tooltip().option(option);
		tooltip.axisPointer().type(AxisPointerType.shadow);
		option.tooltip(tooltip);
		option.legend().show(true).right(RightEnum.center)
				.type(LegendEnum.scroll).top(TopEnum.bottom)
				.orient(Orient.horizontal);

		option.legend().top(TopEnum.bottom);

		String[] color = new String[] { "#faad14", "#f5222d", "#a8071a",
				"#37a2da" };

		option.legend().show(false);

		for (Series e : option.series()) {
			e.label().position(PositionEnum.top).show(true);
			for (Object b : e.getData()) {
				BarData bd = (BarData) b;
				ItemStyle itemStyle = new ItemStyle();
				if (("ERROR").equals(bd.getName())) {
					itemStyle.color(color[1]);
				} else if (("WARN").equals(bd.getName())) {
					itemStyle.color(color[0]);
				} else if (("FATAL").equals(bd.getName())) {
					itemStyle.color(color[2]);
				} else {
					itemStyle.color(color[3]);
				}
				bd.itemStyle(itemStyle);
			}
		}
		option.grid().forEach(
				e -> {
					e.left(LeftEx.create(14)).right(RightEx.create(20))
							.top(TopEx.create(20)).bottom(BottomEx.create(10))
							.containLabel(true);
				});
		tooltip.trigger(Trigger.axis);

		if (option.xAxis().size() > 0) {
			int xNumber = option.xAxis().get(0).getData().size();
			if (xNumber > 10) {
				for (Cartesian2dAxis e : option.xAxis()) {
					e.axisLabel().interval(
							windowSize == 2 ? (xNumber / 10) : (xNumber / 5));
				}
			}
		}
		return Response.ok().data(JSON.toJSON(option).toString()).build();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Response getLineChart(List<JData.Entry> entryList) {
		JData data = new JData();
		data.setType(SeriesEnum.line);
		data.setCoordSys(CoordinateSystem.cartesian2d);
		data.setEntryList(entryList);
		data.setSelfCoordSys(true);
		data.setSelfXAxis(true);
		data.setSelfYAxis(true);

		Option option = new Option();

		JConverter.convert(data, option);
		for (Cartesian2dAxis dAxis : option.xAxis()) {
			dAxis.axisLabel().interval(0);
		}
		// 设置标题，提示框等
		option.title().show(true).left(LeftEnum.left);
		option.setAnimation(true);

		Tooltip tooltip = new Tooltip().option(option);
		tooltip.axisPointer().type(AxisPointerType.shadow);
		option.tooltip(tooltip);
		option.legend().show(true).right(RightEnum.center)
				.type(LegendEnum.scroll).top(TopEnum.bottom)
				.orient(Orient.horizontal);
		tooltip.trigger(Trigger.axis);

		// option.legend().show(false);
		String[] color = new String[] { "#faad14", "#f5222d", "#a8071a",
				"#37a2da" };
		for (Series e : option.series()) {
			((Line) e).areaStyle().origin(Origin.start);
			((Line) e).smooth(true);
			ItemStyle itemStyle = new ItemStyle();
			if (("ERROR").equals(e.getName())) {
				itemStyle.color(color[1]);
			} else if (("WARN").equals(e.getName())) {
				itemStyle.color(color[0]);
			} else if (("FATAL").equals(e.getName())) {
				itemStyle.color(color[2]);
			} else {
				itemStyle.color(color[3]);
			}
			e.setItemStyle(itemStyle);
		}
		if (option.xAxis().size() > 0) {
			int xNumber = option.xAxis().get(0).getData().size();
			option.xAxis().get(0).boundaryGap(false);
			if (xNumber > 10) {
				for (Cartesian2dAxis e : option.xAxis()) {
					e.axisLabel().interval(xNumber / 10);
				}
			}
		}
		option.grid().forEach(
				e -> {
					e.left(LeftEx.create(14)).right(RightEx.create(20))
							.top(TopEx.create(20)).bottom(BottomEx.create(20))
							.containLabel(true);
				});
		return Response.ok().data(JSON.toJSON(option).toString()).build();
	}

}
