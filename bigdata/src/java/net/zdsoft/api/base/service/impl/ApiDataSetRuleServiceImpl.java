package net.zdsoft.api.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.api.base.dao.ApiDataSetRuleDao;
import net.zdsoft.api.base.entity.eis.ApiDataSetRule;
import net.zdsoft.api.base.service.ApiDataSetRuleService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

@Service("apiDataSetRuleService")
public class ApiDataSetRuleServiceImpl extends BaseServiceImpl<ApiDataSetRule, String>  implements ApiDataSetRuleService {

	@Autowired
	private ApiDataSetRuleDao apiDataSetRuleDao;
	@Override
	protected BaseJpaRepositoryDao<ApiDataSetRule, String> getJpaDao() {
		return apiDataSetRuleDao;
	}

	@Override
	protected Class<ApiDataSetRule> getEntityClass() {
		return ApiDataSetRule.class;
	}

	@Override
	public void deleteByDsId(String dsId) {
		apiDataSetRuleDao.deleteByDsId(dsId);
	}

	@Override
	public List<ApiDataSetRule> findByDsId(String dsId) {
		return apiDataSetRuleDao.findByDsId(dsId);
	}

	@Override
	public List<ApiDataSetRule> findByDsIdIn(String[] dsIds) {
		return apiDataSetRuleDao.findByDsIdIn(dsIds);
	}
}
