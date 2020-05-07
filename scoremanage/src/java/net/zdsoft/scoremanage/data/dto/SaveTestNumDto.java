package net.zdsoft.scoremanage.data.dto;

import java.util.List;

import net.zdsoft.basedata.dto.StudentDto;

public class SaveTestNumDto {

	private List<StudentDto> studentDtoList;

	public List<StudentDto> getStudentDtoList() {
		return studentDtoList;
	}

	public void setStudentDtoList(List<StudentDto> studentDtoList) {
		this.studentDtoList = studentDtoList;
	}
}
