package net.zdsoft.bigdata.frame.data.demo.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;

import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.frame.data.elastic.EsClientService;
import net.zdsoft.echarts.Option;
import net.zdsoft.echarts.convert.JConverter;
import net.zdsoft.echarts.convert.api.JData;
import net.zdsoft.echarts.coords.cartesian2d.Cartesian2dAxis;
import net.zdsoft.echarts.coords.enu.AxisType;
import net.zdsoft.echarts.coords.geo.Geo;
import net.zdsoft.echarts.element.Continuous;
import net.zdsoft.echarts.element.Tooltip;
import net.zdsoft.echarts.enu.AxisPointerType;
import net.zdsoft.echarts.enu.CoordinateSystem;
import net.zdsoft.echarts.enu.LeftEnum;
import net.zdsoft.echarts.enu.LegendEnum;
import net.zdsoft.echarts.enu.Orient;
import net.zdsoft.echarts.enu.RightEnum;
import net.zdsoft.echarts.enu.RoamEx;
import net.zdsoft.echarts.enu.SeriesEnum;
import net.zdsoft.echarts.enu.TopEnum;
import net.zdsoft.echarts.enu.Trigger;
import net.zdsoft.echarts.series.EMap;
import net.zdsoft.echarts.series.Series;
import net.zdsoft.echarts.series.data.BarData;
import net.zdsoft.echarts.style.ItemStyle;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.DateUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

@Controller
@RequestMapping("/bigdata/frame/common/demo")
public class EsDemoController extends BaseAction {

    @Autowired
    EsClientService esClientService;

    @RequestMapping("/es")
    public String query(ModelMap map) {

//		String beginDate = DateUtils.date2String(
//				DateUtils.addDay(new Date(), -29), "yyyy-MM-dd")
//				+ " 00:00:00";
//		String endDate = DateUtils.date2String(new Date(), "yyyy-MM-dd")
//				+ " 23:59:59";
//		Json dateRangeParam = new Json();
//		dateRangeParam.put("field", "operationTime");
//		dateRangeParam.put("start_date", beginDate);
//		dateRangeParam.put("end_date", endDate);
//
//		List<Json> aggParamList = new ArrayList<Json>();
//		Json aggParam = new Json();
//		aggParam = new Json();
//		aggParam.put("name", "模型名称");
//		aggParam.put("serials", "次数");
//		aggParam.put("field", "bizName");
//		aggParamList.add(aggParam);

        List<Json> paramList = new ArrayList<Json>();
        Json queryParam1 = new Json();
        queryParam1.put("type", "must");
        queryParam1.put("field", "dbType");
        queryParam1.put("value", "es");
        paramList.add(queryParam1);

        List<Json> rangeList = new ArrayList<Json>();
        Json rangeParam1 = new Json();
        rangeParam1.put("type", "gte");
        rangeParam1.put("field", "duration");
        rangeParam1.put("value", "100");
        rangeList.add(rangeParam1);


        List<String> resultFieldList = new ArrayList<String>();
        resultFieldList.add("sql");
        resultFieldList.add("dbType");
        resultFieldList.add("duration");

        List<Json> aggResultList = esClientService.rangeQuery(
                "sql_analyse_log", "sql_analyse_log", paramList, rangeList, resultFieldList,null, null);

        // int huobao=0,re=0,wen=0,len=0,bing=0;
//		aggResultList.sort(new Comparator<Json>() {
//			@Override
//			public int compare(Json o1, Json o2) {
//				return o2.getIntValue("value") - o1.getIntValue("value");
//			}
//		});
        // List<JSON> entryList = Lists.newArrayList();
        aggResultList.forEach(e -> {
            System.out.print(e.toJSONString());
            System.out.print("\n");
            // entry.setX(e.getString("key"));
            // entry.setY(e.getString("value"));
            // entry.setName(e.getString("serials"));
            // entryList.add(entry);
        });
        map.put("esList", aggResultList);

        // List<String> resultFieldList = new ArrayList<String>();
        // resultFieldList.add("logType");
        // resultFieldList.add("bizCode");
        // resultFieldList.add("bizName");
        // resultFieldList.add("subSystem");
        // resultFieldList.add("description");
        // resultFieldList.add("operator");
        // resultFieldList.add("operationTime");
        // Json page = new Json();
        // page.put("pageIndex", 1);
        // page.put("pageSize", 20);
        //
        // Json queryParam = new Json();
        // queryParam.put("type", "matchAllQuery");// matchAllQuery
        // //prefixQuery前缀
        // // //basicQuery
        // // //queryParam.put("field", "ip");
        // // //queryParam.put("value", "192.168.0.1");
        // // List<Json> esList = esClientService.query("es_teacher_tag",
        // // "teacher_tag",
        // // queryParam, resultFieldList, page);
        // // map.put("esList", esList);
        //
        // // List<Json> paramList=new ArrayList<Json>();
        // // Json queryParam = new Json();
        // // queryParam.put("type", "must");
        // // queryParam.put("field", "user_name");
        // // queryParam.put("value", "shengld");
        // // paramList.add(queryParam);
        //
        // Json sortParam = new Json();
        // sortParam.put("sort_field", "operationTime");
        // sortParam.put("sort_type", "desc");
        //
        // List<Json> esList = esClientService.query("biz_operation_log",
        // "business", queryParam, resultFieldList, sortParam,
        // page);
        // map.put("esList", esList);
        return "/bigdata/demo/esQuery-1.ftl";
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @ControllerInfo("chart1")
    @RequestMapping("/chart1")
    public Response chart1() {
        try {
            Json dateAggParam = new Json();
            dateAggParam.put("field", "operation_date");
            dateAggParam.put("interval", "day");
            dateAggParam.put("name", "登录次数");
            //
            Json dateRangeParam = new Json();
            dateRangeParam.put("field", "operation_date");
            dateRangeParam.put("start_date", "2018-09-15");
            dateRangeParam.put("end_date", "2018-10-20");

            List<Json> aggParamList = new ArrayList<Json>();
            Json aggParam = new Json();
            aggParam = new Json();
            aggParam.put("name", "星期");
            aggParam.put("serials", "登录次数");
            aggParam.put("field", "operation_date");
            aggParamList.add(aggParam);

            List<Json> aggResultList = esClientService.queryAggregation(
                    "es_login_log", "login_log", dateAggParam, dateRangeParam,
                    null, new ArrayList<Json>());
            List<JData.Entry> entryList = Lists.newArrayList();
            aggResultList.forEach(e -> {
                JData.Entry entry = new JData.Entry();
                entry.setX(e.getString("key"));
                entry.setY(e.getString("value"));
                entry.setName(e.getString("serials"));
                entryList.add(entry);
            });

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
            String[] color = new String[]{"#37a2da", "#ee913a", "#9949d7",
                    "#1ebcd3", "#34c4e9", "#96bfff", "#fedb5b", "#96beb8",
                    "#ae89f3", "#ff9f7f", "#ae89f3"};
            Random random = new Random();
            int index = random.nextInt(10) % (10 - 0 + 1) + 0;
            for (Series e : option.series()) {
                ItemStyle itemStyle = new ItemStyle();
                itemStyle.color(color[index]);
                e.setItemStyle(itemStyle);
            }
            return Response.ok().data(JSON.toJSON(option).toString()).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.error().data("出错了:" + e.getMessage()).build();
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @ControllerInfo("chart2")
    @RequestMapping("/chart2")
    public Response chart2() {
        try {
            Json dateAggParam = new Json();
            dateAggParam.put("field", "operation_date");
            dateAggParam.put("interval", "day");
            dateAggParam.put("name", "登录次数");
            //
            Json dateRangeParam = new Json();
            dateRangeParam.put("field", "operation_date");
            dateRangeParam.put("start_date", "2018-09-15");
            dateRangeParam.put("end_date", "2018-12-25");

            List<Json> aggParamList = new ArrayList<Json>();
            Json aggParam = new Json();
            aggParam = new Json();
            aggParam.put("name", "浏览器");
            aggParam.put("serials", "登录次数");
            aggParam.put("field", "browser_name");
            aggParamList.add(aggParam);

            List<Json> aggResultList = esClientService.queryAggregation(
                    "es_login_log", "login_log", null, null, aggParamList,
                    new ArrayList<Json>());
            List<JData.Entry> entryList = Lists.newArrayList();
            aggResultList.forEach(e -> {
                JData.Entry entry = new JData.Entry();
                entry.setX(e.getString("key"));
                entry.setY(e.getString("value"));
                entry.setName(e.getString("serials"));
                entryList.add(entry);
            });

            // entryList.sort(new Comparator<JData.Entry>() {
            //
            // @Override
            // public int compare(Entry o1, Entry o2) {
            // Map<String, Integer> weekMap = new HashMap<String, Integer>();
            // weekMap.put("星期一", 1);
            // weekMap.put("星期二", 2);
            // weekMap.put("星期三", 3);
            // weekMap.put("星期四", 4);
            // weekMap.put("星期五", 5);
            // weekMap.put("星期六", 6);
            // weekMap.put("星期日", 7);
            // return weekMap.get(o1.getX()).compareTo(weekMap.get(o2.getX()));
            // }
            //
            // });

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
                    .type(LegendEnum.scroll).top(TopEnum.top)
                    .orient(Orient.horizontal);

            tooltip.trigger(Trigger.axis);
            String[] color = new String[]{"#37a2da", "#ee913a", "#9949d7",
                    "#1ebcd3", "#34c4e9", "#96bfff", "#fedb5b", "#96beb8",
                    "#ae89f3", "#ff9f7f"};

            Random random = new Random();
            int index = random.nextInt(10) % (10 - 0 + 1) + 0;

            for (Series e : option.series()) {

                for (Object b : e.getData()) {
                    BarData bd = (BarData) b;
                    ItemStyle itemStyle = new ItemStyle();
                    itemStyle.color(color[index]);
                    bd.itemStyle(itemStyle);
                }
                ItemStyle itemStyle = new ItemStyle();
                itemStyle.color(color[index]);
                e.setItemStyle(itemStyle);
            }
            return Response.ok().data(JSON.toJSON(option).toString()).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.error().data("出错了:" + e.getMessage()).build();
        }
    }

    @SuppressWarnings("rawtypes")
    @ResponseBody
    @ControllerInfo("chart3")
    @RequestMapping("/chart3")
    public Response chart3() {
        try {
            Json dateAggParam = new Json();
            dateAggParam.put("field", "operation_date");
            dateAggParam.put("interval", "day");
            dateAggParam.put("name", "登录次数");
            //
            Json dateRangeParam = new Json();
            dateRangeParam.put("field", "operation_date");
            dateRangeParam.put("start_date", "2018-09-15");
            dateRangeParam.put("end_date", "2018-12-25");

            List<Json> aggParamList = new ArrayList<Json>();
            Json aggParam = new Json();
            aggParam.put("name", "省份");
            aggParam.put("serials", "登录次数");
            aggParam.put("field", "unit_name");
            aggParamList.add(aggParam);

            List<Json> aggResultList = esClientService.queryAggregation(
                    "es_login_log", "login_log", null, dateRangeParam,
                    aggParamList, new ArrayList<Json>());

            List<JData.Entry> entryList = Lists.newArrayList();

            aggResultList.forEach(e -> {
                JData.Entry entry = new JData.Entry();
                entry.setX(e.getString("key"));
                entry.setY(e.getString("value"));
                entryList.add(entry);
            });

            JData data = new JData();
            data.setCoordSys(CoordinateSystem.geo);
            data.setSelfCoordSys(true);
            data.setMapType("china");
            data.setType(SeriesEnum.map);
            data.setEntryList(entryList);
            data.setSelfCoordSys(true);
            JData.JCoordSysPosition position = new JData.JCoordSysPosition();
            data.setCoordSysPosition(position);

            Option option = new Option();
            JConverter.convert(data, option);

            // 设置标题，提示框等
            option.title().show(true);
            Tooltip tooltip = new Tooltip().option(option);
            option.tooltip(tooltip);
            tooltip.trigger(Trigger.item).formatter("{b}<br/>{c}");

            // 设置视觉映射组件
            Continuous continuous = new Continuous().option(option);
            double max = 0;
            double min = 0;
            for (JData.Entry entry : entryList) {
                if (entry.getY().getClass().isPrimitive()
                        || entry.getY() instanceof Number) {
                    Double d = Double.valueOf(entry.getY().toString());
                    max = Math.max(d, max);
                    min = Math.min(d, min);
                }
            }

            continuous.calculable(true);
            continuous.max(max);
            if (min > 0) {
                min = 0;
            }
            continuous.min(min);
            option.visualMap(continuous);
            Geo geo = new Geo().option(option);
            geo.setMap("00");
            for (Series series : option.series()) {
                series.label().show(true);
                ((EMap) series).roam(RoamEx.enable()).scaleLimit().max(2d)
                        .min(1d);
            }
            return Response.ok().data(JSON.toJSON(option).toString()).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.error().data("出错了:" + e.getMessage()).build();
        }
    }

    @ResponseBody
    @ControllerInfo("chart4")
    @RequestMapping("/chart4")
    public Response chart4() {
        try {
            return Response.ok().data(JSON.toJSON("").toString()).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.error().data("出错了:" + e.getMessage()).build();
        }
    }

    @ResponseBody
    @ControllerInfo("chart5")
    @RequestMapping("/chart5")
    public Response chart5() {
        try {
            return Response.ok().data(JSON.toJSON("").toString()).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.error().data("出错了:" + e.getMessage()).build();
        }
    }
}
