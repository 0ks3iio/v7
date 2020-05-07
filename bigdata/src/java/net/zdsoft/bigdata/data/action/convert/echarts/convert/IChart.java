package net.zdsoft.bigdata.data.action.convert.echarts.convert;

import net.zdsoft.bigdata.data.action.convert.echarts.enu.AxisType;

import org.apache.commons.lang3.StringUtils;

/**
 * 封装数据库中相关图表配置以及查询结果等信息
 * 为了区别普通图表和大屏的区别
 * 此处利用泛型参数C来动态调整对应的图表类型
 * 利用R动态适配查询结果
 * @author ke_shen@126.com
 * @since 2018/4/11 下午5:14
 */
public interface IChart<C extends IChart.ChartConfig, R> {

    C getConfig();

    R getQueryResult();

    /**
     *  x坐标轴类型 适用范围： 柱状图、折线图、散点图
     *  当x轴为值类型时，Series Data数据将转化为标准格式"[0, 1]"
     *  默认为{@link AxisType#category}
     */
    AxisType xAxisType();

    /**
     * y坐标轴类型 适用范围： 同x轴
     * 默认为{@link AxisType#value}
     */
    AxisType yAxisType();

    /** x轴映射名称 默认x */
    String xMappingName();

    /** y轴映射名称 默认y */
    String yMappingName();

    /** 系列映射名称 默认s */
    String sMappingName();

    /**
     *
     * @author shenke
     * @since 2018/4/18 下午12:55
     */
    interface ChartConfig {

        String getName();

        int getChartType();

        default String getChartId() {
            return StringUtils.EMPTY;
        }

        default String getDivId() {
            return StringUtils.EMPTY;
        }
    }
}
