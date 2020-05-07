package net.zdsoft.basedata.dto;

import net.zdsoft.basedata.entity.CourseSchedule;

public class ScheduleSwapDto {
	private String[] leftCourseScheduleIds;
	private String[] rightCourseScheduleIds;
	/**
	 * 例如：1-2-3 周二-上午-第三节，
	 */
	private String leftPeriod;
	private String rightPeriod;

	private CourseSchedule[] leftSchedules; 
	private CourseSchedule[] rightSchedules; 
	
	public String[] getLeftCourseScheduleIds() {
		return leftCourseScheduleIds;
	}

	public void setLeftCourseScheduleIds(String[] leftCourseScheduleIds) {
		this.leftCourseScheduleIds = leftCourseScheduleIds;
	}

	public String[] getRightCourseScheduleIds() {
		return rightCourseScheduleIds;
	}

	public void setRightCourseScheduleIds(String[] rightCourseScheduleIds) {
		this.rightCourseScheduleIds = rightCourseScheduleIds;
	}

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

}
