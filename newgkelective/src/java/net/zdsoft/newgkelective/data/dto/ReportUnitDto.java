package net.zdsoft.newgkelective.data.dto;

public class ReportUnitDto {
	private String schoolName;
	private String parentUnitName;
	private String schoolLeader;
	private String telephoneNumber;
	private String chooseTime;
	
	public Integer allStudentNum;
	public Integer boyStudentNum;
	public Integer girlStudentNum;
	
	public Double ratioNum;
	
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getParentUnitName() {
		return parentUnitName;
	}
	public void setParentUnitName(String parentUnitName) {
		this.parentUnitName = parentUnitName;
	}
	public String getSchoolLeader() {
		return schoolLeader;
	}
	public void setSchoolLeader(String schoolLeader) {
		this.schoolLeader = schoolLeader;
	}
	public String getTelephoneNumber() {
		return telephoneNumber;
	}
	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}
	public String getChooseTime() {
		return chooseTime;
	}
	public void setChooseTime(String chooseTime) {
		this.chooseTime = chooseTime;
	}
	public Integer getAllStudentNum() {
		return allStudentNum;
	}
	public void setAllStudentNum(Integer allStudentNum) {
		this.allStudentNum = allStudentNum;
	}
	public Integer getBoyStudentNum() {
		return boyStudentNum;
	}
	public void setBoyStudentNum(Integer boyStudentNum) {
		this.boyStudentNum = boyStudentNum;
	}
	public Integer getGirlStudentNum() {
		return girlStudentNum;
	}
	public void setGirlStudentNum(Integer girlStudentNum) {
		this.girlStudentNum = girlStudentNum;
	}
	public Double getRatioNum() {
		return ratioNum;
	}
	public void setRatioNum(Double ratioNum) {
		this.ratioNum = ratioNum;
	}
	
	
}
