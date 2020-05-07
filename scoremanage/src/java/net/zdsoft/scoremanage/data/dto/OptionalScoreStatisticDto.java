package net.zdsoft.scoremanage.data.dto;

import java.util.List;

/**
 * Created by Administrator on 2018/8/29.
 */
public class OptionalScoreStatisticDto {

    private String className;
    private String studentName;
    private String studentCode;
    private String knowledgeScore;
    private String careerScore;
    private String interestScroe;
    private String societyScore;

    private List<String> scoreList;

    public List<String> getScoreList() {
        return scoreList;
    }

    public void setScoreList(List<String> scoreList) {
        this.scoreList = scoreList;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getKnowledgeScore() {
        return knowledgeScore;
    }

    public void setKnowledgeScore(String knowledgeScore) {
        this.knowledgeScore = knowledgeScore;
    }

    public String getCareerScore() {
        return careerScore;
    }

    public void setCareerScore(String careerScore) {
        this.careerScore = careerScore;
    }

    public String getInterestScroe() {
        return interestScroe;
    }

    public void setInterestScroe(String interestScroe) {
        this.interestScroe = interestScroe;
    }

    public String getSocietyScore() {
        return societyScore;
    }

    public void setSocietyScore(String societyScore) {
        this.societyScore = societyScore;
    }
}
