package net.zdsoft.exammanage.data.dao;


import net.zdsoft.exammanage.data.entity.ExammanageQualityScore;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ExammanageQualityScoreDao extends BaseJpaRepositoryDao<ExammanageQualityScore, String> {

    @Query("From ExammanageQualityScore where examId = ?1 and studentId in (?2)")
    List<ExammanageQualityScore> findListByExamIdAndStudentIdsIn(String examId, String[] studentIds);

    @Transactional
    @Modifying
    @Query("delete From ExammanageQualityScore where examId=?1 and studentId in (?2)")
    void deleteByStuIds(String examId, String[] stuIds);

}
