package net.zdsoft.comprehensive.dto;

import java.util.Date;

/**
 * @author yangsj  2017年10月19日下午4:22:46
 */
public class ComperSetScoreDto {
	private String id;
	private String acadyear;
	private String semester;
	private String examId;
	private String subjectId;
	private String unitId;
	private String classId;
	private String classType;
	private String studentId;
	private String operatorId;
	private String inputType;
	private Date creationTime;
	private Date modifyTime;
	private String scoremanageType;
	private Integer ranking;

	private String studnetName;
	private String studentCode;
	private String studentTestNum;
	private String className;
	private String score;
	private String toScore;
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStudnetName() {
		return studnetName;
	}
	public void setStudnetName(String studnetName) {
		this.studnetName = studnetName;
	}
	public String getStudentCode() {
		return studentCode;
	}
	public void setStudentCode(String studentCode) {
		this.studentCode = studentCode;
	}
	public String getStudentTestNum() {
		return studentTestNum;
	}
	public void setStudentTestNum(String studentTestNum) {
		this.studentTestNum = studentTestNum;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	
	public String getToScore() {
		return toScore;
	}
	public void setToScore(String toScore) {
		this.toScore = toScore;
	}
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
	public String getExamId() {
		return examId;
	}
	public void setExamId(String examId) {
		this.examId = examId;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getClassType() {
		return classType;
	}
	public void setClassType(String classType) {
		this.classType = classType;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	public String getInputType() {
		return inputType;
	}
	public void setInputType(String inputType) {
		this.inputType = inputType;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getScoremanageType() {
		return scoremanageType;
	}
	public void setScoremanageType(String scoremanageType) {
		this.scoremanageType = scoremanageType;
	}
	public int getRanking() {
		return ranking;
	}
	public void setRanking(Integer ranking) {
		this.ranking = (ranking == null?0:ranking);
	}
	
	
	
}
