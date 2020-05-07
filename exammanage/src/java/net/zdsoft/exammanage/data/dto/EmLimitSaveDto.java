package net.zdsoft.exammanage.data.dto;

import net.zdsoft.exammanage.data.entity.EmLimit;

import java.util.List;


public class EmLimitSaveDto {
    //emLimitList 中id,teacherId,classIds
    public List<EmLimit> emLimitList;
    private String examId;
    private String subjectId;

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

    public List<EmLimit> getEmLimitList() {
        return emLimitList;
    }

    public void setEmLimitList(List<EmLimit> emLimitList) {
        this.emLimitList = emLimitList;
    }

}
