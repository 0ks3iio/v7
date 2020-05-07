package net.zdsoft.bigdata.extend.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by wangdongdong on 2018/7/9 11:11.
 */
@Entity
@Table(name = "bg_user_tag")
public class UserTag extends BaseEntity<String> {

    private String profileCode;

    private String tagName;

    private Short tagType;

    private String parentId;

    private Short isMainQc;

    private Short isSecondaryQc;

    private Short isMultipleChoice;

    private String updateCycle;

    private Integer orderId;

    private String targetColumn;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;

    public String getProfileCode() {
        return profileCode;
    }

    public void setProfileCode(String profileCode) {
        this.profileCode = profileCode;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Short getTagType() {
        return tagType;
    }

    public void setTagType(Short tagType) {
        this.tagType = tagType;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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

    public Short getIsMainQc() {
        return isMainQc;
    }

    public void setIsMainQc(Short isMainQc) {
        this.isMainQc = isMainQc;
    }

    public Short getIsSecondaryQc() {
        return isSecondaryQc;
    }

    public void setIsSecondaryQc(Short isSecondaryQc) {
        this.isSecondaryQc = isSecondaryQc;
    }

    public Short getIsMultipleChoice() {
        return isMultipleChoice;
    }

    public void setIsMultipleChoice(Short isMultipleChoice) {
        this.isMultipleChoice = isMultipleChoice;
    }

    public String getUpdateCycle() {
        return updateCycle;
    }

    public void setUpdateCycle(String updateCycle) {
        this.updateCycle = updateCycle;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getTargetColumn() {
        return targetColumn;
    }

    public void setTargetColumn(String targetColumn) {
        this.targetColumn = targetColumn;
    }

    @Override
    public String fetchCacheEntitName() {
        return "userTag";
    }
}
