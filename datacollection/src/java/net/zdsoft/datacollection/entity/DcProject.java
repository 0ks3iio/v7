package net.zdsoft.datacollection.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "dc_project")
public class DcProject extends BaseEntity<String> {

	@Column(length=500)
	private String projectName;
	@Column(length=500)
	private String templatePath;
	private Integer titleIndex;
	private Integer dataStartIndex;
	private Integer dataEndIndex;
	private Integer columnSize;

	@Override
	public String fetchCacheEntitName() {
		return null;
	}


	public String getProjectName() {
		return projectName;
	}


	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}


	public String getTemplatePath() {
		return templatePath;
	}


	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}


	public Integer getTitleIndex() {
		return titleIndex;
	}


	public void setTitleIndex(Integer titleIndex) {
		this.titleIndex = titleIndex;
	}


	public Integer getDataStartIndex() {
		return dataStartIndex;
	}


	public void setDataStartIndex(Integer dataStartIndex) {
		this.dataStartIndex = dataStartIndex;
	}


	public Integer getDataEndIndex() {
		return dataEndIndex;
	}


	public void setDataEndIndex(Integer dataEndIndex) {
		this.dataEndIndex = dataEndIndex;
	}


	public Integer getColumnSize() {
		return columnSize;
	}


	public void setColumnSize(Integer columnSize) {
		this.columnSize = columnSize;
	}

}
