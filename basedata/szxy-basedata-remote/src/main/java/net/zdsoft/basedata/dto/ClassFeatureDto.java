package net.zdsoft.basedata.dto;

import net.zdsoft.basedata.entity.CourseSchedule;

public class ClassFeatureDto {
	private String subjectId;
	private String subjectName;
	private String subjectType;
	private String teacherId;
	private String teacherName;
	private int weekType = CourseSchedule.WEEK_TYPE_NORMAL;
	
	private String classId;
	
	
	private Integer courseWorkDay;
	// 1-2-3,3-2-2
	private String noArrangeTime;
	// classId1,classId2
	private String combineClass;
	// classId1 : subjectCode1, classId2 : subjectCode2
	private String meanwhiles;
	
	
	public ClassFeatureDto(String subjectId, String subjectType, Integer period) {
		this.subjectId = subjectId;
		this.subjectType = subjectType;
		this.courseWorkDay = period;
	}
	public ClassFeatureDto() {}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectType() {
		return subjectType;
	}
	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}
	public Integer getCourseWorkDay() {
		return courseWorkDay;
	}
	public void setCourseWorkDay(Integer courseWorkDay) {
		this.courseWorkDay = courseWorkDay;
	}
	public String getNoArrangeTime() {
		return noArrangeTime;
	}
	public void setNoArrangeTime(String noArrangeTime) {
		this.noArrangeTime = noArrangeTime;
	}
	public String getCombineClass() {
		return combineClass;
	}
	public void setCombineClass(String combineClass) {
		this.combineClass = combineClass;
	}
	public String getMeanwhiles() {
		return meanwhiles;
	}
	public void setMeanwhiles(String meanwhiles) {
		this.meanwhiles = meanwhiles;
	}
	
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public String getSubjectCode() {
		return subjectId+"-"+subjectType;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public int getWeekType() {
		return weekType;
	}
	public void setWeekType(int weekType) {
		this.weekType = weekType;
	}
}
