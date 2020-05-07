package net.zdsoft.remote.openapi.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.remote.openapi.entity.OpenApiParameter;

public interface OpenApiParameterService extends BaseService<OpenApiParameter, String> {
    Map<String, List<OpenApiParameter>> findParameters(String... uri);

    void delete(String id);

	void deleteByUri(String uri);

	List<OpenApiParameter> findByUri(String uri);

	void updateUriByOlduri(String newUri, String oldUri);
}
