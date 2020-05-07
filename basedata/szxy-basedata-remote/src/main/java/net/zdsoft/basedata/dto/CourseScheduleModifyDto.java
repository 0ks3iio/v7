package net.zdsoft.basedata.dto;

import java.util.List;

import net.zdsoft.basedata.entity.CourseSchedule;

public class CourseScheduleModifyDto {
	private List<CourseSchedule> scheduleList;
	private List<List<ClassFeatureDto>> ClassSubjectList;
	private List<String> gradeNoClick;
	
	public List<CourseSchedule> getScheduleList() {
		return scheduleList;
	}
	public void setScheduleList(List<CourseSchedule> scheduleList) {
		this.scheduleList = scheduleList;
	}
	public List<List<ClassFeatureDto>> getClassSubjectList() {
		return ClassSubjectList;
	}
	public void setClassSubjectList(List<List<ClassFeatureDto>> classSubjectList) {
		ClassSubjectList = classSubjectList;
	}
	public void setGradeNoClick(List<String> gradeNoClick) {
		this.gradeNoClick = gradeNoClick;
	}
	public List<String> getGradeNoClick() {
		return gradeNoClick;
	}
	
}
