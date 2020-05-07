package net.zdsoft.remote.openapi.remote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.remote.service.impl.BaseRemoteServiceImpl;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.remote.openapi.entity.OpenApiParameter;
import net.zdsoft.remote.openapi.remote.service.OpenApiParameterRemoteService;
import net.zdsoft.remote.openapi.service.OpenApiParameterService;

@Service("openApiParameterRemoteService")
public class OpenApiParameterRemoteServiceImpl extends BaseRemoteServiceImpl<OpenApiParameter, String> implements
		OpenApiParameterRemoteService {
    
	@Autowired
	private OpenApiParameterService openApiParameterService;
	
	@Override
	protected BaseService<OpenApiParameter, String> getBaseService() {
		return openApiParameterService;
	}

	@Override
	public void deleteById(String paramId) {
		openApiParameterService.delete(paramId);
	}

	@Override
	public void deleteByUri(String uri) {
		openApiParameterService.deleteByUri(uri);
	}

	@Override
	public String findByUri(String uri) {
		return SUtils.s(openApiParameterService.findByUri(uri));
	}

	@Override
	public void updateUriByOlduri(String newUri, String oldUri) {
		openApiParameterService.updateUriByOlduri(newUri,oldUri);
	}


}
