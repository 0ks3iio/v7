package net.zdsoft.stutotality.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stutotality.data.entity.StutotalityItem;

import java.util.List;

public interface StutotalityItemService extends BaseService<StutotalityItem,String> {

    List<StutotalityItem> getListByTypeId(String typeId);

    List<StutotalityItem> getListByIds(String[] ids);

    List<StutotalityItem> getListByTypeIds(String[] typeIds);

    List<StutotalityItem> getListByTypeIdsWithMaster(String[] typeIds);

    List<StutotalityItem> findBySubjectIds(String[] subjectIds);

    List<StutotalityItem> findByUnitIdAndSubjectType(String unitId, String[] subjectTypes);

    List<StutotalityItem> findByUnitIdAndYearAndSemesterAndSubjectType(String unitId,String acadyaer,String semester, String[] subjectTypes);

    List<StutotalityItem> findByUnitIdAndSubjectTypeWithMaster(String unitId, String[] subjectTypes);

    List<StutotalityItem> findByParams(String unitId,String year,String semester,String gradeId,String[] subjectIds);

    List<StutotalityItem> getItemListByParams(String unitId,String acadyear,String semeter,String gradeId,Integer hasStat);

    void deleteByIds(String[] ids);

    void deleteByUnitIdAndSubjectType(String unitId, String subjectType);
}
