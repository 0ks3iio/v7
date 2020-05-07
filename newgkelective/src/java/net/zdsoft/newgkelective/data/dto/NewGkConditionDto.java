package net.zdsoft.newgkelective.data.dto;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NewGkConditionDto {
	private Set<String> subjectIds;// (科目id courseId)
	private String subjectIdstr;// (科目id courseId ,隔开)
	private String subjectNames;// 全称
	private String subShortNames;// 简称
	private String[] subNames;
	private Integer sumNum;// 总学生人数
	private boolean limitSubject;
	private Integer classNum;
	private Map<String, Float> scoreMap;// 成绩key:subjectId
	private String studentId;
	private String studentName;
	private String studentCode;
	private String oldClassName;
	private String nowClassName;
	private int sex;
	private Set<String> stuIds = new HashSet<String>();
	private boolean beXzbSub;

	public String getStudentCode() {
		return studentCode;
	}

	public void setStudentCode(String studentCode) {
		this.studentCode = studentCode;
	}

	public String getOldClassName() {
		return oldClassName;
	}

	public void setOldClassName(String oldClassName) {
		this.oldClassName = oldClassName;
	}

	public String getNowClassName() {
		return nowClassName;
	}

	public void setNowClassName(String nowClassName) {
		this.nowClassName = nowClassName;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public Map<String, Float> getScoreMap() {
		return scoreMap;
	}

	public void setScoreMap(Map<String, Float> scoreMap) {
		this.scoreMap = scoreMap;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public Integer getClassNum() {
		return classNum;
	}

	public void setClassNum(Integer classNum) {
		this.classNum = classNum;
	}

	public String getSubjectIdstr() {
		return subjectIdstr;
	}

	public void setSubjectIdstr(String subjectIdstr) {
		this.subjectIdstr = subjectIdstr;
	}

	public boolean isLimitSubject() {
		return limitSubject;
	}

	public void setLimitSubject(boolean limitSubject) {
		this.limitSubject = limitSubject;
	}

	public String[] getSubNames() {
		return subNames;
	}

	public void setSubNames(String[] subNames) {
		this.subNames = subNames;
	}

	public Set<String> getSubjectIds() {
		return subjectIds;
	}

	public void setSubjectIds(Set<String> subjectIds) {
		this.subjectIds = subjectIds;
	}

	public String getSubShortNames() {
		return subShortNames;
	}

	public void setSubShortNames(String subShortNames) {
		this.subShortNames = subShortNames;
	}

	public Integer getSumNum() {
		return sumNum;
	}

	public void setSumNum(Integer sumNum) {
		this.sumNum = sumNum;
	}

	public Set<String> getStuIds() {
		return stuIds;
	}

	public void setStuIds(Set<String> stuIds) {
		this.stuIds = stuIds;
	}

	public boolean isBeXzbSub() {
		return beXzbSub;
	}

	public void setBeXzbSub(boolean beXzbSub) {
		this.beXzbSub = beXzbSub;
	}

	public String getSubjectNames() {
		return subjectNames;
	}

	public void setSubjectNames(String subjectNames) {
		this.subjectNames = subjectNames;
	}
}
