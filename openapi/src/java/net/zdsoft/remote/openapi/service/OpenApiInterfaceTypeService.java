package net.zdsoft.remote.openapi.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.remote.openapi.entity.OpenApiInterfaceType;

public interface OpenApiInterfaceTypeService extends BaseService<OpenApiInterfaceType, String>{

	List<OpenApiInterfaceType> findByClassifyIn(Integer... classify);

	List<OpenApiInterfaceType> getInterfaceTypes(String typeName, Integer classify);

	List<OpenApiInterfaceType> findByTypeAndByClassify(String type, Integer classify);

	
}
