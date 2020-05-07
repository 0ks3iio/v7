package net.zdsoft.bigdata.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 数据模型用户集
 * Created by wangdongdong on 2018/8/28 10:34.
 */
@Entity
@Table(name = "bg_model_dataset_user")
public class ModelDatasetUser extends BaseEntity<String> {

    private String unitId;

    private String modelId;

    private String dsId;

    private String userId;

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

    public String getDsId() {
        return dsId;
    }

    public void setDsId(String dsId) {
        this.dsId = dsId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String fetchCacheEntitName() {
        return "ModelDatasetUser";
    }
}
