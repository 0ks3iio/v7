package net.zdsoft.basedata.dto;

import java.util.ArrayList;
import java.util.List;

public class GradeTeachingListDto {
	private String acadyear;
	private String semester;
	private String gradeId;
	private String classId;
	private String subjectType;
	private String xxType;
	private Integer credit;

	private List<GradeTeachingDto> requiredCourseList = new ArrayList<GradeTeachingDto>();
	private List<GradeTeachingDto> optionaldCourseList = new ArrayList<GradeTeachingDto>();
	private List<GradeTeachingDto> gradeTeachingDtoList = new ArrayList<GradeTeachingDto>();

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

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	public String getXxType() {
		return xxType;
	}

	public void setXxType(String xxType) {
		this.xxType = xxType;
	}

	public Integer getCredit() {
		return credit;
	}

	public void setCredit(Integer credit) {
		this.credit = credit;
	}

	public List<GradeTeachingDto> getGradeTeachingDtoList() {
		return gradeTeachingDtoList;
	}

	public void setGradeTeachingDtoList(
			List<GradeTeachingDto> gradeTeachingDtoList) {
		this.gradeTeachingDtoList = gradeTeachingDtoList;
	}

	public List<GradeTeachingDto> getRequiredCourseList() {
		return requiredCourseList;
	}

	public void setRequiredCourseList(List<GradeTeachingDto> requiredCourseList) {
		this.requiredCourseList = requiredCourseList;
	}

	public List<GradeTeachingDto> getOptionaldCourseList() {
		return optionaldCourseList;
	}

	public void setOptionaldCourseList(
			List<GradeTeachingDto> optionaldCourseList) {
		this.optionaldCourseList = optionaldCourseList;
	}

}
