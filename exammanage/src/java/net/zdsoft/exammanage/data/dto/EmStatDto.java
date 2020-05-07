package net.zdsoft.exammanage.data.dto;

import net.zdsoft.exammanage.data.entity.EmStat;


public class EmStatDto {

    private EmStat emStat = new EmStat();
    private String studentCode;
    private String studentName;
    private String className;
    private Float score;//成绩
    private Float conScore;//赋分成绩
    private Integer classRank;//（班级排名
    private Integer gradeRank;//（年级排名）
    private Float scoreT;//标准分T(年级)
    private Float progressDegree;
    private Integer progressDegreeRankClass;
    private Integer progressDegreeRankGrade;

    public EmStat getEmStat() {
        return emStat;
    }

    public void setEmStat(EmStat emStat) {
        this.emStat = emStat;
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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public Float getConScore() {
        return conScore;
    }

    public void setConScore(Float conScore) {
        this.conScore = conScore;
    }

    public Integer getClassRank() {
        return classRank;
    }

    public void setClassRank(Integer classRank) {
        this.classRank = classRank;
    }

    public Integer getGradeRank() {
        return gradeRank;
    }

    public void setGradeRank(Integer gradeRank) {
        this.gradeRank = gradeRank;
    }

    public Float getScoreT() {
        return scoreT;
    }

    public void setScoreT(Float scoreT) {
        this.scoreT = scoreT;
    }

    public Float getProgressDegree() {
        return progressDegree;
    }

    public void setProgressDegree(Float progressDegree) {
        this.progressDegree = progressDegree;
    }

    public Integer getProgressDegreeRankClass() {
        return progressDegreeRankClass;
    }

    public void setProgressDegreeRankClass(Integer progressDegreeRankClass) {
        this.progressDegreeRankClass = progressDegreeRankClass;
    }

    public Integer getProgressDegreeRankGrade() {
        return progressDegreeRankGrade;
    }

    public void setProgressDegreeRankGrade(Integer progressDegreeRankGrade) {
        this.progressDegreeRankGrade = progressDegreeRankGrade;
    }


}
