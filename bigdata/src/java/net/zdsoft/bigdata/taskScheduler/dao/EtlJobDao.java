package net.zdsoft.bigdata.taskScheduler.dao;

import net.zdsoft.bigdata.taskScheduler.entity.EtlJob;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * etl任务调度 dao
 *
 * @author feekang
 */
public interface EtlJobDao extends BaseJpaRepositoryDao<EtlJob, String> {

    @Query("From EtlJob where unitId = ?1 and etl_type = ?2 order by creationTime desc")
    public List<EtlJob> findEtlJobsByUnitId(String unitId, Integer etlType);

    @Query("From EtlJob where isSchedule = 1 ")
    public List<EtlJob> findScheduledEtlJobs();

    @Query("From EtlJob where unitId = ?1 and name = ?2 ")
    public List<EtlJob> findEtlJobsByName(String unitId, String name);


    @Query("From EtlJob where unitId = ?1 and jobCode = ?2 ")
    public List<EtlJob> findEtlJobsByJobCode(String unitId, String jobCode);

    public List<EtlJob> findByUnitId(String unitId);

    @Query("select count(ej) from EtlJob ej where nodeId =?1")
    Long countByNodeId(String nodeId);

    @Query("select count(ej) from EtlJob ej where nodeId =?1 and jobType =?2")
    Long countByNodeIdAndType(String nodeId, String type);
}
