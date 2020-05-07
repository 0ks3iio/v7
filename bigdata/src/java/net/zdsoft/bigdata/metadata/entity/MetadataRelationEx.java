package net.zdsoft.bigdata.metadata.entity;

import java.util.List;

/**
 * 关系图
 * Created by wangdongdong on 2019/1/11 10:49.
 */
public class MetadataRelationEx {

    private String name;

    private String value;

    private List<MetadataRelationEx> children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<MetadataRelationEx> getChildren() {
        return children;
    }

    public void setChildren(List<MetadataRelationEx> children) {
        this.children = children;
    }
}
