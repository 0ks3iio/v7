package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.dto.EmScoreInfoDto;
import net.zdsoft.exammanage.data.entity.EmScoreInfo;
import net.zdsoft.framework.entity.Pagination;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface EmScoreInfoService extends BaseService<EmScoreInfo, String> {

    public Map<String, EmScoreInfo> findByStudent(String examId, String subjectId, String[] studentIds);

    public Map<String, EmScoreInfo> findByStudent(String examId, String subjectId, String subType, String[] studentIds);

    public void saveAll(List<EmScoreInfo> saveList, Set<String> stuId, String subjectId, String examId);

    public void deleteBy(String examId, String subjectId);

    public List<EmScoreInfo> findByExamIdAndUnitId(String examId, String unitId);

    /**
     * 查询有成绩的考试
     *
     * @param examIds
     * @return
     */
    public List<String> findExamIds(String[] examIds);

    /**
     * 根据学年学期查询学生成绩
     *
     * @param unitId
     * @param acadyear
     * @param semester
     * @param studentId
     * @param examId
     * @return
     */
    public List<EmScoreInfo> findByCondition(String unitId, String acadyear, String semester, String studentId, String examId);

    /**
     * 选考科目赋分查看
     *
     * @param acadyear
     * @param semester
     * @param examId
     * @param subjectId
     * @param page
     * @return
     */
    public List<EmScoreInfoDto> findByXkConListDto(String unitId, String acadyear, String semester, String examId, String subjectId,
                                                   Pagination page);

    void conversionCount(String unitId, String examId);

    public List<EmScoreInfo> findOptionalCourseScore(String unitId, String[] teachClassIds);

    /**
     * 根据学年、学期、单位、科目、教学班级来查找选修课的成绩信息
     *
     * @param unitId
     * @param zeroGuid
     * @param subjectId
     * @param teachClassId
     * @return
     */
    public List<EmScoreInfo> findOptionalCourseScore(String unitId, String subjectId,
                                                     String teachClassId);

    public List<EmScoreInfo> saveAllEntitys(EmScoreInfo... info);

    public int getEditRole(String examId, String classIdSearch,
                           String courseId, String unitId, String acadyear, String semester, String teacherId);

    public List<EmScoreInfo> findByUnitIdAndExamIdAndSubjectId(String unitId, String examId, String subjectId);

    void deleteByExamIdIn(String[] examIds);
}
