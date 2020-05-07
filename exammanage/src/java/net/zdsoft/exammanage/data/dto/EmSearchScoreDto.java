package net.zdsoft.exammanage.data.dto;

public class EmSearchScoreDto {
    private String examId;
    private String emSubjectId;
    private String subjectId;
    private String searchInputType;//1:按班级 2:按考场
    private String searchExamPlanId;
    private String searchClassId;

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

    public String getEmSubjectId() {
        return emSubjectId;
    }

    public void setEmSubjectId(String emSubjectId) {
        this.emSubjectId = emSubjectId;
    }

    public String getSearchInputType() {
        return searchInputType;
    }

    public void setSearchInputType(String searchInputType) {
        this.searchInputType = searchInputType;
    }

    public String getSearchExamPlanId() {
        return searchExamPlanId;
    }

    public void setSearchExamPlanId(String searchExamPlanId) {
        this.searchExamPlanId = searchExamPlanId;
    }

    public String getSearchClassId() {
        return searchClassId;
    }

    public void setSearchClassId(String searchClassId) {
        this.searchClassId = searchClassId;
    }


}
