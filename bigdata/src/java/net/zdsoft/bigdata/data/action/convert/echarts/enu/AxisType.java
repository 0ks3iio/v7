package net.zdsoft.bigdata.data.action.convert.echarts.enu;

/**
 * @author ke_shen@126.com
 * @since 2018/4/12 上午10:48
 */
public enum AxisType {

    /** 类目轴，适应离散的数据类型， 为该类型时必须设置类目数据（data） */
    category,
    /** 数值轴，适应连续的数据 */
    value,
    /** 时间轴，和数值轴略有不同  */
    time,
    /** 对数轴，适用对数数据 */
    log;
}
