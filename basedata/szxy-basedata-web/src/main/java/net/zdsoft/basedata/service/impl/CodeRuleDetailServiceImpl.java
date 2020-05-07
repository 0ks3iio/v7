package net.zdsoft.basedata.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.CodeRuleDetailDao;
import net.zdsoft.basedata.entity.CodeRuleDetail;
import net.zdsoft.basedata.service.CodeRuleDetailService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

/**
 * @author yangsj 2017-1-24下午2:55:29
 */
@Service
public class CodeRuleDetailServiceImpl extends BaseServiceImpl<CodeRuleDetail, String> implements CodeRuleDetailService {

    @Autowired
    private CodeRuleDetailDao codeRuleDetailDao;

    @Override
    protected BaseJpaRepositoryDao<CodeRuleDetail, String> getJpaDao() {
        // TODO Auto-generated method stub
        return codeRuleDetailDao;
    }

    @Override
    protected Class<CodeRuleDetail> getEntityClass() {
        // TODO Auto-generated method stub
        return CodeRuleDetail.class;
    }

}
