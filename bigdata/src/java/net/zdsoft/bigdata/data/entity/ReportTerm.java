package net.zdsoft.bigdata.data.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * Created by wangdongdong on 2018/5/25 9:51.
 */
@Entity
@Table(name = "bg_report_term")
public class ReportTerm extends BaseEntity<String> {

    private String reportId;

    private String termName;

    private String termKey;

    private String termColumn;

    private String dataSet;

    private String cascadeTermId;

    private Short isFinalTerm;

    private Short isQuery;

    private Short orderId;

    @Column(name = "data_source")
    private Integer dataSourceType;

    private String termDatabaseId;

    private String termApiId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public String getTermKey() {
        return termKey;
    }

    public void setTermKey(String termKey) {
        this.termKey = termKey;
    }

    public String getTermColumn() {
        return termColumn;
    }

    public void setTermColumn(String termColumn) {
        this.termColumn = termColumn;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public String getCascadeTermId() {
        return cascadeTermId;
    }

    public void setCascadeTermId(String cascadeTermId) {
        this.cascadeTermId = cascadeTermId;
    }

    public Short getIsFinalTerm() {
        return isFinalTerm;
    }

    public void setIsFinalTerm(Short isFinalTerm) {
        this.isFinalTerm = isFinalTerm;
    }

    public Short getIsQuery() {
        return isQuery;
    }

    public void setIsQuery(Short isQuery) {
        this.isQuery = isQuery;
    }

    public Short getOrderId() {
        return orderId;
    }

    public void setOrderId(Short orderId) {
        this.orderId = orderId;
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

    public Integer getDataSourceType() {
        return dataSourceType;
    }

    public void setDataSourceType(Integer dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public String getTermDatabaseId() {
        return termDatabaseId;
    }

    public void setTermDatabaseId(String termDatabaseId) {
        this.termDatabaseId = termDatabaseId;
    }

    public String getTermApiId() {
        return termApiId;
    }

    public void setTermApiId(String termApiId) {
        this.termApiId = termApiId;
    }

    @Override
    public String fetchCacheEntitName() {
        return "reportTerm";
    }
}
