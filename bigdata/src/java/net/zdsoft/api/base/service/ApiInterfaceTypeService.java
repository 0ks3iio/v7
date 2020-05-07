package net.zdsoft.api.base.service;

import java.util.List;

import net.zdsoft.api.base.entity.eis.ApiInterfaceType;
import net.zdsoft.basedata.service.BaseService;

public interface ApiInterfaceTypeService extends BaseService<ApiInterfaceType, String>{

	List<ApiInterfaceType> findByClassifyIn(Integer... classify);

	List<ApiInterfaceType> getInterfaceTypes(String typeName, Integer classify);

	List<ApiInterfaceType> findByTypeAndByClassify(String type, Integer classify);

	
}
