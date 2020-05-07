package net.zdsoft.bigdata.data.action.convert.echarts.series;

import net.zdsoft.bigdata.data.action.convert.echarts.AbstractData;
import net.zdsoft.bigdata.data.action.convert.echarts.enu.SeriesType;
import net.zdsoft.bigdata.data.action.convert.echarts.style.Label;

/**
 * @author ke_shen@126.com
 * @since 2018/4/11 下午8:18
 */
@SuppressWarnings("unchecked")
public abstract class Series<T extends Series> extends AbstractData<T> {

    private String name;
    private SeriesType type;
    private String stack;
    private Label label;


    public Series() {
        this.label = Label.empty();
    }

    public Series(String name) {
        this();
        this.name = name;
    }

    public T name(String name) {
        this.name = name;
        return (T) this;
    }

    public T type(SeriesType type) {
        this.type = type;
        return (T) this;
    }

    public T stack(String stack) {
        this.stack = stack;
        return (T) this;
    }

    public T label(Label label) {
        this.label = label;
        return (T) this;
    }

    public Label label() {
        if (this.label == null) {
            this.label = new Label();
        }
        return this.label;
    }

    public String name() {
        return this.name;
    }

    public SeriesType type() {
        return this.type;
    }

    public String getName() {
        return name;
    }

    public SeriesType getType() {
        return type;
    }

    public String getStack() {
        return stack;
    }

    public Label getLabel() {
        return label;
    }
}
