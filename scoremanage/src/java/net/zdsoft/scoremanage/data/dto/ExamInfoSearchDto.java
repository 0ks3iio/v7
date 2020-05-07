package net.zdsoft.scoremanage.data.dto;

public class ExamInfoSearchDto{

	private String searchAcadyear;
	private String searchSemester;
	/**
	 * <option value="1">本单位设定的考试</option>
		<option value="2">直属教育局设定的考试</option>
		<option value="3">参与的校校联考</option>
	 */
	private String searchType;
	private String searchExamId;
	//是否包括7选3
	private String isgkExamType;
	
	public String getSearchExamId() {
		return searchExamId;
	}
	public void setSearchExamId(String searchExamId) {
		this.searchExamId = searchExamId;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public String getSearchAcadyear() {
		return searchAcadyear;
	}
	public void setSearchAcadyear(String searchAcadyear) {
		this.searchAcadyear = searchAcadyear;
	}
	public String getSearchSemester() {
		return searchSemester;
	}
	public void setSearchSemester(String searchSemester) {
		this.searchSemester = searchSemester;
	}
	public String getIsgkExamType() {
		return isgkExamType;
	}
	public void setIsgkExamType(String isgkExamType) {
		this.isgkExamType = isgkExamType;
	}
}
