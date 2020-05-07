package net.zdsoft.diathesis.data.dto;

import javax.validation.constraints.NotBlank;

/**
 * @Author: panlf
 * @Date: 2019/6/28 10:22
 */
public class DiathesisGetCreditDto {
    @NotBlank(message = "gradeId不能为空")
    private String gradeId;
    @NotBlank(message = "semester不能为空")
    private String semester;
    @NotBlank(message = "acadyear不能为空")
    private String acadyear;
    @NotBlank(message = "scoreType不能为空")
    private String scoreType;

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getAcadyear() {
        return acadyear;
    }

    public void setAcadyear(String acadyear) {
        this.acadyear = acadyear;
    }

    public String getScoreType() {
        return scoreType;
    }

    public void setScoreType(String scoreType) {
        this.scoreType = scoreType;
    }
}
