package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.EmScoreInfo;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmScoreInfoDao extends BaseJpaRepositoryDao<EmScoreInfo, String> {

    @Query("From EmScoreInfo where examId=?1 and subjectId=?2 and studentId in (?3)")
    List<EmScoreInfo> findByStudent(String examId, String subjectId,
                                    String[] studentIds);

    @Query("From EmScoreInfo where examId=?1 and subjectId=?2 and gkSubType =?3 and studentId in (?4)")
    List<EmScoreInfo> findByStudent(String examId, String subjectId, String subType,
                                    String[] studentIds);

    @Modifying
    @Query("delete From EmScoreInfo where examId=?1 and subjectId=?2 ")
    void deleteBy(String examId, String subjectId);

    @Modifying
    @Query("delete From EmScoreInfo where examId=?1 and subjectId=?2 and studentId in (?3)")
    void deleteByStudent(String examId, String subjectId, String[] studentIds);

    List<EmScoreInfo> findByExamIdAndUnitId(String examId, String unitId);

    List<EmScoreInfo> findByExamId(String examId);

    @Query("select distinct(examId) From EmScoreInfo where examId in (?1)")
    List<String> findExamIds(String[] examIds);

    @Query("From EmExamInfo where unitId = ?1 and acadyear = ?2 and semester = ?3 and acadyear = ?4 and semester = ?5 and isDeleted = 0 order by examCode desc")
    List<EmScoreInfo> findByCondition(String unitId, String acadyear, String semester, String studentId, String examId);

    List<EmScoreInfo> findByUnitIdAndExamIdAndTeachClassIdIn(String unitId, String examId, String[] teachClassIds);

    List<EmScoreInfo> findByUnitIdAndExamIdAndSubjectIdAndTeachClassId(String unitId, String examId, String subjectId, String teachClassId);

    List<EmScoreInfo> findByUnitIdAndExamIdAndSubjectId(String unitId, String examId, String subjectId);

    void deleteByExamIdIn(String[] examIds);
}
