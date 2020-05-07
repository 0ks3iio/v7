package net.zdsoft.teaeaxam.dto;

import java.util.List;

import net.zdsoft.teaeaxam.entity.TeaexamSubject;

public class TeaexamSubjectDto {

	private List<TeaexamSubject> teaexamSubjectList;
	private List<String> schIdList;

	public List<TeaexamSubject> getTeaexamSubjectList() {
		return teaexamSubjectList;
	}

	public void setTeaexamSubjectList(List<TeaexamSubject> teaexamSubjectList) {
		this.teaexamSubjectList = teaexamSubjectList;
	}

	public List<String> getSchIdList() {
		return schIdList;
	}

	public void setSchIdList(List<String> schIdList) {
		this.schIdList = schIdList;
	}	
}
