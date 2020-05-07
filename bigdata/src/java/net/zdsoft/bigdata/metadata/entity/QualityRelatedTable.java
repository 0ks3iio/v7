package net.zdsoft.bigdata.metadata.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 *  元数据的关联表
 * @author zhanwz
 * @since 2019-6-20
 */
@Entity
@Table(name="bg_metadata_related_table")
public class QualityRelatedTable extends BaseEntity<String> {
    private String id;

    /**
     * 主表id
     */
    @NotBlank
    private String mdId;

    /**
     * 关联表id
     */
    @NotBlank
    private String relatedMdId;

    /**
     * 主表的字段
     */
    @NotBlank
    private String mdColumnId;

    /**
     * 关联表的字段
     */
    @NotBlank
    private String relatedColumnId;

    /**
     * default sysdate not null
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;

    /**
     * default sysdate not null
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getMdId() {
        return mdId;
    }

    public void setMdId(String mdId) {
        this.mdId = mdId;
    }

    public String getRelatedMdId() {
        return relatedMdId;
    }

    public void setRelatedMdId(String relatedMdId) {
        this.relatedMdId = relatedMdId;
    }

    public String getMdColumnId() {
        return mdColumnId;
    }

    public void setMdColumnId(String mdColumnId) {
        this.mdColumnId = mdColumnId;
    }

    public String getRelatedColumnId() {
        return relatedColumnId;
    }

    public void setRelatedColumnId(String relatedColumnId) {
        this.relatedColumnId = relatedColumnId;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    @Override
    public String fetchCacheEntitName() {
        return "qualityAssociativeTable";
    }
}
