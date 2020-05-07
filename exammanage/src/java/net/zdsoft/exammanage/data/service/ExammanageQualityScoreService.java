package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.dto.EmQualityScoreDto;
import net.zdsoft.exammanage.data.entity.ExammanageQualityScore;
import net.zdsoft.framework.entity.Pagination;

import java.util.List;

public interface ExammanageQualityScoreService extends BaseService<ExammanageQualityScore, String> {

    List<ExammanageQualityScore> findByList(String examId, String schoolId, String field, String keyWord, Pagination page);

    void batchSave(String acadyear, String semester, String examId, EmQualityScoreDto dto);

}
