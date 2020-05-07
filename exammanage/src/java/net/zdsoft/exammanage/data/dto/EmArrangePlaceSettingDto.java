package net.zdsoft.exammanage.data.dto;

import net.zdsoft.exammanage.data.entity.EmPlaceSetting;

import java.util.ArrayList;
import java.util.List;

public class EmArrangePlaceSettingDto {
    private String arrangeId;
    private String examId;
    //编排方式 0：随机 1：同校不相邻 2：同班不相邻
    private String type;
    private int sumSeatNum;
    private List<EmPlaceSetting> settings = new ArrayList<>();

    public String getArrangeId() {
        return arrangeId;
    }

    public void setArrangeId(String arrangeId) {
        this.arrangeId = arrangeId;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSumSeatNum() {
        return sumSeatNum;
    }

    public void setSumSeatNum(int sumSeatNum) {
        this.sumSeatNum = sumSeatNum;
    }

    public List<EmPlaceSetting> getSettings() {
        return settings;
    }

    public void setSettings(List<EmPlaceSetting> settings) {
        this.settings = settings;
    }

}
