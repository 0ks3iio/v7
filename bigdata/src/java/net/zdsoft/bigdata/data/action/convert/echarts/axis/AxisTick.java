/**
 * FileName: AxisTick
 * Author:   shenke
 * Date:     2018/4/25 下午4:36
 * Descriptor:
 */
package net.zdsoft.bigdata.data.action.convert.echarts.axis;

/**
 * @author shenke
 * @since 2018/4/25 下午4:36
 */
public class AxisTick {

    public static AxisTick empty() {
        return new AxisTick().show(true);
    }

    private Boolean show;
    private Boolean alignWithLabel;

    public AxisTick alignWithLabel(Boolean alignWithLabel) {
        this.alignWithLabel = alignWithLabel;
        return this;
    }

    public AxisTick show(Boolean show) {
        this.show = show;
        return this;
    }

    public Boolean getAlignWithLabel() {
        return alignWithLabel;
    }

    public Boolean getShow() {
        return show;
    }
}
