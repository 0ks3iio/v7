package net.zdsoft.bigdata.datax.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wangdongdong on 2019/4/28 14:11.
 */
@Entity
@Table(name = "bg_datax_job")
public class DataxJob extends BaseEntity<String> {

    private String name;

    private Integer isSchedule;

    private String scheduleParam;

    private String remark;

    private String nodeId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;

    @Transient
    private DataxJobInsLog dataxJobInsLog;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIsSchedule() {
        return isSchedule;
    }

    public void setIsSchedule(Integer isSchedule) {
        this.isSchedule = isSchedule;
    }

    public String getScheduleParam() {
        return scheduleParam;
    }

    public void setScheduleParam(String scheduleParam) {
        this.scheduleParam = scheduleParam;
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

    public DataxJobInsLog getDataxJobInsLog() {
        return dataxJobInsLog;
    }

    public void setDataxJobInsLog(DataxJobInsLog dataxJobInsLog) {
        this.dataxJobInsLog = dataxJobInsLog;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public String fetchCacheEntitName() {
        return "DataxJob";
    }
}
