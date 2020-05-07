package net.zdsoft.diathesis.data.dto;

import net.zdsoft.basedata.dto.BaseDto;

import java.util.List;

/**
 * @DATE: 2019/04/03
 * 对应年级下班级列表
 *
 */
public class DiathesisComprehensiveDto extends BaseDto {
    private String diathesisId;
    private String diathesisName;
    private String evaluationTypes;
    private String[] scoreList;
    private List<DiathesisComprehensiveDto> subList;
    private String remark;
    private String totalScore;
    private String totalRemark;
    private String totalScoreId;

    public String getTotalScoreId() {
        return totalScoreId;
    }

    public void setTotalScoreId(String totalScoreId) {
        this.totalScoreId = totalScoreId;
    }

    public String getTotalRemark() {
        return totalRemark;
    }

    public void setTotalRemark(String totalRemark) {
        this.totalRemark = totalRemark;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public String getDiathesisId() {
        return diathesisId;
    }

    public void setDiathesisId(String diathesisId) {
        this.diathesisId = diathesisId;
    }

    public String getDiathesisName() {
        return diathesisName;
    }

    public void setDiathesisName(String diathesisName) {
        this.diathesisName = diathesisName;
    }

    public String getEvaluationTypes() {
        return evaluationTypes;
    }

    public void setEvaluationTypes(String evaluationTypes) {
        this.evaluationTypes = evaluationTypes;
    }

    public String[] getScoreList() {
        return scoreList;
    }

    public void setScoreList(String[] scoreList) {
        this.scoreList = scoreList;
    }

    public List<DiathesisComprehensiveDto> getSubList() {
        return subList;
    }

    public void setSubList(List<DiathesisComprehensiveDto> subList) {
        this.subList = subList;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
