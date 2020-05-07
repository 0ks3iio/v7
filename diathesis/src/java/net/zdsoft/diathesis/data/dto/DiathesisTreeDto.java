package net.zdsoft.diathesis.data.dto;

import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/6/3 10:15
 */
public class DiathesisTreeDto {
    private String id;
    private String pId;
    private String name;
    private String type;
    private Boolean isParent;
    private List<DiathesisTreeDto> childList;

    public Boolean getParent() {
        return isParent;
    }

    public void setParent(Boolean parent) {
        isParent = parent;
    }

    public List<DiathesisTreeDto> getChildList() {
        return childList;
    }

    public void setChildList(List<DiathesisTreeDto> childList) {
        this.childList = childList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
