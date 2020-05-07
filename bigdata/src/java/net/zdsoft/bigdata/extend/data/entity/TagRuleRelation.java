package net.zdsoft.bigdata.extend.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wangdongdong on 2018/7/11 13:52.
 */
@Entity
@Table(name = "bg_tag_rule_relation")
public class TagRuleRelation extends BaseEntity<String> {

    private String profileCode;

    private String projectId;

    private String tagId;

    private String tagName;

    private Short groupId;
    private String mdId;
    private String mdColumnId;
    
    private Short ruleType;
    @Transient
    private String ruleTypeName;

    private String ruleSymbolId;

    private String ruleSymbolName;

    private String result;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;

    @Transient
    private String metadataName;
    @Transient
    private String metadataColumnName;

    @Override
    public String fetchCacheEntitName() {
        return "tagRuleRelation";
    }

    public String getProfileCode() {
        return profileCode;
    }

    public void setProfileCode(String profileCode) {
        this.profileCode = profileCode;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
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

    public Short getGroupId() {
        return groupId;
    }

    public void setGroupId(Short groupId) {
        this.groupId = groupId;
    }

    public String getRuleSymbolId() {
        return ruleSymbolId;
    }

    public void setRuleSymbolId(String ruleSymbolId) {
        this.ruleSymbolId = ruleSymbolId;
    }

    public String getRuleSymbolName() {
        return ruleSymbolName;
    }

    public void setRuleSymbolName(String ruleSymbolName) {
        this.ruleSymbolName = ruleSymbolName;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
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

    public Short getRuleType() {
        return ruleType;
    }

    public void setRuleType(Short ruleType) {
        this.ruleType = ruleType;
    }

    public String getRuleTypeName() {
        return ruleTypeName;
    }

    public void setRuleTypeName(String ruleTypeName) {
        this.ruleTypeName = ruleTypeName;
    }

	public String getMdId() {
		return mdId;
	}

	public void setMdId(String mdId) {
		this.mdId = mdId;
	}

	public String getMdColumnId() {
		return mdColumnId;
	}

	public void setMdColumnId(String mdColumnId) {
		this.mdColumnId = mdColumnId;
	}

    public String getMetadataName() {
        return metadataName;
    }

    public void setMetadataName(String metadataName) {
        this.metadataName = metadataName;
    }

    public String getMetadataColumnName() {
        return metadataColumnName;
    }

    public void setMetadataColumnName(String metadataColumnName) {
        this.metadataColumnName = metadataColumnName;
    }
}
