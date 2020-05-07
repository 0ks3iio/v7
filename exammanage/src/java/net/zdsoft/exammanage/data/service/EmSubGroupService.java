package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.entity.EmSubGroup;

import java.util.List;

public interface EmSubGroupService extends BaseService<EmSubGroup, String> {

    List<EmSubGroup> findListByExamId(String examId);

    List<EmSubGroup> findByExamIdAndSubjectId(String examId, String subId);

    void deleteByExamIdAndUnitId(String examId, String unitId);

    List<EmSubGroup> findByExamIdAndSubIdAndSubType(String unitId, String examId, String subId, String subType);
}
