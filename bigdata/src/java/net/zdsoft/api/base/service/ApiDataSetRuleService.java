package net.zdsoft.api.base.service;

import java.util.List;

import net.zdsoft.api.base.entity.eis.ApiDataSetRule;
import net.zdsoft.basedata.service.BaseService;

public interface ApiDataSetRuleService extends BaseService<ApiDataSetRule, String>{

	void deleteByDsId(String dsId);

	List<ApiDataSetRule> findByDsId(String dsId);

	List<ApiDataSetRule> findByDsIdIn(String[] dsIds);

}
