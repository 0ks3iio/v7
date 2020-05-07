package net.zdsoft.newgkelective.data.dto;

public class ClassroomUseageDetailDto {
	private String teacherName;
	private String className;
	private String subjectName;
	private Integer dayOfWeek;
	private String period_interval;
	private Integer period;
	private Integer firstsdWeek;
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public Integer getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(Integer dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public String getPeriod_interval() {
		return period_interval;
	}
	public void setPeriod_interval(String period_interval) {
		this.period_interval = period_interval;
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
	public ClassroomUseageDetailDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ClassroomUseageDetailDto(String teacherName, String className,
			String subjectName, Integer dayOfWeek, String period_interval,
			Integer period) {
		super();
		this.teacherName = teacherName;
		this.className = className;
		this.subjectName = subjectName;
		this.dayOfWeek = dayOfWeek;
		this.period_interval = period_interval;
		this.period = period;
	}
	public ClassroomUseageDetailDto(String teacherName, String className, String subjectName, Integer dayOfWeek, String period_interval, Integer period, Integer firstsdWeek) {
		super();
		this.teacherName = teacherName;
		this.className = className;
		this.subjectName = subjectName;
		this.dayOfWeek = dayOfWeek;
		this.period_interval = period_interval;
		this.period = period;
		this.firstsdWeek = firstsdWeek;
	}
	
}
