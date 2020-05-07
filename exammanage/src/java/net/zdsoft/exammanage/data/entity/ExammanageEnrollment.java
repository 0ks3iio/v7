package net.zdsoft.exammanage.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "exammanage_enrollment")
public class ExammanageEnrollment extends BaseEntity<String> {

    private String examId;
    private String acadyear;
    private String semester;
    private String unitId;
    private String parentId;
    private String unifiedNum;
    private String trickNum;
    private String specialNum;
    @Transient
    private String schoolName;
    @Transient
    private String parentSchName;


    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }


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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }


    public String getUnifiedNum() {
        return unifiedNum;
    }

    public void setUnifiedNum(String unifiedNum) {
        this.unifiedNum = unifiedNum;
    }


    public String getTrickNum() {
        return trickNum;
    }

    public void setTrickNum(String trickNum) {
        this.trickNum = trickNum;
    }


    public String getSpecialNum() {
        return specialNum;
    }

    public void setSpecialNum(String specialNum) {
        this.specialNum = specialNum;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getParentSchName() {
        return parentSchName;
    }

    public void setParentSchName(String parentSchName) {
        this.parentSchName = parentSchName;
    }

    @Override
    public String fetchCacheEntitName() {
        return "exammanageEnrollment";
    }
}
