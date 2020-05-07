package net.zdsoft.bigdata.extend.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by wangdongdong on 2018/7/12 17:00.
 */
@Entity
@Table(name = "bg_warning_result")
public class WarningResult extends BaseEntity<String> {

    private String unitId;

    private String projectId;

    private String batchId;

    private String tips;

    private String result;

    private String remark;

    private String callbackJson;

    private Date warnDate;

    private Short isRead;

    private Integer warnResultType;

    private Integer status;

    private String businessKey;

    @Override
    public String fetchCacheEntitName() {
        return "warningResult";
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCallbackJson() {
        return callbackJson;
    }

    public void setCallbackJson(String callbackJson) {
        this.callbackJson = callbackJson;
    }

    public Date getWarnDate() {
        return warnDate;
    }

    public void setWarnDate(Date warnDate) {
        this.warnDate = warnDate;
    }

    public Short getIsRead() {
        return isRead;
    }

    public void setIsRead(Short isRead) {
        this.isRead = isRead;
    }

    public Integer getWarnResultType() {
        return warnResultType;
    }

    public void setWarnResultType(Integer warnResultType) {
        this.warnResultType = warnResultType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }
}
