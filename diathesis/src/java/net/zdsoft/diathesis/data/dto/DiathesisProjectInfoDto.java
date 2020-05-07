package net.zdsoft.diathesis.data.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/6/3 13:33
 */
public class DiathesisProjectInfoDto {
    private String name;
    private List<DiathesisScoreDto> scoreList=new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DiathesisScoreDto> getScoreList() {
        return scoreList;
    }

    public void setScoreList(List<DiathesisScoreDto> scoreList) {
        this.scoreList = scoreList;
    }
}
