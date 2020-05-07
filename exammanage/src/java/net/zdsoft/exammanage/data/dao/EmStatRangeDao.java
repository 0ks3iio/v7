package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.EmStatRange;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmStatRangeDao extends BaseJpaRepositoryDao<EmStatRange, String> {

    void deleteByStatObjectIdIn(String[] statObjectId);


    @Query("From EmStatRange where statObjectId=?1 and examId=?2 and rangeId=?3 and rangeType=?4")
    List<EmStatRange> findByObjIdAndRangeId(String objectId, String examId,
                                            String rangeId, String rangeType);

    @Query("From EmStatRange where statObjectId=?1 and examId=?2 and subjectId=?3 and rangeId=?4 and rangeType=?5 ")
    public EmStatRange findByExamIdAndSubjectIdAndRangeIdAndRangeType(String objectId, String examId, String subjectId, String rangeId, String rangeType);

    @Query("From EmStatRange where statObjectId=?1 and examId=?2 and subjectId=?3 and rangeId=?4 and rangeType=?5 ")
    public List<EmStatRange> findByExamIdAndSubIdAndRangIdAndTypeList(String objectId, String examId, String subjectId, String rangeId, String rangeType);

    @Query("From EmStatRange where statObjectId=?1 and examId=?2 and subjectId in (?3)  ")
    public List<EmStatRange> findByExamIdAndSubIds(String objectId, String examId, String[] subjectIds);

    @Query("From EmStatRange where statObjectId=?1 and examId=?2 and subjectId in (?3) and rangeId=?4 ")
    public List<EmStatRange> findByExamIdAndSubIds(String objectId, String examId, String[] subjectIds, String rangeId);

    @Query("From EmStatRange where statObjectId=?1 and examId=?2 and subjectId=?3 and rangeId=?4 and rangeType=?5 and subType <> 1")
    public EmStatRange findByExamIdAndSubjectIdAndRangeIdAndRangeType1(String objectId, String examId, String subjectId, String rangeId, String rangeType);

    @Query("From EmStatRange where statObjectId=?1 and examId=?2 and subjectId=?3 and rangeId=?4 and rangeType=?5 and subType=?6")
    public EmStatRange findByExamIdAndSubIdAndRangIdAndTypeSubType(String objectId, String examId, String subjectId, String rangeId, String rangeType, String subType);

    @Query("From EmStatRange where statObjectId in (?1) and examId=?2 and subjectId=?3 and rangeId=?4 and rangeType=?5")
    public List<EmStatRange> findBySubjectIdAndRangIdAndTypeAndExamIdIn(String[] objectIds, String examId, String subjectId, String rangeId, String rangeType);

    @Query("From EmStatRange where rangeId=?1 and rangeType=?2 and subjectId=?3 and examId in (?4)")
    List<EmStatRange> findByRangeIdAndSubjectId(String rangeId,
                                                String rangeType, String subjectId, String[] examIds);

    @Query("From EmStatRange where statObjectId=?1 and examId=?2 and subjectId=?3  and rangeType=?4 and rangeId in (?5) order by rank")
    List<EmStatRange> findByExamIdAndRangeIdIn(String statObjectId,
                                               String examId, String subjectId, String rangeType, String[] rangeId);

    @Query("From EmStatRange where statObjectId=?1 and examId=?2 and subjectId=?3 and rangeType=?4 and subType=?5")
    List<EmStatRange> findByExamIdAndSubjectIdAndRangeType(String objectId, String examId, String subjectId, String rangeType, String subType);

    @Query("From EmStatRange where  statObjectId=?1 and examId=?2 and rangeType=?3 and rangeId in (?4)  order by rank")
    List<EmStatRange> findByExamId(String statObjectId, String examId, String rangeType, String[] rangeId);

    @Query("From EmStatRange where subjectId=?1 and rangeId=?2  and rangeType=?3 and subType=?4 and statObjectId in (?5) ")
    List<EmStatRange> findBySubIdAndTypeAndObjectIds(String subjectId, String rangeId, String rangeType, String subType, String[] objectIds);

    @Query("SELECT DISTINCT rangeId From EmStatRange where statObjectId=?1 and examId=?2 and rangeType='1'")
    List<String> findClsIdByObjIdAndExamId(String id, String examId);

    @Query("SELECT DISTINCT rangeId From EmStatRange where statObjectId=?1 and examId=?2 and subjectId=?3 and rangeType='1'")
    List<String> findClsIdByObjIdAndExamId(String id, String examId, String subjectId);

    @Query("SELECT DISTINCT rangeId From EmStatRange where statObjectId=?1 and examId=?2 and subjectId=?3 and subType=?4 and rangeType='1'")
    List<String> findClsIdByObjIdAndExamId(String id, String examId, String subjectId, String subType);

    @Query("SELECT DISTINCT rangeId From EmStatRange where subjectId=?1 and rangeType=?2 and statObjectId in (?3) ")
    List<String> findRangeIdBySubjectId(String subjectId, String rangeType, String[] objectIds);

}
