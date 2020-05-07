package net.zdsoft.bigdata.extend.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by wangdongdong on 2018/7/11 11:09.
 */
@Entity
@Table(name = "bg_stat_social_circles")
public class StatSocialCircles extends BaseEntity<String> {

    private String unitId;

    private String ownerId;

    private int fsAmount;

    private int gzAmount;

    private int hyAmount;

    private int dzAmount;

    private int fxAmount;

    private Date statTime;

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public int getFsAmount() {
        return fsAmount;
    }

    public void setFsAmount(int fsAmount) {
        this.fsAmount = fsAmount;
    }

    public int getGzAmount() {
        return gzAmount;
    }

    public void setGzAmount(int gzAmount) {
        this.gzAmount = gzAmount;
    }

    public int getHyAmount() {
        return hyAmount;
    }

    public void setHyAmount(int hyAmount) {
        this.hyAmount = hyAmount;
    }

    public int getDzAmount() {
        return dzAmount;
    }

    public void setDzAmount(int dzAmount) {
        this.dzAmount = dzAmount;
    }

    public int getFxAmount() {
        return fxAmount;
    }

    public void setFxAmount(int fxAmount) {
        this.fxAmount = fxAmount;
    }

    public Date getStatTime() {
        return statTime;
    }

    public void setStatTime(Date statTime) {
        this.statTime = statTime;
    }

    @Override
    public String fetchCacheEntitName() {
        return "statAppPreference";
    }
}
