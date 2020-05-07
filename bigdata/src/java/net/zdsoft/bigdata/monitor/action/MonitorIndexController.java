package net.zdsoft.bigdata.monitor.action;

import java.util.Date;
import java.util.List;

import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.stat.constant.BgSysStatConstatant;
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
import net.zdsoft.echarts.enu.LeftEx;
import net.zdsoft.echarts.enu.PositionEnum;
import net.zdsoft.echarts.enu.RightEx;
import net.zdsoft.echarts.enu.SeriesEnum;
import net.zdsoft.echarts.enu.TopEx;
import net.zdsoft.echarts.enu.Trigger;
import net.zdsoft.echarts.series.Series;
import net.zdsoft.echarts.series.data.BarData;
import net.zdsoft.echarts.style.ItemStyle;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.RedisUtils;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

@Controller
@RequestMapping(value = "/bigdata/monitor/")
public class MonitorIndexController extends BigdataBaseAction {
	private static final Logger logger = Logger
			.getLogger(MonitorIndexController.class);

	/**
	 * 大数据dashboard页
	 */
	@RequestMapping(value = { "/dashboard", "/common/dashboard" })
	public String dashboard(ModelMap map) {
		Json summaryStat = RedisUtils.getObject(
				BgSysStatConstatant.KEY_STAT_SUMMARY, Json.class);
		if (summaryStat == null)
			summaryStat = new Json();
		map.addAttribute("summaryStat", summaryStat);

		Json mysqlStat = RedisUtils.getObject(
				BgSysStatConstatant.KEY_STAT_MYSQL_USAGE, Json.class);
		if (mysqlStat == null)
			mysqlStat = new Json();
		map.addAttribute("mysqlStat", mysqlStat);

		Json hdfsStat = RedisUtils.getObject(
				BgSysStatConstatant.KEY_STAT_HDFS_USAGE, Json.class);
		if (hdfsStat == null)
			hdfsStat = new Json();
		map.addAttribute("hdfsStat", hdfsStat);

		Json jobStat = RedisUtils.getObject(
				BgSysStatConstatant.KEY_STAT_JOB_STAT, Json.class);
		if (jobStat == null)
			jobStat = new Json();
		map.addAttribute("jobStat", jobStat);

		Json logStat = RedisUtils.getObject(
				BgSysStatConstatant.KEY_STAT_LOG_STAT, Json.class);
		if (logStat == null)
			logStat = new Json();
		map.addAttribute("logStat", logStat);
		map.addAttribute("statTime",
				DateUtils.date2String(new Date(), "yyyy-MM-dd HH") + ":00:00");

		return "/bigdata/monitor/dashboard.ftl";
	}

	@RequestMapping("/getModelChart")
	@ResponseBody
	public Response getModuleChartOption() {
		try {
			List<JData.Entry> entryList = JSONArray.parseArray(
					RedisUtils.get(BgSysStatConstatant.KEY_STAT_MODUE_CLICK),
					JData.Entry.class);
			if (entryList.size() < 1) {
				return Response.error().message("查询无数据").build();
			}
			return Response.ok().data(getBarChartOption(entryList, 2)).build();
		} catch (Exception e) {
			logger.error("出错了:" + e.getMessage());
			return Response.error().message(e.getMessage()).build();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String getBarChartOption(List<JData.Entry> entryList,
			Integer windowSize) {
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
		// option.title().show(true).left(LeftEnum.left);
		Tooltip tooltip = new Tooltip().option(option);
		tooltip.axisPointer().type(AxisPointerType.shadow);
		option.tooltip(tooltip);
		// option.legend().show(true).right(RightEnum.center)
		// .type(LegendEnum.scroll).orient(Orient.horizontal);
		//
		// option.legend().top(TopEnum.bottom);

		String[] color = new String[] { "#597ef7", "#36cfc9", "#bae637",
				"#ffc53d", "#ff7a45", "#f759ab", "#40a9ff", "#73d13d",
				"#ffec3d", "#ffa940", "#ff4d4f", "#9254de" };

		option.legend().show(false);
		for (Series e : option.series()) {
			e.label().position(PositionEnum.top).show(true);
			int i = 0;
			for (Object b : e.getData()) {
				BarData bd = (BarData) b;
				ItemStyle itemStyle = new ItemStyle();
				itemStyle.color(color[i++ % color.length]);
				bd.itemStyle(itemStyle);
			}
		}
		option.grid().forEach(
				e -> {
					e.left(LeftEx.create(14)).right(RightEx.create(20))
							.top(TopEx.create(40)).bottom(BottomEx.create(10))
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
		return JSON.toJSON(option).toString();
	}

}
