/**
 * FileName: WrapChart.java
 * Author:   shenke
 * Date:     2018/6/28 上午9:11
 * Descriptor:
 */
package net.zdsoft.bigdata.data.echarts;

import java.util.Objects;

import net.zdsoft.bigdata.data.entity.Chart;
import net.zdsoft.bigdata.data.entity.ChartDetail;
import net.zdsoft.bigdata.data.manager.api.Result;

/**
 * @author shenke
 * @since 2018/6/28 上午9:11
 */
public class WrapChart {

    private String name;
    private int chartType;
    private String id;
    private Result result;

    private String mapType;

    public static WrapChart build(Chart chart, Result result) {
        WrapChart wrapChart = new WrapChart();
        wrapChart.chartType = chart.getChartType();
        wrapChart.id = chart.getId();
        wrapChart.name = chart.getName();
        wrapChart.result = result;
        wrapChart.mapType = chart.getMap();
        return wrapChart;
    }

    public static WrapChart build(ChartDetail chart, Result result, String mapType) {
        WrapChart wrapChart = new WrapChart();
        wrapChart.chartType = chart.getChartType();
        wrapChart.id = chart.getId();
        wrapChart.name = chart.getName();
        wrapChart.result = result;
        wrapChart.mapType = mapType;
        return wrapChart;
    }

    public String getMapType() {
        return mapType;
    }

    public void setMapType(String mapType) {
        this.mapType = mapType;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getChartType() {
        return chartType;
    }

    public void setChartType(int chartType) {
        this.chartType = chartType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WrapChart wrapChart = (WrapChart) o;
        return chartType == wrapChart.chartType &&
                Objects.equals(name, wrapChart.name) &&
                Objects.equals(id, wrapChart.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, chartType, id);
    }
}
