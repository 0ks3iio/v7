package net.zdsoft.bigdata.taskScheduler.dao;

import net.zdsoft.bigdata.taskScheduler.entity.EtlJobStep;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * etl任务调度step dao
 *
 * @author feekang
 */
public interface EtlJobStepDao extends BaseJpaRepositoryDao<EtlJobStep, String> {

    @Query("From EtlJobStep where groupId = ?1 order by step asc")
    public List<EtlJobStep> findEtlJobStepsByGroupId(String groupId);

    @Query("select count(ejs) from EtlJobStep ejs where jobId =?1")
    Long countByJobId(String jobId);

    @Query(value = "delete from bg_etl_job_step where group_id= ?1", nativeQuery = true)
    @Modifying
    public void deleteByGroupId(String groupId);

}
