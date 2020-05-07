package net.zdsoft.exammanage.data.dto;

import java.util.HashMap;
import java.util.Map;

public class EmRangeStatDto {
    private String statRangeId;//整体统计结果的id

    private String subjectId;
    private String subjectName;//科目

    private Map<String, Float> avgMap = new HashMap<String, Float>();//key:前10%等

    private float averageScore;//总体平均分
    private float maxScore;//最大值
    private float minScore;//最小值
    private int statStuNum;//统计人数

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

    public Map<String, Float> getAvgMap() {
        return avgMap;
    }

    public void setAvgMap(Map<String, Float> avgMap) {
        this.avgMap = avgMap;
    }

    public float getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(float averageScore) {
        this.averageScore = averageScore;
    }

    public float getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(float maxScore) {
        this.maxScore = maxScore;
    }

    public float getMinScore() {
        return minScore;
    }

    public void setMinScore(float minScore) {
        this.minScore = minScore;
    }

    public int getStatStuNum() {
        return statStuNum;
    }

    public void setStatStuNum(int statStuNum) {
        this.statStuNum = statStuNum;
    }

    public String getStatRangeId() {
        return statRangeId;
    }

    public void setStatRangeId(String statRangeId) {
        this.statRangeId = statRangeId;
    }

}
