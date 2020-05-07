package net.zdsoft.newgkelective.data.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubjectInfoListDto {
	
	private List<SubjectInfo> SubjectInfoList = new ArrayList<SubjectInfo>();

	private Map<String, String> teacherIdToTeacherName = new HashMap<String, String>();

	public Map<String, String> getTeacherIdToTeacherName() {
		return teacherIdToTeacherName;
	}

	public void setTeacherIdToTeacherName(
			Map<String, String> teacherIdToTeacherName) {
		this.teacherIdToTeacherName = teacherIdToTeacherName;
	}
	
	public List<SubjectInfo> getSubjectInfoList() {
		return SubjectInfoList;
	}

	public void setSubjectInfoList(List<SubjectInfo> subjectInfoList) {
		SubjectInfoList = subjectInfoList;
	}

}
