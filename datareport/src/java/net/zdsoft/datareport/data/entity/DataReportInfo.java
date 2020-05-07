package net.zdsoft.datareport.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="datareport_info")
public class DataReportInfo extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;
	
	private String title;
	private String unitId; 
	private String ownerId;
	
	private Integer needCheck;
	private Integer isTimeSend;
	private Integer timeSendType;
	
	private Integer state;
	private Integer structType;
	private Integer tableType;
	private String startTime;
	private String endTime;
	private Integer isAttachment;
	
	private	Integer isDeleted;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	
	@Transient
	private String ownerName;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public Integer getNeedCheck() {
		return needCheck;
	}

	public void setNeedCheck(Integer needCheck) {
		this.needCheck = needCheck;
	}

	public Integer getIsTimeSend() {
		return isTimeSend;
	}

	public void setIsTimeSend(Integer isTimeSend) {
		this.isTimeSend = isTimeSend;
	}

	public Integer getTimeSendType() {
		return timeSendType;
	}

	public void setTimeSendType(Integer timeSendType) {
		this.timeSendType = timeSendType;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getStructType() {
		return structType;
	}

	public void setStructType(Integer structType) {
		this.structType = structType;
	}

	public Integer getTableType() {
		return tableType;
	}

	public void setTableType(Integer tableType) {
		this.tableType = tableType;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getIsAttachment() {
		return isAttachment;
	}

	public void setIsAttachment(Integer isAttachment) {
		this.isAttachment = isAttachment;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
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

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	@Override
	public String fetchCacheEntitName() {
		return "dataReportInfo";
	}
	
}
