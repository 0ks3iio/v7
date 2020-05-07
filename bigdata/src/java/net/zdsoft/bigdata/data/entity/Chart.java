package net.zdsoft.bigdata.data.entity;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import net.zdsoft.bigdata.data.utils.JacksonDateDeserializer;
import net.zdsoft.framework.entity.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * 基本图表和大屏明细表基本一致但是不同
 * @author ke_shen@126.com
 * @since 2018/4/17 下午3:42
 */
@Entity
@Table(name = "bg_chart")
public class Chart extends BaseEntity<String> {

    private String unitId;
    private String name;
    private int chartType;
    @Column(name = "data_source")
    private int dataSourceType;
    private String databaseId;
    private String apiId;
    private String dataSet;
    @Column(name = "is_auto_update")
    private int autoUpdate;
    @Column(name = "time_interval")
    private Integer updateInterval;
    /**
     * 报表类型 1 基本报表 2 大屏
     * @see net.zdsoft.bigdata.data.ChartBusinessType
     */
    private Integer businessType;
    @JsonDeserialize(using = JacksonDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationTime;
    @JsonDeserialize(using = JacksonDateDeserializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;
    /** 订阅类型 */
    private int orderType;

    private String thumbnailPath;
    private String templateId;
    private String map;

    private Short isForCockpit;

    private String parameters;

    @Transient
    private Object optionExpose;
    @Transient
    private List<String> tags;
    @Transient
    private List<String> orderUnits;
    @Transient
    private List<String> orderTeachers;
    @Transient
    private String parent;
    @Transient
    private String parentParent;

    // 文件夹字段
    @Transient
    private String folderId;
    @Transient
    private Integer orderId;
    @Transient
    private String operatorId;

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getOrderUnits() {
        return orderUnits;
    }

    public void setOrderUnits(List<String> orderUnits) {
        this.orderUnits = orderUnits;
    }

    public List<String> getOrderTeachers() {
        return orderTeachers;
    }

    public void setOrderTeachers(List<String> orderTeachers) {
        this.orderTeachers = orderTeachers;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    @Override
    public String fetchCacheEntitName() {
        return "chart";
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
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

    public int getDataSourceType() {
        return dataSourceType;
    }

    public void setDataSourceType(int dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public String getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(String databaseId) {
        this.databaseId = databaseId;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public int getAutoUpdate() {
        return autoUpdate;
    }

    public void setAutoUpdate(int autoUpdate) {
        this.autoUpdate = autoUpdate;
    }

    public Integer getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(Integer updateInterval) {
        this.updateInterval = updateInterval;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public Short getIsForCockpit() {
        return isForCockpit;
    }

    public void setIsForCockpit(Short isForCockpit) {
        this.isForCockpit = isForCockpit;
    }

    public Object getOptionExpose() {
        return optionExpose;
    }

    public void setOptionExpose(Object optionExpose) {
        this.optionExpose = optionExpose;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "Chart{" +
                "unitId='" + unitId + '\'' +
                ", name='" + name + '\'' +
                ", chartType=" + chartType +
                ", dataSourceType=" + dataSourceType +
                ", databaseId='" + databaseId + '\'' +
                ", apiId='" + apiId + '\'' +
                ", dataSet='" + dataSet + '\'' +
                ", autoUpdate=" + autoUpdate +
                ", updateInterval=" + updateInterval +
                ", businessType=" + businessType +
                ", creationTime=" + creationTime +
                ", modifyTime=" + modifyTime +
                ", thumbnailPath='" + thumbnailPath + '\'' +
                ", templateId='" + templateId + '\'' +
                '}';
    }

    public String toJSONString() {
        return JSONObject.toJSONString(this);
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getParentParent() {
        return parentParent;
    }

    public void setParentParent(String parentParent) {
        this.parentParent = parentParent;
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

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }
}
