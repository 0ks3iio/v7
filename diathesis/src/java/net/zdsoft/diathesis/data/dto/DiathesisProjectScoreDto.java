package net.zdsoft.diathesis.data.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/6/6 9:44
 */
public class DiathesisProjectScoreDto {
    private String name;
    private List<DiathesisScoreCountDto> scoreList=new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DiathesisScoreCountDto> getScoreList() {
        return scoreList;
    }

    public void setScoreList(List<DiathesisScoreCountDto> scoreList) {
        this.scoreList = scoreList;
    }
}
