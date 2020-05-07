package net.zdsoft.base.service;

import java.util.List;

import net.zdsoft.base.entity.eis.OpenApiInterfaceType;
import net.zdsoft.basedata.service.BaseService;

public interface OpenApiInterfaceTypeService extends BaseService<OpenApiInterfaceType, String>{

	List<OpenApiInterfaceType> findByClassifyIn(Integer... classify);

	List<OpenApiInterfaceType> getInterfaceTypes(String typeName, Integer classify);

	List<OpenApiInterfaceType> findByTypeAndByClassify(String type, Integer classify);

	
}
