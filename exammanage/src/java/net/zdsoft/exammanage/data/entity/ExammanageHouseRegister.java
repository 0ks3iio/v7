package net.zdsoft.exammanage.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@SuppressWarnings("serial")
@Entity
@Table(name = "exammanage_house_register")
public class ExammanageHouseRegister extends BaseEntity<String> {

    private String acadyear;
    private String semester;
    private String examId;
    private String oldUnitId;
    @Transient
    private String oldUnitName;
    private String oldSchoolId;
    @Transient
    private String oldSchoolName;

    private String newUnitId;
    @Transient
    private String newUnitName;

    private String studentId;

    @Transient
    private String card;
    @Transient
    private String stuName;

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


    public String getOldUnitId() {
        return oldUnitId;
    }

    public void setOldUnitId(String oldUnitId) {
        this.oldUnitId = oldUnitId;
    }


    public String getOldSchoolId() {
        return oldSchoolId;
    }

    public void setOldSchoolId(String oldSchoolId) {
        this.oldSchoolId = oldSchoolId;
    }


    public String getNewUnitId() {
        return newUnitId;
    }

    public void setNewUnitId(String newUnitId) {
        this.newUnitId = newUnitId;
    }


    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getOldUnitName() {
        return oldUnitName;
    }

    public void setOldUnitName(String oldUnitName) {
        this.oldUnitName = oldUnitName;
    }

    public String getOldSchoolName() {
        return oldSchoolName;
    }

    public void setOldSchoolName(String oldSchoolName) {
        this.oldSchoolName = oldSchoolName;
    }

    public String getNewUnitName() {
        return newUnitName;
    }

    public void setNewUnitName(String newUnitName) {
        this.newUnitName = newUnitName;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    @Override
    public String fetchCacheEntitName() {
        return "exammanageHouseRegister";
    }

}
