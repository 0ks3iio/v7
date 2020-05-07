package net.zdsoft.stutotality.data.dto;

import java.io.Serializable;

public class StudentDto implements Serializable {
    private String id;
    private String studentName;
    private Integer sex;
    private String studentCode;
    private Float result;
    private String remark;
    private String resultId;
    private String remarkSubStr;
    private String teacherContent;
    private String rewardName;
    // 获奖 备注
    private String description;

    /**
     *
     */
    private Float sickLeave;
    /**
     *
     */
    private Float casualLeave;
    /**
     *
     */
    private Float otherLeave;
    private String finalId;

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTeacherContent() {
        return teacherContent;
    }

    public void setTeacherContent(String teacherContent) {
        this.teacherContent = teacherContent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public Float getResult() {
        return result;
    }

    public void setResult(Float result) {
        this.result = result;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }

    public Float getSickLeave() {
        return sickLeave;
    }

    public void setSickLeave(Float sickLeave) {
        this.sickLeave = sickLeave;
    }

    public Float getCasualLeave() {
        return casualLeave;
    }

    public void setCasualLeave(Float casualLeave) {
        this.casualLeave = casualLeave;
    }

    public Float getOtherLeave() {
        return otherLeave;
    }

    public void setOtherLeave(Float otherLeave) {
        this.otherLeave = otherLeave;
    }

    public String getFinalId() {
        return finalId;
    }

    public void setFinalId(String finalId) {
        this.finalId = finalId;
    }

    public String getRemarkSubStr() {
        return remarkSubStr;
    }

    public void setRemarkSubStr(String remarkSubStr) {
        this.remarkSubStr = remarkSubStr;
    }
}
