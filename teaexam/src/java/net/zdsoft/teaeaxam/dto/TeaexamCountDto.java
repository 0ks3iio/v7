package net.zdsoft.teaeaxam.dto;

public class TeaexamCountDto {

	private String schoolName;
	private String subjectName;
	private int countNum;
	private int yxNum;
	private int hgNum;
	private int bhgNum;
	private String schoolId;
	private String subjectId;
	private float avgScore;
	private float sumScore;
	private float yxPer;
	private float hgPer;
	private float bhgPer;
	private boolean centerSch;
	
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public int getCountNum() {
		return countNum;
	}
	public void setCountNum(int countNum) {
		this.countNum = countNum;
	}
	public int getYxNum() {
		return yxNum;
	}
	public void setYxNum(int yxNum) {
		this.yxNum = yxNum;
	}
	public int getHgNum() {
		return hgNum;
	}
	public void setHgNum(int hgNum) {
		this.hgNum = hgNum;
	}
	public int getBhgNum() {
		return bhgNum;
	}
	public void setBhgNum(int bhgNum) {
		this.bhgNum = bhgNum;
	}
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public float getAvgScore() {
		return avgScore;
	}
	public void setAvgScore(float avgScore) {
		this.avgScore = avgScore;
	}
	public float getYxPer() {
		return yxPer;
	}
	public void setYxPer(float yxPer) {
		this.yxPer = yxPer;
	}
	public float getHgPer() {
		return hgPer;
	}
	public void setHgPer(float hgPer) {
		this.hgPer = hgPer;
	}
	public float getBhgPer() {
		return bhgPer;
	}
	public void setBhgPer(float bhgPer) {
		this.bhgPer = bhgPer;
	}
	public float getSumScore() {
		return sumScore;
	}
	public void setSumScore(float sumScore) {
		this.sumScore = sumScore;
	}
	public boolean isCenterSch() {
		return centerSch;
	}
	public void setCenterSch(boolean centerSch) {
		this.centerSch = centerSch;
	}
	
}
