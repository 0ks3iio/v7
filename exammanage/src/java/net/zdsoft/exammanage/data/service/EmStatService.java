package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.dto.EmScoreSearchDto;
import net.zdsoft.exammanage.data.entity.EmAbilitySet;
import net.zdsoft.exammanage.data.entity.EmStat;
import net.zdsoft.framework.entity.Pagination;

import java.util.List;
import java.util.Map;

public interface EmStatService extends BaseService<EmStat, String> {

    String saveStatByExamId(String examId, String unitId, Integer unitClass) throws Exception;

    public void deleteByStatObjectId(String... statObjectId);

    public List<EmStat> saveAllEntitys(EmStat... emStat);

    List<EmStat> findBySearchDto(EmScoreSearchDto dto);

    public EmStat findByExamIdAndSubjectIdAndStudentId(String statObjectId, String examId, String subjectId, String studentId);


    /**
     * 查询年级名次在rank1-rank2
     *
     * @param examId
     * @param schoolId
     * @param subjectId
     * @param rank1
     * @param rank2
     * @param page
     * @return
     */
    List<EmStat> findBySchoolRank(String statObjectId, String examId, String schoolId,
                                  String subjectId, int rank1, int rank2, Pagination page);

    /**
     * 查询班级名次在rank1-rank2
     *
     * @param examId
     * @param schoolId
     * @param subjectId
     * @param rank1
     * @param rank2
     * @param page
     * @return
     */
    List<EmStat> findByClassRank(String statObjectId, String examId, String schoolId,
                                 String subjectId, int rank1, int rank2);

    /**
     * 某次考试下学生成绩
     *
     * @param id
     * @param examId
     * @param subjectId  可为空
     * @param studentIds
     * @return
     */
    List<EmStat> findByStudentIds(String statObjectId, String examId, String subjectId, String[] studentIds);

    /**
     * 某次考试下学生成绩
     *
     * @param statObjectId
     * @param examIds
     * @param subjectId
     * @param studentId
     * @return
     */
    List<EmStat> findByStudentExamIdIn(String statObjectId, String[] examIds, String subjectId, String studentId);

    /**
     * @param statObjectIds
     * @param examIds
     * @param subjectId
     * @param studentId
     * @return
     */
    List<EmStat> findByObjectIdsExamIdIn(String[] statObjectIds, String[] examIds, String subjectId, String studentId);

    /**
     * @param studentId
     * @return
     */
    List<EmStat> findByStudentId(String studentId);

    /**
     * 某次考试下学生成绩
     *
     * @param statObjectId
     * @param examId
     * @param studentId
     * @return
     */
    public List<EmStat> findByExamIdAndStudentId(String statObjectId, String examId, String studentId);

    /**
     * 某次考试下班级学生的所有科目成绩
     *
     * @param statObjectId
     * @param examId
     * @param classId
     * @return
     */
    List<EmStat> findByClassId(String statObjectId, String examId, String classId);

    /**
     * 某次考试下班级学生的某科目成绩
     *
     * @param statObjectId
     * @param examId
     * @param classId
     * @param subjectId
     * @param subType
     * @return
     */
    List<EmStat> findByClassIdAndSubId(String statObjectId, String examId, String classId, String subjectId, String subType);

    List<EmStat> findByClassIdsAndSubId(String statObjectId, String examId, String[] classIds, String subjectId, String subType);

    List<EmStat> findByExamIdAndObjectSubType(String examId, String objectId);


    List<EmStat> findByExamIdAndObjectsAndSubjectId(String examId, String subjectId, String objectId);

    /**
     * 没有subType<>2
     *
     * @param examId
     * @param subjectId
     * @param objectId
     * @return
     */
    List<EmStat> findByAllExamIdAndObjIdAndSubjectId(String examId, String subjectId, String objectId);

    List<EmStat> findByExamIdAndObjectsAndSubjectIdAndClassId(String examId, String subjectId, String objectId, String classId);

    List<EmStat> findByExamIdAndObjectsAndSubjectIdTotal(String examId, String subjectId, String objectId, String subType);

    List<EmStat> findByExamIdAndObjects(String examId, String objectId);

    /**
     * 修改综合能力分以及排名
     *
     * @param abiMap
     * @param examId
     * @param statObjectId
     * @param statList     TODO
     */
    void saveAbiEmStatList(Map<String, EmAbilitySet> abiMap, String examId, String statObjectId, List<EmStat> statList);

    /**
     * @param examId
     * @param objectId
     * @param subjectIds
     * @return
     */
    List<EmStat> findByExamIdAndObjectSubIds(String examId, String objectId, String[] subjectIds);
}
