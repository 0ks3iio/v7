package net.zdsoft.exammanage.data.entity;


import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Entity
@Table(name = "exammanage_aims_info")
public class EmAimsInfo extends BaseEntity<String> {

    /**
     *
     */
    private static final long serialVersionUID = 7979172596680599469L;
    private String acadyear;
    private String semester;
    private String examId;
    private String isOpen;
    private Date startTime;
    private Date endTime;
    private String schoolIds;

    @Transient
    private String examCode;
    @Transient
    private String examType;
    @Transient
    private String schoolName;
    @Transient
    private String examName;
    @Transient
    private String gradeCode;

    public String getGradeCode() {
        return gradeCode;
    }

    public void setGradeCode(String gradeCode) {
        this.gradeCode = gradeCode;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getExamCode() {
        return examCode;
    }

    public void setExamCode(String examCode) {
        this.examCode = examCode;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
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

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }


    public String getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(String isOpen) {
        this.isOpen = isOpen;
    }

    public String getSchoolIds() {
        return schoolIds;
    }

    public void setSchoolIds(String schoolIds) {
        this.schoolIds = schoolIds;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String fetchCacheEntitName() {
        return "exammanageAimsInfo";
    }
}
