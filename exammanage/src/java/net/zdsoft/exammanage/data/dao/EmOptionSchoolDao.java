package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.EmOptionSchool;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmOptionSchoolDao extends BaseJpaRepositoryDao<EmOptionSchool, String> {

    @Query("From EmOptionSchool where examId = ?1 and joinSchoolId in (?2)")
    public List<EmOptionSchool> findByExamIdAndSchoolIdIn(String examId, String[] schoolIds);

    public List<EmOptionSchool> findByExamIdAndOptionIdIn(String examId, String... optionId);

    public void deleteByExamIdAndOptionIdIn(String examId, String... optionIds);

    public List<EmOptionSchool> findByExamId(String examId);
}
