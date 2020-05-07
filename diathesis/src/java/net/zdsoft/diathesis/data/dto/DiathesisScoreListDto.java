package net.zdsoft.diathesis.data.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/7/4 17:50
 */
public class DiathesisScoreListDto {
    @Valid
    @NotEmpty(message = "分数列表不能为空")
    private List<DiathesisOverallScoreDto> scoreList=new ArrayList<>();

    public List<DiathesisOverallScoreDto> getScoreList() {
        return scoreList;
    }

    public void setScoreList(List<DiathesisOverallScoreDto> scoreList) {
        this.scoreList = scoreList;
    }
}
