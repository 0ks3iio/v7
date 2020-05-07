package net.zdsoft.newgkelective.data.dto;

import java.util.List;

public class SubjectTeacherDto {
	private String teacherPlanId;
	private String subjectId;
	private String subjectName;
	private String teacherIds;
	private String teacherNames;
	private List<String> teacherNameList;
	private Integer minNum;
	public String getTeacherPlanId() {
		return teacherPlanId;
	}
	public void setTeacherPlanId(String teacherPlanId) {
		this.teacherPlanId = teacherPlanId;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getTeacherIds() {
		return teacherIds;
	}
	public void setTeacherIds(String teacherIds) {
		this.teacherIds = teacherIds;
	}
	public String getTeacherNames() {
		return teacherNames;
	}
	public void setTeacherNames(String teacherNames) {
		this.teacherNames = teacherNames;
	}
	public List<String> getTeacherNameList() {
		return teacherNameList;
	}
	public void setTeacherNameList(List<String> teacherNameList) {
		this.teacherNameList = teacherNameList;
	}
	public Integer getMinNum() {
		return minNum;
	}
	public void setMinNum(Integer minNum) {
		this.minNum = minNum;
	}
	
}
