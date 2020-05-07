package net.zdsoft.diathesis.data.dto;

import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/8/15 9:58
 */
public class DiathesisStuScoreDto {
    private String stuName;
    private String stuCode;
    private String clazz;
    private String sex;
    private String subName;  //选修专用
    private List<DiatheisSubjectScoreDto> subjectList;
    private List<DiathesisFieldDto> fieldList;

    public List<DiathesisFieldDto> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<DiathesisFieldDto> fieldList) {
        this.fieldList = fieldList;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getStuCode() {
        return stuCode;
    }

    public void setStuCode(String stuCode) {
        this.stuCode = stuCode;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public List<DiatheisSubjectScoreDto> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<DiatheisSubjectScoreDto> subjectList) {
        this.subjectList = subjectList;
    }
}
