package net.zdsoft.exammanage.data.dto;

import net.zdsoft.exammanage.data.entity.EmOption;

import java.util.ArrayList;
import java.util.List;

public class EmOptionDto {
    private String examId;
    private String regionId;

    private List<EmOption> emOptions = new ArrayList<EmOption>();

    public List<EmOption> getEmOptions() {
        return emOptions;
    }

    public void setEmOptions(List<EmOption> emOptions) {
        this.emOptions = emOptions;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }
}
