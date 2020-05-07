package net.zdsoft.basedata.extension.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @author shenke
 * @since 2019/1/21 上午9:52
 */
@Entity
@Table(name = "base_unit_extension")
public class UnitExtension extends BaseEntity<String> {

    private String unitId;
    private Integer expireTimeType;
    @Temporal(TemporalType.TIMESTAMP)
    private Date expireTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date serviceExpireTime;
    private Integer usingNature;
    private Integer usingState;


    @Override
    public String fetchCacheEntitName() {
        return null;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public Integer getExpireTimeType() {
        return expireTimeType;
    }

    public void setExpireTimeType(Integer expireTimeType) {
        this.expireTimeType = expireTimeType;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Date getServiceExpireTime() {
        return serviceExpireTime;
    }

    public void setServiceExpireTime(Date serviceExpireTime) {
        this.serviceExpireTime = serviceExpireTime;
    }

    public Integer getUsingNature() {
        return usingNature;
    }

    public void setUsingNature(Integer usingNature) {
        this.usingNature = usingNature;
    }

    public Integer getUsingState() {
        return usingState;
    }

    public void setUsingState(Integer usingState) {
        this.usingState = usingState;
    }
}
