package net.zdsoft.bigdata.data.entity;

import java.util.Date;

import javax.persistence.*;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * Created by wangdongdong on 2018/5/15.
 */
@Entity
@Table(name = "bg_tag")
public class Tag extends BaseEntity<String> {

    private String unitId;
    private Short tagType;
    private String tagName;
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
    @Transient
    private boolean selected;

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public Short getTagType() {
        return tagType;
    }

    public void setTagType(Short tagType) {
        this.tagType = tagType;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String fetchCacheEntitName() {
        return "tag";
    }
}
