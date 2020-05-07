package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.entity.EmAbilitySet;

import java.util.List;

public interface EmAbilitySetService extends BaseService<EmAbilitySet, String> {

    List<EmAbilitySet> findListByObjAndExamId(String statObjectId, String examId);

    void deleteByObjAndExamId(String statObjectId, String examId);
}
