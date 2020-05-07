package net.zdsoft.diathesis.data.dto;

import java.util.List;

/**
 * @Date: 2019/03/28
 *
 */
public class DiathesisScoreTypeDto {
    private String scoreId;
    private String scoreType;
    private String scoreName;
    private String importTime;
    private String type;
    //已经统计的百分比
    private String percentage="0";

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    ;





    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private List<String> unCompletedSub;



    public List<String> getUnCompletedSub() {
        return unCompletedSub;
    }

    public void setUnCompletedSub(List<String> unCompletedSub) {
        this.unCompletedSub = unCompletedSub;
    }

    public String getScoreId() {
        return scoreId;
    }

    public void setScoreId(String scoreId) {
        this.scoreId = scoreId;
    }

    public String getScoreType() {
        return scoreType;
    }

    public void setScoreType(String scoreType) {
        this.scoreType = scoreType;
    }

    public String getScoreName() {
        return scoreName;
    }

    public void setScoreName(String scoreName) {
        this.scoreName = scoreName;
    }

    public String getImportTime() {
        return importTime;
    }

    public void setImportTime(String importTime) {
        this.importTime = importTime;
    }
}
