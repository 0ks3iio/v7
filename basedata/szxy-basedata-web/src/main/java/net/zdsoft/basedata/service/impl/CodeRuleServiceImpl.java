package net.zdsoft.basedata.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.CodeRuleDao;
import net.zdsoft.basedata.entity.CodeRule;
import net.zdsoft.basedata.service.CodeRuleService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

/**
 * @author yangsj 2017-1-24下午3:01:47
 */
@Service
public class CodeRuleServiceImpl extends BaseServiceImpl<CodeRule, String> implements CodeRuleService {

    @Autowired
    private CodeRuleDao codeRuleDao;

    @Override
    protected BaseJpaRepositoryDao<CodeRule, String> getJpaDao() {
        // TODO Auto-generated method stub
        return codeRuleDao;
    }

    @Override
    protected Class<CodeRule> getEntityClass() {
        // TODO Auto-generated method stub
        return CodeRule.class;
    }

}
