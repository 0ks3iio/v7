package net.zdsoft.bigdata.taskScheduler.service;

import java.util.Date;
import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.taskScheduler.entity.EtlJobLog;
import net.zdsoft.framework.entity.Pagination;

/**
 * etl日志service
 * @author feekang
 *
 */
public interface EtlJobLogService extends BaseService<EtlJobLog, String>{

    /**
     * 根据单位id分页查询日志
     * @param unitId
     * @param page
     * @param name
     * @param beginDate
     * @param endDate
     * @return
     */
    List<EtlJobLog> findByUnitId(String unitId, Pagination page, String name, Date beginDate, Date endDate);
    
    /**
     * 根据jobId获取日志list
     * @param jobId
     * @return
     */
    List<EtlJobLog> findByJobId(String jobId,Pagination page);

}
