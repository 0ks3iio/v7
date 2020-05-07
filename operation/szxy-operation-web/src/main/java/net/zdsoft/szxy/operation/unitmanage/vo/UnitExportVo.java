package net.zdsoft.szxy.operation.unitmanage.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

public class UnitExportVo extends BaseRowModel {
    @ExcelProperty(value = "序号", index = 0)
    private Integer index;
    @ExcelProperty(value = "单位名称", index = 1)
    private String unitName;
    @ExcelProperty(value = "单位编号", index = 2)
    private String unionCode;
    @ExcelProperty(value = "行政区划", index = 3)
    private String regionName;
    @ExcelProperty(value = "单位类型", index = 4)
    private String unitType;
    @ExcelProperty(value = "到期时间", index = 5)
    private String expireTime;
    @ExcelProperty(value = "子系统到期数量", index = 6)
    private Integer systemCount;
    @ExcelProperty(value="使用状态",index=7)
    private String usingState;
    @ExcelProperty(value="使用类型",index=8)
    private String usingNature;

    public String getUsingNature() {
        return usingNature;
    }

    public void setUsingNature(String usingNature) {
        this.usingNature = usingNature;
    }

    public String getUsingState() {
        return usingState;
    }

    public void setUsingState(String usingState) {
        this.usingState = usingState;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
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

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public Integer getSystemCount() {
        return systemCount;
    }

    public void setSystemCount(Integer systemCount) {
        this.systemCount = systemCount;
    }
}
