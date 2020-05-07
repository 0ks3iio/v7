package net.zdsoft.bigdata.data.action.convert.echarts.convert;

import net.zdsoft.bigdata.data.action.convert.echarts.enu.AxisType;
import net.zdsoft.bigdata.data.manager.api.Result;

import org.apache.commons.lang3.StringUtils;

/**
 * 封装图表的配置信息，查询结果
 * @author ke_shen@126.com
 * @since 2018/4/11 下午5:26
 */
public class IOptionChart implements IChart<IChart.ChartConfig, Result> {

    private ChartConfig chartConfig;
    private Result queryResult;

    //扩展字段
    private AxisType xAxisType = AxisType.category;
    private AxisType yAxisType = AxisType.value;

    private String xMappingName = "x";
    private String yMappingName = "y";
    private String sMappingName = "s";

    public IOptionChart(ChartConfig chartConfig, Result queryResult) {
        this.chartConfig = chartConfig;
        this.queryResult = queryResult;
    }

    @Override
    public ChartConfig getConfig() {
        return this.chartConfig;
    }

    @Override
    public Result getQueryResult() {
        return this.queryResult;
    }

    @Override
    public String xMappingName() {
        return this.xMappingName;
    }

    @Override
    public String yMappingName() {
        return this.yMappingName;
    }

    @Override
    public String sMappingName() {
        return this.sMappingName;
    }

    @Override
    public AxisType xAxisType() {
        return this.xAxisType;
    }

    @Override
    public AxisType yAxisType() {
        return this.yAxisType;
    }

    public IOptionChart xAxisType(AxisType xAxisType) {
        this.xAxisType = xAxisType;
        return this;
    }

    public IOptionChart yAxisType(AxisType yAxisType) {
        this.yAxisType = yAxisType;
        return this;
    }

    /** sMappingName 将全部转换为小写 Oracle 对别名不区分大小写 */
    public IOptionChart sMappingName(String sMappingName) {
        this.sMappingName = StringUtils.lowerCase(sMappingName);
        return this;
    }

    /** xMappingName 将全部转换为小写 */
    public IOptionChart xMappingName(String xMappingName) {
        this.xMappingName = StringUtils.lowerCase(xMappingName);
        return this;
    }

    /** yMappingName 将全部转换为小写 */
    public IOptionChart yMappingName(String yMappingName) {
        this.yMappingName = StringUtils.lowerCase(yMappingName);
        return this;
    }
}
