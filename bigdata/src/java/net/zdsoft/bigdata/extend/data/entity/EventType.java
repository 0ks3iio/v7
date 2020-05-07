package net.zdsoft.bigdata.extend.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 事件类型
 * Created by wangdongdong on 2018/9/25 16:52.
 */
@Entity
@Table(name = "bg_event_type")
public class EventType extends BaseEntity<String> {

    @Override
    public String fetchCacheEntitName() {
        return "EventType";
    }

    private String unitId;

    private String typeName;

    private Integer orderId;

    private String remark;

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
