/**
 * FileName: AxisLabel
 * Author:   shenke
 * Date:     2018/4/25 下午4:10
 * Descriptor:
 */
package net.zdsoft.bigdata.data.action.convert.echarts.axis;

import net.zdsoft.bigdata.data.action.convert.echarts.style.itemstyle.TextStyle;

/**
 * @author shenke
 * @since 2018/4/25 下午4:10
 */
public class AxisLabel {

    public static AxisLabel empty() {
        return new AxisLabel().show(true).interval("auto").rotate(0);
    }

    private Boolean show = Boolean.TRUE;
    /**
     * 显示间隔，仅在类目轴有效
     */
    private Object interval;
    private Integer rotate;
    //这个属性不知道为啥echarts不可用
    private Object color;
    private TextStyle textStyle;

    public TextStyle textStyle() {
        if (this.textStyle == null) {
            this.textStyle = new TextStyle();
        }
        return this.textStyle;
    }

    public AxisLabel textStyle(TextStyle textStyle) {
        this.textStyle = textStyle;
        return this;
    }

    public AxisLabel show(Boolean show) {
        this.show = show;
        return this;
    }

    public AxisLabel interval(Object interval) {
        this.interval = interval;
        return this;
    }

    public AxisLabel rotate(Integer rotate) {
        this.rotate = rotate;
        return this;
    }

    public AxisLabel color(Object color) {
        this.color = color;
        return this;
    }

    public Boolean getShow() {
        return show;
    }

    public Object getInterval() {
        return interval;
    }

    public Integer getRotate() {
        return rotate;
    }

    public Object getColor() {
        return color;
    }

    public TextStyle getTextStyle() {
        return textStyle;
    }
}
