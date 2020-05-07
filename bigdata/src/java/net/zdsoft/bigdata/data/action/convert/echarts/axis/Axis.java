package net.zdsoft.bigdata.data.action.convert.echarts.axis;

import net.zdsoft.bigdata.data.action.convert.echarts.AbstractData;
import net.zdsoft.bigdata.data.action.convert.echarts.Component;
import net.zdsoft.bigdata.data.action.convert.echarts.enu.AxisType;

/**
 * @author ke_shen@126.com
 * @since 2018/4/12 上午10:45
 */
@SuppressWarnings("unchecked")
public abstract class Axis<T extends Axis> extends AbstractData<T> implements Component<T> {

    private Boolean show;
    private AxisType type;
    /** 默认为空*/
    private String name;

    private Number min;
    private Number max;

    private AxisLine axisLine = AxisLine.empty();
    private AxisLabel axisLabel = AxisLabel.empty();
    private AxisTick axisTick ;
    private SplitLine splitLine;

    public T show(boolean show) {
        this.show = show;
        return (T) this;
    }

    public Boolean show() {
        return this.show;
    }

    public T type(AxisType type) {
        this.type = type;
        return (T) this;
    }

    public AxisType type() {
        return type;
    }

    public T name(String name) {
        this.name = name;
        return (T) this;
    }

    public T max(Number max) {
        this.max = max;
        return (T) this;
    }

    public T axisLine(AxisLine axisLine) {
        this.axisLine = axisLine;
        return (T) this;
    }

    public AxisLine axisLine() {
        if (axisLine == null) {
            this.axisLine = new AxisLine();
        }
        return this.axisLine;
    }

    public T axisLabel(AxisLabel axisLabel) {
        this.axisLabel = axisLabel;
        return (T) this;
    }

    public AxisLabel axisLabel() {
        if (this.axisLabel == null) {
            this.axisLabel = new AxisLabel();
        }
        return this.axisLabel;
    }

    public T axisTick(AxisTick axisTick) {
        this.axisTick = axisTick;
        return (T) this;
    }

    public AxisTick axisTick() {
        if (axisTick == null) {
            this.axisTick = new AxisTick();
        }
        return this.axisTick;
    }

    public T splitLine(SplitLine splitLine) {
        this.splitLine = splitLine;
        return (T) this;
    }

    public SplitLine splitLine() {
        if (this.splitLine == null) {
            this.splitLine = new SplitLine();
        }
        return this.splitLine;
    }

    public Number max() {
        return this.max;
    }

    public Number min() {
        return this.min;
    }

    public T min(Number min) {
        this.min = min;
        return (T) this;
    }

    public String name() {
        return this.name;
    }

    public Boolean getShow() {
        return show;
    }

    public AxisType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Number getMin() {
        return min;
    }

    public Number getMax() {
        return max;
    }

    public AxisLine getAxisLine() {
        return axisLine;
    }

    public AxisLabel getAxisLabel() {
        return axisLabel;
    }

    public AxisTick getAxisTick() {
        return axisTick;
    }

    public SplitLine getSplitLine() {
        return splitLine;
    }
}
