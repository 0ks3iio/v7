/**
 * FileName: Align
 * Author:   shenke
 * Date:     2018/4/25 上午9:54
 * Descriptor:
 */
package net.zdsoft.bigdata.data.action.convert.echarts.enu;

/**
 * 文字水平对齐方式，默认自动。
 *
 * 可选：
 *
 *     'left'
 *     'center'
 *     'right'
 *
 * rich 中如果没有设置 align，则会取父层级的 align。例如：
 * {@code {
 *     align: right,
 *     rich: {
 *         a: {
 *             // 没有设置 `align`，则 `align` 为 right
 *         }
 *     }
 * }}
 * @author shenke
 * @since 2018/4/25 上午9:54
 */
public enum Align {
    left,
    right,
    center
}
