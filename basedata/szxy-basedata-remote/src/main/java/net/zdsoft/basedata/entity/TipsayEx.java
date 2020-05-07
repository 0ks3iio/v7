package net.zdsoft.basedata.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 代课调课审核记录
 */
//改名base_tipsay_ex 改为base_transfer_audit
@Table(name="base_transfer_audit")
@Entity
public class TipsayEx extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String schoolId;
	
	private String tipsayId;//主表id
	private String auditorId;//审核老师
	private String state;//审核状态
	private String auditorType;//审核老师类型
	
	private String sourceType;//审核对象类型 01代管课，02调课
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	private String remark;//审核理由

	
	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getTipsayId() {
		return tipsayId;
	}

	public void setTipsayId(String tipsayId) {
		this.tipsayId = tipsayId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAuditorType() {
		return auditorType;
	}

	public void setAuditorType(String auditorType) {
		this.auditorType = auditorType;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public String getAuditorId() {
		return auditorId;
	}

	public void setAuditorId(String auditorId) {
		this.auditorId = auditorId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String fetchCacheEntitName() {
		return "tipsayEx";
	}

}
