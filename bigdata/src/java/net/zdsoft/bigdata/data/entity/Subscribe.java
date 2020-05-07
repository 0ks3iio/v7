/**
 * FileName: Subscribe.java
 * Author:   shenke
 * Date:     2018/5/18 下午1:13
 * Descriptor:
 */
package net.zdsoft.bigdata.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 订阅关系表
 * @author shenke
 * @since 2018/5/18 下午1:13
 */
@Entity
@Table(name = "bg_subscribe")
public class Subscribe extends BaseEntity<String> {

    @Override
    public String fetchCacheEntitName() {
        return "";
    }

    private String businessId;
    private Integer orderType;
    private String unitId;
    private String userId;
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }
}
