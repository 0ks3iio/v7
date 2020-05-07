package net.zdsoft.api.dataset.vo;

import java.util.Date;

public class DateSetDto {

	private String id;
	private String metadataName;
	private String tableName;
	private String metadataId;
	private String name;
	private Date creationTime;
	private String remark;
	private String dataSetRuleDtos;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMetadataName() {
		return metadataName;
	}
	public void setMetadataName(String metadataName) {
		this.metadataName = metadataName;
	}
	public String getMetadataId() {
		return metadataId;
	}
	public void setMetadataId(String metadataId) {
		this.metadataId = metadataId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getDataSetRuleDtos() {
		return dataSetRuleDtos;
	}
	public void setDataSetRuleDtos(String dataSetRuleDtos) {
		this.dataSetRuleDtos = dataSetRuleDtos;
	}
	
}
