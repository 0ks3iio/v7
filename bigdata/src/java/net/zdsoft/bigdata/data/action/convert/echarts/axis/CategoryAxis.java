package net.zdsoft.bigdata.data.action.convert.echarts.axis;

import net.zdsoft.bigdata.data.action.convert.echarts.enu.AxisType;

/**
 * @see AxisType#category
 * @author ke_shen@126.com
 * @since 2018/4/12 上午10:55
 */
public class CategoryAxis extends Axis<CategoryAxis> {

    public CategoryAxis() {
        this.type(AxisType.category);
    }

    public CategoryAxis(String name) {
        this.type(AxisType.category);
        this.name(name);
    }
}
