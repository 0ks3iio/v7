package net.zdsoft.bigdata.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 数据模型用户集
 * Created by wangdongdong on 2018/8/28 10:34.
 */
@Entity
@Table(name = "bg_model_dataset")
public class ModelDataset extends BaseEntity<String> {

    private String unitId;

    private String modelId;

    private String dsName;

    private String dsConditionSql;

    private Integer orderId;

    @Transient
    private String orderUsers;

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getDsName() {
        return dsName;
    }

    public void setDsName(String dsName) {
        this.dsName = dsName;
    }

    public String getDsConditionSql() {
        return dsConditionSql;
    }

    public void setDsConditionSql(String dsConditionSql) {
        this.dsConditionSql = dsConditionSql;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderUsers() {
        return orderUsers;
    }

    public void setOrderUsers(String orderUsers) {
        this.orderUsers = orderUsers;
    }

    @Override
    public String fetchCacheEntitName() {
        return "ModelDataset";
    }
}
