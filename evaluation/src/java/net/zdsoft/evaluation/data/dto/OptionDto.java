package net.zdsoft.evaluation.data.dto;

import java.util.List;

import net.zdsoft.evaluation.data.entity.TeachEvaluateResult;

public class OptionDto {
	/**
	 * 指标类型
	 */
	private String itemType;
	/**
	 * 评价类型
	 */
	private String evaluateType;
	/**
	 * 单选或多选
	 */
	private String showType;
	
	private String projectId;
	
	private String subjectId;
	
	private String acadyear;
	private String semester;
	private String state;
	private String teacherId;
	private String teachOrclassId;
	
	private List<TeachEvaluateResult> resultList;
	
	
	
	public String getTeachOrclassId() {
		return teachOrclassId;
	}
	public void setTeachOrclassId(String teachOrclassId) {
		this.teachOrclassId = teachOrclassId;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getAcadyear() {
		return acadyear;
	}
	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}
	public String getSemester() {
		return semester;
	}
	public void setSemester(String semester) {
		this.semester = semester;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public List<TeachEvaluateResult> getResultList() {
		return resultList;
	}
	public void setResultList(List<TeachEvaluateResult> resultList) {
		this.resultList = resultList;
	}
	public String getShowType() {
		return showType;
	}
	public void setShowType(String showType) {
		this.showType = showType;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public String getEvaluateType() {
		return evaluateType;
	}
	public void setEvaluateType(String evaluateType) {
		this.evaluateType = evaluateType;
	}
	
}
