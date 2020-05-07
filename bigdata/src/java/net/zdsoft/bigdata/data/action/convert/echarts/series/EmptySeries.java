/**
 * FileName: EmptySeries
 * Author:   shenke
 * Date:     2018/4/25 下午4:43
 * Descriptor:
 */
package net.zdsoft.bigdata.data.action.convert.echarts.series;

/**
 * @author shenke
 * @since 2018/4/25 下午4:43
 */
public class EmptySeries extends Series<EmptySeries>{

    public static EmptySeries empty() {
        return new EmptySeries();
    }
}
