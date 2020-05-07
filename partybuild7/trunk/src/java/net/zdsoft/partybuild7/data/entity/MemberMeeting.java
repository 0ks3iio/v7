package net.zdsoft.partybuild7.data.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="pb_meeting_information")
public class MemberMeeting extends BaseEntity<String> {
    //组织id
    private String orgId;
    private String meetingName;
    private String meetingAim;
    private Date   meetingStartDate;
    private Date   meetingEndDate;
    private String meetingPlace;
    private String meetingAgenda;
    private String meetingHarvest;
    private String meetingRemark;
    @Temporal(TemporalType.TIMESTAMP)
	@Column(updatable=false)
    private Date creationTime;

    private Integer runningAccountType ;
    @Transient
    private String runningAccountTypeStr;
    @Transient
	private String timeStr;

    public Integer getRunningAccountType() {
        return runningAccountType;
    }

    public void setRunningAccountType(Integer runningAccountType) {
        this.runningAccountType = runningAccountType;
    }

    public String getRunningAccountTypeStr() {
        return runningAccountTypeStr;
    }

    public void setRunningAccountTypeStr(String runningAccountTypeStr) {
        this.runningAccountTypeStr = runningAccountTypeStr;
    }

    @Transient
    private String memberNames;
    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public String getMeetingAim() {
        return meetingAim;
    }

    public void setMeetingAim(String meetingAim) {
        this.meetingAim = meetingAim;
    }

    public Date getMeetingStartDate() {
        return meetingStartDate;
    }

    public void setMeetingStartDate(Date meetingStartDate) {
        this.meetingStartDate = meetingStartDate;
    }

    public Date getMeetingEndDate() {
        return meetingEndDate;
    }

    public void setMeetingEndDate(Date meetingEndDate) {
        this.meetingEndDate = meetingEndDate;
    }

    public String getMeetingPlace() {
        return meetingPlace;
    }

    public void setMeetingPlace(String meetingPlace) {
        this.meetingPlace = meetingPlace;
    }

    public String getMeetingAgenda() {
        return meetingAgenda;
    }

    public void setMeetingAgenda(String meetingAgenda) {
        this.meetingAgenda = meetingAgenda;
    }

    public String getMeetingHarvest() {
        return meetingHarvest;
    }

    public void setMeetingHarvest(String meetingHarvest) {
        this.meetingHarvest = meetingHarvest;
    }

    public String getMeetingRemark() {
        return meetingRemark;
    }

    public void setMeetingRemark(String meetingRemark) {
        this.meetingRemark = meetingRemark;
    }

    @Override
    public String fetchCacheEntitName() {
        return "MemberMeeting";
    }

    public String getMemberNames() {
        return memberNames;
    }

    public void setMemberNames(String memberNames) {
        this.memberNames = memberNames;
    }

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public String getTimeStr() {
		return timeStr;
	}

	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}

}
