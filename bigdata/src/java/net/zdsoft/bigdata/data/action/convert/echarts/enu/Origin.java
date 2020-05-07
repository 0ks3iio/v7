/**
 * FileName: Origin.java
 * Author:   shenke
 * Date:     2018/6/11 下午6:28
 * Descriptor:
 */
package net.zdsoft.bigdata.data.action.convert.echarts.enu;

/**
 * 图形区域的起始位置。
 *
 * 默认情况下，图形会从坐标轴轴线到数据间进行填充。
 * 如果需要填充的区域是坐标轴最大值到数据间，或者坐标轴最小值到数据间，则可以通过这个配置项进行设置。
 *
 * 可选值包括 'auto'（默认值）、 'start'、 'end'。
 *
 * 'auto' 填充坐标轴轴线到数据间的区域；
 * 'start' 填充坐标轴底部（非 inverse 情况是最小值）到数据间的区域；
 * 'end' 填充坐标轴顶部（非 inverse 情况是最大值）到数据间的区域。
 *
 * @author shenke
 * @since 2018/6/11 下午6:28
 */
public enum Origin {

    auto,
    start,
    end;
}
