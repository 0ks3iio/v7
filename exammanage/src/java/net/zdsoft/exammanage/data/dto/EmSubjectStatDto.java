package net.zdsoft.exammanage.data.dto;

import java.util.HashMap;
import java.util.Map;

public class EmSubjectStatDto {
    private String subjectId;
    private String subjectName;
    private float gradeAvg;
    private float classAvg;
    private float classMax;
    private float classMin;
    private int statStuNum;
    private int rank;
    private Map<String, Float> spaceMap = new HashMap<String, Float>();

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

    public float getGradeAvg() {
        return gradeAvg;
    }

    public void setGradeAvg(float gradeAvg) {
        this.gradeAvg = gradeAvg;
    }

    public float getClassAvg() {
        return classAvg;
    }

    public void setClassAvg(float classAvg) {
        this.classAvg = classAvg;
    }

    public float getClassMax() {
        return classMax;
    }

    public void setClassMax(float classMax) {
        this.classMax = classMax;
    }

    public float getClassMin() {
        return classMin;
    }

    public void setClassMin(float classMin) {
        this.classMin = classMin;
    }

    public int getStatStuNum() {
        return statStuNum;
    }

    public void setStatStuNum(int statStuNum) {
        this.statStuNum = statStuNum;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Map<String, Float> getSpaceMap() {
        return spaceMap;
    }

    public void setSpaceMap(Map<String, Float> spaceMap) {
        this.spaceMap = spaceMap;
    }


}
