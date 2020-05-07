package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.entity.EmRegion;

import java.util.List;

public interface EmExamRegionService extends BaseService<EmRegion, String> {

    public List<EmRegion> findByExamIdAndUnitId(String examId, String unitId);

    public List<EmRegion> saveAllEntitys(EmRegion... examInfo);

    public List<EmRegion> findByExamIdAndRegionCode(String regionCode, String examId);

    public List<EmRegion> findByExamIdAndLikeCode(String examId, String regionCode, String unitId);

    public List<EmRegion> findByExamId(String examId);
}
