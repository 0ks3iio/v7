package net.zdsoft.remote.openapi.remote.service.impl;

import net.zdsoft.basedata.remote.service.impl.BaseRemoteServiceImpl;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.remote.openapi.entity.OpenApiApply;
import net.zdsoft.remote.openapi.remote.service.OpenApiApplyRemoteService;
import net.zdsoft.remote.openapi.service.OpenApiApplyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("openApiApplyRemoteService")
public class OpenApiApplyRemoteServiceImpl extends BaseRemoteServiceImpl<OpenApiApply, String> implements OpenApiApplyRemoteService {
	
	@Autowired
    private OpenApiApplyService openApiApplyService;

	@Override
	protected BaseService<OpenApiApply, String> getBaseService() {
		return openApiApplyService;
	}

	@Override
	public String findByTypeAndStatusIn(String type, int[] status) {
		return SUtils.s(openApiApplyService.findByTypeAndStatusIn(type,status));
	}
	
	
	

}
