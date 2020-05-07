package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.EmClassInfo;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmClassInfoDao extends BaseJpaRepositoryDao<EmClassInfo, String> {

    public List<EmClassInfo> findByExamIdAndSchoolId(String examId, String schoolId);

    public void deleteByExamIdAndSchoolId(String examId, String schoolId);

    public List<EmClassInfo> findByExamId(String examId);

    @Query("From EmClassInfo where schoolId = ?1 and examId in (?2)")
    public List<EmClassInfo> findBySchoolIdAndExamIdIn(String schoolId,
                                                       String[] examIds);

    public EmClassInfo findByExamIdAndSchoolIdAndClassId(String examId, String schoolId, String classId);

    @Query("select distinct(examId) from EmClassInfo where classId in (?1)")
    public List<String> findExamIdByClassIds(String[] classIds);

    @Query("From EmClassInfo where schoolId=?1 and classId in (?2)")
    public List<EmClassInfo> findBySchoolIdAndSubjectInfoIdIn(String schoolId, String... subjectInfoIds);

    @Modifying
    @Query("delete From EmClassInfo where  examId in (?1)")
    void deleteByExamIds(String[] examIds);
}
