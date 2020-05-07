package net.zdsoft.exammanage.data.dto;

import java.util.Date;

public class InspectorsTeacherDto {

    private String subjectInfoId;
    private String examId;        //考试Id
    private String subjectId;    //科目Id
    private String subjectName;    //科目名称
    private Date startTime;        //开始时间
    private Date endTime;        //结束时间
    private Date gkStartTime;
    private Date gkEndTime;
    private String subType;
    private String teacherIds;    //巡考老师Id
    private String teacherNames; //巡考老师名字
    private String emPlaceTeacherId;

    public Date getGkEndTime() {
        return gkEndTime;
    }

    public void setGkEndTime(Date gkEndTime) {
        this.gkEndTime = gkEndTime;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getSubjectInfoId() {
        return subjectInfoId;
    }

    public void setSubjectInfoId(String subjectInfoId) {
        this.subjectInfoId = subjectInfoId;
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

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
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

    public String getTeacherIds() {
        return teacherIds;
    }

    public void setTeacherIds(String teacherIds) {
        this.teacherIds = teacherIds;
    }

    public String getTeacherNames() {
        return teacherNames;
    }

    public void setTeacherNames(String teacherNames) {
        this.teacherNames = teacherNames;
    }

    public String getEmPlaceTeacherId() {
        return emPlaceTeacherId;
    }

    public void setEmPlaceTeacherId(String emPlaceTeacherId) {
        this.emPlaceTeacherId = emPlaceTeacherId;
    }

    public Date getGkStartTime() {
        return gkStartTime;
    }

    public void setGkStartTime(Date gkStartTime) {
        this.gkStartTime = gkStartTime;
    }
}
