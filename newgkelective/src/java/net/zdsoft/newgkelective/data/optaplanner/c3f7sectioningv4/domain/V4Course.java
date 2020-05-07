package net.zdsoft.newgkelective.data.optaplanner.c3f7sectioningv4.domain;

import java.util.List;

import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioningv4.common.AbstractPersistable;

public class V4Course extends AbstractPersistable{
	private String 	courseCode;
	private String 	subjectId;

	List<CourseSection>	courseSectionList;  //当前Course对应的所有的CourseSection

	
	public V4Course() {
		super();
	}

	public V4Course(String courseCode) {
		this.courseCode = courseCode;
	}
	public V4Course(String courseCode,String subjectId) {
		this.courseCode = courseCode;
		this.subjectId = subjectId;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public List<CourseSection> getCourseSectionList() {
		return courseSectionList;
	}

	public void setCourseSectionList(List<CourseSection> courseSectionList) {
		this.courseSectionList = courseSectionList;
	}
	
	
}
