/* 
 * @(#)UnitDto.java    Created on 2017年3月4日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.basedata.dto;

import net.zdsoft.basedata.entity.Unit;

/**
 * @author cuimq
 * @version $Revision: 1.0 $, $Date: 2017年3月4日 下午1:12:17 $
 */
public class UnitDto {

    private static final long serialVersionUID = 6749721634888546719L;

    private String unitId;// 单位id
    private String unitName;// 单位名称
    private String unionCode;// 单位编号
    private Integer unitClass;// 单位类型 1.教育局 2.学校
    private String regionCode;// 行政区划
    private String regionName;// 行政区划地址

    public UnitDto() {

    }

    public UnitDto(Unit unit) {
        this.unitId = unit.getId();
        this.unitName = unit.getUnitName();
        this.unionCode = unit.getUnionCode();
        this.unitClass = unit.getUnitClass();
        this.regionCode = unit.getRegionCode();
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
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

    public Integer getUnitClass() {
        return unitClass;
    }

    public void setUnitClass(Integer unitClass) {
        this.unitClass = unitClass;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

}
