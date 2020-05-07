package net.zdsoft.basedata.dto;

import net.zdsoft.basedata.entity.CourseSchedule;

public class CourseScheduleDto extends CourseSchedule {

    private static final long serialVersionUID = 1L;
    private int dayOfWeek1;//课程开始星期
	private int weekOfWorktime1;//课程开始周次
	private int dayOfWeek2;//课程结束星期
	private int weekOfWorktime2;//课程结束周次
	private String gradeId;
	private String[] classIds;
	
	private String[] timeArr;//周次_上下午_节次
	
	private boolean isXN=false;//默认false;  true:过滤行政班纯虚拟课程的课表:表现class_type=1 and subject_type=3
	//删除特殊处理 如果isXN=true 那么行政班删除时需要使用subjectId+时间点删除
	private String xnSubjectId;
	
	public String getGradeId() {
		return gradeId;
	}
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
	public int getDayOfWeek1() {
		return dayOfWeek1;
	}
	public void setDayOfWeek1(int dayOfWeek1) {
		this.dayOfWeek1 = dayOfWeek1;
	}
	public int getWeekOfWorktime1() {
		return weekOfWorktime1;
	}
	public void setWeekOfWorktime1(int weekOfWorktime1) {
		this.weekOfWorktime1 = weekOfWorktime1;
	}
	public int getDayOfWeek2() {
		return dayOfWeek2;
	}
	public void setDayOfWeek2(int dayOfWeek2) {
		this.dayOfWeek2 = dayOfWeek2;
	}
	public int getWeekOfWorktime2() {
		return weekOfWorktime2;
	}
	public void setWeekOfWorktime2(int weekOfWorktime2) {
		this.weekOfWorktime2 = weekOfWorktime2;
	}
	public String[] getClassIds() {
		return classIds;
	}
	public void setClassIds(String[] classIds) {
		this.classIds = classIds;
	}
	public boolean isXN() {
		return isXN;
	}
	public void setXN(boolean isXN) {
		this.isXN = isXN;
	}
	public String[] getTimeArr() {
		return timeArr;
	}
	public void setTimeArr(String[] timeArr) {
		this.timeArr = timeArr;
	}
	public String getXnSubjectId() {
		return xnSubjectId;
	}
	public void setXnSubjectId(String xnSubjectId) {
		this.xnSubjectId = xnSubjectId;
	}
	
	
}
