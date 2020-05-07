/**
 * FileName: WrapConvert.java
 * Author:   shenke
 * Date:     2018/6/27 下午6:05
 * Descriptor:
 */
package net.zdsoft.bigdata.data.echarts;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import net.zdsoft.bigdata.data.code.ChartType;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.echarts.Option;
import net.zdsoft.echarts.convert.JConverter;
import net.zdsoft.echarts.convert.api.JData;
import net.zdsoft.echarts.coords.AxisLabel;
import net.zdsoft.echarts.coords.AxisLine;
import net.zdsoft.echarts.coords.AxisTick;
import net.zdsoft.echarts.coords.SplitLine;
import net.zdsoft.echarts.coords.cartesian2d.Cartesian2dAxis;
import net.zdsoft.echarts.coords.cartesian2d.Grid;
import net.zdsoft.echarts.coords.data.AxisData;
import net.zdsoft.echarts.coords.data.AxisDataTextStyle;
import net.zdsoft.echarts.coords.enu.AxisPosition;
import net.zdsoft.echarts.coords.enu.AxisType;
import net.zdsoft.echarts.coords.geo.Geo;
import net.zdsoft.echarts.coords.radar.Radar;
import net.zdsoft.echarts.element.Continuous;
import net.zdsoft.echarts.element.Legend;
import net.zdsoft.echarts.element.Title;
import net.zdsoft.echarts.element.Tooltip;
import net.zdsoft.echarts.element.inner.Indicator;
import net.zdsoft.echarts.enu.Align;
import net.zdsoft.echarts.enu.AxisPointerType;
import net.zdsoft.echarts.enu.BottomEx;
import net.zdsoft.echarts.enu.CoordinateSystem;
import net.zdsoft.echarts.enu.FontStyle;
import net.zdsoft.echarts.enu.FontWeightEnum;
import net.zdsoft.echarts.enu.LeftEnum;
import net.zdsoft.echarts.enu.LeftEx;
import net.zdsoft.echarts.enu.LegendEnum;
import net.zdsoft.echarts.enu.LineType;
import net.zdsoft.echarts.enu.Orient;
import net.zdsoft.echarts.enu.Origin;
import net.zdsoft.echarts.enu.PositionEnum;
import net.zdsoft.echarts.enu.PositionEx;
import net.zdsoft.echarts.enu.RightEnum;
import net.zdsoft.echarts.enu.RightEx;
import net.zdsoft.echarts.enu.RippleEffectBrushType;
import net.zdsoft.echarts.enu.RoseType;
import net.zdsoft.echarts.enu.RoseTypeEnum;
import net.zdsoft.echarts.enu.SelectedModeEnum;
import net.zdsoft.echarts.enu.SelectedModelEx;
import net.zdsoft.echarts.enu.SeriesEnum;
import net.zdsoft.echarts.enu.SymbolEnum;
import net.zdsoft.echarts.enu.Target;
import net.zdsoft.echarts.enu.TopEnum;
import net.zdsoft.echarts.enu.TopEx;
import net.zdsoft.echarts.enu.Trigger;
import net.zdsoft.echarts.series.Bar;
import net.zdsoft.echarts.series.EMap;
import net.zdsoft.echarts.series.EffectScatter;
import net.zdsoft.echarts.series.Gauge;
import net.zdsoft.echarts.series.Line;
import net.zdsoft.echarts.series.Lines;
import net.zdsoft.echarts.series.Pie;
import net.zdsoft.echarts.series.Scatter;
import net.zdsoft.echarts.series.Series;
import net.zdsoft.echarts.series.data.BarData;
import net.zdsoft.echarts.series.data.EMapData;
import net.zdsoft.echarts.series.data.ScatterData;
import net.zdsoft.echarts.series.inner.Detail;
import net.zdsoft.echarts.series.inner.LabelLine;
import net.zdsoft.echarts.series.inner.Pointer;
import net.zdsoft.echarts.series.inner.RippleEffect;
import net.zdsoft.echarts.style.AreaColorItemStyle;
import net.zdsoft.echarts.style.Emphasis;
import net.zdsoft.echarts.style.Label;
import net.zdsoft.echarts.style.LegendTextStyle;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author shenke
 * @since 2018/6/27 下午6:05
 */
public class WrapConvert {

    public static final String trigger_formatter_line_bar_k = "{a} <br/> {b}: {c}";
    public static final String trigger_formatter_pie_funnel_gua = "{a} <br/> {b}: {c} {d}%";
    public static final String[] colors =
            new String[]{"#1f83f5", "#d042a4", "#1ebcd3", "#9949d7", "#ee913a", "#3bb7f0", "#cdb112", "#b9396d"};
    //new String[]{"#b9396d", "#cdb112", "#3bb7f0","ee913a", "#9949d7", "#1ebcd3", "#d042a4", "#1f83f5"};
    //new String[]{"#9949d7", "#ee913a", "#1ebcd3", "#cb309a", "#1f83f5", "#93f51f", "#3bb7f0", "#2fe3c7", "#ee3a71", "#d142a4"};

    static Random random = new Random();

    private static OEUtils oeUtils = new OEUtils();

    public static OptionEx convert(List<WrapChart> wrapCharts, int chartType, boolean cockpit, Object optionExpose) throws EntryUtils.DataException {
        if (chartType == 0 || wrapCharts == null || wrapCharts.size() == 0) {
            return null;
        }
        WrapChart wrapChart = wrapCharts.get(0);
        if (wrapChart.getResult().hasError()) {
            throw new EntryUtils.DataException(wrapChart.getResult().getException().getMessage());
        }
        EchartOptionWrap optionWrap = new EchartOptionWrap();
        switch (chartType) {
            case ChartType.BAR_BASIC:
            case ChartType.BAR_STACK:
            case ChartType.BAR_STACK_STRIPE:
            case ChartType.BAR_STRIPE:
            case ChartType.BAR_LINE:
                optionWrap.option(bar(chartType, wrapChart.getName(), wrapChart.getResult(), optionExpose));

                break;
            case ChartType.BAR_2_STRIPE:
                optionWrap.option(bar2(chartType, wrapChart.getName(), wrapChart.getResult(), optionExpose));
                break;

            //optionWrap.option(barLine(chartType, wrapChart.getName(), wrapChart.getResult(), optionExpose));
            //break;
            case ChartType.FUNNEL:
            case ChartType.FUNNEL_ASCENDING:
                optionWrap.option(funnel(chartType, wrapChart.getName(), wrapChart.getResult(), optionExpose));
                break;
            case ChartType.LINE_AREA:
            case ChartType.LINE_BROKEN:
                optionWrap.option(line(chartType, wrapChart.getName(), wrapChart.getResult(), optionExpose));
                break;
            case ChartType.PIE_BASIC:
            case ChartType.PIE_FNF:
            case ChartType.PIE_DOUGHNUT:
            case ChartType.INNER_PIE_OUTTER_DOUGHNUT:
                optionWrap.option(pie(chartType, wrapChart.getName(), wrapChart.getResult(), optionExpose));
                break;
            case ChartType.PIE_DOUGHNUT_COMPOSITE:
                optionWrap.option(compositePie(chartType, wrapChart.getName(), wrapChart.getResult(), optionExpose));
                break;
            case ChartType.SCATTER:
                optionWrap.option(scatter(chartType, wrapChart.getName(), wrapChart.getResult(), optionExpose));
                break;
            case ChartType.RADAR_BASIC:
                optionWrap.option(radar(chartType, wrapChart.getName(), wrapChart.getResult(), optionExpose));
                break;
            case ChartType.MAP:
                optionWrap.option(map(chartType, wrapChart.getName(), wrapChart.getResult(), wrapChart.getMapType(), optionExpose));
                break;
            case ChartType.MAP_LINE:
                optionWrap.option(mapLine(chartType, wrapChart.getName(), wrapChart.getResult(), wrapChart.getMapType(), optionExpose));
                break;
            case ChartType.WORD_CLOUD:
                optionWrap.option(wordCloud(chartType, wrapChart.getName(), wrapChart.getResult(), optionExpose));
                break;
            case ChartType.GAUGE:
                optionWrap.option(gauge(chartType, wrapChart.getName(), wrapChart.getResult(), optionExpose));
                break;
            case ChartType.SANKEY:
                optionWrap.option(SankeyConvert.sankey(chartType, wrapChart.getName(), wrapChart.getResult(), optionExpose));
                break;
            case ChartType.GRAPH:
                optionWrap.option(GraphConvert.graph(chartType, wrapChart.getName(), wrapChart.getResult(), optionExpose));
                break;
            case ChartType.TREE_MAP:
                optionWrap.option(TreeMapConvert.treeMap(chartType, wrapChart.getName(), wrapChart.getResult(), optionExpose));
                break;
            case ChartType.SELF_NUMBER:
                INumberOptionWrap wrap = new INumberOptionWrap();
                wrap.option(number(wrapChart.getResult()));
                return wrap;
            case ChartType.SELF_TABLE:
                TableOptionWrap tr = new TableOptionWrap();
                tr.option(table(wrapChart.getResult()));
                return tr;
            case ChartType.DYNAMIC_NUMBER:
            case ChartType.NUMBER_DOWN:
            case ChartType.NUMBER_UP:
                CommonWrap commonWrap = new CommonWrap();
                DymnamicNumber dymnamicNumber = new DymnamicNumber();
                dymnamicNumber.setTitle(wrapChart.getName());
                if (wrapChart.getResult().getValue() instanceof String) {
                    JSONObject val = null;
                    try {
                        val = JSONObject.parseObject((String) wrapChart.getResult().getValue());
                    } catch (Exception e) {
                        try {
                            JSONArray array = JSONObject.parseArray((String) wrapChart.getResult().getValue());
                            if (array.size() > 0) {
                                val = (JSONObject) array.get(0);
                            } else {
                                val = new JSONObject();
                                val.put("value", 0);
                            }
                        } catch (Exception e1) {
                            throw new EntryUtils.DataException("数据格式错误");
                        }
                    }
                    dymnamicNumber.setValue(val.getIntValue("value"));
                }
                commonWrap.option(dymnamicNumber);
                return commonWrap;
            default:
                return null;

        }
        if (optionWrap.getOption() != null) {
            if (ChartType.WORD_CLOUD != chartType && optionExpose == null) {
                optionWrap.getOption().color(colors);
            }

            //设置颜色
            Option option = optionWrap.getOption();
            if (cockpit && optionExpose == null) {
                for (Cartesian2dAxis dAxis : option.xAxis()) {
                    dAxis.axisLine().lineStyle().color("#ffff");
                }
                for (Cartesian2dAxis dAxis : option.yAxis()) {
                    dAxis.axisLine().lineStyle().color("#ffff");
                    dAxis.splitLine().show(false);
                }
                if (chartType != ChartType.WORD_CLOUD) {
                    LegendTextStyle textStyle = new LegendTextStyle().parent(option.getLegend());
                    textStyle.color("#ffff");
                    if (option.getLegend() != null)
                        option.getLegend().textStyle(textStyle).pageIconColor("#ffff").pageTextStyle().color("#ffff");
                }
                ///option.title().show(false);
                //option.axisPointer().show(true).label().show(true)
                //        .backgroundColor("rgba(50,50,50,0.7)")
                //        .borderColor("#333");
                option.legend().right(RightEnum.center)
                        .type(LegendEnum.scroll).top(TopEnum.top)
                        .orient(Orient.horizontal).z(10);
            }
        }
        return optionWrap;

    }

    public static Option line(int chartType, String name, Result result, Object oe) throws EntryUtils.DataException {

        OptionExposeLine optionExpose = (OptionExposeLine) oe;

        List<JData.Entry> entryList = EntryUtils.parse(result.getValue(), EntryUtils.EntryMapping.entry);
        JData data = new JData();
        data.setType(SeriesEnum.line);
        data.setCoordSys(CoordinateSystem.cartesian2d);
        data.setEntryList(entryList);
        data.setSelfCoordSys(true);
        data.setSelfXAxis(true);
        data.setSelfYAxis(true);
        JData.JCoordSysPosition position = new JData.JCoordSysPosition();
        if (oe != null) {
            position.setTop(TopEx.create(optionExpose.getGridTop()));
            position.setBottom(BottomEx.create(optionExpose.getGridBottom()));
            position.setLeft(LeftEx.create(optionExpose.getGridLeft()));
            position.setRight(RightEx.create(optionExpose.getGridRight()));
        } else {
            position.setTop(TopEx.create("15%"));
            position.setBottom(BottomEx.create("13%"));
        }
        data.setCoordSysPosition(position);
        data.setXAxisType(AxisType.category);
        JData.JAxisPosition xp = new JData.JAxisPosition();
        JData.JAxisPosition yp = new JData.JAxisPosition();
        data.setXJAxisPosition(xp);
        data.setYJAxisPosition(yp);

        Option option = new Option();
        JConverter.convert(data, option);

        if (ChartType.LINE_AREA == chartType && optionExpose == null) {
            //可以确定都是Line
            for (Series series : option.series()) {
                ((Line) series).areaStyle().origin(Origin.start);
            }
        }
        //for (Cartesian2dAxis dAxis : option.xAxis()) {
        //    dAxis.axisLabel().interval(0).rotate(45);
        //}

        if (optionExpose != null) {

            List<OptionExposeSeries> exposeSeries = Optional.ofNullable(optionExpose.getExposeSeries()).orElseGet(ArrayList::new);
            if (updateSeries(exposeSeries, option.getSeries().stream().map(Series::getName).collect(Collectors.toSet()))) {
                exposeSeries = new ArrayList<>();
                for (Series series : option.series()) {
                    OptionExposeSeries oes = new OptionExposeSeries();
                    oes.setName(series.getName());
                    oes.setLineType(LineType.solid.name());
                    oes.setLineArea(ChartType.LINE_AREA == chartType);
                    oes.setType(SeriesEnum.line.name());
                    exposeSeries.add(oes);
                }
                optionExpose.setExposeSeries(exposeSeries);
            }

            if (Boolean.TRUE.equals(optionExpose.getExchangeXY())) {
                //交换 x y轴
                List<Cartesian2dAxis> xAxis = option.xAxis();
                List<Cartesian2dAxis> yAxis = option.yAxis();
                option.xAxis(yAxis).yAxis(xAxis);

                //交换xy轴时需要检查series data是否时数组
                for (Series series : option.series()) {
                    for (Object o : series.getData()) {
                        if (o != null && ((BarData) o).getValue().getClass().isArray()) {
                            ((BarData) o).value(reverse((Object[]) ((BarData) o).getValue()));
                        }
                    }
                }
            }

            //x轴设置
            expose_bar_X(option, optionExpose);
            //y轴设置
            expose_bar_Y(option, optionExpose);
            //图例设置
            expose_bar_legend(option, optionExpose);
            //提示框
            expose_bar_tooltip(option, optionExpose);
            //数据标签
            if (optionExpose.getShowSLabel()) {
                expose_bar_slabel(option, optionExpose);
            }
            //图表外观
            option.color(optionExpose.getColors());

            for (OptionExposeSeries oes : exposeSeries) {
                for (Series series : option.getSeries()) {
                    if (StringUtils.equals(oes.getName(), series.getName())) {
                        series.setType(SeriesEnum.valueOf(oes.getType()));
                        if (series instanceof Line) {
                            ((Line) series).lineStyle().color(oes.getColor());
                            if (Boolean.TRUE.equals(oes.getLineArea())) {
                                ((Line) series).areaStyle().origin(Origin.start);
                            }
                        }
                    }
                }
            }
            exposeTitle(option, optionExpose.getExposeTitle(), name);
        } else {
            //设置标题，提示框等
            option.title().show(true).text(name);
            Tooltip tooltip = new Tooltip().option(option);
            option.tooltip(tooltip);
            option.legend().show(true).right(RightEnum.center)
                    .top(TopEnum.top).type(LegendEnum.scroll)
                    .orient(Orient.horizontal);
            tooltip.trigger(Trigger.item);//.formatter(trigger_formatter_line_bar_k);
            tooltip.axisPointer().type(AxisPointerType.line).show(true);
        }
        return option;
    }


    private static void expose_bar_X(Option option, OptionExposeCartesian2dBase optionExpose) {

        for (Cartesian2dAxis axis : option.getXAxis()) {
            axis.show(optionExpose.getShowX())
                    .name(optionExpose.getxTitle())
                    .nameTextStyle().fontSize(optionExpose.getxTitleFontSize())
                    .color(optionExpose.getxTitleFontColor())
                    .fontWeight(optionExpose.getxTitleFontBold() ? FontWeightEnum.bold : FontWeightEnum.normal)
                    .fontStyle(optionExpose.getxTitleFontItalic() ? FontStyle.italic : FontStyle.normal);
            axis.position(AxisPosition.valueOf(optionExpose.getxPosition()))
                    .inverse(optionExpose.getxInverse());
            axis.axisLine().show(optionExpose.getShowXLine()).lineStyle().color(optionExpose.getxLineColor());
            axis.axisLabel().show(optionExpose.getShowXLabel()).inside(optionExpose.getxLabelInside())
                    .interval(optionExpose.getxLabelInterval()).margin(optionExpose.getxLabelMargin())
                    .rotate(optionExpose.getxLabelRotate()).color(optionExpose.getxLabelColor());

            axis.axisTick().show(optionExpose.getShowXTick()).lineStyle().color(optionExpose.getxTickColor());
            axis.splitLine().show(optionExpose.getShowXSplitLine()).lineStyle().color(optionExpose.getxSplitLineColor());
            axis.setZ(optionExpose.getxZ());
        }
    }

    private static void expose_bar_Y(Option option, OptionExposeCartesian2dBase optionExpose) {
        for (Cartesian2dAxis axis : option.getYAxis()) {
            axis.show(optionExpose.getShowY())
                    .name(optionExpose.getyTitle())
                    .nameTextStyle().fontSize(optionExpose.getyTitleFontSize())
                    .color(optionExpose.getyTitleFontColor())
                    .fontWeight(optionExpose.getyTitleFontBold() ? FontWeightEnum.bold : FontWeightEnum.normal)
                    .fontStyle(optionExpose.getyTitleFontItalic() ? FontStyle.italic : FontStyle.normal);
            axis.position(AxisPosition.valueOf(optionExpose.getyPosition()))
                    .inverse(optionExpose.getyInverse());
            axis.axisLine().show(optionExpose.getShowYLine()).lineStyle().color(optionExpose.getyLineColor());
            axis.axisLabel().show(optionExpose.getShowYLabel()).inside(optionExpose.getyLabelInside())
                    .interval(optionExpose.getyLabelInterval()).margin(optionExpose.getyLabelMargin())
                    .rotate(optionExpose.getyLabelRotate()).color(optionExpose.getyLabelColor());
            axis.axisTick().show(optionExpose.getShowYTick()).lineStyle().color(optionExpose.getyTickColor());
            axis.splitLine().show(optionExpose.getShowYSplitLine()).lineStyle().color(optionExpose.getySplitLineColor());
            axis.setZ(optionExpose.getyZ());

        }
    }

    private static void expose_bar_legend(Option option, OptionExposeCartesian2dBase optionExpose) {
        Legend legend = option.legend();
        legend.show(optionExpose.getShowLegend())
                .left(LeftEnum.valueOf(optionExpose.getLegendLeft()))
                .top(TopEnum.valueOf(optionExpose.getLegendTop()))
                .orient(Orient.valueOf(optionExpose.getLegendOrient()));
        LegendTextStyle textStyle = new LegendTextStyle();
        legend.textStyle(textStyle);
        textStyle.color(optionExpose.getLegendTextFontColor())
                .fontSize(optionExpose.getLegendTextFontSize())
                .fontWeight(optionExpose.getLegendTextFontBold() ? FontWeightEnum.bold : FontWeightEnum.normal)
                .fontStyle(optionExpose.getLegendTextFontItalic() ? FontStyle.italic : FontStyle.normal);

        legend.backgroundColor(OptionExposeUtils.toRGB(optionExpose.getLegendBackgroundColor(), optionExpose.getLegendBackgroundColorTransparent()))
                .borderWidth(optionExpose.getLegendBorderWidth())
                .borderRadius(optionExpose.getLegendBorderRadius())
                .borderColor(optionExpose.getLegendBorderColor());
    }

    private static void expose_bar_tooltip(Option option, OptionExposeCartesian2dBase optionExpose) {
        Tooltip tooltip = new Tooltip();
        option.tooltip(tooltip);
        tooltip.show(optionExpose.getShowTooltip())
                .trigger(Trigger.valueOf(optionExpose.getTooltipTrigger()))
                .confine(optionExpose.getTooltipConfine())
                .backgroundColor(OptionExposeUtils.toRGB(optionExpose.getTooltipBackgroundColor(), optionExpose.getTooltipBackgroundColorTransparent()))
                .borderWidth(optionExpose.getTooltipBorderWidth())
                .borderColor(optionExpose.getTooltipBorderColor())
                .formatter(optionExpose.getTooltipFormatter());
    }

    private static void expose_bar_slabel(Option option, OptionExposeCartesian2dBase optionExpose) {
        for (Series series : option.getSeries()) {
            Label<Series> label = series.label();
            label.show(optionExpose.getShowSLabel())
                    .position(PositionEnum.valueOf(optionExpose.getsLabelPosition()))
                    .backgroundColor(OptionExposeUtils.toRGB(optionExpose.getsLabelBackgroundColor(), optionExpose.getsLabelBackgroundColorTransparent()))
                    .borderColor(OptionExposeUtils.toRGB(optionExpose.getsLabelBorderColor(), optionExpose.getsLabelBackgroundColorTransparent()))
                    .borderWidth(optionExpose.getsLabelBorderWidth())
                    .color(optionExpose.getsLabelTextFontColor())
                    .fontWeight(optionExpose.getsLabelTextFontBold() ? FontWeightEnum.bold : FontWeightEnum.normal)
                    .fontSize(optionExpose.getsLabelTextFontSize())
                    .fontStyle(optionExpose.getsLabelTextFontItalic() ? FontStyle.italic : FontStyle.normal);
        }
    }

    public static Option bar(int chartType, String name, Result result, Object oe) throws EntryUtils.DataException {

        OptionExposeBar optionExpose = (OptionExposeBar) oe;

        List<JData.Entry> entryList = EntryUtils.parse(result.getValue(), EntryUtils.EntryMapping.entry);
        JData data = new JData();
        data.setType(SeriesEnum.bar);
        data.setCoordSys(CoordinateSystem.cartesian2d);
        data.setEntryList(entryList);
        data.setSelfCoordSys(true);
        data.setSelfXAxis(true);
        data.setSelfYAxis(true);
        JData.JCoordSysPosition position = new JData.JCoordSysPosition();
        if (oe != null) {
            position.setTop(TopEx.create(optionExpose.getGridTop()));
            position.setBottom(BottomEx.create(optionExpose.getGridBottom()));
            position.setLeft(LeftEx.create(optionExpose.getGridLeft()));
            position.setRight(RightEx.create(optionExpose.getGridRight()));
        } else {
            position.setTop(TopEx.create("15%"));
            position.setBottom(BottomEx.create("13%"));
        }
        data.setCoordSysPosition(position);
        data.setXAxisType(AxisType.category);
        JData.JAxisPosition xp = new JData.JAxisPosition();
        JData.JAxisPosition yp = new JData.JAxisPosition();
        data.setXJAxisPosition(xp);
        data.setYJAxisPosition(yp);

        Option option = new Option();
        JConverter.convert(data, option);


        //for (Cartesian2dAxis dAxis : option.xAxis()) {
        //    dAxis.axisLabel().interval(0).rotate(45);
        //}

        boolean exchangeXY = optionExpose != null ? Optional.ofNullable(optionExpose.getExchangeXY()).orElse(false)
                : ChartType.BAR_STRIPE == chartType || ChartType.BAR_STACK_STRIPE == chartType;
        if (exchangeXY) {
            //交换 x y轴
            List<Cartesian2dAxis> xAxis = option.xAxis();
            List<Cartesian2dAxis> yAxis = option.yAxis();
            option.xAxis(yAxis).yAxis(xAxis);

            //交换xy轴时需要检查series data是否时数组
            for (Series series : option.series()) {
                for (Object o : series.getData()) {
                    if (o != null && ((BarData) o).getValue().getClass().isArray()) {
                        ((BarData) o).value(reverse((Object[]) ((BarData) o).getValue()));
                    }
                }
            }

        }

        //x、y轴
        if (optionExpose != null) {
            //x轴设置
            expose_bar_X(option, optionExpose);
            //y轴设置
            expose_bar_Y(option, optionExpose);
            //图例设置
            expose_bar_legend(option, optionExpose);
            //提示框
            expose_bar_tooltip(option, optionExpose);
            //数据标签
            if (optionExpose.getShowSLabel()) {
                expose_bar_slabel(option, optionExpose);
            }

            List<OptionExposeSeries> exposeSeries = optionExpose.getExposeSeries();
            if (updateSeries(exposeSeries, option.getSeries().stream().map(Series::getName).collect(Collectors.toSet()))) {
                //设置默认的expose
                exposeSeries = new ArrayList<>(option.getSeries().size());
                for (Series series : option.getSeries()) {
                    OptionExposeSeries oes = new OptionExposeSeries();
                    oes.setType(series.getType().toString());
                    oes.setName(series.getName());
                    exposeSeries.add(oes);
                }
                if (chartType == ChartType.BAR_LINE) {
                    OptionExposeSeries oes = exposeSeries.stream().findFirst().orElseGet(OptionExposeSeries::new);
                    oes.setType(SeriesEnum.line.name());
                    oes.setLineType(LineType.solid.name());
                }
                optionExpose.setExposeSeries(exposeSeries);
            }

            for (OptionExposeSeries oes : exposeSeries) {
                for (Series series : option.getSeries()) {
                    if (StringUtils.equals(series.getName(), oes.getName())) {
                        series.setType(SeriesEnum.valueOf(oes.getType()));
                        series.itemStyle().color(oes.getColor());
                        if (series instanceof Line) {
                            ((Line) series).lineStyle().type(LineType.valueOf(oes.getLineType()));
                            if (Optional.ofNullable(oes.getLineArea()).orElse(Boolean.FALSE)) {
                                ((Line) series).areaStyle().origin(Origin.start);
                            }
                        }
                    }
                }
            }

            for (Series series : option.getSeries()) {
                if (optionExpose.getBarWidth() != null) {
                    ((Bar) series).barWidth(optionExpose.getBarWidth());
                }
                if (StringUtils.isNotBlank(Optional.ofNullable(optionExpose.getBarBorderRadius()).orElse("").toString())) {
                    if (optionExpose.getBarBorderRadius().toString().startsWith("[")) {
                        ((Bar) series).itemStyle().setBarBorderRadius(OptionExposeUtils.toIntArray(optionExpose.getBarBorderRadius().toString()));
                    } else {
                        Integer radius = Integer.valueOf(optionExpose.getBarBorderRadius().toString());
                        ((Bar) series).itemStyle().setBarBorderRadius(radius);
                    }
                }
                if (optionExpose.getBarGap() != null) {
                    ((Bar) series).barGap(optionExpose.getBarGap().toString());
                }
                if (optionExpose.getBarCategoryGap() != null) {
                    ((Bar) series).barCategoryGap(optionExpose.getBarCategoryGap().toString());
                }
            }
            //图表外观
            option.color(optionExpose.getColors());
            exposeTitle(option, optionExpose.getExposeTitle(), name);
        } else {
            if (ChartType.BAR_LINE == chartType) {
                option.series().get(1).type(SeriesEnum.line);
            }
            //设置标题，提示框等
            option.title().show(true).text(name).left(LeftEnum.left);
            Tooltip tooltip = new Tooltip().option(option);
            tooltip.axisPointer().type(AxisPointerType.shadow);
            option.tooltip(tooltip);
            option.legend().show(true).right(RightEnum.center)
                    .type(LegendEnum.scroll).top(TopEnum.top)
                    .orient(Orient.horizontal);
            tooltip.trigger(Trigger.axis);//.formatter("{a} <br/> {b}: {c0}");
            //option.axisPointer().show(true).type(AxisPointerType.shadow);
        }
        return option;
    }

    private static boolean updateSeries(List<OptionExposeSeries> exposeSeries, Set<String> series) {
        if (exposeSeries == null || exposeSeries.size() != series.size()) {
            return true;
        }
        return !exposeSeries.stream().map(OptionExposeSeries::getName).collect(Collectors.toSet()).containsAll(series);
    }

    public static Option bar2(int chartType, String name, Result result, Object oe) throws EntryUtils.DataException {

        OptionExposeBar optionExpose = (OptionExposeBar) oe;

        List<JData.Entry> entryList = EntryUtils.parse(result.getValue(), EntryUtils.EntryMapping.entry);
        Map<String, List<JData.Entry>> entryMap = new LinkedHashMap<>();
        entryList.forEach(entry -> entryMap.computeIfAbsent(entry.getName(), n -> new ArrayList<>()).add(entry));
        String keyLeft = null;
        String keyRight = null;
        if (entryMap.size() != 2) {
            //error
            throw new EntryUtils.DataException("数据格式异常，必须包含两个系列的数据");
        } else {
            int index = 0;
            for (Map.Entry<String, List<JData.Entry>> entry : entryMap.entrySet()) {
                if (index == 0) {
                    keyLeft = entry.getKey();
                } else {
                    keyRight = entry.getKey();
                }
                index++;
            }
        }

        //left
        JData data = new JData();
        data.setType(SeriesEnum.bar);
        data.setCoordSys(CoordinateSystem.cartesian2d);
        data.setEntryList(entryMap.get(keyLeft));
        data.setSelfCoordSys(true);
        data.setSelfXAxis(true);
        data.setSelfYAxis(true);
        JData.JCoordSysPosition position = new JData.JCoordSysPosition();
        if (oe != null) {
            position.setTop(TopEx.create(optionExpose.getGridTop()));
            position.setBottom(BottomEx.create(optionExpose.getGridBottom()));
            position.setLeft(LeftEx.create(optionExpose.getGridLeft()));
        } else {
            position.setTop(TopEx.create("15%"));
            position.setBottom(BottomEx.create("13%"));
            position.setLeft(LeftEx.create("2%"));
        }
        position.setRight(RightEx.create("53%"));
        data.setCoordSysPosition(position);
        data.setSelfYAxis(true);
        data.setSelfXAxis(true);
        data.setXAxisType(AxisType.category);
        JData.JAxisPosition xp = new JData.JAxisPosition();
        JData.JAxisPosition yp = new JData.JAxisPosition();
        data.setXJAxisPosition(xp);
        data.setYJAxisPosition(yp);
        Option option = new Option();
        JConverter.convert(data, option);

        for (Cartesian2dAxis cartesian2dAxis : option.yAxis()) {
            cartesian2dAxis.inverse(true);
        }

        JData right = new JData();
        right.setEntryList(entryMap.get(keyRight));
        right.setType(SeriesEnum.bar);
        right.setCoordSys(CoordinateSystem.cartesian2d);
        right.setSelfCoordSys(true);
        right.setSelfXAxis(true);
        right.setSelfYAxis(true);
        JData.JCoordSysPosition positionRight = new JData.JCoordSysPosition();
        if (oe != null) {
            positionRight.setTop(TopEx.create(optionExpose.getGridTop()));
            positionRight.setBottom(BottomEx.create(optionExpose.getGridBottom()));
            positionRight.setRight(RightEx.create(optionExpose.getGridRight()));
        } else {
            positionRight.setTop(TopEx.create("15%"));
            positionRight.setBottom(BottomEx.create("13%"));
            positionRight.setRight(RightEx.create("2%"));
        }
        positionRight.setLeft(LeftEx.create("53%"));
        right.setCoordSysPosition(positionRight);
        JData.JAxisPosition xp1 = new JData.JAxisPosition();
        JData.JAxisPosition yp1 = new JData.JAxisPosition();
        right.setXJAxisPosition(xp1);
        right.setYJAxisPosition(yp1);
        JConverter.convert(right, option);

        //交换
        List<Cartesian2dAxis> xAxis = option.xAxis();
        List<Cartesian2dAxis> yAxis = option.yAxis();
        option.xAxis(yAxis).yAxis(xAxis);

        //交换xy轴时需要检查series data是否时数组
        for (Series series : option.series()) {
            for (Object o : series.getData()) {
                if (o != null && ((BarData) o).getValue().getClass().isArray()) {
                    ((BarData) o).value(reverse((Object[]) ((BarData) o).getValue()));
                }
            }
        }

        if (optionExpose != null) {
            expose_bar_legend(option, optionExpose);
            expose_bar_slabel(option, optionExpose);
            expose_bar_tooltip(option, optionExpose);
            for (Cartesian2dAxis axis : option.getXAxis()) {
                axis.show(optionExpose.getShowX())
                        .name(optionExpose.getxTitle())
                        .nameTextStyle().fontSize(optionExpose.getxTitleFontSize())
                        .fontWeight(optionExpose.getxTitleFontBold() ? FontWeightEnum.bold : FontWeightEnum.normal)
                        .fontStyle(optionExpose.getxTitleFontItalic() ? FontStyle.italic : FontStyle.normal);
                axis.position(AxisPosition.valueOf(optionExpose.getxPosition()));
                axis.axisLine().show(optionExpose.getShowXLine()).lineStyle().color(optionExpose.getxLineColor());
                axis.axisLabel().show(optionExpose.getShowXLabel()).color(optionExpose.getxLabelColor());
                axis.axisTick().show(optionExpose.getShowXTick()).lineStyle().color(optionExpose.getxTickColor());
                axis.splitLine().show(optionExpose.getShowXSplitLine()).lineStyle().color(optionExpose.getxSplitLineColor());
            }
            for (Cartesian2dAxis axis : option.getYAxis()) {
                axis.inverse(optionExpose.getyInverse());
                axis.axisLine().show(optionExpose.getShowYLine()).lineStyle().color(optionExpose.getyLineColor());
                axis.axisLabel().show(false);
                axis.axisTick().show(optionExpose.getShowYTick()).lineStyle().color(optionExpose.getyTickColor());
                axis.splitLine().show(optionExpose.getShowYSplitLine()).lineStyle().color(optionExpose.getySplitLineColor());
            }
            option.color(optionExpose.getColors());
            exposeTitle(option, optionExpose.getExposeTitle(), name);
        } else {

            for (Cartesian2dAxis dAxis : option.yAxis()) {
                dAxis.axisTick().show(false);
                dAxis.axisLabel().show(false);
            }

            for (Cartesian2dAxis dAxis : option.xAxis()) {
                dAxis.splitLine().show(false);
            }

            Tooltip tooltip = new Tooltip().option(option);
            tooltip.show(true);
            option.tooltip(tooltip);
        }

        //额外增加一个坐标系用于显示y轴
        Grid grid = new Grid().option(option);
        if (oe != null) {
            grid.bottom(BottomEx.create(optionExpose.getGridBottom())).top(TopEx.create(optionExpose.getGridTop()));
        } else {
            grid.bottom(BottomEx.create("13%")).top(TopEx.create("15%"));
        }
        grid.left(LeftEx.create("50%")).show(false);
        //grid.setWidth(0);
        Cartesian2dAxis y = new Cartesian2dAxis();
        y.splitLine().show(false);
        y.axisTick().show(false);
        y.axisLabel().show(true).margin(0);
        for (AxisData<Cartesian2dAxis> axisData : xAxis.get(0).getData()) {
            AxisData<Cartesian2dAxis> d = new AxisData<>();
            AxisDataTextStyle<AxisData<Cartesian2dAxis>> textStyle = new AxisDataTextStyle<>();
            textStyle.align(Align.center);
            d.setValue(axisData.getValue());
            d.textStyle(textStyle);
            y.data(d);
        }
        y.type(AxisType.category);
        y.gridIndex(2);
        y.axisLine().show(false);

        Cartesian2dAxis x = new Cartesian2dAxis();
        x.show(false);
        x.gridIndex(2);
        option.grid().add(grid);
        option.yAxis().add(y);
        option.xAxis().add(x);

        if (optionExpose != null) {
            y.show(optionExpose.getShowY());
            y.axisLabel().show(optionExpose.getShowYLabel())
                    .color(optionExpose.getyLabelColor());
            y.inverse(optionExpose.getyInverse());
        }

        return option;
    }


    public static Option barLine(int chartType, String name, Result result, Object oe) throws EntryUtils.DataException {
        OptionExposeBar optionExpose = (OptionExposeBar) oe;

        List<JData.Entry> entryList = EntryUtils.parse(result.getValue(), EntryUtils.EntryMapping.entry);
        //校验数据
        Set<String> s = entryList.stream().map(JData.Entry::getName).collect(Collectors.toSet());
        if (s.size() != 2) {
            throw new EntryUtils.DataException("数据格式错误,只能有两个系列");
        }

        JData data = new JData();
        data.setType(SeriesEnum.bar);
        data.setCoordSys(CoordinateSystem.cartesian2d);
        data.setEntryList(entryList);
        data.setSelfCoordSys(true);
        data.setSelfXAxis(true);
        data.setSelfYAxis(true);
        JData.JCoordSysPosition position = new JData.JCoordSysPosition();
        position.setTop(TopEx.create("15%"));
        position.setBottom(BottomEx.create("13%"));
        data.setCoordSysPosition(position);
        data.setXAxisType(AxisType.category);
        JData.JAxisPosition xp = new JData.JAxisPosition();
        JData.JAxisPosition yp = new JData.JAxisPosition();
        data.setXJAxisPosition(xp);
        data.setYJAxisPosition(yp);

        Option option = new Option();
        JConverter.convert(data, option);


        //for (Cartesian2dAxis dAxis : option.xAxis()) {
        //    dAxis.axisLabel().interval(0).rotate(45);
        //}
        option.series().get(1).type(SeriesEnum.line);

        if (optionExpose != null) {
            //x轴设置
            expose_bar_X(option, optionExpose);
            //y轴设置
            expose_bar_Y(option, optionExpose);
            //图例设置
            expose_bar_legend(option, optionExpose);
            //提示框
            expose_bar_tooltip(option, optionExpose);
            //数据标签
            if (optionExpose.getShowSLabel()) {
                expose_bar_slabel(option, optionExpose);
            }
            for (Series series : option.getSeries()) {
                if (optionExpose.getBarWidth() != null) {
                    ((Bar) series).barWidth(optionExpose.getBarWidth());
                }
                if (optionExpose.getBarBorderRadius() != null) {
                    ((Bar) series).itemStyle().setBarBorderRadius(optionExpose.getBarBorderRadius());
                }
                if (optionExpose.getBarGap() != null) {
                    ((Bar) series).barGap(optionExpose.getBarGap().toString());
                }
                if (optionExpose.getBarCategoryGap() != null) {
                    ((Bar) series).barCategoryGap(optionExpose.getBarCategoryGap().toString());
                }
            }
            //图表外观
            option.color(optionExpose.getColors());
        } else {

            //设置标题，提示框等
            option.title().show(true).text(name).left(LeftEnum.left);
            Tooltip tooltip = new Tooltip().option(option);
            tooltip.axisPointer().type(AxisPointerType.shadow);
            option.tooltip(tooltip);
            option.legend().show(true).right(RightEnum.center)
                    .type(LegendEnum.scroll).top(TopEnum.top)
                    .orient(Orient.horizontal);
            tooltip.trigger(Trigger.item);//.formatter(trigger_formatter_line_bar_k);
            //option.axisPointer().show(true).type(AxisPointerType.shadow);
        }
        return option;
    }

    private static Object[] reverse(Object[] array) {
        Object[] reverseArray = new Object[array.length];
        for (int i = 0, length = array.length; i < length; i++) {
            reverseArray[i] = array[Math.abs(length - i - 1)];
        }
        return reverseArray;
    }

    public static Option pie(int chartType, String name, Result result, Object oe) throws EntryUtils.DataException {

        OptionExposePie optionExpose = (OptionExposePie) oe;

        List<JData.Entry> entryList = EntryUtils.parse(result.getValue(), EntryUtils.EntryMapping.entry);
        JData data = new JData();
        data.setType(SeriesEnum.pie);
        //data.setCoordSys(CoordinateSystem.cartesian2d);
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

        if (ChartType.PIE_FNF == chartType && optionExpose != null) {
            data.setRoseType(RoseTypeEnum.radius);
        } else {
            data.setRoseType(null);
        }

        Option option = new Option();
        JConverter.convert(data, option);

        if (optionExpose == null) {

            //最里面是饼图
            if (ChartType.INNER_PIE_OUTTER_DOUGHNUT == chartType) {

                int size = option.series().size();
                //多组时，最后一组设置成饼状图
                double max = 9;
                int counter = 1;
                for (Series series : option.series()) {
                    if (counter == size & size != 1) {
                        ((Pie) series).radius(new Object[]{0, max * 10 + "%"})
                                .label().show(true).position(PositionEnum.inside);
                    } else {
                        ((Pie) series).radius(new String[]{(max - 1) * 10 + "%", max * 10 + "%"})
                                .label().position(PositionEnum.inside);
                        max = max - 2;
                        counter++;
                    }
                }
            }
            //环形图
            else if (ChartType.PIE_DOUGHNUT == chartType) {
                int size = option.series().size();
                //多组时，最后一组设置成饼状图
                int counter = 0;
                double max = 9;
                for (Series series : option.series()) {
                    ((Pie) series)
                            .radius(new String[]{(max - 1.5) * 10 + "%", max * 10 + "%"});
                    if (counter > 0) {
                        ((Pie) series).label().position(PositionEnum.inside);
                    }
                    max = max - 2.5;
                    counter++;
                }
            } else if (ChartType.PIE_FNF == chartType) {
                int size = option.series().size();
                double max = 9;
                int counter = 1;
                for (Series series : option.series()) {
                    if (counter == size) {
                        ((Pie) series).radius(new Object[]{"30%", max * 10 + "%"}).startAngle(180).label().position(PositionEnum.inside);
                        ;
                    } else {
                        ((Pie) series).radius(new String[]{(max - 1) * 10 + "%", max * 10 + "%"}).roseType(null);
                        max = max - 2;
                        counter++;
                    }
                }
            }
        }

        if (optionExpose != null) {
            //图例
            Legend legend = option.legend();
            legend.show(optionExpose.getShowLegend())
                    .left(LeftEnum.valueOf(optionExpose.getLegendLeft()))
                    .top(TopEnum.valueOf(optionExpose.getLegendTop()))
                    .orient(Orient.valueOf(optionExpose.getLegendOrient()));
            LegendTextStyle textStyle = new LegendTextStyle();
            legend.textStyle(textStyle);
            textStyle.color(optionExpose.getLegendTextFontColor())
                    .fontSize(optionExpose.getLegendTextFontSize())
                    .fontWeight(optionExpose.getLegendTextFontBold() ? FontWeightEnum.bold : FontWeightEnum.normal)
                    .fontStyle(optionExpose.getLegendTextFontItalic() ? FontStyle.italic : FontStyle.normal);
            legend.backgroundColor(OptionExposeUtils.toRGB(optionExpose.getLegendBackgroundColor(), optionExpose.getLegendBackgroundColorTransparent()))
                    .borderWidth(optionExpose.getLegendBorderWidth())
                    .borderRadius(optionExpose.getLegendBorderRadius())
                    .borderColor(optionExpose.getLegendBorderColor());
            //提示框
            Tooltip tooltip = new Tooltip();
            option.tooltip(tooltip);
            tooltip.show(optionExpose.getShowTooltip())
                    .trigger(Trigger.valueOf(optionExpose.getTooltipTrigger()))
                    .confine(optionExpose.getTooltipConfine())
                    .backgroundColor(OptionExposeUtils.toRGB(optionExpose.getTooltipBackgroundColor(), optionExpose.getTooltipBackgroundColorTransparent()))
                    .borderWidth(optionExpose.getTooltipBorderWidth())
                    .borderColor(optionExpose.getTooltipBorderColor())
                    .formatter(optionExpose.getTooltipFormatter());
            //slabel

            List<OptionExposeCR> exposeCrs = Optional.ofNullable(optionExpose.getExposeCrs()).orElseGet(ArrayList::new);

            boolean update = exposeCrs.size() != option.getSeries().size()
                    || !exposeCrs.stream().map(OptionExposeCR::getName).collect(Collectors.toSet())
                    .containsAll(option.getSeries().stream().map(series -> StringUtils.trimToEmpty(series.getName())).collect(Collectors.toSet()))
                    || !Optional.ofNullable(optionExpose.getExposeLabels()).orElseGet(ArrayList::new).stream()
                    .map(OptionExposeLabel::getName).collect(Collectors.toSet())
                    .containsAll(option.getSeries().stream().map(series -> StringUtils.trimToEmpty(series.getName())).collect(Collectors.toSet()));
            if (update) {
                exposeCrs = new ArrayList<>();
                //内部是饼图，外部是环状图形
                if (ChartType.INNER_PIE_OUTTER_DOUGHNUT == chartType) {
                    double max = 9;
                    int counter = 1;
                    int size = option.getSeries().size();
                    for (Series series : option.getSeries()) {
                        OptionExposeCR exposeCR = new OptionExposeCR();
                        exposeCR.setName(series.getName());
                        exposeCR.setPieCenter("[50%, 50%]");
                        exposeCR.setPieClockWise(true);
                        exposeCR.setPieSelectedMode(SelectedModelEx.disable().toString());
                        exposeCR.setPieSelectedOffset(10);
                        exposeCR.setPieRoseType(Boolean.FALSE.toString());
                        if (counter == size) {
                            exposeCR.setPieRadius("[0," + max * 10 + "%]");
                        } else {
                            exposeCR.setPieRadius("[" + (max - 1) * 10 + "%," + max * 10 + "%]");
                            max = max - 2;
                            counter++;
                        }
                        exposeCrs.add(exposeCR);
                    }
                } else if (ChartType.PIE_DOUGHNUT == chartType) {
                    double max = 9;
                    for (Series series : option.series()) {
                        OptionExposeCR exposeCR = new OptionExposeCR();
                        exposeCR.setName(series.getName());
                        exposeCR.setPieCenter("[50%, 50%]");
                        exposeCR.setPieClockWise(true);
                        exposeCR.setPieSelectedMode(SelectedModelEx.disable().toString());
                        exposeCR.setPieSelectedOffset(10);
                        exposeCR.setPieRoseType(Boolean.FALSE.toString());
                        exposeCR.setPieRadius("[" + (max - 1.5) * 10 + "%," + max * 10 + "%]");
                        max = max - 2.5;
                        exposeCrs.add(exposeCR);
                    }
                } else if (ChartType.PIE_FNF == chartType) {
                    int size = option.series().size();
                    double max = 9;
                    int counter = 1;
                    for (Series series : option.series()) {
                        OptionExposeCR exposeCR = new OptionExposeCR();
                        exposeCR.setName(series.getName());
                        exposeCR.setPieCenter("[50%, 50%]");
                        exposeCR.setPieClockWise(true);
                        exposeCR.setPieSelectedMode(SelectedModelEx.disable().toString());
                        exposeCR.setPieSelectedOffset(10);
                        exposeCR.setPieRoseType(RoseTypeEnum.radius.toString());
                        if (counter == size) {
                            exposeCR.setPieRadius("[30%," + max * 10 + "%]");
                        } else {
                            exposeCR.setPieRadius("[" + (max - 1) * 10 + "%," + max * 10 + "%]");
                            max = max - 2;
                            counter++;
                        }
                        exposeCrs.add(exposeCR);
                    }
                } else {
                    for (Series series : option.series()) {
                        OptionExposeCR exposeCR = new OptionExposeCR();
                        exposeCR.setName(series.getName());
                        exposeCR.setPieCenter("[50%, 50%]");
                        exposeCR.setPieRadius("[0, 75%]");
                        exposeCR.setPieClockWise(true);
                        exposeCR.setPieSelectedMode(SelectedModelEx.disable().toString());
                        exposeCR.setPieSelectedOffset(10);
                        exposeCR.setPieRoseType(Boolean.FALSE.toString());
                        exposeCrs.add(exposeCR);
                    }
                }
                optionExpose.setExposeCrs(exposeCrs);
            }
            //TODO 为什么 我也不知道
                exposeTitle(option, optionExpose.getExposeTitle(), name);
            List<OptionExposeLabel> exposeLabels = Optional.ofNullable(optionExpose.getExposeLabels()).orElseGet(ArrayList::new);
            if (update) {
                exposeLabels = new ArrayList<>();
                for (Series series : option.getSeries()) {
                    OptionExposeLabel exposeLabel = new OptionExposeLabel();
                    exposeLabel.setName(series.getName());
                    exposeLabel.setShowSLabel(true);
                    exposeLabel.setsLabelTextFontColor("#fff");
                    exposeLabel.setsLabelTextFontBold(false);
                    exposeLabel.setsLabelTextFontItalic(false);
                    exposeLabel.setsLabelTextFontSize(12);
                    exposeLabel.setsLabelPosition(PositionEnum.inside.name());
                    exposeLabel.setShowPieLabelLine(false);
                    exposeLabel.setPieLabelLineType(LineType.solid.name());
                    exposeLabel.setPieLabelLineSmooth(0);
                    exposeLabel.setPieLabelLineWidth(1);
                    exposeLabels.add(exposeLabel);
                }
                optionExpose.setExposeLabels(exposeLabels);
            }

            for (Series series : option.getSeries()) {
                for (OptionExposeCR cr : exposeCrs) {
                    if (StringUtils.equals(cr.getName(), series.getName())) {
                        ((Pie) series).center(OptionExposeUtils.toCenter(cr.getPieCenter()))
                                .radius(OptionExposeUtils.toRadius(cr.getPieRadius()))
                                .selectedOffset(cr.getPieSelectedOffset())
                                .clockwise(cr.getPieClockWise());

                        if (Boolean.FALSE.toString().equalsIgnoreCase(cr.getPieSelectedMode())) {
                            ((Pie) series).selectedMode(null);
                        } else {
                            ((Pie) series).selectedMode(SelectedModeEnum.valueOf(cr.getPieSelectedMode()));
                        }
                        if (Boolean.FALSE.toString().equalsIgnoreCase(cr.getPieRoseType())) {
                            ((Pie) series).roseType(null);
                        } else {
                            ((Pie) series).roseType(RoseTypeEnum.valueOf(cr.getPieRoseType()));
                        }
                    }
                }
                for (OptionExposeLabel exposeLabel : exposeLabels) {
                    if (StringUtils.equals(exposeLabel.getName(), series.getName())) {
                        ((Pie) series).label().position(OptionExposeUtils.toPosition(exposeLabel.getsLabelPosition()))
                                .color(exposeLabel.getsLabelTextFontColor())
                                .fontStyle(Boolean.TRUE.equals(exposeLabel.getsLabelTextFontItalic()) ? FontStyle.italic : FontStyle.normal)
                                .fontWeight(Boolean.TRUE.equals(exposeLabel.getsLabelTextFontBold()) ? FontWeightEnum.bold : FontWeightEnum.normal)
                                .fontSize(exposeLabel.getsLabelTextFontSize());
                        ((Pie) series).setLabelLine(new LabelLine<>());
                        ((Pie) series).getLabelLine().show(exposeLabel.getShowPieLabelLine())
                                .lineStyle().type(LineType.valueOf(exposeLabel.getPieLabelLineType()))
                                .width(exposeLabel.getPieLabelLineWidth());
                    }
                }
            }
            option.color(optionExpose.getColors());
        } else {
            //设置标题，提示框等
            option.title().show(true).text(name);
            Tooltip tooltip = new Tooltip().option(option);
            option.tooltip(tooltip);
            option.legend().show(true).right(RightEnum.center)
                    .type(LegendEnum.scroll).top(TopEnum.top)
                    .orient(Orient.horizontal);
            tooltip.trigger(Trigger.item).formatter(trigger_formatter_pie_funnel_gua);
        }


        return option;
    }

    public static Option scatter(int chartType, String name, Result result, Object oe) throws EntryUtils.DataException {

        OptionExposeScatter optionExpose = (OptionExposeScatter) oe;

        List<JData.Entry> entryList = EntryUtils.parse(result.getValue(), EntryUtils.EntryMapping.entry);
        JData data = new JData();
        data.setType(SeriesEnum.scatter);
        data.setCoordSys(CoordinateSystem.cartesian2d);
        data.setEntryList(entryList);
        data.setSelfCoordSys(true);
        data.setSelfXAxis(true);
        data.setSelfYAxis(true);
        JData.JCoordSysPosition position = new JData.JCoordSysPosition();
        if (oe != null) {
            position.setTop(TopEx.create(optionExpose.getGridTop()));
            position.setBottom(BottomEx.create(optionExpose.getGridBottom()));
            position.setLeft(LeftEx.create(optionExpose.getGridLeft()));
            position.setRight(RightEx.create(optionExpose.getGridRight()));
        }
        data.setCoordSysPosition(position);

        Option option = new Option();
        JConverter.convert(data, option);

        //for (Cartesian2dAxis dAxis : option.xAxis()) {
        //    dAxis.axisLabel().interval(0).rotate(45);
        //}

        if (optionExpose != null) {

            if (Boolean.TRUE.equals(optionExpose.getExchangeXY())) {
                //交换 x y轴
                List<Cartesian2dAxis> xAxis = option.xAxis();
                List<Cartesian2dAxis> yAxis = option.yAxis();
                option.xAxis(yAxis).yAxis(xAxis);

                //交换xy轴时需要检查series data是否时数组
                for (Series series : option.series()) {
                    for (Object o : series.getData()) {
                        if (o != null && ((BarData) o).getValue().getClass().isArray()) {
                            ((BarData) o).value(reverse((Object[]) ((BarData) o).getValue()));
                        }
                    }
                }
            }

            //x轴设置
            expose_bar_X(option, optionExpose);
            //y轴设置
            expose_bar_Y(option, optionExpose);
            //图例设置
            expose_bar_legend(option, optionExpose);
            //提示框
            expose_bar_tooltip(option, optionExpose);
            //数据标签
            if (optionExpose.getShowSLabel()) {
                expose_bar_slabel(option, optionExpose);
            }
            //图表外观
            option.color(optionExpose.getColors());
            exposeTitle(option, optionExpose.getExposeTitle(), name);
        } else {

            //设置标题，提示框等
            option.title().show(true).text(name);
            Tooltip tooltip = new Tooltip().option(option);
            option.tooltip(tooltip);
            option.legend().show(true).right(RightEnum.center)
                    .type(LegendEnum.scroll).top(TopEnum.top)
                    .orient(Orient.horizontal);
            tooltip.trigger(Trigger.item).formatter(trigger_formatter_line_bar_k);
            tooltip.axisPointer().type(AxisPointerType.line).show(true);
        }
        return option;
    }

    public static Option radar(int chartType, String name, Result result, Object oe) throws EntryUtils.DataException {
        OptionExposeRadar optionExpose = (OptionExposeRadar) oe;
        List<JData.Entry> entryList = EntryUtils.parse(result.getValue(), EntryUtils.EntryMapping.entry);
        JData data = new JData();
        data.setType(SeriesEnum.radar);
        data.setSelfCoordSys(true);
        data.setCoordSys(CoordinateSystem.radar);
        data.setEntryList(entryList);
        data.setSelfCoordSys(true);
        JData.JCoordSysPosition position = new JData.JCoordSysPosition();
        data.setCoordSysPosition(position);

        Option option = new Option();
        JConverter.convert(data, option);

        if (optionExpose != null) {
            Legend legend = option.legend();
            legend.show(optionExpose.getShowLegend())
                    .left(LeftEnum.valueOf(optionExpose.getLegendLeft()))
                    .top(TopEnum.valueOf(optionExpose.getLegendTop()))
                    .orient(Orient.valueOf(optionExpose.getLegendOrient()));
            LegendTextStyle textStyle = new LegendTextStyle();
            legend.textStyle(textStyle);
            textStyle.color(optionExpose.getLegendTextFontColor())
                    .fontSize(optionExpose.getLegendTextFontSize())
                    .fontWeight(optionExpose.getLegendTextFontBold() ? FontWeightEnum.bold : FontWeightEnum.normal)
                    .fontStyle(optionExpose.getLegendTextFontItalic() ? FontStyle.italic : FontStyle.normal);
            legend.backgroundColor(OptionExposeUtils.toRGB(optionExpose.getLegendBackgroundColor(), optionExpose.getLegendBackgroundColorTransparent()))
                    .borderWidth(optionExpose.getLegendBorderWidth())
                    .borderRadius(optionExpose.getLegendBorderRadius())
                    .borderColor(optionExpose.getLegendBorderColor());
            Tooltip tooltip = new Tooltip();
            option.tooltip(tooltip);
            tooltip.show(optionExpose.getShowTooltip())
                    .trigger(Trigger.valueOf(optionExpose.getTooltipTrigger()))
                    .confine(optionExpose.getTooltipConfine())
                    .backgroundColor(OptionExposeUtils.toRGB(optionExpose.getTooltipBackgroundColor(), optionExpose.getTooltipBackgroundColorTransparent()))
                    .borderWidth(optionExpose.getTooltipBorderWidth())
                    .borderColor(optionExpose.getTooltipBorderColor())
                    .formatter(optionExpose.getTooltipFormatter());
            option.color(optionExpose.getColors());

            List<OptionExposeIndicator> exposeIndicators = Optional.ofNullable(optionExpose.getExposeIndicators()).orElseGet(ArrayList::new);
            if (exposeIndicators.isEmpty()
                    || !exposeIndicators.stream().map(OptionExposeIndicator::getIndicatorName).collect(Collectors.toSet())
                    .containsAll(
                            Optional.ofNullable(option.getRadar().stream().findFirst().orElseGet(Radar::new).getIndicator())
                                    .orElseGet(LinkedHashSet::new).stream().map(Indicator::getName).collect(Collectors.toSet())
                    )) {
                exposeIndicators.clear();
                Optional.ofNullable(
                        option.getRadar().stream().findFirst().orElseGet(Radar::new).getIndicator()
                ).orElseGet(LinkedHashSet::new).forEach(indicator -> {
                    OptionExposeIndicator exposeIndicator = new OptionExposeIndicator();
                    exposeIndicator.setIndicatorName(indicator.getName());
                    exposeIndicator.setIndicatorMax(indicator.getMax());
                    exposeIndicators.add(exposeIndicator);
                });
                optionExpose.setExposeIndicators(exposeIndicators);
            }

            for (Radar radar : option.getRadar()) {
                for (Indicator indicator : radar.getIndicator()) {
                    for (OptionExposeIndicator exposeIndicator : exposeIndicators) {
                        if (indicator.getName().equals(exposeIndicator.getIndicatorName())) {
                            indicator.max(exposeIndicator.getIndicatorMax()).min(exposeIndicator.getIndicatorMin())
                                    .color(exposeIndicator.getIndicatorColor());

                        }
                    }
                }
            }
            option.color(optionExpose.getColors());
            exposeTitle(option, optionExpose.getExposeTitle(), name);
        } else {
            //设置标题，提示框等
            option.title().show(true).text(name);
            Tooltip tooltip = new Tooltip().option(option);
            option.tooltip(tooltip);
            tooltip.trigger(Trigger.item);
        }
        return option;
    }

    public static Option map(int chartType, String name, Result result, String maptype, Object oe) throws EntryUtils.DataException {
        OptionExposeMap optionExpose = (OptionExposeMap) oe;

        List<JData.Entry> entryList = EntryUtils.parse(result.getValue(), EntryUtils.EntryMapping.entry);
        JData data = new JData();
        data.setCoordSys(CoordinateSystem.geo);
        data.setSelfCoordSys(true);
        data.setMapType(maptype);
        data.setType(SeriesEnum.map);
        data.setEntryList(entryList);
        data.setSelfCoordSys(true);
        JData.JCoordSysPosition position = new JData.JCoordSysPosition();
        data.setCoordSysPosition(position);

        Option option = new Option();
        JConverter.convert(data, option);


        data.setCoordSys(CoordinateSystem.geo);
        data.setCoordSysIndex(0);
        data.setType(SeriesEnum.scatter);
        JConverter.convert(data, option);

        //设置标题，提示框等
        if (optionExpose != null) {
            exposeTitle(option, optionExpose.getExposeTitle(), name);
        } else {
            option.title().show(true).text(name);
        }
        Tooltip tooltip = new Tooltip().option(option);
        option.tooltip(tooltip);
        //tooltip.trigger(Trigger.item).formatter("{b}<br/>{c}");

        //设置视觉映射组件
        Continuous continuous = new Continuous().option(option);
        double max = 0;
        double min = 0;
        for (JData.Entry entry : entryList) {
            if (entry.getY().getClass().isPrimitive() || entry.getY() instanceof Number) {
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
        geo.setMap(maptype);
        //option.geo().add(geo);
        //geo.show(true).roam(RoamEx.enable());
        for (Series series : option.series()) {
            if (series instanceof Scatter) {
                if (optionExpose != null) {
                    ((Scatter) series).symbolSize(optionExpose.getShowScatter() ? optionExpose.getScatterSize() : 0)
                            .symbol(SymbolEnum.pin)
                            .z(10)
                            .itemStyle().color(optionExpose.getScatterColor());
                } else {
                    ((Scatter) series).symbolSize(0).symbol(SymbolEnum.pin);
                }
                for (Object d : series.getData()) {
                    ((ScatterData) d).setVisualMap(false);
                }
                series.label().show(true).formatter("{@2}");
            } else if (series instanceof EMap) {
                if (optionExpose != null) {
                    series.label().show(optionExpose.getShowGeoLabel());
                } else {
                    series.label().show(true);
                }
            }
        }
        option.setColor(Arrays.asList("#b9396d", "#d042a4", "#1ebcd3", "#9949d7", "#ee913a", "#3bb7f0", "#cdb112", "#b9396d"));
        return option;
    }

    public static Option mapLine(int chartType, String name, Result result, String maptype, Object oe) throws EntryUtils.DataException {

        OptionExposeMap optionExpose = (OptionExposeMap) oe;

        List<JData.Entry> entryList = EntryUtils.parse(result.getValue(), EntryUtils.EntryMapping.entry);
        JData data = new JData();
        data.setCoordSys(CoordinateSystem.geo);
        data.setSelfCoordSys(true);
        data.setMapType(maptype);
        data.setType(SeriesEnum.lines);
        data.setEntryList(entryList);
        JData.JCoordSysPosition position = new JData.JCoordSysPosition();
        data.setCoordSysPosition(position);

        Option option = new Option();
        //lines转换
        JConverter.convert(data, option);


        data.setSelfCoordSys(false);
        data.setCoordSysIndex(0);
        data.setType(SeriesEnum.effectScatter);
        JConverter.convert(data, option);

        data.setCoordSys(CoordinateSystem.geo);
        data.setCoordSysIndex(0);
        data.setType(SeriesEnum.scatter);
        JConverter.convert(data, option);

        //设置曲线的弯曲程度，样式等
        for (Series series : option.series()) {
            if (SeriesEnum.lines.equals(series.getType())) {
                ((Lines) series).lineStyle().curveness(0.2).opacity(0.5);
                ((Lines) series).effect().show(true).trailLength(0.02).symbolSize(5).symbol(SymbolEnum.arrow).loop(true).period(4);
                ((Lines) series).zlevel(2).symbol(SymbolEnum.circle);
            } else if (SeriesEnum.effectScatter.equals(series.getType())) {
                RippleEffect rippleEffect = new RippleEffect().parent((EffectScatter) series);
                rippleEffect.brushType(RippleEffectBrushType.stroke).scale(3d).period(6);
                ((EffectScatter) series).zlevel(2).rippleEffect(rippleEffect);
            } else if (SeriesEnum.scatter.equals(series.getType())) {
                series.label().show(true).formatter("{@2}");
                if (optionExpose != null) {
                    ((Scatter) series).symbolSize(Optional.ofNullable(optionExpose.getShowScatter()).orElse(Boolean.FALSE) ? optionExpose.getScatterSize() : 0)
                            .zlevel(10)
                            .symbol(SymbolEnum.pin)
                            .itemStyle().color(optionExpose.getScatterColor());
                } else {
                    ((Scatter) series).z(10).symbolSize(0).symbol(SymbolEnum.pin);
                }
            }
        }

        if (optionExpose != null) {
            exposeTitle(option, optionExpose.getExposeTitle(), name);
        }

        for (Geo geo : option.geo()) {
            AreaColorItemStyle<Geo> itemStyle = new AreaColorItemStyle<>();
            itemStyle.borderWidth(1).areaColor("#081f3a").borderColor("rgba(0, 222, 255, 1)").shadowBlur(12).shadowColor("rgba(0, 222, 255, 1)");
            Emphasis<Geo> emphasis = new Emphasis<>();
            emphasis.itemStyle().color("rgba(37, 43, 61, .5)");
            Label label = geo.getLabel();
            if (label != null) {
                label.show(optionExpose == null? Boolean.TRUE : optionExpose.getShowGeoLabel());
                label.color("#fff");
            } else {
                label = new Label();
                label.show(optionExpose == null? Boolean.TRUE : optionExpose.getShowGeoLabel());
                label.color("#fff");
                geo.label(label);
            }
            geo.emphasis(emphasis).itemStyle(itemStyle).show(true);
        }
        EMap eMap = new EMap();
        eMap.type(SeriesEnum.map).map(maptype)
                .label().show(optionExpose != null ? optionExpose.getShowGeoLabel() : true);
        AreaColorItemStyle<EMap> itemStyle = new AreaColorItemStyle<>();
        itemStyle.borderColor("#015678").areaColor("#081f3a").borderWidth(1);
        eMap.label().color("#fff").show(true);
        eMap.setItemStyle(itemStyle);
        eMap.geoIndex(2).data(new EMapData()).name("empty");
        //option.series().add(eMap);

        option.setColor(Arrays.asList("#1f83f5", "#d042a4", "#1ebcd3", "#9949d7", "#ee913a", "#3bb7f0", "#cdb112", "#b9396d"));
        return option;
    }

    public static class LinesCoord {
        private Object coord;
        private Object value;

        public Object getCoord() {
            return coord;
        }

        public void setCoord(Object coord) {
            this.coord = coord;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }

    public static Option funnel(int chartType, String name, Result result, Object oe) throws EntryUtils.DataException {

        OptionExposeFunnel optionExpose = (OptionExposeFunnel) oe;

        List<JData.Entry> entryList = EntryUtils.parse(result.getValue(), EntryUtils.EntryMapping.entry);
        JData data = new JData();
        //data.setCoordSys(CoordinateSystem.polar);
        data.setSelfCoordSys(true);
        data.setType(SeriesEnum.funnel);
        data.setEntryList(entryList);
        data.setSelfCoordSys(true);
        JData.JCoordSysPosition position = new JData.JCoordSysPosition();
        data.setCoordSysPosition(position);

        Option option = new Option();
        JConverter.convert(data, option);

        if (optionExpose != null) {
            Legend legend = option.legend();
            legend.show(optionExpose.getShowLegend())
                    .left(LeftEnum.valueOf(optionExpose.getLegendLeft()))
                    .top(TopEnum.valueOf(optionExpose.getLegendTop()))
                    .orient(Orient.valueOf(optionExpose.getLegendOrient()));
            LegendTextStyle textStyle = new LegendTextStyle();
            legend.textStyle(textStyle);
            textStyle.color(optionExpose.getLegendTextFontColor())
                    .fontSize(optionExpose.getLegendTextFontSize())
                    .fontWeight(optionExpose.getLegendTextFontBold() ? FontWeightEnum.bold : FontWeightEnum.normal)
                    .fontStyle(optionExpose.getLegendTextFontItalic() ? FontStyle.italic : FontStyle.normal);
            legend.backgroundColor(OptionExposeUtils.toRGB(optionExpose.getLegendBackgroundColor(), optionExpose.getLegendBackgroundColorTransparent()))
                    .borderWidth(optionExpose.getLegendBorderWidth())
                    .borderRadius(optionExpose.getLegendBorderRadius())
                    .borderColor(optionExpose.getLegendBorderColor());
            Tooltip tooltip = new Tooltip();
            option.tooltip(tooltip);
            tooltip.show(optionExpose.getShowTooltip())
                    .trigger(Trigger.valueOf(optionExpose.getTooltipTrigger()))
                    .confine(optionExpose.getTooltipConfine())
                    .backgroundColor(OptionExposeUtils.toRGB(optionExpose.getTooltipBackgroundColor(), optionExpose.getTooltipBackgroundColorTransparent()))
                    .borderWidth(optionExpose.getTooltipBorderWidth())
                    .borderColor(optionExpose.getTooltipBorderColor())
                    .formatter(optionExpose.getTooltipFormatter());
            for (Series series : option.getSeries()) {
                Label<Series> label = series.label();
                label.show(optionExpose.getShowSLabel())
                        .position(PositionEnum.valueOf(optionExpose.getsLabelPosition()))
                        .backgroundColor(OptionExposeUtils.toRGB(optionExpose.getsLabelBackgroundColor(), optionExpose.getsLabelBackgroundColorTransparent()))
                        .borderColor(OptionExposeUtils.toRGB(optionExpose.getsLabelBorderColor(), optionExpose.getsLabelBackgroundColorTransparent()))
                        .borderWidth(optionExpose.getsLabelBorderWidth())
                        //.color(optionExpose.getsLabelTextFontColor())
                        .fontWeight(Boolean.TRUE.equals(optionExpose.getsLabelTextFontBold()) ? FontWeightEnum.bold : FontWeightEnum.normal)
                        .fontSize(optionExpose.getsLabelTextFontSize())
                        .fontStyle(Boolean.TRUE.equals(optionExpose.getsLabelTextFontItalic()) ? FontStyle.italic : FontStyle.normal);
            }

            option.color(optionExpose.getColors());
            exposeTitle(option, optionExpose.getExposeTitle(), name);
        } else {
            //设置标题，提示框等
            option.title().show(true).text(name);
            Tooltip tooltip = new Tooltip().option(option);
            option.tooltip(tooltip);
            tooltip.trigger(Trigger.item).formatter(trigger_formatter_pie_funnel_gua);
        }
        return option;
    }

    public static Option wordCloud(int chartType, String name, Result result, Object oe) throws EntryUtils.DataException {
        OptionExposeWordCloud optionExpose = (OptionExposeWordCloud) oe;

        List<JData.Entry> entryList = EntryUtils.parse(result.getValue(), EntryUtils.EntryMapping.entry);
        JData data = new JData();
        data.setSelfCoordSys(true);
        data.setType(SeriesEnum.wordCloud);
        data.setEntryList(entryList);
        data.setSelfCoordSys(true);
        JData.JCoordSysPosition position = new JData.JCoordSysPosition();
        data.setCoordSysPosition(position);

        Option option = new Option();
        JConverter.convert(data, option);

        //设置标题，提示框等
        if (optionExpose != null) {
            exposeTitle(option, optionExpose.getExposeTitle(), name);
        } else {
            option.title().show(true).text(name);
        }
        Tooltip tooltip = new Tooltip().option(option);
        option.tooltip(tooltip);
        tooltip.trigger(Trigger.item);
        option.textStyle().color("function(){ var rd = parseInt(Math.random()*9); " +
                "var array = ['#9949d7', '#ee913a', '#1ebcd3', '#cb309a','#1f83f5', '#93f51f', '#3bb7f0', '#2fe3c7', '#ee3a71', '#d142a4']; " +
                " return array[rd];" +
                "}");
        return option;
    }

    public static INumber number(Result result) throws EntryUtils.DataException {
        String text = null;
        if (result.getValue() instanceof String) {
            text = (String) result.getValue();
        } else {
            text = JSONObject.toJSONString(result.getValue());
        }
        JSONArray array = null;
        try {
            array = JSONObject.parseArray(text);
        } catch (Exception e) {
            throw new EntryUtils.DataException("数据格式错误");
        }
        if (array.size() != 1) {
            throw new EntryUtils.DataException("数据格式错误");
        }
        INumber number = new INumber();
        INumber.Ration ration = null;
        ration = JSONObject.parseObject(array.get(0).toString(), INumber.Ration.class);
        number.setRation(ration);
        return number;
    }

    public static Table table(Result result) throws EntryUtils.DataException {
        String text = null;
        if (result.getValue() instanceof String) {
            text = (String) result.getValue();
        } else {
            text = JSONObject.toJSONString(result.getValue());
        }
        List<JSONObject> array = null;
        try {
            array = JSONObject.parseObject(text, new TypeReference<List<JSONObject>>() {
            }, Feature.OrderedField);
        } catch (Exception e) {
            throw new EntryUtils.DataException("数据格式错误");
        }
        Table table = new Table();
        List<Object> tHead = new ArrayList<>();
        List<Table.Tr> trList = new ArrayList<>(array.size() - 1);
        LinkedHashSet<String> headers = new LinkedHashSet<>();
        for (int i = 0, length = array.size(); i < length; i++) {
            //第一个是标题行
            JSONObject val = (JSONObject) array.get(i);
            if (i == 0) {
                val.entrySet().stream().forEach(entry -> {
                    headers.add(entry.getKey());
                    tHead.add(entry.getValue());
                });
            } else {
                trList.add(parseTr(val, headers));
            }
        }
        table.setThead(tHead);
        table.setTrs(trList);
        return table;
    }

    private static Table.Tr parseTr(JSONObject val, Set<String> headerKey) {
        Table.Tr tr = new Table.Tr();
        List<Object> tds = new ArrayList<>(val.size());
        headerKey.forEach(key -> {
            tds.add(val.get(key));
        });
        tr.setTds(tds);
        return tr;
    }

    public static Option compositePie(int chartType, String name, Result result, Object oe) throws EntryUtils.DataException {
        OptionExposePie optionExpose = (OptionExposePie) oe;

        List<JData.Entry> entryList = EntryUtils.parse(result.getValue(), EntryUtils.EntryMapping.entry);
        JData data = new JData();
        data.setType(SeriesEnum.pie);
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

        data.setRoseType(null);

        Option option = new Option();
        JConverter.convert(data, option);

        if (optionExpose != null) {
            //图例
            Legend legend = option.legend();
            legend.show(optionExpose.getShowLegend())
                    .left(LeftEnum.valueOf(optionExpose.getLegendLeft()))
                    .top(TopEnum.valueOf(optionExpose.getLegendTop()))
                    .orient(Orient.valueOf(optionExpose.getLegendOrient()));
            LegendTextStyle textStyle = new LegendTextStyle();
            legend.textStyle(textStyle);
            textStyle.color(optionExpose.getLegendTextFontColor())
                    .fontSize(optionExpose.getLegendTextFontSize())
                    .fontWeight(optionExpose.getLegendTextFontBold() ? FontWeightEnum.bold : FontWeightEnum.normal)
                    .fontStyle(optionExpose.getLegendTextFontItalic() ? FontStyle.italic : FontStyle.normal);
            legend.backgroundColor(OptionExposeUtils.toRGB(optionExpose.getLegendBackgroundColor(), optionExpose.getLegendBackgroundColorTransparent()))
                    .borderWidth(optionExpose.getLegendBorderWidth())
                    .borderRadius(optionExpose.getLegendBorderRadius())
                    .borderColor(optionExpose.getLegendBorderColor());
            //提示框
            Tooltip tooltip = new Tooltip();
            option.tooltip(tooltip);
            tooltip.show(optionExpose.getShowTooltip())
                    .trigger(Trigger.valueOf(optionExpose.getTooltipTrigger()))
                    .confine(optionExpose.getTooltipConfine())
                    .backgroundColor(OptionExposeUtils.toRGB(optionExpose.getTooltipBackgroundColor(), optionExpose.getTooltipBackgroundColorTransparent()))
                    .borderWidth(optionExpose.getTooltipBorderWidth())
                    .borderColor(optionExpose.getTooltipBorderColor())
                    .formatter(optionExpose.getTooltipFormatter());

            List<OptionExposeCR> exposeCrs = Optional.ofNullable(optionExpose.getExposeCrs()).orElseGet(ArrayList::new);
            List<OptionExposeLabel> exposeLabels = Optional.ofNullable(optionExpose.getExposeLabels()).orElseGet(ArrayList::new);
            option.setColor(optionExpose.getColors());
            if (exposeCrs.isEmpty()) {
                int counter = 1;
                int size = option.series().size();
                double mod = 100d / (double) (size * 2);
                for (Series series : option.getSeries()) {
                    OptionExposeCR exposeCr = new OptionExposeCR();
                    exposeCr.setName(series.getName());
                    exposeCr.setPieRoseType(RoseType.DISABLE.toString());
                    exposeCr.setPieSelectedOffset(0);
                    exposeCr.setPieSelectedMode(SelectedModelEx.disable().toString());

                    exposeCr.setPieCenter("[" + mod * (double) (counter * 2 - 1) + "%," + "50%]");
                    exposeCr.setPieRadius("[" + (100 / size - 5) + "%," + (100 / size) + "%]");
                    //.label().show(true).position(PositionEx.create("center")).fontSize(22);
                    counter++;
                    exposeCrs.add(exposeCr);
                }
                optionExpose.setExposeCrs(exposeCrs);
            }
            if (exposeLabels.isEmpty()) {
                for (Series series : option.getSeries()) {
                    OptionExposeLabel exposeLabel = new OptionExposeLabel();
                    exposeLabel.setName(series.getName());
                    exposeLabel.setsLabelTextFontColor("#000");
                    exposeLabel.setsLabelTextFontBold(false);
                    exposeLabel.setsLabelTextFontItalic(false);
                    exposeLabel.setsLabelTextFontSize(20);
                    exposeLabel.setsLabelPosition(PositionEx.create("center").toString());
                    exposeLabel.setShowPieLabelLine(false);
                    exposeLabel.setPieLabelLineType(LineType.solid.name());
                    exposeLabel.setPieLabelLineSmooth(0);
                    exposeLabel.setPieLabelLineWidth(1);
                    exposeLabel.setShowSLabel(false);
                    exposeLabels.add(exposeLabel);
                }
                optionExpose.setExposeLabels(exposeLabels);
            }

            for (Series series : option.getSeries()) {
                for (OptionExposeCR cr : exposeCrs) {
                    if (StringUtils.equals(cr.getName(), series.getName())) {
                        ((Pie) series).center(OptionExposeUtils.toCenter(cr.getPieCenter()))
                                .radius(OptionExposeUtils.toRadius(cr.getPieRadius())).hoverOffset(0);
                        if (Boolean.FALSE.toString().equalsIgnoreCase(cr.getPieSelectedMode())) {
                            ((Pie) series).setSelectedMode(null);
                        } else {
                            ((Pie) series).setSelectedMode(SelectedModeEnum.valueOf(cr.getPieSelectedMode()));
                        }
                        if (Boolean.FALSE.toString().equalsIgnoreCase(cr.getPieRoseType())) {
                            ((Pie) series).setRoseType(null);
                        } else {
                            ((Pie) series).setRoseType(RoseTypeEnum.valueOf(cr.getPieRoseType()));
                        }
                        ((Pie) series).setSelectedOffset(cr.getPieSelectedOffset());
                    }
                }
                for (OptionExposeLabel exposeLabel : exposeLabels) {
                    if (StringUtils.equals(exposeLabel.getName(), series.getName())) {
                        ((Pie) series).label().position(OptionExposeUtils.toPosition(exposeLabel.getsLabelPosition()))
                                .color(exposeLabel.getsLabelTextFontColor())
                                .fontStyle(Boolean.TRUE.equals(exposeLabel.getsLabelTextFontItalic()) ? FontStyle.italic : FontStyle.normal)
                                .fontWeight(Boolean.TRUE.equals(exposeLabel.getsLabelTextFontBold()) ? FontWeightEnum.bold : FontWeightEnum.normal)
                                .fontSize(exposeLabel.getsLabelTextFontSize());
                        ((Pie) series).setLabelLine(new LabelLine<>());
                        ((Pie) series).getLabelLine().show(exposeLabel.getShowPieLabelLine())
                                .lineStyle().type(LineType.valueOf(exposeLabel.getPieLabelLineType()))
                                .width(exposeLabel.getPieLabelLineWidth());
                    }
                }
            }
            exposeTitle(option, optionExpose.getExposeTitle(), name);
        } else {
            int counter = 1;
            int size = option.series().size();
            double mod = 100d / (double) (size * 2);
            for (Series series : option.series()) {
                //System.out.println(mod * (double) (counter * 2 - 1));
                ((Pie) series).center(new String[]{mod * (double) (counter * 2 - 1) + "%", "50%"})
                        .radius(new String[]{(100 / size - 5) + "%", (100 / size) + "%"}).hoverOffset(0)
                        .label().show(true).position(PositionEx.create("center")).fontSize(22);
                counter++;
            }
            option.legend().show(false);
        }
        return option;
    }

    public static Option gauge(int chartType, String name, Result result, Object oe) throws EntryUtils.DataException {

        OptionExposeGauge optionExpose = (OptionExposeGauge) oe;

        List<JData.Entry> entryList = EntryUtils.parse(result.getValue(), EntryUtils.EntryMapping.entry);
        if (entryList.size() > 1) {
            //throw new EntryUtils.DataException("只能包含一组数据");
        }
        JData data = new JData();
        data.setType(SeriesEnum.gauge);
        data.setEntryList(entryList);
        Option option = new Option();
        JConverter.convert(data, option);
        if (optionExpose != null) {
            List<OptionExposeGauge.Cr> crs = Optional.ofNullable(optionExpose.getCrs()).orElseGet(ArrayList::new);
            List<OptionExposeGauge.GaugeStyle> gaugeStyles = Optional.ofNullable(optionExpose.getGaugeStyles()).orElseGet(ArrayList::new);

            if (crs.isEmpty()) {
                for (Series series : option.getSeries()) {
                    OptionExposeGauge.Cr cr = new OptionExposeGauge.Cr();
                    cr.setName(series.getName());
                    cr.setGaugeCenter("[50%, 50%]");
                    cr.setGaugeStartAngle(225);
                    cr.setGaugeEndAngle(-45);
                    cr.setGaugeRadius("75%");
                    cr.setGaugeSplitNumber(10);
                    cr.setGaugeMin(0);
                    cr.setGaugeMax(100);
                    crs.add(cr);
                }
                optionExpose.setCrs(crs);
            }
            if (gaugeStyles.isEmpty()) {
                for (Series series : option.getSeries()) {
                    OptionExposeGauge.GaugeStyle style = OptionExposeGauge.GaugeStyle.create();
                    style.setName(series.getName());
                    gaugeStyles.add(style);
                }
                optionExpose.setGaugeStyles(gaugeStyles);
            }

            Gauge gauge = null;
            for (Series series : option.getSeries()) {
                gauge = ((Gauge) series);

                for (OptionExposeGauge.Cr cr : crs) {
                    if (StringUtils.equals(cr.getName(), StringUtils.trimToEmpty(series.getName()))) {
                        gauge.setCenter(OptionExposeUtils.toCenter(cr.getGaugeCenter(), Object[]::new));
                        gauge.setRadius(cr.getGaugeRadius());
                        gauge.setStartAngle(Double.valueOf(cr.getGaugeStartAngle()));
                        gauge.setEndAngle(Double.valueOf(cr.getGaugeEndAngle()));
                        gauge.setSplitNumber(cr.getGaugeSplitNumber());
                        gauge.setMin(cr.getGaugeMin());
                        gauge.setMax(cr.getGaugeMax());
                    }
                }
                for (OptionExposeGauge.GaugeStyle style : gaugeStyles) {
                    AxisLine<Gauge> axisLine = new AxisLine<>();
                    axisLine.show(style.getShowGaugeAxisLine())
                            .lineStyle().width(style.getGaugeAxisLineWidth());
                    gauge.setAxisLine(axisLine);

                    SplitLine<Gauge> splitLine = new SplitLine<>();
                    splitLine.show(style.getShowGaugeSplitLine())
                            .lineStyle().width(style.getGaugeSplitLineWidth());
                    splitLine.setLength(style.getGaugeSplitLineLength());
                    gauge.setSplitLine(splitLine);

                    AxisTick<Gauge> axisTick = new AxisTick<>();
                    axisTick.show(style.getShowGaugeAxisTick())
                            .lineStyle().width(style.getGaugeAxisTickWidth());
                    axisTick.length(style.getGaugeAxisTickLength());
                    gauge.setAxisTick(axisTick);

                    AxisLabel<Gauge> axisLabel = new AxisLabel<>();
                    axisLabel.show(style.getShowGaugeAxisLabel())
                            .fontSize(style.getGaugeAxisLabelFontSize())
                            .fontWeight(Boolean.TRUE.equals(style.getGaugeAxisLabelFontBold()) ? FontWeightEnum.bold : FontWeightEnum.normal)
                            .fontStyle(Boolean.TRUE.equals(style.getGaugeAxisLabelFontItalic()) ? FontStyle.italic : FontStyle.normal);
                    axisLabel.setDistance(style.getGaugeAxisLabelDistance());
                    gauge.setAxisLabel(axisLabel);

                    Pointer pointer = new Pointer();
                    pointer.setShow(style.getShowGaugePointer());
                    pointer.setLength(style.getGaugePointerLength());
                    pointer.setWidth(style.getGaugePointerWidth());
                    gauge.setPointer(pointer);

                    Title title = new Title();
                    title.textStyle()
                            .fontSize(style.getGaugeTitleFontSize())
                            .fontWeight(Boolean.TRUE.equals(style.getGaugeTitleFontBold()) ? FontWeightEnum.bold : FontWeightEnum.normal)
                            .fontStyle(Boolean.TRUE.equals(style.getGaugeTitleFontItalic()) ? FontStyle.italic : FontStyle.normal)
                            .color(style.getGaugeTitleFontColor());
                    title.setOffsetCenter(OptionExposeUtils.toCenter(style.getGaugeTitleOffsetCenter()));
                    gauge.setTitle(title);

                    Detail detail = new Detail();
                    detail.setShow(style.getShowGaugeDetail());
                    detail.setOffsetCenter(OptionExposeUtils.toCenter(style.getGaugeDetailOffsetCenter(), Object[]::new));
                    detail.setFontSize(style.getGaugeDetailFontSize());
                    detail.setFontWeight(Boolean.TRUE.equals(style.getGaugeDetailFontBold()) ? FontWeightEnum.bold : FontWeightEnum.normal);
                    detail.setFontStyle(Boolean.TRUE.equals(style.getGaugeDetailFontItalic()) ? FontStyle.italic : FontStyle.normal);
                    detail.setFormatter(style.getGaugeDetailFormatter());
                    gauge.setDetail(detail);
                }
            }

            option.color(optionExpose.getColors());
            exposeTitle(option, optionExpose.getExposeTitle(), name);
        } else {
            for (Series series : option.series()) {
                AxisLine<Gauge> axisLine = new AxisLine<>();
                axisLine.lineStyle().width(10);
                AxisTick<Gauge> axisTick = new AxisTick<>();
                axisTick.length(8);
                axisTick.show(false);
                SplitLine<Gauge> splitLine = new SplitLine<>();
                splitLine.setLength(12);
                splitLine.show(false);
                AxisLabel<Gauge> axisLabel = new AxisLabel<>();
                axisLabel.setShow(false);
                ((Gauge) series).setAxisLine(axisLine);
                ((Gauge) series).setAxisTick(axisTick);
                ((Gauge) series).setRadius("100%");
                ((Gauge) series).setSplitLine(splitLine);
                Title title = new Title();
                title.textStyle().fontSize(12).color("#d042a4");
                ((Gauge) series).setTitle(title);
                ((Gauge) series).getDetail().setColor("#cdb112");
            }
        }
        return option;
    }

    public static void exposeTitle(Option option, OptionExposeTitle exposeTitle, String text) {
        if (exposeTitle == null || option == null) {
            return;
        }
        if (exposeTitle.getTitleLeft() == null) {
            return;
        }
        Title title = option.title();
        title.text(text).link(exposeTitle.getTitleLink()).target(Target.valueOf(exposeTitle.getTitleLinkTarget()))
                .show(exposeTitle.getShowTitle()).left(LeftEnum.valueOf(exposeTitle.getTitleLeft()))
                .top(TopEnum.valueOf(exposeTitle.getTitleTop()))
                .textStyle()
                .color(exposeTitle.getTitleFontColor())
                .fontSize(exposeTitle.getTitleFontSize())
                .fontStyle(Optional.ofNullable(exposeTitle.getTitleFontItalic()).orElse(Boolean.FALSE) ? FontStyle.italic : FontStyle.normal)
                .fontWeight(Optional.ofNullable(exposeTitle.getTitleFontBold()).orElse(Boolean.TRUE) ? FontWeightEnum.bold : FontWeightEnum.normal);
    }
}
