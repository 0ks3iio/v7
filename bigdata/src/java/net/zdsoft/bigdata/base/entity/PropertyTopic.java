package net.zdsoft.bigdata.base.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * 资产主题实体类
 * @author yangkj
 * @since 2019/5/20 10:46
 */
@Entity
@Table(name="bg_property_topic")
public class PropertyTopic extends BaseEntity<String> {

    private String name;
    private String remark;
    /**
     * @see net.zdsoft.bigdata.base.enu.PropertyTopicCustomCode
     */
    private Integer isCustom;
    /**
     * @see net.zdsoft.bigdata.base.enu.PropertyTopicOrderCode
     */
    private Integer orderId;
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;

    @Override
    public String fetchCacheEntitName() {
        return "propertyTopic";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public Integer getIsCustom() {
        return isCustom;
    }

    public void setIsCustom(Integer isCustom) {
        this.isCustom = isCustom;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}
