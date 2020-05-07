package net.zdsoft.newgkelective.data.dto;

public class ClassTimetableDto {

	private String timeTableOtherId;
	private String placeName;
	private String subjectName;
	private String teacherName;
	private Integer dayOfWeek;
	private String periodInterval;
	private Integer period;
	private Integer firstsdWeek;
	
	private String bgColor;

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public Integer getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(Integer dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public String getPeriodInterval() {
		return periodInterval;
	}

	public void setPeriodInterval(String periodInterval) {
		this.periodInterval = periodInterval;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

public Integer getFirstsdWeek() {
		return firstsdWeek;
	}

	public void setFirstsdWeek(Integer firstsdWeek) {
		this.firstsdWeek = firstsdWeek;
	}
	public String getTimeTableOtherId() {
		return timeTableOtherId;
	}

	public void setTimeTableOtherId(String timeTableOtherId) {
		this.timeTableOtherId = timeTableOtherId;
	}
	public String getBgColor() {
		return bgColor;
	}
	
	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}
}
