package net.zdsoft.diathesis.data.dto;

import net.zdsoft.diathesis.data.entity.DiathesisOption;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/4/1 14:17
 */
public class DiathesisIdAndNameDto {
    private String id;
    private String name;
    private List<DiathesisIdAndNameDto> studentList=new ArrayList<>();
    private LinkedHashMap<String,List<DiathesisOption>> list;
    private Integer childProjectCount;
    private Integer childRecordCount;
    private Integer proportion;


    public LinkedHashMap<String, List<DiathesisOption>> getList() {
        return list;
    }

    public Integer getProportion() {
        return proportion;
    }

    public void setProportion(Integer proportion) {
        this.proportion = proportion;
    }

    public Integer getChildProjectCount() {
        return childProjectCount;
    }

    public void setChildProjectCount(Integer childProjectCount) {
        this.childProjectCount = childProjectCount;
    }

    public Integer getChildRecordCount() {
        return childRecordCount;
    }

    public void setChildRecordCount(Integer childRecordCount) {
        this.childRecordCount = childRecordCount;
    }

    public void setList(LinkedHashMap<String, List<DiathesisOption>> list) {
        this.list = list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DiathesisIdAndNameDto(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public DiathesisIdAndNameDto() {
    }

    public List<DiathesisIdAndNameDto> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<DiathesisIdAndNameDto> studentList) {
        this.studentList = studentList;
    }
}
