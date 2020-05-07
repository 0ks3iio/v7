package net.zdsoft.bigdata.data.action.convert.echarts.axis;

import net.zdsoft.bigdata.data.action.convert.echarts.enu.AxisType;

/**
 * @see net.zdsoft.bigdata.data.action.convert.echarts.enu.AxisType#time
 * @author ke_shen@126.com
 * @since 2018/4/12 下午3:27
 */
public class TimeAxis extends ValueAxis {

    public TimeAxis() {
        this.type(AxisType.time);
    }

    public TimeAxis(String name) {
        this();
        this.name(name);
    }
}
