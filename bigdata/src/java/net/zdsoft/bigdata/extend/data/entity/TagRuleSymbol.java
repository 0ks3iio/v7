package net.zdsoft.bigdata.extend.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * Created by wangdongdong on 2018/7/11 11:09.
 */
@Entity
@Table(name = "bg_tag_rule_symbol")
public class TagRuleSymbol extends BaseEntity<String> {

    private Short ruleType;

    private String symbolName;

    private String symbolCode;

    @Override
    public String fetchCacheEntitName() {
        return "tagRuleSymbol";
    }

    public Short getRuleType() {
        return ruleType;
    }

    public void setRuleType(Short ruleType) {
        this.ruleType = ruleType;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public void setSymbolName(String symbolName) {
        this.symbolName = symbolName;
    }

    public String getSymbolCode() {
        return symbolCode;
    }

    public void setSymbolCode(String symbolCode) {
        this.symbolCode = symbolCode;
    }
}
