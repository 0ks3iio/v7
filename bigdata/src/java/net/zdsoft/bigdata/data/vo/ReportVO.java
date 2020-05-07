package net.zdsoft.bigdata.data.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * 报表查询VO
 * Created by wangdongdong on 2018/5/8.
 */
public class ReportVO extends ChartVO {

    /**
     * 数据源类型
     */
    private int dataSourceType;

    /**
     * 数据源Id 可能为ApiId也可能为databaseId 根据dataSourceType确定
     */
    private String dataSourceId;

    /**
     * 数据集或者SQL等
     */
    private String dataSet;

    /**
     * 报表模版id
     */
    private String templateId;

    private int autoUpdate;
    private long updateInterval;

    private String reportId;

    private Map<String, Object> paramMap = new HashMap<>();

    private String folderId;
    private Integer orderId;

    public int getDataSourceType() {
        return dataSourceType;
    }

    public ReportVO setDataSourceType(int dataSourceType) {
        this.dataSourceType = dataSourceType;
        return this;
    }

    public String getDataSourceId() {
        return dataSourceId;
    }

    public ReportVO setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
        return this;
    }

    public String getDataSet() {
        return dataSet;
    }

    public ReportVO setDataSet(String dataSet) {
        this.dataSet = dataSet;
        return this;
    }

    public String getTemplateId() {
        return templateId;
    }

    public ReportVO setTemplateId(String templateId) {
        this.templateId = templateId;
        return this;
    }

    public int getAutoUpdate() {
        return autoUpdate;
    }

    public ReportVO setAutoUpdate(int autoUpdate) {
        this.autoUpdate = autoUpdate;
        return this;
    }

    public long getUpdateInterval() {
        return updateInterval;
    }

    public ReportVO setUpdateInterval(long updateInterval) {
        this.updateInterval = updateInterval;
        return this;
    }

    public String getReportId() {
        return reportId;
    }

    public ReportVO setReportId(String reportId) {
        this.reportId = reportId;
        return this;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public ReportVO setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
        return this;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}
