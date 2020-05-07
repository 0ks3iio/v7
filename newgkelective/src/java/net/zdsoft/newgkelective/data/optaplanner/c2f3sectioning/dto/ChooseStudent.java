package net.zdsoft.newgkelective.data.optaplanner.c2f3sectioning.dto;

import java.util.Set;

public class ChooseStudent {
	private String studentId;
	private Set<String> chooseSubjectIds;
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public Set<String> getChooseSubjectIds() {
		return chooseSubjectIds;
	}
	public void setChooseSubjectIds(Set<String> chooseSubjectIds) {
		this.chooseSubjectIds = chooseSubjectIds;
	}

	
}
