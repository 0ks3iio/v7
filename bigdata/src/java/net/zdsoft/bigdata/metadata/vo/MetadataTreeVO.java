package net.zdsoft.bigdata.metadata.vo;

import java.util.List;

/**
 * Created by wangdongdong on 2019/5/22 20:42.
 */
public class MetadataTreeVO {

    private String id;

    private String name;

    private String type;

    private String dbType;

    private Integer status;

    private Integer isPhoenix;

    private List<MetadataTreeVO> child;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<MetadataTreeVO> getChild() {
        return child;
    }

    public void setChild(List<MetadataTreeVO> child) {
        this.child = child;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsPhoenix() {
        return isPhoenix;
    }

    public void setIsPhoenix(Integer isPhoenix) {
        this.isPhoenix = isPhoenix;
    }
}
