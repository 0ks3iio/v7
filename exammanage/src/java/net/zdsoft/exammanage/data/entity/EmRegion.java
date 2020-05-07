package net.zdsoft.exammanage.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "exammanage_region")
public class EmRegion extends BaseEntity<String> {

    private String examId;
    private String unitId;
    private String examRegionCode;
    private Integer examOptionNum;
    private String regionCode;
    private String regionName;

    @Override
    public String fetchCacheEntitName() {
        return "emRegion";
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getExamRegionCode() {
        return examRegionCode;
    }

    public void setExamRegionCode(String examRegionCode) {
        this.examRegionCode = examRegionCode;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public Integer getExamOptionNum() {
        return examOptionNum;
    }

    public void setExamOptionNum(Integer examOptionNum) {
        this.examOptionNum = examOptionNum;
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
