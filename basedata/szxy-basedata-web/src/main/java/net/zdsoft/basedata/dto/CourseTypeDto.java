package net.zdsoft.basedata.dto;

public class CourseTypeDto {
	private String id;
	private String courseTypename;
	
	public String getCourseTypename() {
		return courseTypename;
	}
	public void setCourseTypename(String courseTypename) {
		this.courseTypename = courseTypename;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public CourseTypeDto(String id, String courseTypename) {
		super();
		this.id = id;
		this.courseTypename = courseTypename;
	}
	public CourseTypeDto() {
		super();
		// TODO Auto-generated constructor stub
	}
}
