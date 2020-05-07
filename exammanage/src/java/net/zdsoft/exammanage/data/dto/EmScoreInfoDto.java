package net.zdsoft.exammanage.data.dto;

import net.zdsoft.exammanage.data.entity.EmScoreInfo;

public class EmScoreInfoDto {
    private String examNum;//考号
    private String stuName;
    private String placeCode;
    private String stuCode;//学号
    private String className;
    private String teachClassName;
    private EmScoreInfo scoreInfo;
    private String classId;
    private String studentId;

    public String getTeachClassName() {
        return teachClassName;
    }

    public void setTeachClassName(String teachClassName) {
        this.teachClassName = teachClassName;
    }

    public String getExamNum() {
        return examNum;
    }

    public void setExamNum(String examNum) {
        this.examNum = examNum;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getPlaceCode() {
        return placeCode;
    }

    public void setPlaceCode(String placeCode) {
        this.placeCode = placeCode;
    }

    public String getStuCode() {
        return stuCode;
    }

    public void setStuCode(String stuCode) {
        this.stuCode = stuCode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public EmScoreInfo getScoreInfo() {
        return scoreInfo;
    }

    public void setScoreInfo(EmScoreInfo scoreInfo) {
        this.scoreInfo = scoreInfo;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }


}
