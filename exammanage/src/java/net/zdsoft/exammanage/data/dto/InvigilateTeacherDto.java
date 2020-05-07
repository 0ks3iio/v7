package net.zdsoft.exammanage.data.dto;

import java.util.Date;

public class InvigilateTeacherDto {

    private String emSubjectInfoId;
    private String emPlaceId;    //考场id
    private String examId;        //考试Id
    private String subjectId;    //科目Id
    private String subjectName;    //科目名称
    private Date startTime;        //开始时间
    private Date endTime;        //结束时间
    private Date gkStartTime;
    private Date gkEndTime;
    private String subType;
    private String examPlaceId; //场地Id
    private String examPlaceCode; //考场编号
    private String examPlaceName; //场地名称
    private String examStartAndEndNumber; //开始结束考号
    private String teacherIds;   //监考老师Id
    private String teacherNames; //监考老师名字
    private String teacherInId;    //编内监考老师Id
    private String teacherInName; //编内监考老师名字
    private String teacherOutId;    //编外监考老师Id
    private String teacherOutName; //编外监考老师名字
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

    public String getExamPlaceId() {
        return examPlaceId;
    }

    public void setExamPlaceId(String examPlaceId) {
        this.examPlaceId = examPlaceId;
    }

    public String getExamPlaceName() {
        return examPlaceName;
    }

    public void setExamPlaceName(String examPlaceName) {
        this.examPlaceName = examPlaceName;
    }

    public String getEmSubjectInfoId() {
        return emSubjectInfoId;
    }

    public void setEmSubjectInfoId(String emSubjectInfoId) {
        this.emSubjectInfoId = emSubjectInfoId;
    }

    public String getEmPlaceId() {
        return emPlaceId;
    }

    public void setEmPlaceId(String emPlaceId) {
        this.emPlaceId = emPlaceId;
    }

    public String getExamStartAndEndNumber() {
        return examStartAndEndNumber;
    }

    public void setExamStartAndEndNumber(String examStartAndEndNumber) {
        this.examStartAndEndNumber = examStartAndEndNumber;
    }

    public String getTeacherInId() {
        return teacherInId;
    }

    public void setTeacherInId(String teacherInId) {
        this.teacherInId = teacherInId;
    }

    public String getTeacherInName() {
        return teacherInName;
    }

    public void setTeacherInName(String teacherInName) {
        this.teacherInName = teacherInName;
    }

    public String getTeacherOutId() {
        return teacherOutId;
    }

    public void setTeacherOutId(String teacherOutId) {
        this.teacherOutId = teacherOutId;
    }

    public String getTeacherOutName() {
        return teacherOutName;
    }

    public void setTeacherOutName(String teacherOutName) {
        this.teacherOutName = teacherOutName;
    }

    public String getExamPlaceCode() {
        return examPlaceCode;
    }

    public void setExamPlaceCode(String examPlaceCode) {
        this.examPlaceCode = examPlaceCode;
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
