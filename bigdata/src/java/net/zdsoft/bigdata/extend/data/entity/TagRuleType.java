package net.zdsoft.bigdata.extend.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * Created by wangdongdong on 2018/7/11 11:09.
 */
@Entity
@Table(name = "bg_tag_rule_type")
public class TagRuleType extends BaseEntity<String> {

    private Short ruleType;

    private String ruleTypeName;

    @Override
    public String fetchCacheEntitName() {
        return "tagRuleType";
    }

    public Short getRuleType() {
        return ruleType;
    }

    public void setRuleType(Short ruleType) {
        this.ruleType = ruleType;
    }

    public String getRuleTypeName() {
        return ruleTypeName;
    }

    public void setRuleTypeName(String ruleTypeName) {
        this.ruleTypeName = ruleTypeName;
    }
}
