package net.zdsoft.datareport.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="datareport_column")
public class DataReportColumn extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;
	
	private String reportId;
	private Integer type;
	private String columnName;
	private Integer columnIndex;
	private Integer isNotnull;
	private Integer columnType;
	private Integer methodType;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	
	public String getReportId() {
		return reportId;
	}



	public void setReportId(String reportId) {
		this.reportId = reportId;
	}



	public Integer getType() {
		return type;
	}



	public void setType(Integer type) {
		this.type = type;
	}



	public String getColumnName() {
		return columnName;
	}



	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}



	public Integer getColumnIndex() {
		return columnIndex;
	}



	public void setColumnIndex(Integer columnIndex) {
		this.columnIndex = columnIndex;
	}



	public Integer getIsNotnull() {
		return isNotnull;
	}



	public void setIsNotnull(Integer isNotnull) {
		this.isNotnull = isNotnull;
	}



	public Integer getColumnType() {
		return columnType;
	}



	public void setColumnType(Integer columnType) {
		this.columnType = columnType;
	}



	public Integer getMethodType() {
		return methodType;
	}



	public void setMethodType(Integer methodType) {
		this.methodType = methodType;
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
		return "dataReportColumn";
	}

}
