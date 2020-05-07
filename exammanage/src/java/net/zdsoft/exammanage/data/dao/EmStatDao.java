package net.zdsoft.exammanage.data.dao;


import net.zdsoft.exammanage.data.entity.EmStat;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmStatDao extends BaseJpaRepositoryDao<EmStat, String> {

    void deleteByStatObjectIdIn(String[] statObjectId);

    public void deleteByStatObjectId(String... statObjectId);

    @Query("From EmStat where statObjectId=?1 and examId=?2 and subjectId = ?3 and studentId = ?4 and subType <> 1")
    public EmStat findByExamIdAndSubjectIdAndStudentId(String statObjectId, String examId, String subjectId, String studentId);


    @Query("From EmStat where statObjectId=?1 and examId=?2 and schoolId=?3" +
            " and subjectId=?4 and  gradeRank >= ?5 and  gradeRank<= ?6  order by gradeRank,classId")
    List<EmStat> findBySchoolRank(String statObjectId, String examId,
                                  String schoolId, String subjectId, int rank1, int rank2);

    @Query("select count(id) From EmStat where statObjectId=?1 and examId=?2 and schoolId=?3" +
            " and subjectId=?4 and  gradeRank >= ?5 and  gradeRank<= ?6 ")
    int countBySchoolRank(String statObjectId, String examId,
                          String schoolId, String subjectId, int rank1, int rank2);

    @Query("From EmStat where statObjectId=?1 and examId=?2 and classId=?3" +
            " and subjectId=?4 and  classRank >= ?5 and  classRank<= ?6  order by classRank")
    List<EmStat> findByClassRank(String statObjectId, String examId, String classId, String subjectId, int rank1, int rank2);

    @Query("From EmStat where statObjectId=?1 and examId=?2 and schoolId=?3" +
            " and subjectId=?4 and  gradeRank >= ?5 and  gradeRank<= ?6 order by gradeRank,classId")
    List<EmStat> findBySchoolRank(String statObjectId, String examId,
                                  String schoolId, String subjectId, int rank1, int rank2, Pageable page);


    @Query("From EmStat where statObjectId=?1 and examId=?2 and studentId in (?3)")
    List<EmStat> findByStudentIds(String statObjectId, String examId,
                                  String[] studentIds);

    @Query("From EmStat where statObjectId=?1 and examId=?2 and subjectId=?3 and studentId in (?4)")
    List<EmStat> findByStudentIds(String statObjectId, String examId, String subjectId,
                                  String[] studentIds);

    @Query("From EmStat where statObjectId=?1 and examId=?2 and classId=?3 order by classRank")
    List<EmStat> findByClassId(String statObjectId, String examId, String classId);

    @Query("From EmStat where statObjectId=?1 and examId=?2 and classId=?3 and subjectId=?4 and subType=?5 order by classRank")
    List<EmStat> findByClassIdAndSubId(String statObjectId, String examId, String classId, String subjectId, String subType);

    @Query("From EmStat where statObjectId=?1 and examId=?2  and subjectId=?3 and subType=?4 and classId in (?5) order by gradeRank")
    List<EmStat> findByClassIdsAndSubId(String statObjectId, String examId, String subjectId, String subType, String[] classIds);

    @Query("From EmStat where statObjectId=?1 and examId in (?2) and subjectId=?3 and studentId = ?4")
    List<EmStat> findByStudentExamIdIn(String statObjectId, String[] examIds, String subjectId, String studentId);

    @Query("From EmStat where statObjectId in (?1) and examId in (?2) and subjectId=?3 and studentId = ?4")
    List<EmStat> findByObjectIdsExamIdIn(String[] statObjectIds, String[] examIds, String subjectId, String studentId);

    @Query("From EmStat where statObjectId in (?1) and examId in (?2)  and studentId = ?3")
    List<EmStat> findByObjectIdsExamIdIn(String[] statObjectIds, String[] examIds, String studentId);

    List<EmStat> findByStudentId(String studentId);

    @Query("From EmStat where statObjectId=?1 and examId = ?2  and studentId = ?3")
    public List<EmStat> findByExamIdAndStudentId(String statObjectId, String examId, String studentId);

    @Query("From EmStat where examId=?1 and subType<>2 and statObjectId=?2")
    List<EmStat> findByExamIdAndObjects(String examId, String objectId);


    @Query("From EmStat where examId=?1 and statObjectId=?2")
    List<EmStat> findByExamIdAndObjectSubType(String examId, String objectId);

    @Query("From EmStat where examId=?1 and statObjectId=?2 and subjectId in (?3) ")
    List<EmStat> findByExamIdAndObjectSubIds(String examId, String objectId, String[] subjectId);

    @Query("From EmStat where examId=?1 and subType<>2 and subjectId=?2 and statObjectId=?3 order by gradeRank")
    List<EmStat> findByExamIdAndObjectsAndSubjectId(String examId, String subjectId, String objectId);

    @Query("From EmStat where examId=?1  and subjectId=?2 and statObjectId=?3 order by gradeRank")
    List<EmStat> findByAllExamIdAndObjIdAndSubjectId(String examId, String subjectId, String objectId);

    @Query("From EmStat where examId=?1 and subjectId=?2 and statObjectId=?3 and classId=?4 order by gradeRank")
    List<EmStat> findByExamIdAndObjectsAndSubjectIdAndClassId(String examId, String subjectId, String objectId, String classId);

    @Query("From EmStat where examId=?1 and subjectId=?2 and statObjectId=?3 and subType=?4 order by gradeRank")
    List<EmStat> findByExamIdAndObjectsAndSubjectIdTotal(String examId, String subjectId, String objectId, String subType);

}
