package net.zdsoft.credit.data.dto;

public class DailyEditDto {
	private String dailyId;
	private String subDailyId;
	private float totalScore;
	private float score;
	private String studentId;
	private String studentCode;
	private String studentName;
	
	private String scoreType;
	private String examType;
	private String examSetId;
	
	public String getScoreType() {
		return scoreType;
	}
	public void setScoreType(String scoreType) {
		this.scoreType = scoreType;
	}
	public String getExamType() {
		return examType;
	}
	public void setExamType(String examType) {
		this.examType = examType;
	}
	public String getExamSetId() {
		return examSetId;
	}
	public void setExamSetId(String examSetId) {
		this.examSetId = examSetId;
	}
	public String getDailyId() {
		return dailyId;
	}
	public void setDailyId(String dailyId) {
		this.dailyId = dailyId;
	}
	public String getSubDailyId() {
		return subDailyId;
	}
	public void setSubDailyId(String subDailyId) {
		this.subDailyId = subDailyId;
	}
	public float getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(float totalScore) {
		this.totalScore = totalScore;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getStudentCode() {
		return studentCode;
	}
	public void setStudentCode(String studentCode) {
		this.studentCode = studentCode;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	
	
}
