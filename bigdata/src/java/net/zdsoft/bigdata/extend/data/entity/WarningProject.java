package net.zdsoft.bigdata.extend.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wangdongdong on 2018/7/12 17:00.
 */
@Entity
@Table(name = "bg_warning_project")
public class WarningProject extends BaseEntity<String> {

    private String projectName;

    private Date startTime;

    private Short isAllTime;

    private Date endTime;

    private Integer warnTimes;

    private String scheduleParam;

    private String callbackApis;

    private Date lastWarnDate;

    private String unitId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;

    private Integer warnLevel;

    private Integer warnResultType;

    private Integer effectiveDay;

    private Integer feedbackType;

    private String jobId;

    @Transient
    private String userIds;

    @Override
    public String fetchCacheEntitName() {
        return "warningProject";
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Short getIsAllTime() {
        return isAllTime;
    }

    public void setIsAllTime(Short isAllTime) {
        this.isAllTime = isAllTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getWarnTimes() {
        return warnTimes;
    }

    public void setWarnTimes(Integer warnTimes) {
        this.warnTimes = warnTimes;
    }

    public String getScheduleParam() {
        return scheduleParam;
    }

    public void setScheduleParam(String scheduleParam) {
        this.scheduleParam = scheduleParam;
    }

    public String getCallbackApis() {
        return callbackApis;
    }

    public void setCallbackApis(String callbackApis) {
        this.callbackApis = callbackApis;
    }

    public Date getLastWarnDate() {
        return lastWarnDate;
    }

    public void setLastWarnDate(Date lastWarnDate) {
        this.lastWarnDate = lastWarnDate;
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

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public Integer getWarnLevel() {
        return warnLevel;
    }

    public void setWarnLevel(Integer warnLevel) {
        this.warnLevel = warnLevel;
    }

    public Integer getWarnResultType() {
        return warnResultType;
    }

    public void setWarnResultType(Integer warnResultType) {
        this.warnResultType = warnResultType;
    }

    public Integer getEffectiveDay() {
        return effectiveDay;
    }

    public void setEffectiveDay(Integer effectiveDay) {
        this.effectiveDay = effectiveDay;
    }

    public Integer getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(Integer feedbackType) {
        this.feedbackType = feedbackType;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }
}
