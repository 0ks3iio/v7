package net.zdsoft.bigdata.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * Created by wangdongdong on 2018/5/15 9:29.
 */
@Entity
@Table(name = "bg_tag_relaiton")
public class TagRelation extends BaseEntity<String> {

    private String businessId;

    private String tagId;

    private String tagName;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
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

    @Override
    public String fetchCacheEntitName() {
        return "tagRelation";
    }
}
