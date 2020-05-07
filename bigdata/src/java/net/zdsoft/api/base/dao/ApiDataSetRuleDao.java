package net.zdsoft.api.base.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.api.base.entity.eis.ApiDataSetRule;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface ApiDataSetRuleDao extends BaseJpaRepositoryDao<ApiDataSetRule, String>{

	@Modifying
    @Query("delete from ApiDataSetRule where dsId = ?1 ")
	void deleteByDsId(String dsId);

	List<ApiDataSetRule> findByDsId(String dsId);

	
	List<ApiDataSetRule> findByDsIdIn(String[] dsIds);

}
