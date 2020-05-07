package net.zdsoft.comprehensive.dto;

import java.util.Map;

/**
 * 传递学生总评各模块成绩、
 *
 * @since 2018/12/12
 */
public class CompreScoreMapDto {
    private String studentId;
    private String studentName;
    private String studentCode;
    private String className;
    private Integer ranking;
    private Map<String, String> scoreMap;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public Map<String, String> getScoreMap() {
        return scoreMap;
    }

    public void setScoreMap(Map<String, String> scoreMap) {
        this.scoreMap = scoreMap;
    }
}
