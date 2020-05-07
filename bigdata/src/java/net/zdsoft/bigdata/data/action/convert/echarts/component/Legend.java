package net.zdsoft.bigdata.data.action.convert.echarts.component;

import java.util.Arrays;
import java.util.LinkedHashSet;

import net.zdsoft.bigdata.data.action.convert.echarts.BaseComponent;
import net.zdsoft.bigdata.data.action.convert.echarts.Component;
import net.zdsoft.bigdata.data.action.convert.echarts.Data;
import net.zdsoft.bigdata.data.action.convert.echarts.enu.Align;
import net.zdsoft.bigdata.data.action.convert.echarts.enu.LegendAlign;
import net.zdsoft.bigdata.data.action.convert.echarts.enu.Orient;
import net.zdsoft.bigdata.data.action.convert.echarts.style.itemstyle.TextStyle;

/**
 * @author ke_shen@126.com
 * @since 2018/4/11 下午8:25
 */
public class Legend extends BaseComponent<Legend> implements Data<Legend>, Component {

    /**
     * 可以不实用set，即使存在相同名称的图例echarts也不会重复显示
     */
    private LinkedHashSet<Object> data;

    private Object left;
    private Object right;
    private Object top;
    private Object bottom;
    private Orient orient;
    private LegendAlign align;
    private TextStyle textStyle;

    public Legend() {
        this.left = Align.center;
        this.orient = Orient.horizontal;
        this.align = LegendAlign.auto;
    }

    public TextStyle textStyle() {
        if (this.textStyle == null) {
            this.textStyle = new TextStyle();
        }
        return this.textStyle;
    }

    public Legend left(Object left) {
        this.left = left;
        return this;
    }

    public Legend right(Object right) {
        this.right = right;
        return this;
    }

    public Legend bottom(Object bottom) {
        this.bottom = bottom;
        return this;
    }

    public Legend top(Object top) {
        this.top = top;
        return this;
    }

    public Legend orient(Orient orient) {
        this.orient = orient;
        return this;
    }

    public Legend align(LegendAlign align) {
        this.align = align;
        return this;
    }

    @Override
    public Legend data(Object... values) {
        if (values == null || values.length == 0) {
            return this;
        }
        this.data().addAll(Arrays.asList(values));
        return this;
    }

    public LinkedHashSet<Object> data() {
        if (this.data == null) {
            this.data = new LinkedHashSet<>();
        }
        return data;
    }

    public LinkedHashSet<Object> getData() {
        return data;
    }

    public Object getLeft() {
        return left;
    }

    public Object getRight() {
        return right;
    }

    public Object getBottom() {
        return bottom;
    }

    public Orient getOrient() {
        return orient;
    }

    public LegendAlign getAlign() {
        return align;
    }

    public Object getTop() {
        return top;
    }

    public TextStyle getTextStyle() {
        return textStyle;
    }
}
