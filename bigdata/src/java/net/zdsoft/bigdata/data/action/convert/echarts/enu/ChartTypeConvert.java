package net.zdsoft.bigdata.data.action.convert.echarts.enu;

import net.zdsoft.bigdata.data.code.ChartType;

/**
 * @author ke_shen@126.com
 * @since 2018/4/12 下午4:58
 */
final public class ChartTypeConvert {

    public static SeriesType convert(int chartType) {
        if (ChartType.LINE_STRAIGHT == chartType
                || ChartType.LINE_BROKEN == chartType) {
            return SeriesType.line;
        }
        //if (ChartType.BAR_3D == chartType) {
        //    return SeriesType.bar3D;
        //}
        if (ChartType.BAR_BASIC == chartType) {
            return SeriesType.bar;
        }
        if (ChartType.SCATTER == chartType) {
            return SeriesType.scatter;
        }
        if (ChartType.PIE_BASIC == chartType
                || ChartType.PIE_DOUGHNUT == chartType) {
            return SeriesType.pie;
        }
        if (ChartType.MAP == chartType) {
            return SeriesType.map;
        }
        if (ChartType.SELF_NUMBER == chartType) {
            return SeriesType.number;
        }
        if (ChartType.SELF_TABLE == chartType) {
            return SeriesType.table;
        }
        if (ChartType.RADAR_BASIC == chartType) {
            return SeriesType.radar;
        }
        throw new RuntimeException("不支持的chartType");
    }

    public static int parseChartType(String seriesName) {
        SeriesType type = SeriesType.valueOf(seriesName);
        switch (type) {
            case pie:
                return ChartType.PIE_BASIC;
            case line:
                return ChartType.LINE_BROKEN;
            case bar:
                return ChartType.BAR_BASIC;
            case scatter:
                return ChartType.SCATTER;
            case map:
                return ChartType.MAP;
            //case bar3D:
            //    return ChartType.BAR_3D;
            default:
                return -1;
        }
    }
}
