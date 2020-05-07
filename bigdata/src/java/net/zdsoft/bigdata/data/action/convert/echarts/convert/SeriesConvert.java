package net.zdsoft.bigdata.data.action.convert.echarts.convert;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import net.zdsoft.bigdata.data.action.convert.echarts.axis.CommonAxis;
import net.zdsoft.bigdata.data.action.convert.echarts.axis.ValueAxis;
import net.zdsoft.bigdata.data.action.convert.echarts.component.Tooltip;
import net.zdsoft.bigdata.data.action.convert.echarts.data.BarData;
import net.zdsoft.bigdata.data.action.convert.echarts.data.MapData;
import net.zdsoft.bigdata.data.action.convert.echarts.data.PieData;
import net.zdsoft.bigdata.data.action.convert.echarts.data.TableData;
import net.zdsoft.bigdata.data.action.convert.echarts.enu.AxisType;
import net.zdsoft.bigdata.data.action.convert.echarts.enu.ChartTypeConvert;
import net.zdsoft.bigdata.data.action.convert.echarts.enu.SeriesType;
import net.zdsoft.bigdata.data.action.convert.echarts.enu.Trigger;
import net.zdsoft.bigdata.data.action.convert.echarts.op.Option;
import net.zdsoft.bigdata.data.action.convert.echarts.series.Bar;
import net.zdsoft.bigdata.data.action.convert.echarts.series.EMap;
import net.zdsoft.bigdata.data.action.convert.echarts.series.Line;
import net.zdsoft.bigdata.data.action.convert.echarts.series.Pie;
import net.zdsoft.bigdata.data.action.convert.echarts.series.Scatter;
import net.zdsoft.bigdata.data.echarts.EchartOptionWrap;
import net.zdsoft.bigdata.data.manager.api.Result;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * <p>
 *     在baidu的echarts jsAPI中series.data属性更为全面，这里仅从数据的层面进行考虑
 *
 *     无论是Line还是Bar都会只创建一个y轴体系， 更多的y体系请自行设置,
 *     另外针对X轴为value的情况只会给出默认的Axis的实现，并不会给出最大最小值
 *     <li>
 *          line：
 *          实现了基本的折线、平滑曲线的数据转换
 *          基于给定x、y和s数据格式的x轴数据转换，但不包含Y轴的坐标数据转换
 *          基于给定的x轴数据类型，生成不多余两种x轴坐标的转换 (根据x值进行动态判定)，若x轴坐标体系多余两种则抛出异常
 *     </li>
 *     <li>
 *          bar：
 *          基本的柱状图（但不包括条形图，堆叠的柱状图需要额外的stack参数才能实现，这里暂时不支持）
 *          关于x轴和y轴的坐标体系和line的情况基本一致
 *     </li>
 *     <li>
 *          pie：
 *          饼状图的数据结构比较特殊（使用x作为name y作为value， s仍然作为系列值）
 *          pie 给定默认的tooltip值的formatter {@link Tooltip#PIE_FORMATTER}
 *     </li>
 *     <li>
 *          map:
 *          基本的map图形（注意：3DMap属于单独的类型）
 *          数据结构和pie类似
 *     </li>
 *     <li>
 *         scatter:
 *         基本的散点图
 *         要求x系列和y系列的数据必须对应， s系列可有可无，并且图列显示为false
 *     </li>
 * </p>
 * 基本的line bar pie scatter 给定了最基本的Tooltip，即设置tooltip显示
 * 对于Bar Line等默认的x坐标系名称不再显示
 * @author ke_shen@126.com
 * @since 2018/4/12 上午10:11
 */
@Component
public class SeriesConvert implements OptionConvert.Convert<IChart<IChart.ChartConfig, Result>, EchartOptionWrap> {

    private Logger logger = LoggerFactory.getLogger(SeriesConvert.class);

    @Override
    public EchartOptionWrap convert(IChart<IChart.ChartConfig, Result> iChart) {
        JSONArray array = (JSONArray) iChart.getQueryResult().getValue();
        if (array.isEmpty()) {
            return null;
        }

        SeriesType type = ChartTypeConvert.convert(iChart.getConfig().getChartType());
        Option option = new Option();
        switch (type) {
            case line:
                convertLine(array, option, iChart);
                option.tooltip().trigger(Trigger.axis).show(true);
                break;
            case bar:
                convertBar(array, option, iChart);
                option.tooltip().trigger(Trigger.axis).show(true);
                break;
            /** 自定义的数字类型 */
            case number:
                convertPie(array, option, iChart);
                option.tooltip().trigger(Trigger.axis).show(false);
                option.series().forEach(s->s.type(SeriesType.number));
                if (logger.isDebugEnabled()) {
                    logger.debug("{}", option);
                }
                break;
            case table:
                convertTable(array, option, iChart);
                option.tooltip().trigger(Trigger.axis).show(false);
                option.series().forEach(s->s.type(SeriesType.table));
                if (logger.isDebugEnabled()) {
                    logger.debug("{}", option);
                }
                break;
            case pie:
                convertPie(array, option, iChart);
                option.tooltip().trigger(Trigger.item).show(true).formatter(Tooltip.PIE_FORMATTER);
                break;
            case scatter:
                convertScatter(array, option, iChart);
                option.tooltip().trigger(Trigger.item).show(true);
                break;
            case map:
                convertMap(array, option, iChart);
                break;
            default:
        }
        option.xAxis(option.xAxis().stream().map(s -> s.name("")).collect(Collectors.toList()));
        return null;
    }

    /** 直接生成不带任何样式的HTML代码 */
    private void convertNativeTable(JSONArray array, Option option, IChart<IChart.ChartConfig, Result> iChart) {
        StringBuilder tableHTMLBuilder = new StringBuilder();
        tableHTMLBuilder.append("<table>");
        //拿到标题
        JSONObject thead = (JSONObject) array.get(0);

        tableHTMLBuilder.append("</table>");
    }

    private void convertLine(JSONArray array, Option option, IChart<IChart.ChartConfig, Result> iChart) {
        for (Object anArray : array) {
            JSONObject val = (JSONObject) anArray;
            String s = Optional.ofNullable(val.get(iChart.sMappingName())).orElse("").toString();
            Object y = val.get(iChart.yMappingName());
            String x = Optional.ofNullable(val.get(iChart.xMappingName())).orElse("").toString();
            option.series(s, Line::new).data(y, v -> {
                //当x轴为值类型时，转换为标准数值
                if (AxisType.value.equals(iChart.xAxisType())) {
                    return asArray(x, y);
                }
                return v;
            });
            //当系列名称为空时不再填充图例名称
            if (s != null && !"".equals(s)) {
                option.legend().data(s).show(true);
            }
            //当x坐标轴类型为 category时，则生成x坐标轴数据
            if (AxisType.category.equals(iChart.xAxisType())) {
                //x轴名称相同则不会生成信息的坐标体系
                if (option.xAxis().stream().noneMatch(e -> e.contains(x))) {
                    option.xAxis(s, CommonAxis::new).type(AxisType.category).show(true).data(x);
                }
            }
        }
        if (AxisType.value.equals(iChart.yAxisType())) {
            option.yAxis(new CommonAxis().type(AxisType.value).show(true));
        }
        if (AxisType.value.equals(iChart.xAxisType())) {
            option.xAxis(new CommonAxis().type(AxisType.value).show(true));
        }
    }

    private Object[] asArray(Object ...val) {
        return val;
    }

    /** Bar的数据结构转换基本和Line一致 */
    private void convertBar(JSONArray array, Option option, IChart<IChart.ChartConfig, Result> iChart) {
        boolean isNumberic = false;
        for (Object anArray : array) {
            JSONObject val = (JSONObject) anArray;
            String s = Optional.ofNullable(val.get(iChart.sMappingName())).orElse("").toString();
            Object y = val.get(iChart.yMappingName());
            String x = Optional.ofNullable(val.get(iChart.xMappingName())).orElse("").toString();
            //一般情况下bar的x轴类型应为 category， 支持类型Line图形的标准坐标形式
            option.series(s, Bar::new).data(y, v -> {
                //当x轴为值类型时，转换为标准数值
                if (AxisType.value.equals(iChart.xAxisType())) {
                    return asArray(x, y);
                }
                //当坐标轴为category
                else {
                    return new BarData().name(x).value(y);
            }
            });
            //当系列名称为空时不再填充图例名称
            if (s != null && !"".equals(s)) {
                option.legend().data(s).show(true);
            }
            //当x坐标轴类型为 category时，则生成x坐标轴数据
            if (AxisType.category.equals(iChart.xAxisType())) {
                //x轴名称相同则不会生成信息的坐标体系
                if (option.xAxis().stream().noneMatch(e -> e.contains(x))) {
                    option.xAxis(s, CommonAxis::new).type(iChart.xAxisType()).show(true).data(x);
                }
            }
            isNumberic = StringUtils.isNumeric(y.toString());
        }
        if (isNumberic || AxisType.value.equals(iChart.yAxisType())) {
            option.yAxis(new ValueAxis().show(true));
        }
    }

    /** 饼状图的数据结构比较特殊，数据值仅需要key:value的形式，因此x轴在此处不可用 */
    private void convertPie(JSONArray array, Option option, IChart<IChart.ChartConfig, Result> iChart) {
        Set<String> sSet = new LinkedHashSet<>();
        for ( int i=0, length=array.size(); i<length; i++) {
            JSONObject val = (JSONObject) array.get(i);
            String s = Optional.ofNullable(val.get(iChart.sMappingName())).orElse("").toString();
            Object y = val.get(iChart.yMappingName()).toString();
            String x = Optional.ofNullable(val.get(iChart.xMappingName())).orElse("").toString();
            option.series(s, Pie::new).data(new PieData(x, y));
            sSet.add(x);
        }
        option.legend().data(sSet.toArray()).show(true);
    }

    private void convertTable(JSONArray array, Option option, IChart<IChart.ChartConfig, Result> iChart) {
        Set<String> sSet = new LinkedHashSet<>();
        for ( int i=0, length=array.size(); i<length; i++) {
            JSONObject val = (JSONObject) array.get(i);
            if (val.size()>3) {
                option.series("", Pie::new).data(new TableData().kv(
                        val.entrySet().stream().map(entry -> new TableData.KV().setK(entry.getKey()).setV(entry.getValue())).collect(Collectors.toList())
                ));
            } else {
                String s = Optional.ofNullable(val.get(iChart.sMappingName())).orElse("").toString();
                Object y = val.get(iChart.yMappingName()).toString();
                String x = Optional.ofNullable(val.get(iChart.xMappingName())).orElse("").toString();
                option.series(s, Pie::new).data(new PieData(x, y));
                sSet.add(x);
            }
        }
        option.legend().data(sSet.toArray()).show(true);
    }

    /** map数据结构转换，类似Pie */
    private void convertMap(JSONArray array, Option option, IChart<IChart.ChartConfig, Result> iChart) {
        int max = 0;
        int min = Integer.MAX_VALUE;
        for ( int i=0, length=array.size(); i<length; i++) {
            JSONObject val = (JSONObject) array.get(i);
            String s = Optional.ofNullable(val.get(iChart.sMappingName())).orElse("").toString();
            Object x = Optional.ofNullable(val.get(iChart.xMappingName())).orElse("");
            Object y = val.get(iChart.yMappingName()).toString();
            option.series(s, EMap::new).data(new MapData(x.toString(), y));
            if (StringUtils.isNumeric(y.toString())) {
                max = Math.max(Integer.parseInt(y.toString()), max);
                min = Math.min(Integer.parseInt(y.toString()), min);
            }
        }
        if (max != 0) {
            option.visualMap().max(max).min(min);
        }
    }

    /** 标准散点图形 这里 series.data 为标准数据结构 */
    private void convertScatter(JSONArray array, Option option, IChart<IChart.ChartConfig, Result> iChart) {
        for (Object o : array) {
            JSONObject val = (JSONObject) o;
            String s = Optional.ofNullable(val.get(iChart.sMappingName())).orElse("").toString();
            Object y = val.get(iChart.yMappingName()).toString();
            String x = Optional.ofNullable(val.get(iChart.xMappingName())).orElse("").toString();
            option.series(s, Scatter::new).data(y, v -> {
                //当x轴为值类型时，转换为标准数值
                if (AxisType.value.equals(iChart.xAxisType())) {
                    return asArray(x, y);
                }
                return v;
            });
            //散点图不显示图例
            if (s != null && !"".equals(s)) {
                option.legend().data(s).show(true);
            }
            //当x坐标轴类型为 category时，则生成x坐标轴数据
            if (AxisType.category.equals(iChart.xAxisType())) {
                //x轴名称相同则不会生成信息的坐标体系
                if (option.xAxis().stream().noneMatch(e -> e.contains(x))) {
                    option.xAxis(s, CommonAxis::new).type(AxisType.category).show(true).data(x);
                }
            }
        }
        if (AxisType.value.equals(iChart.yAxisType())) {
            option.yAxis(new CommonAxis().type(AxisType.value).show(true));
        }
        if (AxisType.value.equals(iChart.xAxisType())) {
            option.xAxis(new CommonAxis().type(AxisType.value).show(true));
        }
    }
}
