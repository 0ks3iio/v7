/**
 * FileName: ChartEditVO
 * Author:   shenke
 * Date:     2018/4/18 下午1:14
 * Descriptor:
 */
package net.zdsoft.bigdata.data.vo;

import net.zdsoft.bigdata.data.DataSourceType;
import net.zdsoft.bigdata.data.entity.Chart;

import org.springframework.data.annotation.Transient;

/**
 * @author shenke
 * @since 2018/4/18 下午1:14
 */
public class ChartEditVO extends ChartVO {

    private int datasourceType;
    /**
     * 数据源Id 可能为ApiId也可能为databaseId 根据datasouceType确定
     */
    private String datasourceId;
    private int orderType;
    /**
     * 图表类型的具体值
     */
    private int type;
    private int chartType;
    /**
     * 数据集或者SQL等
     */
    private String dataSet;

    private String unitId;
    private int autoUpdate;
    private Integer updateInterval;
    /** 是否可编辑 */
    private boolean edit;

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public int getAutoUpdate() {
        return autoUpdate;
    }

    public void setAutoUpdate(int autoUpdate) {
        this.autoUpdate = autoUpdate;
    }

    public long getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(Integer updateInterval) {
        this.updateInterval = updateInterval;
    }

    public int getDatasourceType() {
        return datasourceType;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public ChartEditVO setDatasourceType(int datasourceType) {
        this.datasourceType = datasourceType;
        return this;
    }

    public String getDatasourceId() {
        return datasourceId;
    }

    public ChartEditVO setDatasourceId(String datasourceId) {
        this.datasourceId = datasourceId;
        return this;
    }

    public int getType() {
        return type;
    }

    public ChartEditVO setType(int type) {
        this.type = type;
        return this;
    }

    public String getDataSet() {
        return dataSet;
    }

    public ChartEditVO setDataSet(String dataSet) {
        this.dataSet = dataSet;
        return this;
    }

    public int getChartType() {
        return chartType;
    }

    public void setChartType(int chartType) {
        this.chartType = chartType;
    }

    @Override
    public String toString() {
        return super.toString() + "\n" + "ChartEditVO{" +
                "datasourceType=" + datasourceType +
                ", datasourceId='" + datasourceId + '\'' +
                ", type=" + type +
                ", dataSet='" + dataSet + '\'' +
                ", unitId='" + unitId + '\'' +
                ", autoUpdate=" + autoUpdate +
                ", updateInterval=" + updateInterval +
                '}';
    }

    @Transient
    public Chart convert() {
        Chart chart = new Chart();
        chart.setId(this.getId());
        chart.setName(this.getName());
        chart.setDataSourceType(datasourceType);
        //int parseChartType = ChartTypeConvert.parseChartType(getChartTypeName());
        //if (parseChartType != -1) {
        //    chart.setChartType(parseChartType);
        //}
        if (type == 0 && chartType != 0) {
            type = chartType;
        }
        chart.setChartType(type);
        if (datasourceType == DataSourceType.DB.getValue()) {
            chart.setDatabaseId(datasourceId);
        } else if (datasourceType == DataSourceType.API.getValue()) {
            chart.setApiId(datasourceId);
        }
        chart.setDataSet(dataSet);
        chart.setUnitId(unitId);
        chart.setAutoUpdate(autoUpdate);
        chart.setUpdateInterval(updateInterval);
        chart.setOrderType(orderType);
        return chart;
    }
}
