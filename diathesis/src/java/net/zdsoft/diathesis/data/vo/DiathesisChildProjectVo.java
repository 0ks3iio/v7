package net.zdsoft.diathesis.data.vo;

import net.zdsoft.diathesis.data.dto.DiathesisChildProjectDto;

import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/4/11 10:15
 */
public class DiathesisChildProjectVo {

    private String unitId;
    private String parentId;
    private List<String> proportions;
    private List<DiathesisChildProjectDto> childProjectList;
    private String realName;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<DiathesisChildProjectDto> getChildProjectList() {
        return childProjectList;
    }

    public void setChildProjectList(List<DiathesisChildProjectDto> childProjectList) {
        this.childProjectList = childProjectList;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitId() {
        return unitId;
    }

    public List<String> getProportions() {
        return proportions;
    }

    public void setProportions(List<String> proportions) {
        this.proportions = proportions;
    }
}
