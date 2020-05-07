/**
 * FileName: EchartConvert.java
 * Author:   shenke
 * Date:     2018/5/28 下午5:47
 * Descriptor:
 */
package net.zdsoft.bigdata.data.action.convert.echarts.convert;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import net.zdsoft.bigdata.data.action.convert.echarts.axis.Axis;
import net.zdsoft.bigdata.data.action.convert.echarts.axis.CommonAxis;
import net.zdsoft.bigdata.data.action.convert.echarts.axis.ValueAxis;
import net.zdsoft.bigdata.data.action.convert.echarts.component.Radar;
import net.zdsoft.bigdata.data.action.convert.echarts.data.BarData;
import net.zdsoft.bigdata.data.action.convert.echarts.data.FunnelData;
import net.zdsoft.bigdata.data.action.convert.echarts.data.MapData;
import net.zdsoft.bigdata.data.action.convert.echarts.data.PieData;
import net.zdsoft.bigdata.data.action.convert.echarts.data.RadarData;
import net.zdsoft.bigdata.data.action.convert.echarts.data.ScatterData;
import net.zdsoft.bigdata.data.action.convert.echarts.data.Sort;
import net.zdsoft.bigdata.data.action.convert.echarts.data.WordCloudData;
import net.zdsoft.bigdata.data.action.convert.echarts.enu.AxisType;
import net.zdsoft.bigdata.data.action.convert.echarts.enu.Origin;
import net.zdsoft.bigdata.data.action.convert.echarts.enu.PolarType;
import net.zdsoft.bigdata.data.action.convert.echarts.enu.RoseType;
import net.zdsoft.bigdata.data.action.convert.echarts.enu.Trigger;
import net.zdsoft.bigdata.data.action.convert.echarts.enu.WordShape;
import net.zdsoft.bigdata.data.action.convert.echarts.op.Option;
import net.zdsoft.bigdata.data.action.convert.echarts.series.Bar;
import net.zdsoft.bigdata.data.action.convert.echarts.series.EMap;
import net.zdsoft.bigdata.data.action.convert.echarts.series.Funnel;
import net.zdsoft.bigdata.data.action.convert.echarts.series.Line;
import net.zdsoft.bigdata.data.action.convert.echarts.series.Pie;
import net.zdsoft.bigdata.data.action.convert.echarts.series.RadarSeries;
import net.zdsoft.bigdata.data.action.convert.echarts.series.Scatter;
import net.zdsoft.bigdata.data.action.convert.echarts.series.Series;
import net.zdsoft.bigdata.data.action.convert.echarts.series.WordCloud;
import net.zdsoft.bigdata.data.code.ChartCategory;
import net.zdsoft.bigdata.data.echarts.EchartOptionWrap;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.framework.config.EisContextLoaderListener;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author shenke
 * @since 2018/5/28 下午5:47
 */
@Component
public class EchartConvert implements DataFormatConvert<IChart<IChart.ChartConfig, Result>, EchartOptionWrap> {

    private List<Convert> convertList;

    private Logger logger = LoggerFactory.getLogger(EchartConvert.class);

    @PostConstruct
    public void initEchartInnerConvert() {
        Map<String, Convert> convertMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(
                EisContextLoaderListener.getCurrentWebApplicationContext(), Convert.class,
                true,true);
        convertList = convertMap.entrySet().parallelStream().map(Map.Entry::getValue).collect(Collectors.toList());
    }

    @Override
    public EchartOptionWrap convert(IChart<IChart.ChartConfig, Result> config) {
        EchartOptionWrap wrap = new EchartOptionWrap();
        Option option = null;
        try {
            for (Convert convert : convertList) {
                if ((option = convert.convert(config)) != null) {
                    break;
                }
            }
        } catch (MappingErrorException e) {
            logger.error("echarts convert error", e);
            return new EchartOptionWrap().message(e.getMessage()).success(false);
        }
        if (option == null) {
            return null;
        }
        option.title().text(config.getConfig().getName());
        //wrap.option(option);
        return wrap;
    }

    interface Convert {

        Option convert(IChart<IChart.ChartConfig, Result> config) throws MappingErrorException;

    }

    /**
     * 基本饼图转换
     * 只需要 x y
     */
    @Component
    public static class BasicPieConvert implements Convert {
        @Override
        public Option convert(IChart<IChart.ChartConfig, Result> config) throws MappingErrorException {
            if (!ChartCategory.basic_pie.getChartType().equals(config.getConfig().getChartType())) {
                return null;
            }

            Option option = innerConvert(config);
            showTooltip(option, Trigger.item);
            return option;
        }

        protected Option innerConvert(IChart<IChart.ChartConfig, Result> config) throws MappingErrorException {
            Option option = new Option();
            JSONArray array = (JSONArray) config.getQueryResult().getValue();
            Set<String> lenged = new HashSet<>();
            Series pie = new Pie().option(option);
            boolean multi = multiSeries(array, config.sMappingName());
            if (multi) {
                throw new MappingErrorException("简单饼状图不支持多个系列");
            }
            for ( int i=0, length = array.size(); i<length; i++) {
                JSONObject val = (JSONObject) array.get(i);
                String y = val.getString(config.yMappingName());
                checkMapping(y, config.yMappingName());
                String x = val.getString(config.xMappingName());
                checkMapping(x, config.xMappingName());
                pie.data(new PieData(x, y));
                lenged.add(x);
            }
            option.series(pie);
            option.legend().data(lenged.toArray(new Object[0])).show(true);
            return option;
        }
    }

    /**
     * 内饼图，外环形图
     */
    @Component
    public static class InnerPieOutterDoughnutConvert extends BasicPieConvert implements Convert {

        @Override
        public Option convert(IChart<IChart.ChartConfig, Result> config) throws MappingErrorException {
            if (!ChartCategory.inner_pie_outer_doughnut.getChartType().equals(config.getConfig().getChartType())) {
                return null;
            }
            Option option = new Option();
            JSONArray array = (JSONArray) config.getQueryResult().getValue();
            //只能是两个系列
            int seriesSize = seriesSize(array, config.sMappingName());
            if (seriesSize > 2 || seriesSize < 2) {
                throw new MappingErrorException("数据格式错误");
            }
            Set<String> lenged = new HashSet<>();
            for ( int i=0, length = array.size(); i<length; i++) {
                JSONObject val = (JSONObject) array.get(i);
                String y = val.getString(config.yMappingName());
                checkMapping(y, config.yMappingName());
                String x = val.getString(config.xMappingName());
                checkMapping(x, config.xMappingName());
                String s = val.getString(config.sMappingName());
                PieData pieData = new PieData(x, y);
                option.series(s, Pie::new).data(pieData);
                lenged.add(x);
            }
            option.legend().data(lenged.toArray(new Object[0])).show(true);
            int index = 0;
            for (Series series : option.series()) {
                if (index == 0) {
                    ((Pie)series).setRadius(asArray("0", "30%"));
                    series.label().normal().position("inner");
                } else {
                    ((Pie)series).setRadius(asArray("50%", "70%"));
                }
                index ++;
                showTooltip(option, Trigger.item);
            }
            return option;
        }
    }

    /**
     * 单系列环形图
     */
    @Component
    public static class DoughnutConvert extends BasicPieConvert {
        @Override
        public Option convert(IChart<IChart.ChartConfig, Result> config) throws MappingErrorException {
            if (!ChartCategory.pie_doughnut.getChartType().equals(config.getConfig().getChartType())) {
                return null;
            }
            Option option = super.innerConvert(config);
            if (option != null) {
                if (option.series().size() == 1) {
                    Series series = option.series().get(0);
                    if (series instanceof Pie) {
                        ((Pie)series).setRadius(asArray("50%", "70%"));
                    }
                }
                showTooltip(option, Trigger.item);
            }
            return option;
        }
    }

    /**
     * 南丁格尔图
     */
    @Component
    public static class FnfConvert extends BasicPieConvert implements Convert {
        @Override
        public Option convert(IChart<IChart.ChartConfig, Result> config) throws MappingErrorException {
            if (!ChartCategory.pie_fnf.getChartType().equals(config.getConfig().getChartType())) {
                return null;
            }
            Option option = super.innerConvert(config);
            if (option.series().size() == 1) {
                Pie pie = (Pie) option.series().get(0);
                pie.setRadius(asArray("20%", "50%"));
                pie.setRoseType(RoseType.radius);
                showTooltip(option, Trigger.item);
            }
            return option;
        }
    }

    /** 基本折线转换 */
    @Component
    public static class BasicLineConvert implements Convert {
        @Override
        public Option convert(IChart<IChart.ChartConfig, Result> config) throws MappingErrorException {
            if (!ChartCategory.basic_line.getChartType().equals(config.getConfig().getChartType())) {
                return null;
            }
            return innerConvert(config);
        }

        protected Option innerConvert(IChart<IChart.ChartConfig, Result> config) throws MappingErrorException {
            Option option = new Option();

            JSONArray array = (JSONArray) config.getQueryResult().getValue();
            boolean multi = multiSeries(array, config.sMappingName());
            String singleSeries = "";
            for ( int i=0, length = array.size(); i<length; i++) {
                JSONObject val = (JSONObject) array.get(i);
                Object x = val.get(config.xMappingName());
                checkMapping(x, config.xMappingName());
                Object y = val.get(config.yMappingName());
                checkMapping(y, config.yMappingName());
                //单个系列
                if (!multi) {
                    setAxisAndSeriesData(config.xAxisType(), option, x, y, singleSeries);
                }
                //多系列
                else {
                    String s = val.getString(config.sMappingName());
                    setAxisAndSeriesData(config.xAxisType(), option, x, y, s);
                    //设置图例
                    option.legend().data(s);
                }
            }
            if (AxisType.value.equals(config.yAxisType())) {
                option.yAxis(new CommonAxis().type(AxisType.value).show(true));
            }
            if (AxisType.value.equals(config.xAxisType())) {
                option.xAxis(new CommonAxis().type(AxisType.value).show(true));
            }
            showTooltip(option, Trigger.axis);
            return option;
        }

        private void setAxisAndSeriesData(AxisType xAxisType, Option option, Object x, Object y, String s) {
            if (AxisType.category.equals(xAxisType)) {
                option.series(s, Line::new).data(y);
                if (option.xAxis().stream().noneMatch(e -> e.contains(x))) {
                    option.xAxis(s, CommonAxis::new).type(AxisType.category).show(true).data(x);
                }
            } else {
                option.series(s, Line::new).data(y, v-> asArray(x, v));
            }
        }
    }

    @Component
    public static class LineArea extends BasicLineConvert implements Convert {

        @Override
        public Option convert(IChart<IChart.ChartConfig, Result> config) throws MappingErrorException {
            if (!ChartCategory.area_line.getChartType().equals(config.getConfig().getChartType())) {
                return null;
            }
            Option option = super.innerConvert(config);
            option.series().forEach(series -> {
                ((Line)series).areaStyle().setOrigin(Origin.auto);
            });
            return option;
        }
    }

    /** 简单柱状图，可以有多个系列 但不堆叠 */
    @Component
    public static class BasicBarConvert implements Convert {
        @Override
        public Option convert(IChart<IChart.ChartConfig, Result> config) throws MappingErrorException {
            if (!ChartCategory.basic_bar.getChartType().equals(config.getConfig().getChartType())) {
                return null;
            }
            return innerConvert(config, null);
        }

        protected Option innerConvert(IChart<IChart.ChartConfig, Result> config, String stack) throws MappingErrorException {
            Option option = new Option();
            JSONArray array = (JSONArray) config.getQueryResult().getValue();
            boolean multi = multiSeries(array, config.sMappingName());
            String singleSeries = "";
            boolean yNumber = false;
            for ( int i=0, length = array.size(); i<length; i++) {
                JSONObject val = (JSONObject) array.get(i);
                String x = val.getString(config.xMappingName());
                checkMapping(x, config.xMappingName());
                Object y = val.get(config.yMappingName());
                checkMapping(y, config.yMappingName());
                if (!multi) {
                    setAxisAndSeries(config.xAxisType(), option, singleSeries, x, y);
                }
                //多系列
                else {
                    String s = val.getString(config.sMappingName());
                    Series series = setAxisAndSeries(config.xAxisType(), option, s, x, y);
                    if (StringUtils.isNotBlank(stack)) {
                        if (StringUtils.isBlank(series.getStack())) {
                            String stackStr = val.getString(stack);
                            series.stack(stackStr);
                        }
                    }
                    //设置图例
                    option.legend().data(s).show(true);
                }
                yNumber = StringUtils.isNumeric(Optional.ofNullable(y).orElse("").toString());
            }
            if (yNumber) {
                option.yAxis(new ValueAxis().show(true));
            }
            showTooltip(option, Trigger.axis);
            return option;
        }

        protected Series setAxisAndSeries(AxisType xAxisType, Option option, String s, String x, Object y) {
            Series series = null;
            if (AxisType.category.equals(xAxisType)) {
                if (option.xAxis().stream().noneMatch(e -> e.contains(x))) {
                    option.xAxis("", CommonAxis::new).type(xAxisType).show(true).data(x);
                }
                series = option.series(s, Bar::new);
                series.data(new BarData().name(x).value(y));
            } else {
                series = option.series(s, Bar::new);
                series.data(y, v->asArray(x, v));
            }
            return series;
        }
    }

    /** 堆叠柱转图 */
    @Component
    public static class StackBarConvert extends BasicBarConvert implements Convert{
        @Override
        public Option convert(IChart<IChart.ChartConfig, Result> config) throws MappingErrorException {
            if (!ChartCategory.stack_bar.getChartType().equals(config.getConfig().getChartType())) {
                return null;
            }
            return super.innerConvert(config, "stack");
        }
    }

    @Component
    public static class StripeBarConvert extends BasicBarConvert implements Convert {

        @Override
        public Option convert(IChart<IChart.ChartConfig, Result> config) throws MappingErrorException {
            if (!ChartCategory.stripe_bar.getChartType().equals(config.getConfig().getChartType())) {
                return null;
            }
            Option option = super.innerConvert(config, null);
            List<Axis> xAxes = option.xAxis();
            List<Axis> yAxis = option.yAxis();
            option.xAxis(yAxis).yAxis(xAxes);
            return option;
        }
    }

    @Component
    public static class StackStripeBar extends BasicBarConvert implements Convert {
        @Override
        public Option convert(IChart<IChart.ChartConfig, Result> config) throws MappingErrorException {
            if (!ChartCategory.stack_stripe_bar.getChartType().equals(config.getConfig().getChartType())) {
                return null;
            }
            Option option = super.innerConvert(config, "stack");
            List<Axis> xAxes = option.xAxis();
            List<Axis> yAxis = option.yAxis();
            option.xAxis(yAxis).yAxis(xAxes);
            return option;
        }
    }

    /**
     * 并非是带有涟漪的气泡图
     * 只是简单的气泡图
     */
    @Component
    public static class ScatterEffectConvert extends BasicScatterConvert implements Convert {
        @Override
        public Option convert(IChart<IChart.ChartConfig, Result> config) throws MappingErrorException {
            //if (!ChartCategory.effect_scatter.getChartType().equals(config.getConfig().getChartType())) {
            //    return null;
            //}

            //return super.innerConvert(config, "symbolSize");
            return null;
        }
    }

    @Component
    public static class BasicScatterConvert implements Convert {

        @Override
        public Option convert(IChart<IChart.ChartConfig, Result> config) throws MappingErrorException {
            if (!ChartCategory.basic_scatter.getChartType().equals(config.getConfig().getChartType())) {
                return null;
            }
            return innerConvert(config, null);
        }

        protected Option innerConvert(IChart<IChart.ChartConfig, Result> config, String symbolSizeName) throws MappingErrorException {
            Option option = new Option();

            JSONArray array = (JSONArray) config.getQueryResult().getValue();
            boolean multi = multiSeries(array, config.sMappingName());
            String singleSeries = "";
            for ( int i=0, length = array.size(); i<length; i++) {
                JSONObject val = (JSONObject) array.get(i);
                Object x = val.get(config.xMappingName());
                checkMapping(x, config.xMappingName());
                Object y = val.get(config.yMappingName());
                checkMapping(y, config.yMappingName());
                Double symbolSize = val.getDouble(symbolSizeName);
                //单个系列
                if (!multi) {
                    Scatter scatter = setAxisAndSeriesData(config.xAxisType(), option, x, y, singleSeries, symbolSize);
                }
                //多系列
                else {
                    String s = val.getString(config.sMappingName());
                    Scatter scatter = setAxisAndSeriesData(config.xAxisType(), option, x, y, s, symbolSize);
                    //设置图例
                    option.legend().data(s);
                }
            }
            if (AxisType.value.equals(config.yAxisType())) {
                option.yAxis(new CommonAxis().type(AxisType.value).show(true));
            }
            if (AxisType.value.equals(config.xAxisType())) {
                option.xAxis(new CommonAxis().type(AxisType.value).show(true));
            }
            showTooltip(option, Trigger.axis);
            return option;
        }

        protected Scatter setAxisAndSeriesData(AxisType xAxisType, Option option, Object x, Object y, String s, Double symbolSize) {
            Scatter series = null;
            if (AxisType.category.equals(xAxisType)) {
                series = (Scatter) option.series(s, Scatter::new);
                if (symbolSize != null) {
                    ScatterData scatterData = new ScatterData();
                    scatterData.setSymbolSize(symbolSize);
                    scatterData.setValue(y);
                    series.data(scatterData);
                } else {
                    series.data(y);
                }
                if (option.xAxis().stream().noneMatch(e -> e.contains(x))) {
                    option.xAxis("", CommonAxis::new).type(AxisType.category).show(true).data(x);
                }
            } else {
                series = (Scatter) option.series(s, Scatter::new);
                if (symbolSize != null) {
                    ScatterData scatterData = new ScatterData();
                    scatterData.setSymbolSize(symbolSize);
                    scatterData.setValue(asArray(x, y));
                    series.data(scatterData);
                } else {
                    series.data(y, v-> asArray(x, v));
                }
            }
            return series;
        }
    }

    @Component
    public static class BasicRadarConvert implements Convert {
        @Override
        public Option convert(IChart<IChart.ChartConfig, Result> config) throws MappingErrorException {
            if (!ChartCategory.basic_radar.getChartType().equals(config.getConfig().getChartType())) {
                return null;
            }
            JSONArray array = (JSONArray) config.getQueryResult().getValue();
            boolean multi = multiSeries(array, config.sMappingName());

            Option option = multi ? parseMultiRadar(array, config) : parseSingleRadar(array, config);
            //设置为圆形
            option.radar().setShape(PolarType.circle);
            showTooltip(option, Trigger.item);
            return option;
        }

        /**
         * 单系列
         */
        private Option parseSingleRadar(JSONArray array, IChart<IChart.ChartConfig, Result> config) throws MappingErrorException {
            RadarData radarData = new RadarData();
            Option option = new Option();
            int max = 0;
            for (Object o : array) {
                JSONObject val = (JSONObject) o;
                String x = val.getString(config.xMappingName());
                checkMapping(x, config.xMappingName());
                Object y = val.get(config.yMappingName());
                checkMapping(y, config.yMappingName());
                if (!StringUtils.isNumeric(y.toString())) {
                    throw new MappingErrorException(String.format("字段 %s 必须为数字", config.yMappingName()));
                }
                //radar组件需要的数据
                Radar.Indicator indicator = new Radar.Indicator();
                indicator.setName(x);
                max = Math.max(max, NumberUtils.toInt(y.toString()));
                indicator.max(max);
                option.radar().indicator(indicator);
                //图例
                option.legend().show(false);

                //series data 数据
                radarData.value().add(y);
            }
            option.series(new RadarSeries().data(radarData));
            return option;
        }

        private Option parseMultiRadar(JSONArray array, IChart<IChart.ChartConfig, Result> config) throws MappingErrorException {
            Option option = new Option();
            Map<String, RadarData> radarMap = new LinkedHashMap<>();
            int max = 0;
            for (Object o : array) {
                JSONObject val = (JSONObject) o;
                String x = val.getString(config.xMappingName());
                checkMapping(x, config.xMappingName());
                Object y = val.get(config.yMappingName());
                checkMapping(y, config.yMappingName());
                if (!StringUtils.isNumeric(y.toString())) {
                    throw new MappingErrorException(String.format("字段 %s 必须为数字", config.yMappingName()));
                }

                //radar 组件需要的数据
                Radar.Indicator indicator = option.radar().indicator(x, Radar.Indicator::new);
                indicator.setName(x);
                max = Math.max(max, NumberUtils.toInt(y.toString()));
                indicator.max(max);

                //图列数据
                String s = val.getString(config.sMappingName());
                option.legend().show(true).data(s);

                //series 组件需要的data数据
                RadarData radarData = radarMap.get(s);
                if (radarData == null) {
                    radarData = new RadarData();
                    radarMap.put(s, radarData);
                }
                radarData.setName(s);
                radarData.value().add(y);
            }
            RadarSeries radarSeries = new RadarSeries();
            radarSeries.data(radarMap.entrySet().stream().map(Map.Entry::getValue).toArray());
            option.series(radarSeries);
            return option;
        }
    }

    /**
     * 漏斗图
     */
    @Component
    public static class FunnelConvert implements Convert {

        @Override
        public Option convert(IChart<IChart.ChartConfig, Result> config) throws MappingErrorException {
            if (!ChartCategory.funnel.getChartType().equals(config.getConfig().getChartType())) {
                return null;
            }

            return innerConvert(config);
        }

        protected Option innerConvert(IChart<IChart.ChartConfig, Result> config) throws MappingErrorException {
            JSONArray array = (JSONArray) config.getQueryResult().getValue();
            Funnel funnel = new Funnel();
            Option option = new Option();
            for (Object o : array) {
                JSONObject val = (JSONObject) o;
                String x = val.getString(config.xMappingName());
                checkMapping(x, config.xMappingName());
                Object y = val.get(config.yMappingName());
                checkMapping(y, config.yMappingName());
                FunnelData funnelData = new FunnelData();
                funnelData.setName(x);
                funnelData.setValue(y);
                funnel.data(funnelData);
                //option.legend().data(val.getString(config.xMappingName())).show(true);
            }
            funnel.setSort(Sort.descending);
            funnel.label().normal().position("inside").show(true);

            option.series(funnel);
            option.tooltip().trigger(Trigger.item).show(true).formatter("{b} : {d}%");
            return option;
        }
    }

    /**
     * 正金字塔
     */
    @Component
    public static class FunnelAsceding extends FunnelConvert implements Convert {

        @Override
        public Option convert(IChart<IChart.ChartConfig, Result> config) throws MappingErrorException {
            //if (!ChartCategory.funnel_ascending.getChartType().equals(config.getConfig().getChartType())) {
            //    return null;
            //}
            return null;
            //Option option = super.innerConvert(config);
            //for (Series series : option.series()) {
            //    ((Funnel)series).setSort(Sort.ascending);
            //}
            //return option;
        }
    }

    /**
     * 词云
     */
    @Component
    public static class WordCloudConvert implements Convert {

        @Override
        public Option convert(IChart<IChart.ChartConfig, Result> config) throws MappingErrorException {
            if (!ChartCategory.word_cloud.getChartType().equals(config.getConfig().getChartType())) {
                return null;
            }
            Option option = new Option();
            JSONArray array = (JSONArray) config.getQueryResult().getValue();
            WordCloud wordCloud = new WordCloud();
            for (Object o : array) {
                JSONObject val = (JSONObject)o;
                WordCloudData data = new WordCloudData();
                data.setName(val.getString(config.xMappingName()));
                data.setValue(val.get(config.yMappingName()));
                wordCloud.data(data);
            }
            wordCloud.setShape(WordShape.circle);
            wordCloud.setDrawOutofBound(false);
            option.series(wordCloud);
            showTooltip(option, Trigger.item);
            return option;
        }
    }

    /***
     * 地图和其他图形不同，不同的控件对应不同的数据格式
     * ex ：
     *  基础控件：地图层
     *  扩展组件：飞线控件、区域热力控件、气泡控件 （不同控件的数据格式和Option的组件不尽相同）
     *  视觉映射组件
     */
    @Component
    public static class BasicMapConvert implements Convert {
        @Override
        public Option convert(IChart<IChart.ChartConfig, Result> config) throws MappingErrorException {
            if (!ChartCategory.basic_map.getChartType().equals(config.getConfig().getChartType())) {
                return null;
            }

            //
            Option option = new Option();
            EMap eMap = new EMap();
            JSONArray array = (JSONArray) config.getQueryResult().getValue();
            double max = 0;
            double min = 0;
            for (Object o : array) {
                String name = ((JSONObject)o).getString(config.xMappingName());
                Object value = ((JSONObject)o).get(config.yMappingName());
                MapData data = new MapData(name, value);
                eMap.data(data);
                max = Math.max(NumberUtils.toDouble(value.toString()), max);
                min = Math.min(NumberUtils.toDouble(value.toString()), min);
            }
            eMap.label().normal().show(true);
            eMap.label().emphasis().show(true);
            option.series(eMap);
            option.visualMap().max(Double.valueOf(max).intValue()).min(Double.valueOf(min).intValue())
                    .calculable(true).setType("continuous");
            return option;
        }

        //各组件数据格式转换

        /** 飞线组件 */
        public void linesDataConvert() {

        }

        /** 气泡组件 */
        public void scatterDataConver() {

        }

        /** 带涟漪的气泡组件 */
        public void effectScatterDataConvert() {

        }

    }

    static double average(double... numbers) {
        double total = 0;
        for (int i=0, length = numbers.length; i < length; i++) {
            total += numbers[i];
        }
        return total/numbers.length;
    }

    static double standardDeviation(double[] numbers, double average) {
        double total = 0;
        for (int i=0, length = numbers.length; i < length; i++) {
            double s = numbers[i] - average;
            total += Math.pow(s, s);
        }
        return Math.sqrt((total)/numbers.length);
    }

    static double JiuJiuGuiYi(double src, double standardDeviation, double average) {
        return (src - average)/standardDeviation;
    }


    static Object[] asArray(Object ... obj) {
        return obj;
    }

    static void showTooltip(Option option, Trigger trigger) {
        option.tooltip().trigger(trigger).show(true);
    }

    static boolean multiSeries(JSONArray array, String seriesMapping) {

        return seriesSize(array, seriesMapping) > 1;
    }

    static int seriesSize(JSONArray array, String seriesMapping) {
        Set<String> series = new HashSet<>();
        JSONObject val;
        for ( int i=0, length = array.size(); i<length; i++) {
            val = (JSONObject) array.get(i);
            String s = val.getString(seriesMapping);
            //任何一条数据的系列值为空，则忽略所有的系列值
            if (StringUtils.isBlank(s)) {
                series.clear();
                break;
            }
            series.add(s);
        }
        return series.size();
    }

    static void checkMapping(Object mapping,String mappingName) throws MappingErrorException {
        if (mapping == null) {
            throw new MappingErrorException(String.format("无法匹配字段 %s", mappingName));
        }
        if (mapping instanceof String && StringUtils.isBlank(mapping.toString())) {
            throw new MappingErrorException(String.format("无法匹配字段 %s", mappingName));
        }
    }

    /** 映射字段错误 */
    static class MappingErrorException extends Exception {
        public MappingErrorException(String message) {
            super(message);
        }
    }
}
