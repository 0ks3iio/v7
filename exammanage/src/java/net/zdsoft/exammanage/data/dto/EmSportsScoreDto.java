package net.zdsoft.exammanage.data.dto;

import net.zdsoft.exammanage.data.entity.ExammanageSportsScore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class EmSportsScoreDto implements Serializable {

    private List<ExammanageSportsScore> dtos = new ArrayList<>();

    public List<ExammanageSportsScore> getDtos() {
        return dtos;
    }

    public void setDtos(List<ExammanageSportsScore> dtos) {
        this.dtos = dtos;
    }
}
