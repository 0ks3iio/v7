package net.zdsoft.datareport.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="datareport_obj")
public class DataReportObj extends BaseEntity<String>{
	
	private static final long serialVersionUID = 1L;

	private String reportId;
	private String objectId;
	private Integer objectType;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	
	@Transient
	private String objectName;
	
	public String getReportId() {
		return reportId;
	}



	public void setReportId(String reportId) {
		this.reportId = reportId;
	}



	public String getObjectId() {
		return objectId;
	}



	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}



	public Integer getObjectType() {
		return objectType;
	}



	public void setObjectType(Integer objectType) {
		this.objectType = objectType;
	}



	public String getObjectName() {
		return objectName;
	}



	public void setObjectName(String objectName) {
		this.objectName = objectName;
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



	@Override
	public String fetchCacheEntitName() {
		return "dataReportObj";
	}
}
