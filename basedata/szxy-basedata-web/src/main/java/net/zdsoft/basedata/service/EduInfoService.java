package net.zdsoft.basedata.service;

import net.zdsoft.basedata.entity.EduInfo;

public interface EduInfoService extends BaseService<EduInfo, String> {

    void deleteEduInfosByUnitId(String unitId);
}
