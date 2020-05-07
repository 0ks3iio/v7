package net.zdsoft.stutotality.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stutotality.data.entity.StutotalityType;

import java.util.List;

public interface StutotalityTypeService extends BaseService<StutotalityType,String>{
    /**
     * 获取对应条件的类型 注意空值
     * @param unitId
     * @param acadyear
     * @param semester
     * @param gradeId 可为空
     * @param hasStat 可为空
     * @return
     */
    List<StutotalityType> findByUnitIdAndAcadyearAndSemesterAndGradeIdAndHasStat(String unitId, String acadyear, String semester, String gradeId, Integer hasStat);

    List<StutotalityType> findByUnitIdAndAcadyearAndSemesterAndGradeIdWithMaster(String unitId, String acadyear, String semester, String gradeId);

    List<StutotalityType> findByUnitIdAndAcadyearAndSemesterAndGradeId(String unitId, String acadyear, String semester, String gradeId);

    List<StutotalityType> findByUnitId(String unitId);

    List<StutotalityType> findByUnitIdGradeId(String unitId,String gradeId);

    List<StutotalityType> findByUnitIdGradeIdWithMaster(String unitId,String gradeId);
    /**
     * 模板数据通过单位id和年级传32个0 删除对应的类别
     * @param unitId
     * @param gradeId
     */
    void deleteByUnitIdAndGradeId(String unitId,String gradeId);
    /**
     * 某学年年级数据通过单位id和年级id以及学年学期删除对应的类别
     * @param unitId
     * @param acadyear
     * @param semester
     * @param gradeId
     */
    void deleteByCons(String unitId,String acadyear,String semester,String gradeId);
}
