package net.zdsoft.bigdata.metadata.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.metadata.entity.QualityRule;

import java.util.List;

public interface QualityRuleService extends BaseService<QualityRule, String>{


	/**
	 * 根据dimcode获取质量规则list
	 * @param dimCode (传空值可以获取所有的规则)
	 * @return
	 */
	public List<QualityRule> findQualityRulesByDimCode(String dimCode);
	
	/**
	 * 更新质量维度
	 * @param qualityRule
	 */
	public void saveQualityRule(QualityRule qualityRule) ;

	/**
	 * 根据规则类型查询
	 * @param ruleType
	 * @return
	 */
	List<QualityRule> findQualityRulesByRuleType(Integer ruleType);
}
