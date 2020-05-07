package net.zdsoft.remote.openapi.remote.service;

import net.zdsoft.basedata.remote.service.BaseRemoteService;
import net.zdsoft.remote.openapi.entity.OpenApiParameter;

public interface OpenApiParameterRemoteService extends BaseRemoteService<OpenApiParameter,String>{

	void deleteById(String paramId);

	void deleteByUri(String uri);

	String findByUri(String uri);

	void updateUriByOlduri(String newUri, String oldUri);

}
