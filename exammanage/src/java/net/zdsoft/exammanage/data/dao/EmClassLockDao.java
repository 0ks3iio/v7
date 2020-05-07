package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.EmClassLock;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmClassLockDao extends BaseJpaRepositoryDao<EmClassLock, String> {

    List<EmClassLock> findBySchoolIdAndExamIdAndSubjectId(String schoolId, String examId, String subjectId);

    List<EmClassLock> findBySchoolIdAndExamIdAndSubjectIdAndClassId(String schoolId, String examId, String subjectId, String classId);

    @Modifying
    @Query("delete From EmClassLock where examId=?1 and subjectId=?2 ")
    void deleteBy(String examId, String subjectId);

    void deleteByExamIdIn(String[] examIds);
}
