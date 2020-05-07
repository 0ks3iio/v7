package net.zdsoft.bigdata.data.entity;

import com.google.common.collect.Lists;
import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * 数据模型
 * Created by wangdongdong on 2018/8/28 10:34.
 */
@Entity
@Table(name = "bg_model_favorite")
public class DataModelFavorite extends BaseEntity<String> {

	private String unitId;

    private String userId;

    private String modelId;

    private Integer isShowChart;

    private Integer isShowReport;

    private String chartType;

    private Integer windowSize;
    
    private String favoriteName;

    private String querySql;

    private String queryChartSql;

    private String queryColumn;

    private String queryRow;

    private String queryIndex;

    private Integer orderId;

    private Integer orderType;

    private String remark;
    @Transient
    private String[] tags;
    @Transient
    private boolean canDelete;
    @Transient
    private String orderName;
    // 文件夹字段
    @Transient
    private String folderId;
    @Transient
    private List<DataModelFavoriteParam> dataModelFavoriteParams = Lists.newArrayList();

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public Integer getIsShowChart() {
        return isShowChart;
    }

    public void setIsShowChart(Integer isShowChart) {
        this.isShowChart = isShowChart;
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    public Integer getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(Integer windowSize) {
        this.windowSize = windowSize;
    }

    public String getFavoriteName() {
        return favoriteName;
    }

    public void setFavoriteName(String favoriteName) {
        this.favoriteName = favoriteName;
    }

    public String getQuerySql() {
        return querySql;
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }

    public String getQueryColumn() {
        return queryColumn;
    }

    public void setQueryColumn(String queryColumn) {
        this.queryColumn = queryColumn;
    }

    public String getQueryRow() {
        return queryRow;
    }

    public void setQueryRow(String queryRow) {
        this.queryRow = queryRow;
    }

    public String getQueryIndex() {
        return queryIndex;
    }

    public void setQueryIndex(String queryIndex) {
        this.queryIndex = queryIndex;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getQueryChartSql() {
        return queryChartSql;
    }

    public void setQueryChartSql(String queryChartSql) {
        this.queryChartSql = queryChartSql;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public List<DataModelFavoriteParam> getDataModelFavoriteParams() {
        return dataModelFavoriteParams;
    }

    public void setDataModelFavoriteParams(List<DataModelFavoriteParam> dataModelFavoriteParams) {
        this.dataModelFavoriteParams = dataModelFavoriteParams;
    }

    public Integer getIsShowReport() {
        return isShowReport;
    }

    public void setIsShowReport(Integer isShowReport) {
        this.isShowReport = isShowReport;
    }

    @Override
    public String fetchCacheEntitName() {
        return "DataModelFavorite";
    }
}
