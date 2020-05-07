package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.entity.EmTitleInfo;

public interface EmTitleInfoService extends BaseService<EmTitleInfo, String> {
    public void deleteByExamIdAndSubjectId(String examId, String subjectId);

    void deleteByExamIds(String[] examIds);
}
