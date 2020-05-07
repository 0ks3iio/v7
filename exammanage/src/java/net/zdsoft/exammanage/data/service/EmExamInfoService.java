package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.dto.EmExamInfoSearchDto;
import net.zdsoft.exammanage.data.entity.EmExamInfo;
import net.zdsoft.exammanage.data.entity.EmJoinexamschInfo;

import java.util.List;

public interface EmExamInfoService extends BaseService<EmExamInfo, String> {
    /**
     * @param day       30天  为空:则不受时间影响
     * @param unitId
     * @param searchDto
     * @param flag      true:30天以内(包括30天)新增  false:30天前新增
     * @return
     */
    public List<EmExamInfo> findExamList(Integer day, String unitId, EmExamInfoSearchDto searchDto, boolean flag);

    public List<String> findExamCodeMax();

    public void saveExamInfoOne(EmExamInfo examInfo, List<EmJoinexamschInfo> joinexamschInfoAddList);

    public List<EmExamInfo> saveAllEntitys(EmExamInfo... examInfo);

    public EmExamInfo findExamInfoOne(String id);

    public void deleteAllIsDeleted(String... id);

    public List<EmExamInfo> findByUnitIdAndAcadyear(String unitId, String acadyear, String searchSemester);

    public List<EmExamInfo> findBySemesterAndIdIn(String[] ids, String searchSemester);

    public List<EmExamInfo> findByUnitIdAndAcadAndGradeId(String unitId, String acadyear, String searchSemester, String gradeCode);

    public List<EmExamInfo> findByOriginExamId(String originExamId);

    public List<EmExamInfo> findListByIdsNoDel(String[] ids);
}
