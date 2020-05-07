package net.zdsoft.exammanage.data.dto;

import net.zdsoft.exammanage.data.entity.EmScoreInfo;

import java.util.Date;

/**
 * scoremanage ScoreInfo
 *
 * @author XuW
 */
public class ScoreInfoDto {

    private String id;
    private String acadyear;
    private String semester;
    private String examId;
    private String subjectId;
    private String unitId;
    private String classId;//行政班
    private String teachClassId;//教学班
    private String studentId;
    private String scoreStatus;//成绩状态 DM-CJLX  正常 缺考 作弊
    private String score;//原始分
    private String gradeType;//几等分
    private String toScore;//赋分值
    private Date creationTime;
    private Date modifyTime;
    private String operatorId;
    private String inputType;//成绩录入方式
    private String isInStat;//是否计入统计（统计之后是否可以修改）

    public EmScoreInfo getScoreInfoByDto() {
        EmScoreInfo scoreInfo = new EmScoreInfo();
        scoreInfo.setAcadyear(acadyear);
        scoreInfo.setSemester(semester);
        scoreInfo.setExamId(examId);
        scoreInfo.setSubjectId(subjectId);
        scoreInfo.setUnitId(unitId);
        scoreInfo.setClassId(classId);
        scoreInfo.setTeachClassId(teachClassId);
        scoreInfo.setStudentId(studentId);
        scoreInfo.setScoreStatus(scoreStatus);
        scoreInfo.setScore(score);
        scoreInfo.setToScore(toScore);
        scoreInfo.setCreationTime(creationTime);
        scoreInfo.setModifyTime(modifyTime);
        scoreInfo.setOperatorId(operatorId);
        scoreInfo.setInputType(inputType);
        scoreInfo.setIsInStat(isInStat);
        return scoreInfo;
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

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getTeachClassId() {
        return teachClassId;
    }

    public void setTeachClassId(String teachClassId) {
        this.teachClassId = teachClassId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getScoreStatus() {
        return scoreStatus;
    }

    public void setScoreStatus(String scoreStatus) {
        this.scoreStatus = scoreStatus;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getGradeType() {
        return gradeType;
    }

    public void setGradeType(String gradeType) {
        this.gradeType = gradeType;
    }

    public String getToScore() {
        return toScore;
    }

    public void setToScore(String toScore) {
        this.toScore = toScore;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getIsInStat() {
        return isInStat;
    }

    public void setIsInStat(String isInStat) {
        this.isInStat = isInStat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
