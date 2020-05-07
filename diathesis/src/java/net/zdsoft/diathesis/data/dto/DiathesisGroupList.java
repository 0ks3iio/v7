package net.zdsoft.diathesis.data.dto;

import net.zdsoft.diathesis.data.entity.DiathesisMutualGroup;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/7/3 16:03
 */
public class DiathesisGroupList {
    @Valid
    @NotEmpty(message = "分组信息不能为空")
    private List<DiathesisMutualGroup> groupList=new ArrayList<>();
    @NotBlank(message = "classId不能为空")
    private String classId;

    private String acadyear;

    private Integer semester;

    private String unitId;

    private String realName;

    public String getAcadyear() {
        return acadyear;
    }

    public void setAcadyear(String acadyear) {
        this.acadyear = acadyear;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public List<DiathesisMutualGroup> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<DiathesisMutualGroup> groupList) {
        this.groupList = groupList;
    }
}
