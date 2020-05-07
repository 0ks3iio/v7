package net.zdsoft.gkelective.data.dto;

public class ChosenSubjectSearchDto {
	
	private String searchClassId;
	private String searchSex;
	private String searchSubject;
	private String searchSelectType;
	private String searchCondition;
	private String searchScoreType;
	
	public String getSearchScoreType() {
		return searchScoreType;
	}
	public void setSearchScoreType(String searchScoreType) {
		this.searchScoreType = searchScoreType;
	}
	public String getSearchClassId() {
		return searchClassId;
	}
	public void setSearchClassId(String searchClassId) {
		this.searchClassId = searchClassId;
	}
	public String getSearchSex() {
		return searchSex;
	}
	public void setSearchSex(String searchSex) {
		this.searchSex = searchSex;
	}
	public String getSearchSubject() {
		return searchSubject;
	}
	public void setSearchSubject(String searchSubject) {
		this.searchSubject = searchSubject;
	}
	public String getSearchSelectType() {
		return searchSelectType;
	}
	public void setSearchSelectType(String searchSelectType) {
		this.searchSelectType = searchSelectType;
	}
	public String getSearchCondition() {
		return searchCondition;
	}
	public void setSearchCondition(String searchCondition) {
		this.searchCondition = searchCondition;
	}
	
}
