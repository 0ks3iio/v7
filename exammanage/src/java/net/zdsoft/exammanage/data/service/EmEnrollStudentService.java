package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.entity.EmEnrollStudent;
import net.zdsoft.framework.entity.Pagination;

import java.util.List;
import java.util.Map;

public interface EmEnrollStudentService extends BaseService<EmEnrollStudent, String> {

    public void saveByIds(String examId, String state, String[] studentIds);

    public List<EmEnrollStudent> findByExamId(String examIds);

    public List<EmEnrollStudent> findByExamIdAndSchoolId(String examId, String schoolId);

    public List<EmEnrollStudent> findByIdsAndstate(String examId, String schoolId, String classId, String state, Pagination page);

    public List<EmEnrollStudent> findByIdsAndGood(String examId, String schoolId, String hasGood, Pagination page);

    public List<EmEnrollStudent> findByStudentIdIn(String[] studentIds);

    public List<EmEnrollStudent> findByExamIdAndHasPass(String examId, String hasPass);

    public Map<String, Integer> findByExamIdAndHasPassAndHasGood(String examId, String hasPass, String hasGood);

    public Map<String, List<EmEnrollStudent>> findMapByExamIds(String[] examIds);

    public List<EmEnrollStudent> findMapByExamIdsAndStudentId(String[] examIds, String studentId);

    public Map<String, Integer> getStudentAllCount(String[] examIds);

    public Map<String, Integer> getStudentPassNum(String[] examIds);

    public Map<String, Integer> getStudentAuditNum(String[] examIds);

    public List<EmEnrollStudent> findByExamIdAndSchoolIdInClassIds(String examId, String schoolId, String[] classIds);

    public List<EmEnrollStudent> findByExamIdAndSchoolIdAndStudentIdIn(String examId, String schoolId, String[] studentIds);

    void deleteByExamIdAndUnitId(String examId, String unitId);

    public List<EmEnrollStudent> findByStuIdAndState(String stuId, String state);

    public List<Clazz> findClsByExamIdAndSchId(String acadyear, String examId, String schoolId);
}
