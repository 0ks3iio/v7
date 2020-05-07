package net.zdsoft.newgkelective.data.dto;

import java.util.List;

public class NewGkTeacherPlanExDto {
	private String teacherId;
	private String teacherName;
	private String deptName;
	private String classNum;
	private List<String> classList;
	private List<String> lessionList;

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getClassNum() {
		return classNum;
	}

	public void setClassNum(String classNum) {
		this.classNum = classNum;
	}

	public List<String> getClassList() {
		return classList;
	}

	public void setClassList(List<String> classList) {
		this.classList = classList;
	}

	public List<String> getLessionList() {
		return lessionList;
	}

	public void setLessionList(List<String> lessionList) {
		this.lessionList = lessionList;
	}

}
