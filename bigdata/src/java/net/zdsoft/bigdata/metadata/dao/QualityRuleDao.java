package net.zdsoft.bigdata.metadata.dao;

import net.zdsoft.bigdata.metadata.entity.QualityRule;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 质量维度 dao
 * @author feekang
 *
 */
public interface QualityRuleDao extends BaseJpaRepositoryDao<QualityRule, String>{

	@Query("From QualityRule order by orderId")
	public List<QualityRule> findQualityRules();
	
	@Query("From QualityRule where dimCode = ?1 order by orderId")
	public List<QualityRule> findQualityRulesByDimCode(String dimCode);

	List<QualityRule> findAllByRuleType(Integer ruleType);
}
