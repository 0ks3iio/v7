package net.zdsoft.exammanage.data.dto;

import net.zdsoft.exammanage.data.entity.EmSubjectInfo;

import java.util.List;

public class SubjectClassInfoSaveDto {
    private String examId;
    private String[] classIds;
    private String classIdStr;
    private String unitId;
    private List<EmSubjectInfo> emSubjectInfoList;

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String[] getClassIds() {
        return classIds;
    }

    public void setClassIds(String[] classIds) {
        this.classIds = classIds;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public List<EmSubjectInfo> getEmSubjectInfoList() {
        return emSubjectInfoList;
    }

    public void setEmSubjectInfoList(List<EmSubjectInfo> emSubjectInfoList) {
        this.emSubjectInfoList = emSubjectInfoList;
    }

    public String getClassIdStr() {
        return classIdStr;
    }

    public void setClassIdStr(String classIdStr) {
        this.classIdStr = classIdStr;
    }


}
