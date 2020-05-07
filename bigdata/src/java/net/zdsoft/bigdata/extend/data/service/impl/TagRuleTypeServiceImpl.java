package net.zdsoft.bigdata.extend.data.service.impl;

import javax.annotation.Resource;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.extend.data.dao.TagRuleTypeDao;
import net.zdsoft.bigdata.extend.data.entity.TagRuleType;
import net.zdsoft.bigdata.extend.data.service.TagRuleTypeService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.stereotype.Service;

/**
 * Created by wangdongdong on 2018/7/9 13:42.
 */
@Service
public class TagRuleTypeServiceImpl extends BaseServiceImpl<TagRuleType, String> implements TagRuleTypeService {

    @Resource
    private TagRuleTypeDao tagRuleTypeDao;

    @Override
    protected BaseJpaRepositoryDao<TagRuleType, String> getJpaDao() {
        return tagRuleTypeDao;
    }

    @Override
    protected Class<TagRuleType> getEntityClass() {
        return TagRuleType.class;
    }
}
