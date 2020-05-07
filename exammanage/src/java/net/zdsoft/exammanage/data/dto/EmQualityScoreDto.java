package net.zdsoft.exammanage.data.dto;

import net.zdsoft.exammanage.data.entity.ExammanageQualityScore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EmQualityScoreDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<ExammanageQualityScore> dtos = new ArrayList<>();

    public List<ExammanageQualityScore> getDtos() {
        return dtos;
    }

    public void setDtos(List<ExammanageQualityScore> dtos) {
        this.dtos = dtos;
    }

}
