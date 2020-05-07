package net.zdsoft.basedata.dto;

import net.zdsoft.basedata.entity.CourseSchedule;

public class PeriodSwapDto {

	private String[] leftTimeTableOtherIds;
	private String[] rightTimeTableOtherIds;
	/**
	 * 例如：1-2-3  周二-上午-第三节，
	 */
	private String leftPeriod;
	private String rightPeriod;
	
	private CourseSchedule[] leftSchedules; 
	private CourseSchedule[] rightSchedules; 
	// 科目 禁排限制 和 教师禁排限制
	private String courseLimit;
	private String teacherLimit;
	
	public String getLeftPeriod() {
		return leftPeriod;
	}
	public void setLeftPeriod(String leftPeriod) {
		this.leftPeriod = leftPeriod;
	}
	public String getRightPeriod() {
		return rightPeriod;
	}
	public void setRightPeriod(String rightPeriod) {
		this.rightPeriod = rightPeriod;
	}
	public String[] getLeftTimeTableOtherIds() {
		return leftTimeTableOtherIds;
	}
	public void setLeftTimeTableOtherIds(String[] leftTimeTableOtherIds) {
		this.leftTimeTableOtherIds = leftTimeTableOtherIds;
	}
	public String[] getRightTimeTableOtherIds() {
		return rightTimeTableOtherIds;
	}
	public void setRightTimeTableOtherIds(String[] rightTimeTableOtherIds) {
		this.rightTimeTableOtherIds = rightTimeTableOtherIds;
	}
	public CourseSchedule[] getLeftSchedules() {
		return leftSchedules;
	}
	public void setLeftSchedules(CourseSchedule[] leftSchedules) {
		this.leftSchedules = leftSchedules;
	}
	public CourseSchedule[] getRightSchedules() {
		return rightSchedules;
	}
	public void setRightSchedules(CourseSchedule[] rightSchedules) {
		this.rightSchedules = rightSchedules;
	}
	public String getCourseLimit() {
		return courseLimit;
	}
	public void setCourseLimit(String coruseLimit) {
		this.courseLimit = coruseLimit;
	}
	public String getTeacherLimit() {
		return teacherLimit;
	}
	public void setTeacherLimit(String teacherLimit) {
		this.teacherLimit = teacherLimit;
	}
}
