package net.zdsoft.newgkelective.data.dto;

import java.util.List;

public class TimetableScheduleDto {
	
	private String subjectId;
	private String classId;
	private String subjectName;
	private String className;
	private String placeId;
	private String placeName;
	
	private String dayOfWeek;
	private String periodInterval;
	private String period;
	
	private List<String> showSchedule;//科目(班级，教师，场地)
	private String firstsdWeek;
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getPlaceId() {
		return placeId;
	}
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}
	public String getPlaceName() {
		return placeName;
	}
	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}
	public String getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public String getPeriodInterval() {
		return periodInterval;
	}
	public void setPeriodInterval(String periodInterval) {
		this.periodInterval = periodInterval;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public List<String> getShowSchedule() {
		return showSchedule;
	}
	public void setShowSchedule(List<String> showSchedule) {
		this.showSchedule = showSchedule;
	}
	public String getFirstsdWeek() {
		return firstsdWeek;
	}
	public void setFirstsdWeek(String firstsdWeek) {
		this.firstsdWeek = firstsdWeek;
	}

}
