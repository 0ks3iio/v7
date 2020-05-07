package net.zdsoft.bigdata.datax.dao;

import net.zdsoft.bigdata.datax.entity.DataxJob;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DataxJobDao extends BaseJpaRepositoryDao<DataxJob, String> {

    @Query("From DataxJob where isSchedule = 1 ")
    List<DataxJob> findScheduledDataxJobs();

    List<DataxJob> findAllByOrderByModifyTimeDesc();
}
