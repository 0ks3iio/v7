package net.zdsoft.remote.openapi.remote.service.impl;

import net.zdsoft.basedata.remote.service.impl.BaseRemoteServiceImpl;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.remote.openapi.entity.OpenApiInterfaceType;
import net.zdsoft.remote.openapi.remote.service.OpenApiInterfaceTypeRemoteService;
import net.zdsoft.remote.openapi.service.OpenApiInterfaceTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("openApiInterfaceTypeRemoteService")
public class OpenApiInterfaceTypeRemoteServiceImpl extends BaseRemoteServiceImpl<OpenApiInterfaceType, String> implements
		OpenApiInterfaceTypeRemoteService {

	@Autowired
    private OpenApiInterfaceTypeService openApiInterfaceTypeService;
	
	@Override
	protected BaseService<OpenApiInterfaceType, String> getBaseService() {
		return openApiInterfaceTypeService;
	}
	
	@Override
	public String findByClassifyIn(Integer... classify) {
		return SUtils.s(openApiInterfaceTypeService.findByClassifyIn(classify));
	}

	@Override
	public String getInterfaceTypes(String typeName, Integer classify) {
		return SUtils.s(openApiInterfaceTypeService.getInterfaceTypes(typeName,classify));
	}

	@Override
	public String findByTypeAndByClassify(String type, Integer classify) {
		return SUtils.s(openApiInterfaceTypeService.findByTypeAndByClassify(type,classify));
	}

	@Override
	public void deleteById(String typeId) {
		openApiInterfaceTypeService.delete(typeId);
	}
}
