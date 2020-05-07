package net.zdsoft.credit.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Entity
@Table(name = "exammanage_credit_module_info")
public class CreditModuleInfo extends BaseEntity<String> {

    /**
     *
     */
    private static final long serialVersionUID = 5276609738588815467L;
    private String unitId;
    private String acadyear;
    private String semester;
    private String gradeId;
    private String subjectId;
    private String classType;
    private String classId;
    private String setId;
    private String studentId;
    private String studentCode;
    private String studentName;
    private String examType;
    private Float score;
    private String scoreType;
    private String examSetId;
    private String operator;
    private Date creationTime;
    //某次平时成绩的set名称
    @Transient
    private String setName;
    
    

    public String getUnitId() {
        return unitId;
    }


    public void setUnitId(String unitId) {
        this.unitId = unitId;
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


    public String getGradeId() {
        return gradeId;
    }


    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }


    public String getSubjectId() {
        return subjectId;
    }


    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }


    public String getClassType() {
        return classType;
    }


    public void setClassType(String classType) {
        this.classType = classType;
    }


    public String getClassId() {
        return classId;
    }


    public void setClassId(String classId) {
        this.classId = classId;
    }


    public String getSetId() {
        return setId;
    }


    public void setSetId(String setId) {
        this.setId = setId;
    }


    public String getStudentId() {
        return studentId;
    }


    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }


    public String getStudentCode() {
        return studentCode;
    }


    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }


    public String getStudentName() {
        return studentName;
    }


    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }


    public String getExamType() {
        return examType;
    }


    public void setExamType(String examType) {
        this.examType = examType;
    }


    public Float getScore() {
        return score;
    }


    public void setScore(Float score) {
        this.score = score;
    }


    public String getScoreType() {
        return scoreType;
    }


    public void setScoreType(String scoreType) {
        this.scoreType = scoreType;
    }


    public String getExamSetId() {
        return examSetId;
    }


    public void setExamSetId(String examSetId) {
        this.examSetId = examSetId;
    }


    public String getOperator() {
        return operator;
    }


    public void setOperator(String operator) {
        this.operator = operator;
    }


    public Date getCreationTime() {
        return creationTime;
    }


    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    @Override
    public String fetchCacheEntitName() {
        return "getCreditModuleInfo";
    }

}
