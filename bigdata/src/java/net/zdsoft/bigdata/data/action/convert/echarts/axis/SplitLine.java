/**
 * FileName: SplitLine
 * Author:   shenke
 * Date:     2018/4/25 下午7:52
 * Descriptor:
 */
package net.zdsoft.bigdata.data.action.convert.echarts.axis;

/**
 * 坐标轴的网格线
 * @author shenke
 * @since 2018/4/25 下午7:52
 */
public class SplitLine {

    private Boolean show;

    public SplitLine show(Boolean show) {
        this.show = show;
        return this;
    }

    public Boolean getShow() {
        return show;
    }
}
