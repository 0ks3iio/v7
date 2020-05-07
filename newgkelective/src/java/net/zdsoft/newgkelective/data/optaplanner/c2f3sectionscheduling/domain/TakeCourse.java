package net.zdsoft.newgkelective.data.optaplanner.c2f3sectionscheduling.domain;

import net.zdsoft.newgkelective.data.optaplanner.c2f3sectionscheduling.common.AbstractPersistable;
/**
 * 为了调用与基础Course区分 改名TakeCourse 走班课程
 *
 */
public class TakeCourse  extends AbstractPersistable {
	private String courseId;//一般： subjectId 分层：subjectId+bestType
	
	private String courseName;
	private int sectionSizeMean;
	private int sectionSizeMargin;
	
	public TakeCourse () {
		
	}
	
	public TakeCourse (String name) {
		courseName = name;
	}
	
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public int getSectionSizeMean() {
		return sectionSizeMean;
	}
	public void setSectionSizeMean(int sectionSizeMean1) {
		this.sectionSizeMean = sectionSizeMean1;
	}
	public int getSectionSizeMargin() {
		return sectionSizeMargin;
	}
	public void setSectionSizeMargin(int sectionSizeMargin1) {
		this.sectionSizeMargin = sectionSizeMargin1;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	
	
}
