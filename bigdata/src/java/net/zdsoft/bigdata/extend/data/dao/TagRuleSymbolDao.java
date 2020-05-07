package net.zdsoft.bigdata.extend.data.dao;

import java.util.List;

import net.zdsoft.bigdata.extend.data.entity.TagRuleSymbol;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

/**
 * Created by wangdongdong on 2018/7/9 13:44.
 */
public interface TagRuleSymbolDao extends BaseJpaRepositoryDao<TagRuleSymbol, String> {

    List<TagRuleSymbol> getTagRuleSymbolsByRuleType(Short ruleType);

}
