package net.zdsoft.bigdata.datax.dao;

import net.zdsoft.bigdata.datax.entity.DataxJobIns;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import java.util.List;

public interface DataxJobInsDao extends BaseJpaRepositoryDao<DataxJobIns, String> {

    List<DataxJobIns> findAllByJobId(String jobId);

    void deleteByJobId(String jobId);
}
