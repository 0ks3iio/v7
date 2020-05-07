package net.zdsoft.bigdata.taskScheduler.dao;

import java.util.Date;
import java.util.List;

import net.zdsoft.bigdata.taskScheduler.entity.EtlJobLog;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

/**
 * etl job日志dao
 * @author feekang
 *
 */
public interface EtlJobLogDao extends BaseJpaRepositoryDao<EtlJobLog, String>{

    @Query("FROM EtlJobLog where unitId =?1 and name like ?2 and  logTime > ?3 and logTime < ?4 order by logTime desc")
    List<EtlJobLog> findByUnitId(String unitId, String name, Date beginDate, Date endDate, Pageable page);

    @Query("select count(kl) from EtlJobLog kl where unitId =?1 and name like ?2 and  logTime > ?3 and logTime < ?4 ")
    Long countByUnitId(String unitId, String name, Date beginDate, Date endDate);
    
	@Query("From EtlJobLog where jobId = ?1 order by logTime desc")
	List<EtlJobLog> findByJobId(String jobId,Pageable page);
}
