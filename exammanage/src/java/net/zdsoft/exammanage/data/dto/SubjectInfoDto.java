package net.zdsoft.exammanage.data.dto;

import net.zdsoft.exammanage.data.entity.EmSubjectInfo;
import net.zdsoft.framework.annotation.ColumnInfo;

/**
 * scoremanage SubjectInfo
 *
 * @author XuW
 */
public class SubjectInfoDto {
    private String id;
    @ColumnInfo(displayName = "单位id", nullable = false)
    private String unitId;
    @ColumnInfo(displayName = "考试id", nullable = false)
    private String examId;
    @ColumnInfo(displayName = "科目id", nullable = false)
    private String subjectId;
    @ColumnInfo(displayName = "考试方式", nullable = false, mcodeId = "DM-KSFS")
    private String examMode;
    @ColumnInfo(displayName = "成绩录入方式", nullable = false, mcodeId = "DM-CJLRXSFS")
    private String inputType;
    @ColumnInfo(displayName = "满分值")
    private Float fullScore;
    @ColumnInfo(displayName = "范围")
    private String rangeType;
    @ColumnInfo(displayName = "等分", mcodeId = "DM-DDMC")
    private String gradeType;

    public EmSubjectInfo getSubjectInfoByDto() {
        EmSubjectInfo subInfo = new EmSubjectInfo();
        subInfo.setUnitId(unitId);
        subInfo.setExamId(examId);
        subInfo.setSubjectId(subjectId);
        subInfo.setInputType(inputType);
        subInfo.setFullScore(fullScore);
        subInfo.setGradeType(gradeType);//examMode rangeType
        return subInfo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getExamMode() {
        return examMode;
    }

    public void setExamMode(String examMode) {
        this.examMode = examMode;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public Float getFullScore() {
        return fullScore;
    }

    public void setFullScore(Float fullScore) {
        this.fullScore = fullScore;
    }

    public String getRangeType() {
        return rangeType;
    }

    public void setRangeType(String rangeType) {
        this.rangeType = rangeType;
    }

    public String getGradeType() {
        return gradeType;
    }

    public void setGradeType(String gradeType) {
        this.gradeType = gradeType;
    }


}
