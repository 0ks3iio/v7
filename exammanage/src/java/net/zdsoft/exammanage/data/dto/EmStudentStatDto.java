package net.zdsoft.exammanage.data.dto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EmStudentStatDto {

    List<String> scoresList = new ArrayList<String>();
    Map<String, Float> statBySubjectMap = new LinkedHashMap<String, Float>();
    Map<String, Integer> gradeRankBySubjectMap = new LinkedHashMap<String, Integer>();
    private String subjectId;
    private int classRank;//当subjectId是32个0 总分排名
    private int gradeRank;//当subjectId是32个0 总分排名
    private String studentCode;
    private String studentName;
    private String className;
    private String studentId;
    private String classId;
    private String examCode;//考号

    public List<String> getScoresList() {
        return scoresList;
    }

    public void setScoresList(List<String> scoresList) {
        this.scoresList = scoresList;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public int getClassRank() {
        return classRank;
    }

    public void setClassRank(int classRank) {
        this.classRank = classRank;
    }

    public int getGradeRank() {
        return gradeRank;
    }

    public void setGradeRank(int gradeRank) {
        this.gradeRank = gradeRank;
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

    public Map<String, Float> getStatBySubjectMap() {
        return statBySubjectMap;
    }

    public void setStatBySubjectMap(Map<String, Float> statBySubjectMap) {
        this.statBySubjectMap = statBySubjectMap;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getExamCode() {
        return examCode;
    }

    public void setExamCode(String examCode) {
        this.examCode = examCode;
    }

    public Map<String, Integer> getGradeRankBySubjectMap() {
        return gradeRankBySubjectMap;
    }

    public void setGradeRankBySubjectMap(Map<String, Integer> gradeRankBySubjectMap) {
        this.gradeRankBySubjectMap = gradeRankBySubjectMap;
    }

}
