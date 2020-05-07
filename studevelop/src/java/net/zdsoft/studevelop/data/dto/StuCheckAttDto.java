package net.zdsoft.studevelop.data.dto;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.studevelop.data.entity.StuCheckAttendance;

public class StuCheckAttDto {
	
	private String acadyear;
	private String semester;
	private String classId;
	private String code;
	private String subjectId;
	
	List<StuCheckAttendance> checkAttList=new ArrayList<>();
	
	public List<StuCheckAttendance> getCheckAttList() {
		return checkAttList;
	}
	public void setCheckAttList(List<StuCheckAttendance> checkAttList) {
		this.checkAttList = checkAttList;
	}
	public String getAcadyear() {
		return acadyear;
	}
	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}
	public String getSemester() {
		return semester;
	}
	public void setSemester(String semester) {
		this.semester = semester;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	
}
