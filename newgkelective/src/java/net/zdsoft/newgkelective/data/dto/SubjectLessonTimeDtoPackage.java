package net.zdsoft.newgkelective.data.dto;

import java.util.List;

public class SubjectLessonTimeDtoPackage {
	private String subjectId;
	private List<SubjectLessonTimeDto> subjectLessonTimeDtoList;

	public List<SubjectLessonTimeDto> getSubjectLessonTimeDtoList() {
		return subjectLessonTimeDtoList;
	}

	public void setSubjectLessonTimeDtoList(
			List<SubjectLessonTimeDto> subjectLessonTimeDtoList) {
		this.subjectLessonTimeDtoList = subjectLessonTimeDtoList;
	}
}
