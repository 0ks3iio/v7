package net.zdsoft.bigdata.datax.dao;

import net.zdsoft.bigdata.datax.entity.DataxJobInsLog;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import java.util.List;

public interface DataxJobInsLogDao extends BaseJpaRepositoryDao<DataxJobInsLog, String> {

    List<DataxJobInsLog> findAllByJobInstanceId(String jobInstanceId);

    List<DataxJobInsLog> findAllByJobIdOrderByStartTimeDesc(String jobId);

    void deleteByJobId(String jobId);

    List<DataxJobInsLog> findAllByResultIsNull();
}
