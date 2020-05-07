package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.EmOption;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmOptionDao extends BaseJpaRepositoryDao<EmOption, String>, EmOptionJdbcDao {

    public List<EmOption> findByExamIdAndExamRegionId(String examId, String examRegionId);

    public List<EmOption> findByExamId(String examId);

    @Query("From EmOption where examId = ?1 and optionSchoolId in (?2)")
    public List<EmOption> findByExamIdAndSchools(String examId, String... schools);

    public void deleteByExamIdAndExamRegionId(String examId, String examRegionId);

    public void deleteById(String optionId);

    public List<EmOption> findByExamIdAndExamRegionIdIn(String examId, String[] examRegionIds);
}
