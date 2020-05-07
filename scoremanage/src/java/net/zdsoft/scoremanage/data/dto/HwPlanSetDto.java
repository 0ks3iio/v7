package net.zdsoft.scoremanage.data.dto;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/11/6 16:09
 */
public class HwPlanSetDto {
    @Valid
    List<SubjectDto> subList;

    List<String> delPlanIds;
    public List<String> getDelPlanIds() {
        return delPlanIds;
    }

    public void setDelPlanIds(List<String> delPlanIds) {
        this.delPlanIds = delPlanIds;
    }
    
    public List<SubjectDto> getSubList() {
        return subList;
    }

    public void setSubList(List<SubjectDto> subList) {
        this.subList = subList;
    }
}
