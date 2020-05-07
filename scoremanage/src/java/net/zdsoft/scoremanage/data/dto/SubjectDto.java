package net.zdsoft.scoremanage.data.dto;

import net.zdsoft.scoremanage.data.constant.HwConstants;

import javax.validation.constraints.NotBlank;

/**
 * @Author: panlf
 * @Date: 2019/11/6 15:08
 */
public class SubjectDto {
    @NotBlank(message = "方案Id不能为空")
    private String hwPlanId;
    @NotBlank(message = "科目id 不能为空")
    private String objKey;
    private String subjectName;
    @NotBlank(message = "成绩类型不能为空")
    private String scoreType="1"; //默认为考试成绩

    public String getHwPlanId() {
        return hwPlanId;
    }

    public void setHwPlanId(String hwPlanId) {
        this.hwPlanId = hwPlanId;
    }

    public String getObjKey() {
        return objKey;
    }

    public void setObjKey(String objKey) {
        this.objKey = objKey;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getScoreType() {
        return scoreType;
    }

    public void setScoreType(String scoreType) {
        this.scoreType = scoreType;
    }

    public static SubjectDto getSumAndRank(String hwPlanId){
        SubjectDto sumAndRank = new SubjectDto();
        sumAndRank.setObjKey(HwConstants.SUM_SCORE_RANK);
        sumAndRank.setSubjectName("总分排名");
        sumAndRank.setHwPlanId(hwPlanId);
        return sumAndRank;
    }
}
