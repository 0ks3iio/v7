package net.zdsoft.stuwork.data.dto;

public class ClazzPermissionDto {

	private String classId;
	private String className;
	private String permisionUserIds;
	private String permisionUserNames;

	private String courseName;
	private String teacherName;
	private String gradeName;

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getPermisionUserIds() {
		return permisionUserIds;
	}

	public void setPermisionUserIds(String permisionUserIds) {
		this.permisionUserIds = permisionUserIds;
	}

	public String getPermisionUserNames() {
		return permisionUserNames;
	}

	public void setPermisionUserNames(String permisionUserNames) {
		this.permisionUserNames = permisionUserNames;
	}

}
