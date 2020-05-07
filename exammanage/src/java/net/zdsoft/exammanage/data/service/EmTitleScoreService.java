package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.entity.EmTitleScore;

public interface EmTitleScoreService extends BaseService<EmTitleScore, String> {
    public void deleteByExamIdAndSubjectId(String examId, String subjectId);

    void deleteByExamIds(String[] examIds);
}
