package net.zdsoft.newgkelective.data.dto;

public class NewGkChoResourceDto {
    private String courseName;
    private int totalTimeCount;
    private int teacherCount;
    private String totalTimeAverage;

    // 选考详情
    private int examChosenNum;
    private int examClassNum;
    private int examClassStudentAverage;
    private int examTimeCount;
    private int examTotalTimeCount;

    // 学考详情
    private int studyChosenNum;
    private int studyClassNum;
    private int studyClassStudentAverage;
    private int studyTimeCount;
    private int studyTotalTimeCount;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getTotalTimeCount() {
        return totalTimeCount;
    }

    public void setTotalTimeCount(int totalTimeCount) {
        this.totalTimeCount = totalTimeCount;
    }

    public String getTotalTimeAverage() {
        return totalTimeAverage;
    }

    public void setTotalTimeAverage(String totalTimeAverage) {
        this.totalTimeAverage = totalTimeAverage;
    }

    public int getTeacherCount() {
        return teacherCount;
    }

    public void setTeacherCount(int teacherCount) {
        this.teacherCount = teacherCount;
    }

    public int getExamChosenNum() {
        return examChosenNum;
    }

    public void setExamChosenNum(int examChosenNum) {
        this.examChosenNum = examChosenNum;
    }

    public int getExamClassNum() {
        return examClassNum;
    }

    public void setExamClassNum(int examClassNum) {
        this.examClassNum = examClassNum;
    }

    public int getExamClassStudentAverage() {
        return examClassStudentAverage;
    }

    public void setExamClassStudentAverage(int examClassStudentAverage) {
        this.examClassStudentAverage = examClassStudentAverage;
    }

    public int getExamTimeCount() {
        return examTimeCount;
    }

    public void setExamTimeCount(int examTimeCount) {
        this.examTimeCount = examTimeCount;
    }

    public int getExamTotalTimeCount() {
        return examTotalTimeCount;
    }

    public void setExamTotalTimeCount(int examTotalTimeCount) {
        this.examTotalTimeCount = examTotalTimeCount;
    }

    public int getStudyChosenNum() {
        return studyChosenNum;
    }

    public void setStudyChosenNum(int studyChosenNum) {
        this.studyChosenNum = studyChosenNum;
    }

    public int getStudyClassNum() {
        return studyClassNum;
    }

    public void setStudyClassNum(int studyClassNum) {
        this.studyClassNum = studyClassNum;
    }

    public int getStudyClassStudentAverage() {
        return studyClassStudentAverage;
    }

    public void setStudyClassStudentAverage(int studyClassStudentAverage) {
        this.studyClassStudentAverage = studyClassStudentAverage;
    }

    public int getStudyTimeCount() {
        return studyTimeCount;
    }

    public void setStudyTimeCount(int studyTimeCount) {
        this.studyTimeCount = studyTimeCount;
    }

    public int getStudyTotalTimeCount() {
        return studyTotalTimeCount;
    }

    public void setStudyTotalTimeCount(int studyTotalTimeCount) {
        this.studyTotalTimeCount = studyTotalTimeCount;
    }
}
