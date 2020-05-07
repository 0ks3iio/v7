package net.zdsoft.exammanage.data.dto;

public class EmExamInfoSearchDto {
    private String searchAcadyear;
    private String searchSemester;
    /**
     * <option value="1">本单位设定的考试</option>
     * <option value="2">直属教育局设定的考试</option>
     * <option value="3">参与的校校联考</option>
     */
    private String searchType;
    private String searchGradeCode;
    private String searchGk;

    public String getSearchGk() {
        return searchGk;
    }

    public void setSearchGk(String searchGk) {
        this.searchGk = searchGk;
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

    public String getSearchGradeCode() {
        return searchGradeCode;
    }

    public void setSearchGradeCode(String searchGradeCode) {
        this.searchGradeCode = searchGradeCode;
    }

}
