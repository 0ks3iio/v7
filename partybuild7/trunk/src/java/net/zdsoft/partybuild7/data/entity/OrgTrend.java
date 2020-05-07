package net.zdsoft.partybuild7.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.Date;

@Entity
@Table(name="pb_organization_trends")
public class OrgTrend extends BaseEntity<String> {

    private String orgId;
    private String topic;
    private Date trendsDate;
    private String trendsContent;
    private String remark;
    private Integer runningAccountType;
    @Temporal(TemporalType.TIMESTAMP)
	@Column(updatable=false)
    private Date creationTime;
    
    @Transient
    private String runningAccountTypeStr;
    @Transient
	private String timeStr;

    public String getRunningAccountTypeStr() {
        return runningAccountTypeStr;
    }

    public void setRunningAccountTypeStr(String runningAccountTypeStr) {
        this.runningAccountTypeStr = runningAccountTypeStr;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Date getTrendsDate() {
        return trendsDate;
    }

    public void setTrendsDate(Date trendsDate) {
        this.trendsDate = trendsDate;
    }

    public String getTrendsContent() {
        return trendsContent;
    }

    public void setTrendsContent(String trendsContent) {
        this.trendsContent = trendsContent;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getRunningAccountType() {
        return runningAccountType;
    }

    public void setRunningAccountType(Integer runningAccountType) {
        this.runningAccountType = runningAccountType;
    }

    @Override
    public String fetchCacheEntitName() {
        return "OrgTrend";
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
