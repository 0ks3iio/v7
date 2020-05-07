package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.EmEnrollStuCount;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmEnrollStuCountDao extends BaseJpaRepositoryDao<EmEnrollStuCount, String> {

    @Query("From EmEnrollStuCount where examId = ?1 and schoolId in (?2)")
    List<EmEnrollStuCount> findByExamIdAndSchoolIdIn(String examId, String[] schoolIds);

}
