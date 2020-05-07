/**
 * FileName: RadarSeries.java
 * Author:   shenke
 * Date:     2018/5/30 上午11:35
 * Descriptor:
 */
package net.zdsoft.bigdata.data.action.convert.echarts.series;

import net.zdsoft.bigdata.data.action.convert.echarts.enu.SeriesType;

/**
 * @author shenke
 * @since 2018/5/30 上午11:35
 */
public class RadarSeries extends Series<RadarSeries> {

    public RadarSeries() {
        this.type(SeriesType.radar);
    }

    public RadarSeries(String name) {
        this();
        this.name(name);
    }


}
