package net.zdsoft.diathesis.data.dto;

import java.util.ArrayList;
import java.util.List;

public class DiathesisChildProjectDto {
	
	private String id;
	private String projectName;
	private String remark;
	private String projectType;
	private String parentId;
	private List<String> evaluationTypes;
	private List<String> proportions;
	private Integer sortNumber;
	private List<DiathesisChildProjectDto> childProjectList=new ArrayList<DiathesisChildProjectDto>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<DiathesisChildProjectDto> getChildProjectList() {
		return childProjectList;
	}

	public void setChildProjectList(List<DiathesisChildProjectDto> childProjectList) {
		this.childProjectList = childProjectList;
	}

	public Integer getSortNumber() {
		return sortNumber;
	}

	public void setSortNumber(Integer sortNumber) {
		this.sortNumber = sortNumber;
	}

	public List<String> getProportions() {
		return proportions;
	}

	public void setProportions(List<String> proportions) {
		this.proportions = proportions;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public List<String> getEvaluationTypes() {
		return evaluationTypes;
	}

	public void setEvaluationTypes(List<String> evaluationTypes) {
		this.evaluationTypes = evaluationTypes;
	}

}
