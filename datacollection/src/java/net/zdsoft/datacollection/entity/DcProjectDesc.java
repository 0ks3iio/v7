package net.zdsoft.datacollection.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "dc_project_desc")
public class DcProjectDesc extends BaseEntity<String> {

	@Column(length=32)
	private String projectId;
	@Column(length=500)
	private String columnName;
	private Integer updateable;
	private String columnDesc;
	private Integer sumable;
	@Column(length=500)
	private String ruleDesc;

	@Override
	public String fetchCacheEntitName() {
		return null;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Integer getUpdateable() {
		return updateable;
	}

	public void setUpdateable(Integer updateable) {
		this.updateable = updateable;
	}

	public String getColumnDesc() {
		return columnDesc;
	}

	public void setColumnDesc(String columnDesc) {
		this.columnDesc = columnDesc;
	}

	public Integer getSumable() {
		return sumable;
	}

	public void setSumable(Integer sumable) {
		this.sumable = sumable;
	}

	public String getRuleDesc() {
		return ruleDesc;
	}

	public void setRuleDesc(String ruleDesc) {
		this.ruleDesc = ruleDesc;
	}
}
