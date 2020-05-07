package net.zdsoft.exammanage.data.dto;

import net.zdsoft.exammanage.data.entity.EmScoreInfo;

import java.util.List;

public class ScoreInputDto {
    private String classId;

    private String subjectId;

    private boolean isSubmit;

    private boolean isLock;

    private String className;

    private String classType;

    private String subjectName;

    private String teacherNames;

    private String stuId;

    private String stuName;

    private String stuExamNum;//考号

    private String stuCode;//学号

    private String teachClassId;//教学班id

    private String examId;//教学班id

    private String scoreStatus;//成绩状态
    private String score;
    private String toScore;  //  学分/总评成绩
    private String scoreId;//成绩id;
    private String inputType;
    private String gradeType;
    private String writingScore;
    private String speehScore;
    private Integer fullMark;
    private String unitiveCode;

    //-----成绩录入form提交保存某个班级数据-----------
    private List<EmScoreInfo> dtoList;//表格数据

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public boolean isSubmit() {
        return isSubmit;
    }

    public void setSubmit(boolean isSubmit) {
        this.isSubmit = isSubmit;
    }

    public boolean isLock() {
        return isLock;
    }

    public void setLock(boolean isLock) {
        this.isLock = isLock;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getTeacherNames() {
        return teacherNames;
    }

    public void setTeacherNames(String teacherNames) {
        this.teacherNames = teacherNames;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getStuExamNum() {
        return stuExamNum;
    }

    public void setStuExamNum(String stuExamNum) {
        this.stuExamNum = stuExamNum;
    }

    public String getStuCode() {
        return stuCode;
    }

    public void setStuCode(String stuCode) {
        this.stuCode = stuCode;
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

    public String getToScore() {
        return toScore;
    }

    public void setToScore(String toScore) {
        this.toScore = toScore;
    }

    public String getScoreId() {
        return scoreId;
    }

    public void setScoreId(String scoreId) {
        this.scoreId = scoreId;
    }

    public String getTeachClassId() {
        return teachClassId;
    }

    public void setTeachClassId(String teachClassId) {
        this.teachClassId = teachClassId;
    }

    public List<EmScoreInfo> getDtoList() {
        return dtoList;
    }

    public void setDtoList(List<EmScoreInfo> dtoList) {
        this.dtoList = dtoList;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getGradeType() {
        return gradeType;
    }

    public void setGradeType(String gradeType) {
        this.gradeType = gradeType;
    }

    public String getWritingScore() {
        return writingScore;
    }

    public void setWritingScore(String writingScore) {
        this.writingScore = writingScore;
    }

    public String getSpeehScore() {
        return speehScore;
    }

    public void setSpeehScore(String speehScore) {
        this.speehScore = speehScore;
    }

    public Integer getFullMark() {
        return fullMark;
    }

    public void setFullMark(Integer fullMark) {
        this.fullMark = fullMark;
    }

    public String getUnitiveCode() {
        return unitiveCode;
    }

    public void setUnitiveCode(String unitiveCode) {
        this.unitiveCode = unitiveCode;
    }

}
