package net.zdsoft.bigdata.data.action.convert.echarts.series;

import static net.zdsoft.bigdata.data.action.convert.echarts.enu.SeriesType.bar;

/**
 * @author ke_shen@126.com
 * @since 2018/4/11 下午8:39
 */
final public class Bar extends Series<Bar> {

    public Bar() {
        this.type(bar);
    }

    public Bar(String name) {
        super(name);
        this.type(bar);
    }
}
