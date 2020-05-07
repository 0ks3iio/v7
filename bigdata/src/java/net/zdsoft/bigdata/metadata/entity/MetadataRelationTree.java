package net.zdsoft.bigdata.metadata.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 关系图
 * Created by wangdongdong on 2019/1/11 10:49.
 */
public class MetadataRelationTree implements Serializable {

    private String id;

    private String name;

    private String mdType;

    private List<MetadataRelationTree> parent;

    private List<MetadataRelationTree> children;

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

    public String getMdType() {
        return mdType;
    }

    public void setMdType(String mdType) {
        this.mdType = mdType;
    }

    public List<MetadataRelationTree> getParent() {
        return parent;
    }

    public void setParent(List<MetadataRelationTree> parent) {
        this.parent = parent;
    }

    public List<MetadataRelationTree> getChildren() {
        return children;
    }

    public void setChildren(List<MetadataRelationTree> children) {
        this.children = children;
    }
}
