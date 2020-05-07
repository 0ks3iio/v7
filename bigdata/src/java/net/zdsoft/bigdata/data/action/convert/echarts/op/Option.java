package net.zdsoft.bigdata.data.action.convert.echarts.op;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.zdsoft.bigdata.data.action.convert.echarts.EchartConstant;
import net.zdsoft.bigdata.data.action.convert.echarts.axis.Axis;
import net.zdsoft.bigdata.data.action.convert.echarts.component.Grid;
import net.zdsoft.bigdata.data.action.convert.echarts.component.Legend;
import net.zdsoft.bigdata.data.action.convert.echarts.component.Radar;
import net.zdsoft.bigdata.data.action.convert.echarts.component.Title;
import net.zdsoft.bigdata.data.action.convert.echarts.component.Tooltip;
import net.zdsoft.bigdata.data.action.convert.echarts.component.VisualMap;
import net.zdsoft.bigdata.data.action.convert.echarts.series.Series;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 终极组件（😄）
 * @author ke_shen@126.com
 * @since 2018/4/11 下午8:28
 */
public class Option {

    @FunctionalInterface
    public interface Loader<String, R> {

        R load(String name);
    }


    private Title title;
    private Grid grid;
    private Legend legend;
    private Tooltip tooltip;
    private List<Series> series;
    private List<Axis> xAxis;
    private List<Axis> yAxis;
    private Radar radar;
    /**
     * 调色盘颜色列表。如果系列没有设置颜色，则会依次循环从该列表中取颜色作为系列颜色
     * 数组
     */
    private Object color;
    private VisualMap visualMap;

    /** 非echarts图表的数据封装 */

    public Option() {
        this.color = EchartConstant.OPTION_COLOR;
        this.grid = Grid.empty();
    }


    public VisualMap visualMap() {
        if (this.visualMap == null) {
            this.visualMap = new VisualMap();
        }
        return this.visualMap;
    }

    public Option tooltip(Tooltip tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    public Tooltip tooltip() {
        if (this.tooltip == null) {
            this.tooltip = new Tooltip();
            this.tooltip.option(this);
        }
        return this.tooltip;
    }


    public Option title(Title title) {
        this.title = title;
        return this;
    }

    public Title title() {
        if (this.title == null) {
            this.title = new Title();
            this.title.option(this);
        }
        return this.title;
    }

    public Grid grid() {
        if (this.grid == null) {
            this.grid = new Grid();
        }
        return this.grid;
    }

    public Option grid(Grid grid) {
        this.grid = grid;
        return this;
    }

    public Option title(String text) {
        this.title().text(text);
        return this;
    }

    public Option legend(Legend legend) {
        this.legend = legend;
        return this;
    }

    public Legend legend() {
        if (this.legend == null) {
            this.legend = new Legend();
            this.legend.option(this);
        }
        return this.legend;
    }

    public Series series(String name, Loader<String, Series> loader) {
        for (Series s : series()) {
            if (name.equals(s.name())) {
                return s;
            }
        }
        Series series = loader.load(name);
        series.option(this);
        series(series);
        return series;
    }

    public Option series(Series ...series) {
        this.series().addAll(Arrays.asList(series));
        return this;
    }

    public Option series(List<Series> series) {
        this.series = series;
        return this;
    }

    public List<Series> series() {
        if (this.series == null) {
            this.series = new ArrayList<>();
        }
        return this.series;
    }

    public Axis xAxis(String name, Loader<String, Axis> loader) {
        for (Axis axis : xAxis()) {
            if (name.equals(axis.name())) {
                return axis;
            }
        }
        Axis axis = loader.load(name);
        axis.option(this);
        xAxis(axis);
        return axis;
    }

    public Option xAxis(Axis...xAxis) {
        if (xAxis == null || xAxis.length == 0) {
            return this;
        }
        if (this.xAxis().size() == 2) {
            throw new RuntimeException("xAxis已经存在2个，无法继续添加!");
        }
        if (this.xAxis().size() + xAxis.length > 2) {
            throw new RuntimeException("xAxis最多只能存在两个");
        }
        this.xAxis().addAll(Arrays.asList(xAxis));
        return this;
    }

    public Axis yAxis(String name, Loader<String, Axis> loader) {
        for (Axis yAxis : yAxis()) {
            if (yAxis.name().equals(name)) {
                return yAxis;
            }
        }
        Axis yAxis = loader.load(name);
        this.yAxis(yAxis);
        return yAxis;
    }

    public List<Axis> xAxis() {
        if (this.xAxis == null) {
            this.xAxis = new ArrayList<>(2);
        }
        return this.xAxis;
    }

    public Option xAxis(List<Axis> xAxis) {
        this.xAxis = xAxis;
        return this;
    }

    public Option yAxis(Axis ...yAxis) {
        if (yAxis == null || yAxis.length == 0) {
            return this;
        }
        if (this.yAxis().size() == 2) {
            throw new RuntimeException("yAxis已经存在2个，无法继续添加!");
        }
        if (this.yAxis().size() + yAxis.length > 2) {
            throw new RuntimeException("yAxis最多只能存在两个");
        }
        this.yAxis().addAll(Arrays.asList(yAxis));
        return this;
    }

    public List<Axis> yAxis() {
        if (this.yAxis == null) {
            this.yAxis = new ArrayList<>(2);
        }
        return this.yAxis;
    }

    public Option yAxis(List<Axis> yAxis) {
        this.yAxis = yAxis;
        return this;
    }

    public Option color(Object color) {
        this.color = color;
        return this;
    }

    public Radar radar() {
        if (radar == null) {
            radar = new Radar();
            radar.option(this);
        }
        return this.radar;
    }


    public Title getTitle() {
        return title;
    }

    public Legend getLegend() {
        return legend;
    }

    public List<Series> getSeries() {
        return series;
    }

    public List<Axis> getxAxis() {
        return xAxis;
    }

    public List<Axis> getyAxis() {
        return yAxis;
    }

    public Tooltip getTooltip() {
        return tooltip;
    }

    public Object getColor() {
        return color;
    }

    public Grid getGrid() {
        return grid;
    }

    public VisualMap getVisualMap() {
        return visualMap;
    }

    public Radar getRadar() {
        return radar;
    }

    /** 重写toString 将Option转为标准JSON格式，空值 空集合都将忽略 */
    @Override
    public String toString() {
        return toJSONString();
    }

    public String toJSONString() {
        return JSONObject.toJSONString(this, (PropertyFilter) (owner, propertyName, value) -> {
            if (value == null ) {
                return false;
            }
            if (value instanceof String) {
                return StringUtils.isNotBlank(value.toString());
            }
            if (value instanceof Collection) {
                if (((Collection)value).isEmpty()) {
                    return false;
                }
                return ((Collection)value).stream().filter(e->e != null && StringUtils.isNotBlank(e.toString())).count() != 0;
            }
            return true;
        }, SerializerFeature.WriteEnumUsingToString);
    }
}
