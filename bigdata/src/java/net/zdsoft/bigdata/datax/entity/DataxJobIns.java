package net.zdsoft.bigdata.datax.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wangdongdong on 2019/4/28 14:26.
 */
@Entity
@Table(name = "bg_datax_job_ins")
public class DataxJobIns extends BaseEntity<String> {

    private String jobId;

    private String parentId;

    private String name;

    private String remark;

    private Integer isIncrement;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastCommitTime;

    @Transient
    private DataxJobIns child;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public DataxJobIns getChild() {
        return child;
    }

    public void setChild(DataxJobIns child) {
        this.child = child;
    }

    public Integer getIsIncrement() {
        return isIncrement;
    }

    public void setIsIncrement(Integer isIncrement) {
        this.isIncrement = isIncrement;
    }

    public Date getLastCommitTime() {
        return lastCommitTime;
    }

    public void setLastCommitTime(Date lastCommitTime) {
        this.lastCommitTime = lastCommitTime;
    }

    @Override
    public String fetchCacheEntitName() {
        return "DataxJobInstance";
    }
}
