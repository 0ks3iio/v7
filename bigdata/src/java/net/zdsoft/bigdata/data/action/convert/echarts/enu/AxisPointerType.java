/**
 * FileName: AxisPointerType
 * Author:   shenke
 * Date:     2018/4/26 上午10:52
 * Descriptor:
 */
package net.zdsoft.bigdata.data.action.convert.echarts.enu;

/**
 * 指示器类型。
 *
 * 可选
 *
 *     'line' 直线指示器
 *
 *     'shadow' 阴影指示器
 *
 *     'cross' 十字准星指示器。其实是种简写，表示启用两个正交的轴的 axisPointer
 * @author shenke
 * @since 2018/4/26 上午10:52
 */
public enum AxisPointerType {

    line,
    shadow,
    cross
}
