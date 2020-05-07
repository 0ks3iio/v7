package net.zdsoft.exammanage.data.service;

import net.zdsoft.exammanage.data.dto.EmSportsScoreDto;
import net.zdsoft.exammanage.data.entity.ExammanageSportsScore;
import net.zdsoft.framework.entity.Pagination;

import java.util.List;

public interface ExammanageSportsScoreService {

    public List<ExammanageSportsScore> findByList(String examId, String schoolId, String field, String keyWord, Pagination page);

    public void batchSave(String acadyear, String semester, String examId, EmSportsScoreDto dto);
}
