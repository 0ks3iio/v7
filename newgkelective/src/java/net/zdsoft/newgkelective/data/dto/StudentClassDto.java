package net.zdsoft.newgkelective.data.dto;

public class StudentClassDto {

	private String studentId;
	
	private String studentName;
	
	private String studentCode;
	
	private String yClassName;
	
	private String yClassId;
	
	private String newClassName;
	
	private String[] arr;

	
	public String[] getArr() {
		return arr;
	}

	public void setArr(String[] arr) {
		this.arr = arr;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getStudentCode() {
		return studentCode;
	}

	public void setStudentCode(String studentCode) {
		this.studentCode = studentCode;
	}

	public String getyClassName() {
		return yClassName;
	}

	public void setyClassName(String yClassName) {
		this.yClassName = yClassName;
	}

	public String getNewClassName() {
		return newClassName;
	}

	public void setNewClassName(String newClassName) {
		this.newClassName = newClassName;
	}

	public String getyClassId() {
		return yClassId;
	}

	public void setyClassId(String yClassId) {
		this.yClassId = yClassId;
	}
	
	
}
