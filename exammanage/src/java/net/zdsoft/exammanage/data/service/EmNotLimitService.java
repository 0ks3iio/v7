package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.entity.EmNotLimit;

import java.util.List;

public interface EmNotLimitService extends BaseService<EmNotLimit, String> {

    List<String> findTeacherIdByUnitId(String unitId);

    void saveTeacherIds(String[] teacherIds, String unitId);

    void deleteByUnitId(String unitId);
}
