package net.zdsoft.bigdata.taskScheduler.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.taskScheduler.entity.EtlJob;
import net.zdsoft.bigdata.taskScheduler.entity.EtlJobStep;

import java.util.Date;
import java.util.List;

/**
 * etl任务service
 *
 * @author feekang
 */
public interface EtlJobStepService extends BaseService<EtlJobStep, String> {

    /**
     * 根据组获取所有的steps
     *
     * @param groupId
     * @return
     */
    public List<EtlJobStep> findEtlJobStepsByGroupId(String groupId);

    /**
     * 保存job step
     *
     * @param groupId
     * @param jobIds
     */
    public void saveJobSteps(String groupId, String jobIds);

    /**
     * 根据jobid获取个数
     * @param jobId
     * @return
     */
    Long countByJobId(String jobId);

    /**
     * 删除Job by groupId
     *
     * @param groupId
     */
    public void deleteJobStepByGroupId(String groupId);

}
