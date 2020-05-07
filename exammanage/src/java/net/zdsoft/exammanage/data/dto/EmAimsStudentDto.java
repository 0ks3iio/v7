package net.zdsoft.exammanage.data.dto;

import net.zdsoft.basedata.entity.Student;

import java.util.Date;

public class EmAimsStudentDto {
    private String examCode;
    private String examName;
    private String examType;
    private Date startDate;
    private Date endDate;
    private String aimsSchoolName;
    private String aimsSchoolId;
    private String aimsId;
    private String state;//0待填报，1已填报
    private String isEdit;
    private Student student;
    private String schoolName;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getIsEdit() {
        return isEdit;
    }

    public void setIsEdit(String isEdit) {
        this.isEdit = isEdit;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getExamCode() {
        return examCode;
    }

    public void setExamCode(String examCode) {
        this.examCode = examCode;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getAimsSchoolName() {
        return aimsSchoolName;
    }

    public void setAimsSchoolName(String aimsSchoolName) {
        this.aimsSchoolName = aimsSchoolName;
    }

    public String getAimsSchoolId() {
        return aimsSchoolId;
    }

    public void setAimsSchoolId(String aimsSchoolId) {
        this.aimsSchoolId = aimsSchoolId;
    }

    public String getAimsId() {
        return aimsId;
    }

    public void setAimsId(String aimsId) {
        this.aimsId = aimsId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
