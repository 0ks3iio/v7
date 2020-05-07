package net.zdsoft.basedata.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.CodeRuleDetail;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

/**
 * @author yangsj  2017-1-24下午2:49:59
 */
public interface CodeRuleDetailDao extends BaseJpaRepositoryDao<CodeRuleDetail, String> {

	/**
	 * @param ruleId
	 * @return
	 */
	@Query("From CodeRuleDetail where isDeleted = 0 and  ruleId=?1  order by rulePosition ")
	List<CodeRuleDetail> findByRuleId(String ruleId);

	
}
