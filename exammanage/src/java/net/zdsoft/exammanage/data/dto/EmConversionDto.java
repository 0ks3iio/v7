package net.zdsoft.exammanage.data.dto;

import net.zdsoft.exammanage.data.entity.EmConversion;

import java.util.ArrayList;
import java.util.List;

public class EmConversionDto {

    private List<EmConversion> emConlist = new ArrayList<>();
    private String unitId;

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public List<EmConversion> getEmConlist() {
        return emConlist;
    }

    public void setEmConlist(List<EmConversion> emConlist) {
        this.emConlist = emConlist;
    }
}
