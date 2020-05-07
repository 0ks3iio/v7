package net.zdsoft.diathesis.data.dto;

import net.zdsoft.diathesis.data.entity.DiathesisOption;

import java.util.ArrayList;
import java.util.List;

public class DiathesisProjectDto {
	
	private String id;
	private String projectName;
	private String status;
	private Integer toAuditorNum;
	private String score;
	private String semesterMax;
	private String allMax;
	private String scoreType;
	private String scoreStructureId;
	private List<DiathesisProjectDto> childList = new ArrayList<DiathesisProjectDto>();
	private List<DiathesisStructureSettingDto> structureList =new ArrayList<>();
	private List<DiathesisOption> optionList;

	public List<DiathesisOption> getOptionList() {
		return optionList;
	}

	public void setOptionList(List<DiathesisOption> optionList) {
		this.optionList = optionList;
	}

	public String getScoreStructureId() {
		return scoreStructureId;
	}

	public void setScoreStructureId(String scoreStructureId) {
		this.scoreStructureId = scoreStructureId;
	}

	public String getScoreType() {
		return scoreType;
	}

	public void setScoreType(String scoreType) {
		this.scoreType = scoreType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getToAuditorNum() {
		return toAuditorNum;
	}

	public void setToAuditorNum(Integer toAuditorNum) {
		this.toAuditorNum = toAuditorNum;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getSemesterMax() {
		return semesterMax;
	}

	public void setSemesterMax(String semesterMax) {
		this.semesterMax = semesterMax;
	}

	public String getAllMax() {
		return allMax;
	}

	public void setAllMax(String allMax) {
		this.allMax = allMax;
	}

	public List<DiathesisProjectDto> getChildList() {
		return childList;
	}

	public void setChildList(List<DiathesisProjectDto> childList) {
		this.childList = childList;
	}

	public List<DiathesisStructureSettingDto> getStructureList() {
		return structureList;
	}

	public void setStructureList(List<DiathesisStructureSettingDto> structureList) {
		this.structureList = structureList;
	}
}
