package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.EmSubjectInfo;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EmSubjectInfoDao extends BaseJpaRepositoryDao<EmSubjectInfo, String> {

    @Query("From EmSubjectInfo where examId = ?1 order by startDate")
    public List<EmSubjectInfo> findByExamId(String examId);

    @Query("From EmSubjectInfo where unitId=?1 and examId in ?2 order by startDate")
    public List<EmSubjectInfo> findByUnitIdAndExamId(String UnitId, String... examIds);

    public void deleteByIdIn(String[] id);

    public void deleteByExamId(String examId);

    public List<EmSubjectInfo> findByExamIdAndSubjectId(String examId,
                                                        String subjectId);

    @Modifying
    @Query("update EmSubjectInfo set isLock=?2 where examId=?1")
    public void updateIsLock(String examId, String isLock);

    @Query("SELECT DISTINCT subjectId From EmSubjectInfo where inputType = 'S' and examId in ?1")
    public List<String> findSubIdByExamIds(String[] examIds);

    @Query("From EmSubjectInfo where inputType = 'S' and examId in ?1")
    public List<EmSubjectInfo> findByExamIds(String[] examIds);

    @Transactional
    public void deleteByExamIdIn(String[] examIds);
}
