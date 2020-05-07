package net.zdsoft.bigdata.datax.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by wangdongdong on 2019/4/28 14:51.
 */
@Entity
@Table(name = "bg_datax_job_ins_log")
public class DataxJobInsLog extends BaseEntity<String> {

    private String jobId;

    private String jobInstanceId;

    private String dataSourceId;

    private String dataSourceName;

    private String dataTargetId;

    private String dataTargetName;

    private String jobName;

    private String jobRemark;

    private String jobInsName;

    private String jobInsRemark;

    private Integer status;

    private Integer result;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    private Long duration;

    private Long totalCount;

    private Long successCount;

    private Long errorCount;

    private String averageFlow;

    private String writeSpeed;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobInstanceId() {
        return jobInstanceId;
    }

    public void setJobInstanceId(String jobInstanceId) {
        this.jobInstanceId = jobInstanceId;
    }

    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public String getDataTargetId() {
        return dataTargetId;
    }

    public void setDataTargetId(String dataTargetId) {
        this.dataTargetId = dataTargetId;
    }

    public String getDataTargetName() {
        return dataTargetName;
    }

    public void setDataTargetName(String dataTargetName) {
        this.dataTargetName = dataTargetName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobRemark() {
        return jobRemark;
    }

    public void setJobRemark(String jobRemark) {
        this.jobRemark = jobRemark;
    }

    public String getJobInsName() {
        return jobInsName;
    }

    public void setJobInsName(String jobInsName) {
        this.jobInsName = jobInsName;
    }

    public String getJobInsRemark() {
        return jobInsRemark;
    }

    public void setJobInsRemark(String jobInsRemark) {
        this.jobInsRemark = jobInsRemark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Long getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Long successCount) {
        this.successCount = successCount;
    }

    public Long getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Long errorCount) {
        this.errorCount = errorCount;
    }

    public String getAverageFlow() {
        return averageFlow;
    }

    public void setAverageFlow(String averageFlow) {
        this.averageFlow = averageFlow;
    }

    public String getWriteSpeed() {
        return writeSpeed;
    }

    public void setWriteSpeed(String writeSpeed) {
        this.writeSpeed = writeSpeed;
    }

    @Override
    public String fetchCacheEntitName() {
        return "DataxJobInsLog";
    }
}
