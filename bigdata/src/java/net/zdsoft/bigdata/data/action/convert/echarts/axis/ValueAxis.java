package net.zdsoft.bigdata.data.action.convert.echarts.axis;

import net.zdsoft.bigdata.data.action.convert.echarts.enu.AxisType;

/**
 * @see AxisType#value
 * @author ke_shen@126.com
 * @since 2018/4/12 下午3:23
 */
public class ValueAxis extends Axis<ValueAxis> {

    public ValueAxis() {
        this.type(AxisType.value);
    }

    public ValueAxis(String name) {
        this();
        this.name(name);
    }
}
