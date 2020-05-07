package net.zdsoft.bigdata.monitor.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.metadata.entity.QualityDimResult;
import net.zdsoft.bigdata.metadata.service.QualityDimResultService;
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
import net.zdsoft.framework.utils.DateUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping(value = "/bigdata/monitor/")
public class MonitorQualityController extends BigdataBaseAction {
	private static final Logger logger = Logger
			.getLogger(MonitorIndexController.class);

	@Autowired
	private QualityDimResultService qualityDimResultService;

	/**
	 * 大数据质量分析
	 */
	@RequestMapping(value = { "/quality", "/common/quality" })
	public String index(ModelMap map) {
		List<QualityDimResult> dimResultList = qualityDimResultService
				.findQualityDimResultsByTypeAndStatus(1, 1);
		if (CollectionUtils.isNotEmpty(dimResultList)) {
			map.put("qualityResult", dimResultList.get(0));
		} else {
			map.put("qualityResult", new QualityDimResult());
		}
		return "/bigdata/monitor/qualityAnalyse.ftl";
	}

	@RequestMapping("/quality/getRadarChart")
	@ResponseBody
	public Response getQualityRadarChart() {
		try {
			List<JData.Entry> entryList = new ArrayList<JData.Entry>();
			List<QualityDimResult> dimResultList = qualityDimResultService
					.findQualityDimResultsByTypeAndStatus(2, 1);
			for (QualityDimResult dimResult : dimResultList) {
				JData.Entry data = new JData.Entry();
				data.setX(dimResult.getDimName());
				data.setY(dimResult.getResult());
				data.setName("质量均衡度");
				entryList.add(data);
			}
			if (entryList.size() < 1) {
				return Response.error().message("查询无数据").build();
			}
			return Response.ok().data(getRadarChartOption(entryList)).build();
		} catch (Exception e) {
			logger.error("出错了:" + e.getMessage());
			return Response.error().message(e.getMessage()).build();
		}
	}

	@RequestMapping("/quality/getBarChart")
	@ResponseBody
	public Response getQualityBarOption() {
		try {
			List<JData.Entry> entryList = new ArrayList<JData.Entry>();
			List<QualityDimResult> dimResultList = qualityDimResultService
					.findQualityDimResultsByType(2);
			for (QualityDimResult dimResult : dimResultList) {
				JData.Entry data = new JData.Entry();
				data.setX(DateUtils.date2String(dimResult.getStatTime(),
						"MM-dd HH:mm:ss"));
				data.setY(dimResult.getResult());
				data.setName(dimResult.getDimName());
				data.setStack("质量指数");
				entryList.add(data);
			}
			if (entryList.size() < 1) {
				return Response.error().message("查询无数据").build();
			}
			return Response.ok().data(getBarChartOption(entryList, 2)).build();
		} catch (Exception e) {
			logger.error("出错了:" + e.getMessage());
			return Response.error().message(e.getMessage()).build();
		}
	}

	@RequestMapping("/quality/getLineChart")
	@ResponseBody
	public Response getQualityLineOption() {
		try {
			List<JData.Entry> entryList = new ArrayList<JData.Entry>();
			List<QualityDimResult> dimResultList = qualityDimResultService
					.findQualityDimResultsByType(1);
			for (QualityDimResult dimResult : dimResultList) {
				JData.Entry data = new JData.Entry();
				data.setX(DateUtils.date2String(dimResult.getStatTime(),
						"MM-dd HH:mm:ss"));
				data.setY(dimResult.getResult());
				data.setName("质量指数");
				entryList.add(data);
			}
			if (entryList.size() < 1) {
				return Response.error().message("查询无数据").build();
			}
			return Response.ok().data(getLineChart(entryList, false)).build();
		} catch (Exception e) {
			logger.error("出错了:" + e.getMessage());
			return Response.error().message(e.getMessage()).build();
		}
	}

	@RequestMapping("/quality/getLineChartByDimName")
	@ResponseBody
	public Response getLineChartByDimName(String dimName) {
		try {
			List<JData.Entry> entryList = new ArrayList<JData.Entry>();
			List<QualityDimResult> dimResultList = qualityDimResultService
					.findQualityDimResultsByTypeAndDimName(2, dimName);
			for (QualityDimResult dimResult : dimResultList) {
				JData.Entry data = new JData.Entry();
				data.setX(DateUtils.date2String(dimResult.getStatTime(),
						"MM-dd HH:mm:ss"));
				data.setY(dimResult.getResult());
				data.setName("质量指数");
				entryList.add(data);
			}
			if (entryList.size() < 1) {
				return Response.error().message("查询无数据").build();
			}
			return Response.ok().data(getLineChart(entryList, false)).build();
		} catch (Exception e) {
			logger.error("出错了:" + e.getMessage());
			return Response.error().message(e.getMessage()).build();
		}
	}

	private String getRadarChartOption(List<JData.Entry> entryList) {
		// 雷达图
		JSONObject option = new JSONObject();

		Object[] xAxisValues = new Object[entryList.size()];
		JSONArray indicatorArray = new JSONArray();
		int i = 0;
		for (JData.Entry data : entryList) {
			xAxisValues[i] = data.getY();
			JSONObject indicator = new JSONObject();
			indicator.put("name", data.getX());
			indicator.put("max", 100);
			indicatorArray.add(indicator);
			i++;
		}

		// option.put("backgroundColor", "#01193d");

		JSONObject tooltip = new JSONObject();
		tooltip.put("trigger", "axis");
		option.put("tooltip", tooltip);

		JSONObject radar = new JSONObject();
		radar.put("indicator", indicatorArray);

		JSONObject name = new JSONObject();
		JSONObject name_textStyle = new JSONObject();
		name_textStyle.put("fontSize", 14);
		name_textStyle.put("color", "#000000");
		name.put("textStyle", name_textStyle);
		radar.put("name", name);
		radar.put("triggerEvent", true);

		option.put("radar", radar);

		JSONObject series = new JSONObject();
		series.put("type", "radar");
		JSONObject series_tooltip = new JSONObject();
		series_tooltip.put("trigger", "item");
		series.put("tooltip", series_tooltip);

		JSONObject series_data = new JSONObject();
		JSONArray series_data_array = new JSONArray();
		series_data.put("value", xAxisValues);
		series_data.put("name", "质量均衡度");

		JSONObject series_data_itemStyle = new JSONObject();
		JSONObject series_data_itemStyle_normal = new JSONObject();
		series_data_itemStyle_normal.put("color", "rgba(5, 128, 242, 0.8)");
		series_data_itemStyle.put("normal", series_data_itemStyle_normal);
		series_data.put("itemStyle", series_data_itemStyle);

		JSONObject series_data_areaStyle = new JSONObject();
		JSONObject series_data_areaStyle_normal = new JSONObject();
		series_data_areaStyle_normal.put("color", "#BE4E20");
		series_data_areaStyle.put("normal", series_data_areaStyle_normal);
		series_data.put("areaStyle", series_data_areaStyle);

		series_data_array.add(series_data);
		series.put("data", series_data_array);
		option.put("series", series);
		return JSON.toJSON(option).toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String getLineChart(List<JData.Entry> entryList,
			boolean isMutilSeries) {
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
				.type(LegendEnum.scroll).top(TopEnum.top)
				.orient(Orient.horizontal);
		tooltip.trigger(Trigger.axis);
		if (!isMutilSeries) {
			option.legend().show(false);
			String[] color = new String[] { "#597ef7", "#36cfc9", "#bae637",
					"#ffc53d", "#ff7a45", "#f759ab", "#40a9ff", "#73d13d",
					"#ffec3d", "#ffa940", "#ff4d4f", "#9254de" };

			Random random = new Random();
			int index = random.nextInt(10) % (10 - 0 + 1) + 0;
			for (Series e : option.series()) {
				((Line) e).areaStyle().origin(Origin.start);
				((Line) e).smooth(true);
				ItemStyle itemStyle = new ItemStyle();
				itemStyle.color(color[index]);
				e.setItemStyle(itemStyle);
			}
		}

		if (option.xAxis().size() > 0) {
			int xNumber = option.xAxis().get(0).getData().size();
			if (xNumber > 10) {
				for (Cartesian2dAxis e : option.xAxis()) {
					e.axisLabel().interval(xNumber / 5);
				}
			}
		}

		option.grid().forEach(
				e -> {
					e.left(LeftEx.create(20)).right(RightEx.create(20))
							.top(TopEx.create(20)).bottom(BottomEx.create(20))
							.containLabel(true);
				});
		return JSON.toJSON(option).toString();
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
		option.legend().show(true).right(RightEnum.center)
				.type(LegendEnum.scroll).orient(Orient.horizontal);

		option.legend().top(TopEnum.top);

		// String[] color = new String[] { "#597ef7", "#36cfc9", "#bae637",
		// "#ffc53d", "#ff7a45", "#f759ab", "#40a9ff", "#73d13d",
		// "#ffec3d", "#ffa940", "#ff4d4f", "#9254de" };

		// option.legend().show(false);
		for (Series e : option.series()) {
			// e.label().position(PositionEnum.insideTop).show(true);
			// int i = 0;
			for (Object b : e.getData()) {
				BarData bd = (BarData) b;
				ItemStyle itemStyle = new ItemStyle();
				// itemStyle.color(color[i++ % color.length]);
				bd.itemStyle(itemStyle);
			}
		}
		option.grid().forEach(
				e -> {
					e.left(LeftEx.create(20)).right(RightEx.create(20))
							.top(TopEx.create(60)).bottom(BottomEx.create(20))
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
