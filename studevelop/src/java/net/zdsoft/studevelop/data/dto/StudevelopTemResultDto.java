package net.zdsoft.studevelop.data.dto;

import net.zdsoft.studevelop.data.entity.StudevelopTemplateResult;

import java.util.List;

/**
 * Created by luf on 2018/12/19.
 */
public class StudevelopTemResultDto {

    private String acadyear;
    private String semester;
    private String unitId;

    private List<StudevelopTemplateResult> resultList;

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

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public List<StudevelopTemplateResult> getResultList() {
        return resultList;
    }

    public void setResultList(List<StudevelopTemplateResult> resultList) {
        this.resultList = resultList;
    }
}
