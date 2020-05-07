package net.zdsoft.bigdata.extend.data.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.extend.data.dao.TagRuleSymbolDao;
import net.zdsoft.bigdata.extend.data.entity.TagRuleSymbol;
import net.zdsoft.bigdata.extend.data.service.TagRuleSymbolService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.stereotype.Service;

/**
 * Created by wangdongdong on 2018/7/9 13:42.
 */
@Service
public class TagRuleSymbolServiceImpl extends BaseServiceImpl<TagRuleSymbol, String> implements TagRuleSymbolService {


    @Resource
    private TagRuleSymbolDao tagRuleSymbolDao;

    @Override
    protected BaseJpaRepositoryDao<TagRuleSymbol, String> getJpaDao() {
        return tagRuleSymbolDao;
    }

    @Override
    protected Class<TagRuleSymbol> getEntityClass() {
        return TagRuleSymbol.class;
    }

    @Override
    public List<TagRuleSymbol> getTagRuleSymbolByRuleType(Short ruleType) {
        return tagRuleSymbolDao.getTagRuleSymbolsByRuleType(ruleType);
    }

    @Override
    public Map<String, TagRuleSymbol> getTagRuleSymbolMap() {
        return this.findAll().stream().collect(Collectors.toMap(TagRuleSymbol::getId, tagRuleSymbol -> tagRuleSymbol));
    }
}
