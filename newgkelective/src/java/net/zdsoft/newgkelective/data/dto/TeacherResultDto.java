package net.zdsoft.newgkelective.data.dto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TeacherResultDto {
	private String teacherId;// 老师的Id
	private String teacherName;// 老师的姓名
	private String oneSubjectId;
	
	private List<String> subjectNames = new ArrayList<String>();// 授课课程名字
	private Map<String, Integer> subjectNumberOfDay = new LinkedHashMap<String, Integer>();// 一周老师上课数量
	private Map<String, String> subjectNameAndPlace = new LinkedHashMap<String, String>();// 周课表
	private List<String> classNames = new ArrayList<String>(); // 上课班级名字

	private int periodNums;//总课时
	private String courseNames;//subjectNames 组装后的字符串
	
	public TeacherResultDto() {
	}
	
	public TeacherResultDto(String teacherId, String teacherName) {
		super();
		this.teacherId = teacherId;
		this.teacherName = teacherName;
	}

	public List<String> getClassNames() {
		return classNames;
	}

	public void setClassNames(List<String> classNames) {
		this.classNames = classNames;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public List<String> getSubjectNames() {
		return subjectNames;
	}

	public void setSubjectNames(List<String> subjectNames) {
		this.subjectNames = subjectNames;
	}

	public Map<String, Integer> getSubjectNumberOfDay() {
		return subjectNumberOfDay;
	}

	public void setSubjectNumberOfDay(Map<String, Integer> subjectNumberOfDay) {
		this.subjectNumberOfDay = subjectNumberOfDay;
	}

	public Map<String, String> getSubjectNameAndPlace() {
		return subjectNameAndPlace;
	}

	public void setSubjectNameAndPlace(Map<String, String> subjectNameAndPlace) {
		this.subjectNameAndPlace = subjectNameAndPlace;
	}

	public int getPeriodNums() {
		return periodNums;
	}

	public void setPeriodNums(int periodNums) {
		this.periodNums = periodNums;
	}

	public String getCourseNames() {
		return courseNames;
	}

	public void setCourseNames(String courseNames) {
		this.courseNames = courseNames;
	}

	public String getOneSubjectId() {
		return oneSubjectId;
	}

	public void setOneSubjectId(String oneSubjectId) {
		this.oneSubjectId = oneSubjectId;
	}

}
