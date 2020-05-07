package net.zdsoft.exammanage.data.dto;

import java.util.ArrayList;
import java.util.List;

public class EmSubDateDto {

    private String date;//日期
    private List<EmSubDto> subDtos = new ArrayList<>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<EmSubDto> getSubDtos() {
        return subDtos;
    }

    public void setSubDtos(List<EmSubDto> subDtos) {
        this.subDtos = subDtos;
    }

}
