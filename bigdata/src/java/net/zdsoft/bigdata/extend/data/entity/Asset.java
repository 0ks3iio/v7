package net.zdsoft.bigdata.extend.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by wangdongdong on 2018/7/11 11:09.
 */
@Entity
@Table(name = "bg_asset")
public class Asset extends BaseEntity<String> {

    private String assetName;

    private String assetCode;

    private String apiUrl;

    private Integer orderId;

    @Transient
    private Object assetValue;

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Object getAssetValue() {
        return assetValue;
    }

    public void setAssetValue(Object assetValue) {
        this.assetValue = assetValue;
    }

    @Override
    public String fetchCacheEntitName() {
        return "Asset";
    }
}
