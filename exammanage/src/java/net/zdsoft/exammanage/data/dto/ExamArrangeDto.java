package net.zdsoft.exammanage.data.dto;

import java.util.List;

public class ExamArrangeDto {

    private String examPlaceCode;
    private String examPlaceName;
    private List<String> invigilateList;

    public String getExamPlaceCode() {
        return examPlaceCode;
    }

    public void setExamPlaceCode(String examPlaceCode) {
        this.examPlaceCode = examPlaceCode;
    }

    public String getExamPlaceName() {
        return examPlaceName;
    }

    public void setExamPlaceName(String examPlaceName) {
        this.examPlaceName = examPlaceName;
    }

    public List<String> getInvigilateList() {
        return invigilateList;
    }

    public void setInvigilateList(List<String> invigilateList) {
        this.invigilateList = invigilateList;
    }


}
