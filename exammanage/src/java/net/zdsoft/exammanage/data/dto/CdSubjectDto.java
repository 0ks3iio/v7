package net.zdsoft.exammanage.data.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CdSubjectDto implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String subkeyId;//容厚id
    private String subjectName;
    private String tablename;
    private float zf;//总分

    private String relaSubjectId;//关联本地科目

    private List<CdStudentDto> cdStuList = new ArrayList<>();

    public String getSubkeyId() {
        return subkeyId;
    }

    public void setSubkeyId(String subkeyId) {
        this.subkeyId = subkeyId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public float getZf() {
        return zf;
    }

    public void setZf(float zf) {
        this.zf = zf;
    }

    public List<CdStudentDto> getCdStuList() {
        return cdStuList;
    }

    public void setCdStuList(List<CdStudentDto> cdStuList) {
        this.cdStuList = cdStuList;
    }

    public String getRelaSubjectId() {
        return relaSubjectId;
    }

    public void setRelaSubjectId(String relaSubjectId) {
        this.relaSubjectId = relaSubjectId;
    }


}
