package net.zdsoft.scoremanage.data.dto;

import java.util.List;

import net.zdsoft.scoremanage.data.entity.SubjectInfo;

public class SubjectInfoSaveDto {
	public List<SubjectInfo> subjectInfoList;
	public String examInfoId;

	public String getExamInfoId() {
		return examInfoId;
	}

	public void setExamInfoId(String examInfoId) {
		this.examInfoId = examInfoId;
	}

	public List<SubjectInfo> getSubjectInfoList() {
		return subjectInfoList;
	}

	public void setSubjectInfoList(List<SubjectInfo> subjectInfoList) {
		this.subjectInfoList = subjectInfoList;
	}

}
