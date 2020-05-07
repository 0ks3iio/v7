package net.zdsoft.stutotality.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stutotality.data.entity.StutotalityScale;

import java.util.List;

public interface StutotalityScaleService extends BaseService<StutotalityScale,String>{
    //根据 学校 学年 学期 年级 类别查规则
    List<StutotalityScale> findByUnitIdAndGradeIdAndAcadyearAndSemester(String unitId, String acadyear, String semester, String gardeId ,String type);
    //根据 学校 学年 学期 类别查规则
    List<StutotalityScale> findByUnitIdAndGradeIdAndtype(String unitId, String gradeId, String type);

    List<StutotalityScale> findByUnitIdAndClassIdsAndtype(String unitId, String acadyear, String semester, String[] classIds, String type);

    //根据 学校 学年 学期 年级
    List<StutotalityScale> findByUnitIdAndGradeId(String unitId, String acadyear, String semester, String gardeId);

    void deleteByIds(String[] ids);

    void deleteByUnitIdAndAcadyearAndSemesterAndGradeId(String unitId, String acadyear, String semester, String gardeId );

    void deleteByUnitIdAndAcadyearAndSemesterAndClassId(String unitId, String acadyear, String semester, String classId );
}
