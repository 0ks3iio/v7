package net.zdsoft.newgkelective.data.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubjectInfo {
	private String subjectId;// 科目Id
	private String subjectName;// 科目名称
	private Integer classNumber;// 班级数
	private Integer teacherNumber;// 老师数
	private Integer selectTeacherNumber;
	private String subjectTypes;
	private Map<String, Integer> subTimes;
	// 老师Id对应状态 0代表未选择 1代表选择
	private Map<String, String> teacherIdAndState = new HashMap<String, String>();
	private List<String> teacherIds = new ArrayList<String>();

	public List<String> getTeacherIds() {
		return teacherIds;
	}

	public void setTeacherIds(List<String> teacherIds) {
		this.teacherIds = teacherIds;
	}

	public Map<String, String> getTeacherIdAndState() {
		return teacherIdAndState;
	}

	public void setTeacherIdAndState(Map<String, String> teacherIdAndState) {
		this.teacherIdAndState = teacherIdAndState;
	}

	public Integer getSelectTeacherNumber() {
		return selectTeacherNumber;
	}

	public void setSelectTeacherNumber(Integer selectTeacherNumber) {
		this.selectTeacherNumber = selectTeacherNumber;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public Integer getClassNumber() {
		return classNumber;
	}

	public void setClassNumber(Integer classNumber) {
		this.classNumber = classNumber;
	}

	public Integer getTeacherNumber() {
		return teacherNumber;
	}

	public void setTeacherNumber(Integer teacherNumber) {
		this.teacherNumber = teacherNumber;
	}

	public String getSubjectTypes() {
		return subjectTypes;
	}

	public void setSubjectTypes(String subjectTypes) {
		this.subjectTypes = subjectTypes;
	}

	public Map<String, Integer> getSubTimes() {
		return subTimes;
	}

	public void setSubTimes(Map<String, Integer> subTimes) {
		this.subTimes = subTimes;
	}

}
