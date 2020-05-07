package net.zdsoft.bigdata.datax.dao;

import net.zdsoft.bigdata.datax.entity.DataxJobInsRule;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import java.util.List;

public interface DataxJobInsRuleDao extends BaseJpaRepositoryDao<DataxJobInsRule, String> {

    void deleteByJobInsId(String jobInsId);

    List<DataxJobInsRule> findAllByJobInsIdOrderByOrderIdAsc(String jobInsId);

    void deleteByJobId(String jobId);
}
