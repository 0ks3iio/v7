package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.dto.ScoreLimitSearchDto;
import net.zdsoft.exammanage.data.entity.EmLimit;
import net.zdsoft.exammanage.data.entity.EmLimitDetail;

import java.util.List;
import java.util.Set;

public interface EmLimitService extends BaseService<EmLimit, String> {

    /**
     * 获取
     *
     * @param examId
     * @param unitId
     * @param subjectId
     * @return
     */
    List<EmLimit> findLimitList(String examId, String unitId, String subjectId);

    void deleteByIds(String[] ids);

    void saveLimitAll(List<EmLimit> emLimit, boolean isDeleteClass);

    public List<EmLimit> saveAllEntitys(EmLimit... emLimitList);

    List<EmLimit> findByExamIdTeacher(String examId, Set<String> teacherIds);

    void saveByTeacher(List<EmLimit> limitList, Set<String> teacherIds, String examId);

    List<EmLimit> findByExamIdST(String examId, String teacherId,
                                 String subjectId, String classId);

    List<EmLimit> findBySearchDto(ScoreLimitSearchDto dto);

    void saveAllEntitys(EmLimit[] array, EmLimitDetail[] array2);

    void deleteByClassIdAndTeacherId(ScoreLimitSearchDto dto);

    void initAllEntitys(Set<String> deleteIds, List<EmLimit> emlist, List<EmLimitDetail> emDetaills);

    /**
     * 获取
     *
     * @param unitId
     * @return
     */
    Set<String> findByUnitId(String unitId);

    Set<String> findByUnitIdAndExamId(String unitId, String examId);

    Set<String> findByUnitIdAndExamIdNot(String unitId, String examId);
}
