package net.zdsoft.basedata.remote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.CodeRuleDao;
import net.zdsoft.basedata.entity.CodeRule;
import net.zdsoft.basedata.remote.service.CodeRuleRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.CodeRuleService;
import net.zdsoft.framework.utils.SUtils;

/**
 * @author yangsj 2017-1-24下午2:39:59
 */
@Service("codeRuleRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class CodeRuleRemoteServiceImpl extends BaseRemoteServiceImpl<CodeRule,String> implements CodeRuleRemoteService {

    @Autowired
    private CodeRuleService codeRuleService;
    @Autowired
    private CodeRuleDao codeRuleDao;

    @Override
    protected BaseService<CodeRule, String> getBaseService() {
        // TODO Auto-generated method stub
        return codeRuleService;
    }

    @Override
    public String findBySectionCodeType(String section, int type) {
        // TODO Auto-generated method stub
        return SUtils.s(codeRuleDao.findBySectionCodeType(section, type));
    }

    @Override
    public String findByCodeType(int type) {
        // TODO Auto-generated method stub
        return SUtils.s(codeRuleDao.findByCodeType(type));
    }

    @Override
    public String findByUnitIdCodeType(String schoolId, int codeType) {
        // TODO Auto-generated method stub
        return SUtils.s(codeRuleDao.findByUnitIdCodeType(schoolId, codeType));
    }

}
