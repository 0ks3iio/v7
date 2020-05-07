package net.zdsoft.bigdata.extend.data.action;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.extend.data.biz.GroupAnalysisBiz;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.echarts.Option;
import net.zdsoft.echarts.convert.JConverter;
import net.zdsoft.echarts.convert.api.JData;
import net.zdsoft.echarts.coords.cartesian2d.Cartesian2dAxis;
import net.zdsoft.echarts.coords.enu.AxisType;
import net.zdsoft.echarts.element.Tooltip;
import net.zdsoft.echarts.enu.*;
import net.zdsoft.echarts.series.Series;
import net.zdsoft.echarts.series.data.BarData;
import net.zdsoft.echarts.style.ItemStyle;
import net.zdsoft.framework.entity.Json;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 学生画像分析
 * Created by wangdongdong on 2018/8/22 16:58.
 */
@Controller
@RequestMapping("/bigdata/groupAnalysis/student")
public class StudentAnalysisController extends BigdataBaseAction {

	@Resource
	private GroupAnalysisBiz groupAnalysisBiz;

	/**
	 * 学段
	 *
	 * @param profileCode
	 * @param tagArray
	 * @return
	 */
	@RequestMapping("/getSchoolSectionOption")
	@ResponseBody
	public Response getSchoolSectionOption(String profileCode, String tagArray) {
		try {
			List<Json> result = groupAnalysisBiz.queryTagResult(profileCode, tagArray, "section");
			return result.size() > 0 ? Response.ok().data(result).build() : Response.error().build();
		} catch (Exception e) {
			return Response.error().message("查询出错，请检查标签是否维护正确").build();
		}
	}

	/**
	 * 年龄段
	 *
	 * @param profileCode
	 * @param tagArray
	 * @return
	 */
	@RequestMapping("/getAge")
	@ResponseBody
	public Response getAge(String profileCode, String tagArray) {
		try {
			List<Json> result = groupAnalysisBiz.queryTagResult(profileCode, tagArray, "age");
			return result.size() > 0 ? Response.ok().data(result).build() : Response.error().build();
		} catch (Exception e) {
			return Response.error().message("查询出错，请检查标签是否维护正确").build();
		}
	}

	/**
	 * 民族
	 *
	 * @param profileCode
	 * @param tagArray
	 * @return
	 */
	@RequestMapping("/getNationOption")
	@ResponseBody
	public Response getNationOption(String profileCode, String tagArray) {
		List<Json> kylinList = null;
		try {
			kylinList = groupAnalysisBiz.queryTagResult(profileCode, tagArray, "nation");
		} catch (Exception e) {
			return Response.error().message("查询出错，请检查标签是否维护正确").build();
		}
		if (kylinList.size() == 0) {
			return Response.error().build();
		}
		List<JData.Entry> entryList = Lists.newArrayList();
		kylinList.forEach(e -> {
			JData.Entry entry = new JData.Entry();
			entry.setX(e.getString("key"));
			entry.setY(e.getLong("value"));
			entryList.add(entry);
		});
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
		option.legend().show(true);
		tooltip.trigger(Trigger.axis);

		option.grid().forEach(e-> e.left(LeftEx.create("10%")).right(RightEx.create("5%")).top(TopEx.create("5%")).bottom(BottomEx.create("15%")));


		String[] color = new String[] { "#37a2da", "#34c4e9", "#96bfff",
				"#317deb", "#fedb5b", "#96beb8", "#ae89f3", "#ff9f7f",
				"#ae89f3", "#46c6a3" };

		for (Series e : option.series()) {
			int i = 0;
			for (Object b : e.getData()) {
				BarData bd = (BarData) b;
				ItemStyle itemStyle = new ItemStyle();
				itemStyle.color(color[i++ % color.length]);
				bd.itemStyle(itemStyle);
			}
		}

		return Response.ok().data(JSON.toJSON(option).toString()).build();
	}

}
