package net.zdsoft.credit.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "exammanage_credit_stat")
public class CreditStat extends BaseEntity<String> {

    /**
     *
     */
    private static final long serialVersionUID = -6825384541204609310L;
    private String setId;
    private String unitId;
    private String acadyear;
    private String semester;
    private String subjectId;
    private String studentCode;
    private String studentId;
    private String studentName;
    private String gradeId;
    private String dailySetId;
    private String subDailySetId;
    private Float score;
    private String setType;
    private String classType;
    private String classId;
    @Transient
    private Float percentScore;

    public String getSetId() {
        return setId;
    }

    public void setSetId(String setId) {
        this.setId = setId;
    }

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

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getDailySetId() {
        return dailySetId;
    }

    public void setDailySetId(String dailySetId) {
        this.dailySetId = dailySetId;
    }

    public String getSubDailySetId() {
        return subDailySetId;
    }

    public void setSubDailySetId(String subDailySetId) {
        this.subDailySetId = subDailySetId;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public String getSetType() {
        return setType;
    }

    public void setSetType(String setType) {
        this.setType = setType;
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

    public Float getPercentScore() {
        return percentScore;
    }

    public void setPercentScore(Float percentScore) {
        this.percentScore = percentScore;
    }

    @Override
    public String fetchCacheEntitName() {
        return "getCreditStat";
    }

}
