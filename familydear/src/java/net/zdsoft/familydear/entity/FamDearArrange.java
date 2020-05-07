package net.zdsoft.familydear.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="famdear_arrange")
public class FamDearArrange extends BaseEntity<String> {
    private String activityId;
    private String batchType;
    private Date startTime;
    private Date endTime;
    private String rural;
    @Transient
    private String sortBatch;
    @Transient
    private String ruralSub;
    private String ruralValue;
    private int peopleNumber;
    private Date applyTime;
    private Date applyEndTime;
    private String unitId;
    private String leaderUserId;
    @Transient
    private String[] leaderUserIds;
    @Transient
    private String[] leaderUserName;
    @Transient
    private String leaderUserNames;
    @Transient
    private boolean isOverApplyEndTime;
    @Transient
    private String activityTimeStr;
    @Transient
    private String applyTimeStr;
    @Transient
    private boolean canApply;
    @Transient
    private boolean canFull;
    @Transient
    private boolean canLowTime;
    @Transient
    private List<String> selectedRural;

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getBatchType() {
        return batchType;
    }

    public void setBatchType(String batchType) {
        this.batchType = batchType;
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

    public String getRural() {
        return rural;
    }

    public void setRural(String rural) {
        this.rural = rural;
    }

    public int getPeopleNumber() {
        return peopleNumber;
    }

    public void setPeopleNumber(int peopleNumber) {
        this.peopleNumber = peopleNumber;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public Date getApplyEndTime() {
        return applyEndTime;
    }

    public void setApplyEndTime(Date applyEndTime) {
        this.applyEndTime = applyEndTime;
    }

    @Override
    public String fetchCacheEntitName() {
        return "famDearArrange";
    }

    public String getActivityTimeStr() {
        return activityTimeStr;
    }

    public void setActivityTimeStr(String activityTimeStr) {
        this.activityTimeStr = activityTimeStr;
    }

    public boolean isCanApply() {
		return canApply;
	}

	public void setCanApply(boolean canApply) {
		this.canApply = canApply;
	}

	public boolean isCanFull() {
		return canFull;
	}

	public void setCanFull(boolean canFull) {
		this.canFull = canFull;
	}

	public boolean isCanLowTime() {
		return canLowTime;
	}

	public void setCanLowTime(boolean canLowTime) {
		this.canLowTime = canLowTime;
	}

	public String getApplyTimeStr() {
        return applyTimeStr;
    }

    public void setApplyTimeStr(String applyTimeStr) {
        this.applyTimeStr = applyTimeStr;
    }

    public String getLeaderUserId() {
        return leaderUserId;
    }

    public void setLeaderUserId(String leaderUserId) {
        this.leaderUserId = leaderUserId;
    }

    public boolean isOverApplyEndTime() {
        return isOverApplyEndTime;
    }

    public void setOverApplyEndTime(boolean overApplyEndTime) {
        isOverApplyEndTime = overApplyEndTime;
    }

    public String getLeaderUserNames() {
        return leaderUserNames;
    }

    public void setLeaderUserNames(String leaderUserNames) {
        this.leaderUserNames = leaderUserNames;
    }

    public String[] getLeaderUserIds() {
        return leaderUserIds;
    }

    public void setLeaderUserIds(String[] leaderUserIds) {
        this.leaderUserIds = leaderUserIds;
    }

    public String[] getLeaderUserName() {
        return leaderUserName;
    }

    public void setLeaderUserName(String[] leaderUserName) {
        this.leaderUserName = leaderUserName;
    }

    public String getRuralValue() {
        return ruralValue;
    }

    public void setRuralValue(String ruralValue) {
        this.ruralValue = ruralValue;
    }

    public List<String> getSelectedRural() {
        return selectedRural;
    }

    public void setSelectedRural(List<String> selectedRural) {
        this.selectedRural = selectedRural;
    }

    public String getRuralSub() {
        return ruralSub;
    }

    public void setRuralSub(String ruralSub) {
        this.ruralSub = ruralSub;
    }

    public String getSortBatch() {
        return sortBatch;
    }

    public void setSortBatch(String sortBatch) {
        this.sortBatch = sortBatch;
    }
}
