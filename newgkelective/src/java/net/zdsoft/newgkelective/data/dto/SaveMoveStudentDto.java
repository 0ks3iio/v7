package net.zdsoft.newgkelective.data.dto;

import java.io.Serializable;

public class SaveMoveStudentDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String studentId;
	private String divideId;
	private String subjectType;
	
	private String[] courseClassId;//courseId_classId

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getDivideId() {
		return divideId;
	}

	public void setDivideId(String divideId) {
		this.divideId = divideId;
	}

	public String[] getCourseClassId() {
		return courseClassId;
	}

	public void setCourseClassId(String[] courseClassId) {
		this.courseClassId = courseClassId;
	}

	public String getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	
}
