package net.zdsoft.diathesis.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.diathesis.data.entity.DiathesisScoreInfo;

import java.util.List;

public interface DiathesisScoreInfoService extends BaseService<DiathesisScoreInfo, String> {

    List<DiathesisScoreInfo> findByTypeAndScoreTypeId(String type, String scoreTypeId);

    List<DiathesisScoreInfo> findByStudentIdAndScoreTypeIdIn(String studentId, String[] scoreTypeIds);

    void deleteByScoreTypeId(String scoreTypeId);
    
    List<DiathesisScoreInfo> findByUnitIdAndStudentId(String unitId,String studentId);
    
    List<DiathesisScoreInfo> findByScoreTypeIdIn(String[] scoreTypeIds);
    
    void deleteByScoreTypeIdIn(String[] scoreTypeIds);

    boolean isUsingByUnitId(String unitId);


    List<DiathesisScoreInfo> findByUnitIdAndProjectIdIn(String unitId, List<String> projectIdList);


    List<DiathesisScoreInfo> findByScoreTypeIdAndStuId(String scoreTypeId, String stuId);

    List<DiathesisScoreInfo> findByUnitIdAndProjectIdInAndScoreTypeIdIn(String unitId, List<String> projectIds, List<String> typeList);

    List<DiathesisScoreInfo> findByUnitIdAndProjectIdInAndScoreTypeIdInAndStudentId(String unitId, List<String> projectIds, List<String> typeList, String studentId);

    List<DiathesisScoreInfo> findByScoreTypeIdAndStuIdAndProjectIdIn(String id, String studentId, List<String> topIds);

    List<DiathesisScoreInfo> findByUnitIdAndProjectIdInAndScoreTypeIdInAndEvaluateId(String unitId, List<String> projectIds, List<String> typeList, String evaluateStuId);
}
