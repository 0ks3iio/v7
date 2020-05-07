/**
 * FileName: RoseType.java
 * Author:   shenke
 * Date:     2018/6/12 下午2:31
 * Descriptor:
 */
package net.zdsoft.bigdata.data.action.convert.echarts.enu;

/**
 * 是否展示成南丁格尔图，通过半径区分数据大小。可选择两种模式：
 *
 * 'radius' 扇区圆心角展现数据的百分比，半径展现数据的大小。
 * 'area' 所有扇区圆心角相同，仅通过半径展现数据大小。
 * @author shenke
 * @since 2018/6/12 下午2:31
 */
public enum RoseType {

    radius,
    area
}
