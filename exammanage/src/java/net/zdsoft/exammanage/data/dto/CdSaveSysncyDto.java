package net.zdsoft.exammanage.data.dto;

import java.io.Serializable;
import java.util.List;

public class CdSaveSysncyDto implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String acadyear;
    private String semester;
    private String examKey;
    private String uKeyId;
    private String gradeCode;
    private List<CdSubjectDto> dtoList;

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

    public String getExamKey() {
        return examKey;
    }

    public void setExamKey(String examKey) {
        this.examKey = examKey;
    }

    public String getuKeyId() {
        return uKeyId;
    }

    public void setuKeyId(String uKeyId) {
        this.uKeyId = uKeyId;
    }

    public List<CdSubjectDto> getDtoList() {
        return dtoList;
    }

    public void setDtoList(List<CdSubjectDto> dtoList) {
        this.dtoList = dtoList;
    }

    public String getGradeCode() {
        return gradeCode;
    }

    public void setGradeCode(String gradeCode) {
        this.gradeCode = gradeCode;
    }


}
