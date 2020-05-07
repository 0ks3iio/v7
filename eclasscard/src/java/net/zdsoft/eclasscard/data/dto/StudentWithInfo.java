package net.zdsoft.eclasscard.data.dto;

import net.zdsoft.basedata.entity.Student;
/**
 * 封装学生和附加信息
 *
 */
public class StudentWithInfo {

	private Student student;
	private int status;

	private String showPictrueUrl;
	
	private int rowNo;
	private int colNo;

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public String getShowPictrueUrl() {
		return showPictrueUrl;
	}

	public void setShowPictrueUrl(String showPictrueUrl) {
		this.showPictrueUrl = showPictrueUrl;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getRowNo() {
		return rowNo;
	}

	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}

	public int getColNo() {
		return colNo;
	}

	public void setColNo(int colNo) {
		this.colNo = colNo;
	}

}
