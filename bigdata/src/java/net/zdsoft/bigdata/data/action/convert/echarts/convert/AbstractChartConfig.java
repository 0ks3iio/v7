/**
 * FileName: AbstractChartConfig
 * Author:   shenke
 * Date:     2018/4/18 下午12:59
 * Descriptor:
 */
package net.zdsoft.bigdata.data.action.convert.echarts.convert;

/**
 * @author shenke
 * @since 2018/4/18 下午12:59
 */
public abstract class AbstractChartConfig implements IChart.ChartConfig {

    protected String name;
    protected int chartType;
    protected String chartId;
    protected String divId;

    public AbstractChartConfig() {
    }

    public AbstractChartConfig(String name, int chartType) {
        this.name = name;
        this.chartType = chartType;
    }

    public AbstractChartConfig(String name, int chartType, String chartId) {
        this.name = name;
        this.chartType = chartType;
        this.chartId = chartId;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getChartType() {
        return this.chartType;
    }

    @Override
    public String getChartId() {
        return this.chartId;
    }

    @Override
    public String getDivId() {
        return this.divId;
    }
}
