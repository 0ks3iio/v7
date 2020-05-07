package net.zdsoft.diathesis.data.dto;

import net.zdsoft.framework.entity.Json;

public class DiathesisScoreInfoDto {
    private String studentId;
    private String studentCode;
    private String studentName;
    private String sex;
    private String className;
    private Json[] mainScoreList;
    private String[] scoreList;

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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Json[] getMainScoreList() {
        return mainScoreList;
    }

    public void setMainScoreList(Json[] mainScoreList) {
        this.mainScoreList = mainScoreList;
    }

    public String[] getScoreList() {
        return scoreList;
    }

    public void setScoreList(String[] scoreList) {
        this.scoreList = scoreList;
    }
}
