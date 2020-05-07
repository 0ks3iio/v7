package net.zdsoft.api.base.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.api.base.entity.eis.ApiParameter;
import net.zdsoft.basedata.service.BaseService;

public interface ApiParameterService extends BaseService<ApiParameter, String> {
    Map<String, List<ApiParameter>> findParameters(String... uri);

    void delete(String id);

	List<ApiParameter> findByInterfaceId(String interfaceId);

	List<ApiParameter> findByInterfaceIdIn(String... interfaceIds);

	void deleteByInterfaceId(String interfaceId);
}
