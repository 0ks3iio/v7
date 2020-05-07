package net.zdsoft.szxy.operation.unitmanage.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

import java.util.Date;

public class UnitExportDto  {
    private String id;
    private String unitName;
    private String unionCode;
    private String regionCode;
    private Integer unitType;
    private Date expireTime;
    private Integer expireTimeType;
    private Integer usingNature;
    private Integer usingState;
    private Integer SysExpiredCount;
    private String regionName;




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public Integer getUnitType() {
        return unitType;
    }

    public void setUnitType(Integer unitType) {
        this.unitType = unitType;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Integer getExpireTimeType() {
        return expireTimeType;
    }

    public void setExpireTimeType(Integer expireTimeType) {
        this.expireTimeType = expireTimeType;
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

    public Integer getSysExpiredCount() {
        return SysExpiredCount;
    }

    public void setSysExpiredCount(Integer sysExpiredCount) {
        SysExpiredCount = sysExpiredCount;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
}
