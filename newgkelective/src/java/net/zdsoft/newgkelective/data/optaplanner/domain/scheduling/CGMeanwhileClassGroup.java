package net.zdsoft.newgkelective.data.optaplanner.domain.scheduling;

import java.io.Serializable;
import java.util.Set;

public class CGMeanwhileClassGroup implements Serializable{
	private static final long serialVersionUID = 1L;
	// str 格式 ：classId-subjectId-subjectType
	private Set<Integer> classSubjectCodeGroup;

	public Set<Integer> getClassSubjectCodeGroup() {
		return classSubjectCodeGroup;
	}

	public void setClassSubjectCodeGroup(Set<Integer> classSubjectCodeGroup) {
		this.classSubjectCodeGroup = classSubjectCodeGroup;
	}
}
