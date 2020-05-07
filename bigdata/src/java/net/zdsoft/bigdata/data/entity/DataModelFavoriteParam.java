package net.zdsoft.bigdata.data.entity;

import net.zdsoft.framework.entity.BaseEntity;
import net.zdsoft.framework.utils.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by wangdongdong on 2019/3/15 10:34.
 */
@Entity
@Table(name = "bg_model_favorite_param")
public class DataModelFavoriteParam extends BaseEntity<String> {

	private String favoriteId;

    private String paramType;

    private String paramValue;

    public String getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(String favoriteId) {
        this.favoriteId = favoriteId;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getParamValue() {
        return paramValue == null ? StringUtils.EMPTY : paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    @Override
    public String fetchCacheEntitName() {
        return "DataModelFavoriteParam";
    }
}
