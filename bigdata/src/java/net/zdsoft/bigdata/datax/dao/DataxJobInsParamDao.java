package net.zdsoft.bigdata.datax.dao;

import net.zdsoft.bigdata.datax.entity.DataxJobInsParam;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import java.util.List;

public interface DataxJobInsParamDao extends BaseJpaRepositoryDao<DataxJobInsParam, String> {

    List<DataxJobInsParam> findAllByJobInsId(String jobInsId);

    void deleteByJobInsId(String jobInsId);

    void deleteByJobId(String jobId);
}
