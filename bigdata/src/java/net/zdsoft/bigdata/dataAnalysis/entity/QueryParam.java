package net.zdsoft.bigdata.dataAnalysis.entity;

import java.util.Optional;

/**
 * Created by wangdongdong on 2019/6/19 17:48.
 */
public class QueryParam {

    // 模型id
    private String modelId;
    // 行维度
    private String[] dimensionRowParam;
    // 列维度
    private String[] dimensionColumnParam;
    // 指标
    private String[] measureParam;
    // 筛选参数
    private String filterDataMap;
    // 是否包含表头
    private boolean isContainHeader;
    // 排序参数
    private String orderDataMap;
    // 下钻维度
    private String childDimensionId;
    // 下钻维度和下层维度
    private String[] childDimensionIdArray;
    // 下钻维度上级参数
    private String childDimensionFilterMap;
    // 下钻位置
    private String childPosition;
    // 行下钻专用过滤数据
    private String rowFilterDataMap;
    // 预警数据
    private String warningDataMap;

    private int rowSize;

    private int columnSize;

    public String getModelId() {
        return modelId;
    }

    public QueryParam setModelId(String modelId) {
        this.modelId = modelId;
        return this;
    }

    public String[] getDimensionRowParam() {
        return dimensionRowParam;
    }

    public QueryParam setDimensionRowParam(String[] dimensionRowParam) {
        this.dimensionRowParam = Optional.ofNullable(dimensionRowParam).orElse(new String[]{});
        return this;
    }

    public String[] getDimensionColumnParam() {
        return dimensionColumnParam;
    }

    public QueryParam setDimensionColumnParam(String[] dimensionColumnParam) {
        this.dimensionColumnParam = Optional.ofNullable(dimensionColumnParam).orElse(new String[]{});
        return this;
    }

    public String[] getMeasureParam() {
        return measureParam;
    }

    public QueryParam setMeasureParam(String[] measureParam) {
        this.measureParam = Optional.ofNullable(measureParam).orElse(new String[]{});
        return this;
    }

    public String getFilterDataMap() {
        return filterDataMap;
    }

    public QueryParam setFilterDataMap(String filterDataMap) {
        this.filterDataMap = filterDataMap;
        return this;
    }

    public boolean isContainHeader() {
        return isContainHeader;
    }

    public QueryParam setContainHeader(boolean containHeader) {
        isContainHeader = containHeader;
        return this;
    }

    public String getOrderDataMap() {
        return orderDataMap;
    }

    public QueryParam setOrderDataMap(String orderDataMap) {
        this.orderDataMap = orderDataMap;
        return this;
    }

    public int getRowSize() {
        return rowSize;
    }

    public QueryParam setRowSize(int rowSize) {
        this.rowSize = rowSize;
        return this;
    }

    public int getColumnSize() {
        return columnSize;
    }

    public QueryParam setColumnSize(int columnSize) {
        this.columnSize = columnSize;
        return this;
    }

    public String getChildDimensionId() {
        return childDimensionId;
    }

    public QueryParam setChildDimensionId(String childDimensionId) {
        this.childDimensionId = childDimensionId;
        return this;
    }

    public String getChildDimensionFilterMap() {
        return childDimensionFilterMap;
    }

    public QueryParam setChildDimensionFilterMap(String childDimensionFilterMap) {
        this.childDimensionFilterMap = childDimensionFilterMap;
        return this;
    }

    public String getChildPosition() {
        return childPosition;
    }

    public QueryParam setChildPosition(String childPosition) {
        this.childPosition = childPosition;
        return this;
    }

    public String[] getChildDimensionIdArray() {
        return childDimensionIdArray;
    }

    public QueryParam setChildDimensionIdArray(String[] childDimensionIdArray) {
        this.childDimensionIdArray = childDimensionIdArray;
        return this;
    }

    public String getRowFilterDataMap() {
        return rowFilterDataMap;
    }

    public QueryParam setRowFilterDataMap(String rowFilterDataMap) {
        this.rowFilterDataMap = rowFilterDataMap;
        return this;
    }

    public String getWarningDataMap() {
        return warningDataMap;
    }

    public QueryParam setWarningDataMap(String warningDataMap) {
        this.warningDataMap = warningDataMap;
        return this;
    }
}
