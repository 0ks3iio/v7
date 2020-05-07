package net.zdsoft.diathesis.data.dto;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/6/24 17:12
 */
public class DiathesisGetScoreDto {
    @NotBlank(message = "年级id不能为空")
    public String gradeId;
    @NotBlank(message = "学期不能为空")
    public String semester;

    public String gradeCode;

    public String examId;
    @NotBlank(message = "选修/必修类型不能为空 1:必修  2:选修")
    public String scoreType;

    public List<String> subjectIdList;

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

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getScoreType() {
        return scoreType;
    }

    public void setScoreType(String scoreType) {
        this.scoreType = scoreType;
    }

    public List<String> getSubjectIdList() {
        return subjectIdList;
    }

    public void setSubjectIdList(List<String> subjectIdList) {
        this.subjectIdList = subjectIdList;
    }
}
