package net.zdsoft.diathesis.data.dto;

import javax.validation.constraints.NotBlank;

/**
 * @Author: panlf
 * @Date: 2019/7/26 14:54
 */
public class DiathesisOverallChangeDto {
    @NotBlank(message = "分数id不能为空")
    private String totalScoreId;

    @NotBlank(message = "分数不能为空")
    private String score;
    @NotBlank(message = "备注不能为空")
    private String remark;

    public String getTotalScoreId() {
        return totalScoreId;
    }

    public void setTotalScoreId(String totalScoreId) {
        this.totalScoreId = totalScoreId;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
