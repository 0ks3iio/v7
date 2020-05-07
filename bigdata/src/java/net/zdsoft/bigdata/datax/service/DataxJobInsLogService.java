package net.zdsoft.bigdata.datax.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.datax.entity.DataxJobInsLog;

import java.util.List;

public interface DataxJobInsLogService extends BaseService<DataxJobInsLog, String> {

    void saveDataxJobInstanceLog(DataxJobInsLog dataxJobInsLog);

    List<DataxJobInsLog> findByJobInstanceId(String jobInstanceId);

    List<DataxJobInsLog> findByJobId(String jobId);

    void deleteByJobId(String jobId);

    void updateJobStatus();
}
