package net.zdsoft.newgkelective.data.dto;

public class NewGkScoreDto {
	private String subjectId;
	private String subjectName;
	private int selectNum;
	private int gradeRanking;
	private int selectRanking;
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
	public int getSelectNum() {
		return selectNum;
	}
	public void setSelectNum(int selectNum) {
		this.selectNum = selectNum;
	}
	public int getGradeRanking() {
		return gradeRanking;
	}
	public void setGradeRanking(int gradeRanking) {
		this.gradeRanking = gradeRanking;
	}
	public int getSelectRanking() {
		return selectRanking;
	}
	public void setSelectRanking(int selectRanking) {
		this.selectRanking = selectRanking;
	}
	
}
