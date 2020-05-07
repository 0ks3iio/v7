package net.zdsoft.base.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.base.entity.eis.OpenApiParameter;
import net.zdsoft.basedata.service.BaseService;

public interface OpenApiParameterService extends BaseService<OpenApiParameter, String> {
    Map<String, List<OpenApiParameter>> findParameters(String... uri);

    void delete(String id);

	void deleteByUri(String uri);

	List<OpenApiParameter> findByUri(String uri);

	void updateUriByOlduri(String newUri, String oldUri);

	List<OpenApiParameter> findByInterfaceId(String interfaceId);
}
