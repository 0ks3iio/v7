package net.zdsoft.exammanage.data.service;

import net.zdsoft.exammanage.data.entity.EmStatRange;

import java.util.List;
import java.util.Set;

public interface EmStatRangeService {

    public List<EmStatRange> saveAllEntitys(EmStatRange... emStatRange);

    public void deleteByStatObjectId(String... statObjectId);

    /**
     * 查询某次考试某个对象各科目统计结果
     *
     * @param objectId
     * @param examId
     * @param rangeId
     * @param rangeType
     * @return
     */
    public List<EmStatRange> findByObjIdAndRangeId(String objectId, String examId,
                                                   String rangeId, String rangeType);

    /**
     * 查询某次考试某个对象单个统计结果
     *
     * @param objectId
     * @param examId
     * @param rangeId
     * @param rangeType
     * @return
     */
    public EmStatRange findByExamIdAndSubIdAndRangIdAndType(String objectId, String examId, String subjectId, String rangeId, String rangeType);

    /**
     * 查询某次考试某个对象多个统计结果
     *
     * @param objectId
     * @param examId
     * @param rangeId
     * @param rangeType
     * @return
     */
    public List<EmStatRange> findByExamIdAndSubIdAndRangIdAndTypeList(String objectId, String examId, String subjectId, String rangeId, String rangeType);

    /**
     * 获取多个subjectId对应的数据
     *
     * @param objectId
     * @param examId
     * @param subjectIds
     * @param rangeId
     * @return
     */
    public List<EmStatRange> findByExamIdAndSubIds(String objectId, String examId, String[] subjectIds, String rangeId);

    /**
     * 查询某次考试某个对象单个统计结果
     *
     * @param objectId
     * @param examId
     * @param rangeId
     * @param rangeType
     * @return
     */
    public EmStatRange findByExamIdAndSubIdAndRangIdAndTypeSubType(String objectId, String examId, String subjectId, String rangeId, String rangeType, String subType);

    /**
     * 查询某次考试某个对象单个统计结果
     *
     * @param objectIds
     * @param examId
     * @param rangeId
     * @param rangeType
     * @return
     */
    public List<EmStatRange> findBySubIdAndRangIdAndTypeAndExamIdIn(String[] objectIds, String examId, String subjectId, String rangeId, String rangeType);

    /**
     * 查询某个对象在某个科目在不同考试下的统计结果
     *
     * @param schoolId
     * @param rangeType
     * @param subjectId
     * @param examIds
     * @return
     */
    public List<EmStatRange> findByRangeIdAndSubjectId(String schoolId, String rangeType, String subjectId, String[] examIds);

    public List<EmStatRange> findByRangeIdAndSubjectIds(String schoolId, String rangeType, String subjectId, String[] examIds);

    public List<EmStatRange> findByExamIdAndRangeIdIn(String statObjectId, String examId,
                                                      String subjectId, String rangeType, String[] rangeId);

    public List<EmStatRange> findByExamId(String statObjectId, String examId, String rangeType, String[] rangeId);

    /**
     * 查询某次考试某个对象多个统计结果
     *
     * @param objectId
     * @param examId
     * @param subjectId
     * @param rangeType
     * @param subType
     * @return
     */
    public List<EmStatRange> findByExamIdAndSubIdAndType(String objectId, String examId, String subjectId, String rangeType, String subType);

    /**
     * 查询多个考试某个对象多个统计结果
     *
     * @param subjectId
     * @param rangeId
     * @param rangeType
     * @param subType
     * @param objectIds
     * @return
     */
    public List<EmStatRange> findBySubIdAndTypeAndObjectIds(String subjectId, String rangeId, String rangeType, String subType, String[] objectIds);

    public List<String> getClassIdsBy(String unitId, String examId);

    public List<String> getClassIdsBy(String unitId, String examId, String subjectId, String subType);

    /**
     * @param subjectId
     * @param rangeType
     * @param unitId    统计单位
     * @param examIds
     * @return
     */
    public List<String> findRangeIdBySubjectId(String subjectId, String rangeType, String unitId, Set<String> examIds);

    public EmStatRange findByExamIdAndSubIdAndRangIdAndType1(String id, String examId, String subjectId, String unitId,
                                                             String rangeSchool);
}
