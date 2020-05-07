package net.zdsoft.diathesis.data.dto;

import javax.validation.constraints.NotBlank;

/**
 * @Author: panlf
 * @Date: 2019/7/4 17:51
 */
public class DiathesisOverallScoreDto {
    @NotBlank(message = "studentId不能为空")
    private String studentId;
    @NotBlank(message = "projectId不能为空")
    private String projectId;
    @NotBlank(message = "scoreTypeId不能为空")
    private String scoreTypeId;
    @NotBlank(message = "备注不能为空")
    private String remark;
    @NotBlank(message = "成绩不能为空")
    private String score;

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getScoreTypeId() {
        return scoreTypeId;
    }

    public void setScoreTypeId(String scoreTypeId) {
        this.scoreTypeId = scoreTypeId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
