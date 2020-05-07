package net.zdsoft.szxy.operation.dto;


import java.io.Serializable;

public class TreeNode implements Serializable {

    private String id;
    private String pId;
    private String name;
    private Boolean isParent;

    public TreeNode() {}

    public TreeNode(String id, String name) {
        this.id = id;
        this.name = name;
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

    public Boolean getIsParent() {
        return isParent;
    }

    public void setIsParent(Boolean parent) {
        isParent = parent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "id='" + id + '\'' +
                ", pId='" + pId + '\'' +
                ", name='" + name + '\'' +
                ", isParent=" + isParent +
                '}';
    }
}
