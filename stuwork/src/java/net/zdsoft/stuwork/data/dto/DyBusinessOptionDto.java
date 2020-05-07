package net.zdsoft.stuwork.data.dto;

import java.util.List;

import net.zdsoft.stuwork.data.entity.DyBusinessOption;

public class DyBusinessOptionDto {

	private String acadyear;
	private String semester;
	private String classId;
	private String studentId;
	
	private String optionName;
	
	private List<DyBusinessOption> dyBusinessOptionList;
	
	
	
	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getOptionName() {
		return optionName;
	}

	public void setOptionName(String optionName) {
		this.optionName = optionName;
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

	public List<DyBusinessOption> getDyBusinessOptionList() {
		return dyBusinessOptionList;
	}

	public void setDyBusinessOptionList(List<DyBusinessOption> dyBusinessOptionList) {
		this.dyBusinessOptionList = dyBusinessOptionList;
	}
	
	
}
