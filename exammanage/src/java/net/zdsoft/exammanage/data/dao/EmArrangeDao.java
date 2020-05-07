package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.EmArrange;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmArrangeDao extends BaseJpaRepositoryDao<EmArrange, String> {

    public EmArrange findByExamId(String examId);

    @Query("From EmArrange where examId in (?1)")
    public List<EmArrange> findByExamIdIn(String[] examIds);
}
