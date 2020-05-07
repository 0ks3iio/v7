package net.zdsoft.familydear.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Entity
@Table(name="famdear_plan")
public class FamDearPlan extends BaseEntity<String> {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String year;
    private String createUserId;
    @Transient
    private String createUserName;
    private Date createTime;
    @Transient
    private String createTimeStr;
    private String unitId;
    private String title;
    private Date startTime;
    private Date endTime;
    private String guideThought;
    private String workObjective;
    private String mainTask;
    private String activityArrange;
    private String state;

    @Override
    public String fetchCacheEntitName() {
        return "famDearPlan";
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getGuideThought() {
        return guideThought;
    }

    public void setGuideThought(String guideThought) {
        this.guideThought = guideThought;
    }

    public String getWorkObjective() {
        return workObjective;
    }

    public void setWorkObjective(String workObjective) {
        this.workObjective = workObjective;
    }

    public String getMainTask() {
        return mainTask;
    }

    public void setMainTask(String mainTask) {
        this.mainTask = mainTask;
    }

    public String getActivityArrange() {
        return activityArrange;
    }

    public void setActivityArrange(String activityArrange) {
        this.activityArrange = activityArrange;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }
}
