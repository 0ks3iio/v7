package net.zdsoft.remote.openapi.remote.service;

import net.zdsoft.basedata.remote.service.BaseRemoteService;
import net.zdsoft.remote.openapi.entity.OpenApiInterfaceType;

public interface OpenApiInterfaceTypeRemoteService extends BaseRemoteService<OpenApiInterfaceType,String>{

	String findByClassifyIn(Integer... classify);

	String getInterfaceTypes(String typeName, Integer classify);

	String findByTypeAndByClassify(String type, Integer classify);

	void deleteById(String typeId);
	
}
