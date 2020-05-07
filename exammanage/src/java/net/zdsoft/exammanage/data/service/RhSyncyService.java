package net.zdsoft.exammanage.data.service;

import net.zdsoft.exammanage.data.dto.CdSaveSysncyDto;

public interface RhSyncyService {

    public String findExamList(String unitId, String acadyear, String semester);

    public String findSubjectList(String unitId, String acadyear, String semester, String examUid, String uKeyId);

    public String saveExamResultByUnitId(String ownerId, String unitId, CdSaveSysncyDto saveDto);

}
