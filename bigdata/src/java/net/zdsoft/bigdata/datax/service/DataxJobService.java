package net.zdsoft.bigdata.datax.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.datax.entity.DataxJob;

import java.util.List;

public interface DataxJobService extends BaseService<DataxJob, String> {

    void saveDataxJob(DataxJob dataxJob);

    /**
     * 执行datax任务
     * @param jobId
     * @throws BigDataBusinessException
     */
    void executeDataxJob(String jobId) throws BigDataBusinessException;

    /**
     * 获取datax json
     * @param jobId
     * @return
     * @throws BigDataBusinessException
     */
    String getDataxJobJson(String jobId) throws BigDataBusinessException;

    /**
     * 查看datax日志
     * @param jobId
     * @return
     * @throws BigDataBusinessException
     */
    List<String> getDataxJobJsons(String jobId) throws BigDataBusinessException;

    void deleteDataxJob(String jobId);

    /**
     * 查看datax日志
     * @param logId
     * @return
     * @throws BigDataBusinessException
     */
    String viewJobLog(String logId) throws BigDataBusinessException;

    List<DataxJob> findScheduledDataxJobs();

    List<DataxJob> findAllDataxJob();
}
