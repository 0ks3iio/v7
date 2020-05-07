package net.zdsoft.basedata.remote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.CodeRuleDetailDao;
import net.zdsoft.basedata.entity.CodeRuleDetail;
import net.zdsoft.basedata.remote.service.CodeRuleDetailRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.CodeRuleDetailService;
import net.zdsoft.framework.utils.SUtils;

/**
 * @author yangsj 2017-1-24下午2:46:34
 */
@Service("codeRuleDetailRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class CodeRuleDetailRemoteServiceImpl extends BaseRemoteServiceImpl<CodeRuleDetail,String> implements
        CodeRuleDetailRemoteService {

    @Autowired
    private CodeRuleDetailService codeRuleDetailService;

    @Autowired
    private CodeRuleDetailDao codeRuleDetailDao;

    @Override
    protected BaseService<CodeRuleDetail, String> getBaseService() {
        // TODO Auto-generated method stub
        return codeRuleDetailService;
    }

    @Override
    public String findByRuleId(String ruleId) {
        // TODO Auto-generated method stub
        return SUtils.s(codeRuleDetailDao.findByRuleId(ruleId));
    }

}
