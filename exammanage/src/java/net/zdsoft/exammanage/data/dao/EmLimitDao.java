package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.EmLimit;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmLimitDao extends BaseJpaRepositoryDao<EmLimit, String> {

    @Query("From EmLimit where examId=?1 and unitId=?2 and subjectId=?3 ")
    List<EmLimit> findList(String examId, String unitId, String subjectId);

    @Modifying
    @Query("delete from EmLimit where id in (?1)")
    void deleteAllByIds(String[] ids);

    @Query("From EmLimit where examId=?1 and teacherId in (?2)")
    List<EmLimit> findByExamIdTeacher(String examId, String[] teacherIds);

    @Query("From EmLimit where examId=?1 and subjectId=?2 and teacherId in (?3)")
    List<EmLimit> findByExamIdST(String examId, String subjectId, String[] teacherId);

    List<EmLimit> findByUnitId(String unitId);

    List<EmLimit> findByUnitIdAndExamId(String unitId, String examId);

    @Query("From EmLimit where unitId=?1 and examId != ?2 ")
    List<EmLimit> findByUnitIdAndExamIdNot(String unitId, String examId);
}
