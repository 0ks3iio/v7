/**
 * FileName: Funnel.java
 * Author:   shenke
 * Date:     2018/6/12 下午6:06
 * Descriptor:
 */
package net.zdsoft.bigdata.data.action.convert.echarts.series;

import net.zdsoft.bigdata.data.action.convert.echarts.enu.SeriesType;

/**
 * @author shenke
 * @since 2018/6/12 下午6:06
 */
public class Funnel extends Series<Funnel> {

    private Object sort;

    public Funnel() {
        this.type(SeriesType.funnel);
    }

    public Object getSort() {
        return sort;
    }

    public void setSort(Object sort) {
        this.sort = sort;
    }
}
