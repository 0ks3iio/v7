package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.ExammanageSportsScore;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ExammanageSportsScoreDao extends BaseJpaRepositoryDao<ExammanageSportsScore, String> {

    @Query("From ExammanageSportsScore where examId = ?1 and studentId in (?2)")
    List<ExammanageSportsScore> findListByExamIdAndStudentIdsIn(String examId, String[] studentIds);

    @Transactional
    @Modifying
    @Query("delete From ExammanageSportsScore where examId=?1 and studentId in (?2)")
    void deleteByStuIds(String examId, String[] stuIds);
}
