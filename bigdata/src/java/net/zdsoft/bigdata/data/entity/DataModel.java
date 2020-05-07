package net.zdsoft.bigdata.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 数据模型
 * Created by wangdongdong on 2018/8/28 10:02.
 */
@Entity
@Table(name = "bg_model")
public class DataModel extends BaseEntity<String> {

    private String unitId;

    private String name;

    private String code;

    private String project;

    private String type;

    private String remark;

    private Integer orderId;

    private Integer orderType;

    private Integer datasetType;

    private Integer userDatasetSwitch;

    private Integer dateDimSwitch;

    private String dateDimTable;

    private String dateColumn;

    private String dbId;

    private Integer source;

    @Transient
    private boolean canEdit;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public Integer getDatasetType() {
        return datasetType;
    }

    public void setDatasetType(Integer datasetType) {
        this.datasetType = datasetType;
    }

    public Integer getUserDatasetSwitch() {
        return userDatasetSwitch;
    }

    public void setUserDatasetSwitch(Integer userDatasetSwitch) {
        this.userDatasetSwitch = userDatasetSwitch;
    }

    public Integer getDateDimSwitch() {
        return dateDimSwitch;
    }

    public void setDateDimSwitch(Integer dateDimSwitch) {
        this.dateDimSwitch = dateDimSwitch;
    }

    public String getDateDimTable() {
        return dateDimTable;
    }

    public void setDateDimTable(String dateDimTable) {
        this.dateDimTable = dateDimTable;
    }

    public String getDateColumn() {
        return dateColumn;
    }

    public void setDateColumn(String dateColumn) {
        this.dateColumn = dateColumn;
    }

    public String getDbId() {
        return dbId;
    }

    public void setDbId(String dbId) {
        this.dbId = dbId;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    @Override
    public String fetchCacheEntitName() {
        return "DataModel";
    }
}
