package net.zdsoft.bigdata.datax.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.datax.entity.DataxJobInsRule;

import java.util.List;

public interface DataxJobInsRuleService extends BaseService<DataxJobInsRule, String> {

    void saveDataxJobInsRuleList(List<DataxJobInsRule> dataxJobInsRules);

    void deleteByJobInsId(String jobInsId);

    List<DataxJobInsRule> findAllByJobInsId(String jobInsId);

    void deleteByJobId(String jobId);
}
