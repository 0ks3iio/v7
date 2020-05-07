package net.zdsoft.exammanage.data.dto;

import net.zdsoft.exammanage.data.entity.EmAbilitySet;

import java.util.ArrayList;
import java.util.List;

public class EmSubDto {
    private String subName;
    private String startDate;
    private String endDate;

    private List<EmAbilitySet> abiList = new ArrayList<>();

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<EmAbilitySet> getAbiList() {
        return abiList;
    }

    public void setAbiList(List<EmAbilitySet> abiList) {
        this.abiList = abiList;
    }

}
