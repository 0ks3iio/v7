package net.zdsoft.bigdata.datax.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.datax.dao.DataxJobInsRuleDao;
import net.zdsoft.bigdata.datax.entity.DataxJobInsRule;
import net.zdsoft.bigdata.datax.service.DataxJobInsRuleService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DataxJobInsRuleServiceImpl extends BaseServiceImpl<DataxJobInsRule, String> implements DataxJobInsRuleService {

    @Resource
    private DataxJobInsRuleDao dataxJobInsRuleDao;

    @Override
    protected BaseJpaRepositoryDao<DataxJobInsRule, String> getJpaDao() {
        return dataxJobInsRuleDao;
    }

    @Override
    protected Class<DataxJobInsRule> getEntityClass() {
        return DataxJobInsRule.class;
    }

    @Override
    public void saveDataxJobInsRuleList(List<DataxJobInsRule> dataxJobInsRules) {
        for (DataxJobInsRule rule : dataxJobInsRules) {
            rule.setId(UuidUtils.generateUuid());
            dataxJobInsRuleDao.save(rule);
        }
    }

    @Override
    public void deleteByJobInsId(String jobInsId) {
        dataxJobInsRuleDao.deleteByJobInsId(jobInsId);
    }

    @Override
    public List<DataxJobInsRule> findAllByJobInsId(String jobInsId) {
        return dataxJobInsRuleDao.findAllByJobInsIdOrderByOrderIdAsc(jobInsId);
    }

    @Override
    public void deleteByJobId(String jobId) {
        dataxJobInsRuleDao.deleteByJobId(jobId);
    }
}
