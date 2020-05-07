package net.zdsoft.basedata.dto;

import java.util.ArrayList;
import java.util.List;

public class TeachSettingDto {
	private String classTeachingId;
	private String clazzId;
	private String className;
	private String mainTeacherId;
	private String mainTeacherName;
	private String subName;
	private String subjectId;
	private String otherTeacherIds;
	private List<TeacherDto> teacherDtoList = new ArrayList<TeacherDto>();

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getClassTeachingId() {
		return classTeachingId;
	}

	public void setClassTeachingId(String classTeachingId) {
		this.classTeachingId = classTeachingId;
	}

	public List<TeacherDto> getTeacherDtoList() {
		return teacherDtoList;
	}

	public void setTeacherDtoList(List<TeacherDto> teacherDtoList) {
		this.teacherDtoList = teacherDtoList;
	}

	public String getClazzId() {
		return clazzId;
	}

	public void setClazzId(String clazzId) {
		this.clazzId = clazzId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMainTeacherId() {
		return mainTeacherId;
	}

	public void setMainTeacherId(String mainTeacherId) {
		this.mainTeacherId = mainTeacherId;
	}

	public String getMainTeacherName() {
		return mainTeacherName;
	}

	public void setMainTeacherName(String mainTeacherName) {
		this.mainTeacherName = mainTeacherName;
	}

	public String getSubName() {
		return subName;
	}

	public void setSubName(String subName) {
		this.subName = subName;
	}

	public String getOtherTeacherIds() {
		return otherTeacherIds;
	}

	public void setOtherTeacherIds(String otherTeacherIds) {
		this.otherTeacherIds = otherTeacherIds;
	}

}
