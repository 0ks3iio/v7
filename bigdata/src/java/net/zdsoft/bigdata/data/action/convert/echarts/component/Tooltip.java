/**
 * FileName: Tooltip
 * Author:   shenke
 * Date:     2018/4/19 下午4:27
 * Descriptor:
 */
package net.zdsoft.bigdata.data.action.convert.echarts.component;

import net.zdsoft.bigdata.data.action.convert.echarts.BaseComponent;
import net.zdsoft.bigdata.data.action.convert.echarts.enu.AxisPointerType;
import net.zdsoft.bigdata.data.action.convert.echarts.enu.Trigger;

/**
 * @author shenke
 * @since 2018/4/19 下午4:27
 */
public class Tooltip extends BaseComponent<Tooltip> {

    public static final String PIE_FORMATTER = "{a} <br/>{b} : {c} ({d}%)";

    /**
     * 提示框浮层内容格式器，支持字符串模板和回调函数两种形式。
     *
     * 1, 字符串模板
     *
     * 模板变量有 {a}, {b}，{c}，{d}，{e}，分别表示系列名，数据名，数据值等。 在 trigger 为 'axis' 的时候，会有多个系列的数据，此时可以通过 {a0}, {a1}, {a2} 这种后面加索引的方式表示系列的索引。 不同图表类型下的 {a}，{b}，{c}，{d} 含义不一样。 其中变量{a}, {b}, {c}, {d}在不同图表类型下代表数据含义为：
     *
     * 折线（区域）图、柱状（条形）图、K线图 : {a}（系列名称），{b}（类目值），{c}（数值）, {d}（无）
     *
     * 散点图（气泡）图 : {a}（系列名称），{b}（数据名称），{c}（数值数组）, {d}（无）
     *
     * 地图 : {a}（系列名称），{b}（区域名称），{c}（合并数值）, {d}（无）
     *
     * 饼图、仪表盘、漏斗图: {a}（系列名称），{b}（数据项名称），{c}（数值）, {d}（百分比）
     *
     * 更多其它图表模板变量的含义可以见相应的图表的 label.normal.formatter 配置项。
     *
     * 示例：
     *
     * formatter: '{b0}: {c0}<br />{b1}: {c1}'
     */
    private String formatter;
    /**
     * 触发类型。
     *
     * 可选：
     *
     * 'item'
     *
     * 数据项图形触发，主要在散点图，饼图等无类目轴的图表中使用。
     *
     * 'axis'
     *
     * 坐标轴触发，主要在柱状图，折线图等会使用类目轴的图表中使用。
     *
     * 在 ECharts 2.x 中只支持类目轴上使用 axis trigger，在 ECharts 3 中支持在直角坐标系和极坐标系上的所有类型的轴。并且可以通过 axisPointer.axis 指定坐标轴。
     *
     * 'none'
     *
     * 什么都不触发
     */
    private Trigger trigger;
    private AxisPointer axisPointer = AxisPointer.empty();

    public Tooltip axisPointer(AxisPointer axisPointer) {
        this.axisPointer = axisPointer;
        return this;
    }

    public Tooltip formatter(String formatter) {
        this.formatter = formatter;
        return this;
    }

    public String formatter() {
        return this.formatter;
    }

    public Tooltip trigger(Trigger trigger) {
        this.trigger = trigger;
        return this;
    }

    public Trigger trigger() {
        return this.trigger;
    }

    public String getFormatter() {
        return formatter;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public AxisPointer getAxisPointer() {
        return axisPointer;
    }

    /** zu坐标轴指示器 */
    public static class AxisPointer {

        public static AxisPointer empty() {
            return new AxisPointer().type(AxisPointerType.line);
        }

        private AxisPointerType type;

        public AxisPointer type(AxisPointerType axisPointerType) {
            this.type = axisPointerType;
            return this;
        }

        public AxisPointerType getType() {
            return type;
        }
    }
}
