package net.zdsoft.framework.echarts.entity;

import java.util.List;

import net.zdsoft.framework.echarts.entity.sub.DataZoom;
import net.zdsoft.framework.echarts.entity.sub.Legend;
import net.zdsoft.framework.echarts.entity.sub.Series;
import net.zdsoft.framework.echarts.entity.sub.Title;
import net.zdsoft.framework.echarts.entity.sub.Toolbox;
import net.zdsoft.framework.echarts.entity.sub.Tooltip;
import net.zdsoft.framework.echarts.entity.sub.XAxis;
import net.zdsoft.framework.echarts.entity.sub.YAxis;

public class Bar {

    private Title title;
    private Legend legend;
    private XAxis xAxis;
    private List<YAxis> yAxis;
    private List<Series> series;
    private Tooltip tooltip;
    private boolean calculable = false;
    private DataZoom dataZoom;
    private Toolbox toolbox;

    



    

   

    

    

    

   

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public Legend getLegend() {
        return legend;
    }

    public void setLegend(Legend legend) {
        this.legend = legend;
    }

    public XAxis getxAxis() {
        return xAxis;
    }

    public void setxAxis(XAxis xAxis) {
        this.xAxis = xAxis;
    }

    public List<YAxis> getyAxis() {
        return yAxis;
    }

    public void setyAxis(List<YAxis> yAxis) {
        this.yAxis = yAxis;
    }

    public List<Series> getSeries() {
        return series;
    }

    public void setSeries(List<Series> series) {
        this.series = series;
    }

    public Tooltip getTooltip() {
        return tooltip;
    }

    public void setTooltip(Tooltip tooltip) {
        this.tooltip = tooltip;
    }

    public boolean isCalculable() {
        return calculable;
    }

    public void setCalculable(boolean calculable) {
        this.calculable = calculable;
    }

    public DataZoom getDataZoom() {
        return dataZoom;
    }

    public void setDataZoom(DataZoom dataZoom) {
        this.dataZoom = dataZoom;
    }

    public Toolbox getToolbox() {
        return toolbox;
    }

    public void setToolbox(Toolbox toolbox) {
        this.toolbox = toolbox;
    }

}
