package net.zdsoft.remote.openapi.remote.service;

import net.zdsoft.basedata.remote.service.BaseRemoteService;
import net.zdsoft.remote.openapi.entity.OpenApiApply;

public interface OpenApiApplyRemoteService extends BaseRemoteService<OpenApiApply, String>{

	String findByTypeAndStatusIn(String type, int[] status);
	
}
