package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.entity.EmAimsInfo;

import java.util.List;

public interface EmAimsInfoService extends BaseService<EmAimsInfo, String> {

    List<EmAimsInfo> findByExams(String unitId, String acadyear, String semester);

    EmAimsInfo findByExamIds(String examId);

    void saveAims(EmAimsInfo aims);

    List<EmAimsInfo> findOpenByExamIds(String[] examIds);

}
