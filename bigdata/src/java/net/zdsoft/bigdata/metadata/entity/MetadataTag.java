package net.zdsoft.bigdata.metadata.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * 元数据
 * Created by wangdongdong on 2019/1/4 10:36.
 */
@Entity
@Table(name = "bg_metadata_tag")
public class MetadataTag extends BaseEntity<String> {

    private String mdId;

    private String tagId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;

    public String getMdId() {
        return mdId;
    }

    public void setMdId(String mdId) {
        this.mdId = mdId;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public String fetchCacheEntitName() {
        return "MetadataTag";
    }
}
