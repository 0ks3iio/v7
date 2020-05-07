package net.zdsoft.bigdata.extend.data.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.extend.data.entity.TagRuleSymbol;

/**
 * Created by wangdongdong on 2018/7/9 13:41.
 */
public interface TagRuleSymbolService extends BaseService<TagRuleSymbol, String> {

    List<TagRuleSymbol> getTagRuleSymbolByRuleType(Short ruleType);

    /**
     * 获取符号map(id, TagRuleSymbol)
     * @return
     */
    Map<String, TagRuleSymbol> getTagRuleSymbolMap();

}
