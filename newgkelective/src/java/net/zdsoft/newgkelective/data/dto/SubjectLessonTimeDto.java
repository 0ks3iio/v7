package net.zdsoft.newgkelective.data.dto;

import java.util.List;

public class SubjectLessonTimeDto {
	private String subjectId;
	private String subjectIdName;
	private List<TimeInfDto> timeInf;
	public String getSubjectIdName() {
		return subjectIdName;
	}
	public void setSubjectIdName(String subjectIdName) {
		this.subjectIdName = subjectIdName;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public List<TimeInfDto> getTimeInf() {
		return timeInf;
	}
	public void setTimeInf(List<TimeInfDto> timeInf) {
		this.timeInf = timeInf;
	}
}
