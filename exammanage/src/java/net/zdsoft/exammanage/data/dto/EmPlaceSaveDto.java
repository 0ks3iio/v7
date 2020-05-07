package net.zdsoft.exammanage.data.dto;

import net.zdsoft.exammanage.data.entity.EmPlace;

import java.util.List;

public class EmPlaceSaveDto {

    private List<EmPlace> emPlaceList;
    private String examId;
    private String unitId;

    //批量
    private int avgCount;//考场容纳人数
    private String placeIds;//场地


    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public List<EmPlace> getEmPlaceList() {
        return emPlaceList;
    }

    public void setEmPlaceList(List<EmPlace> emPlaceList) {
        this.emPlaceList = emPlaceList;
    }

    public int getAvgCount() {
        return avgCount;
    }

    public void setAvgCount(int avgCount) {
        this.avgCount = avgCount;
    }

    public String getPlaceIds() {
        return placeIds;
    }

    public void setPlaceIds(String placeIds) {
        this.placeIds = placeIds;
    }


}
