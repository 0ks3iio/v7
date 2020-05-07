package net.zdsoft.remote.openapi.remote.service;

import net.zdsoft.basedata.remote.service.BaseRemoteService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.remote.openapi.entity.OpenApiInterface;

public interface OpenApiInterfaceRemoteService extends BaseRemoteService<OpenApiInterface,String> {

    String findInterfaces(String type);
    
    String getByType(String type);

    String getByResultType(String type);
    
    String findDistinctType();

	/**
	 * @param dataType
	 * @return
	 */
	String findDistinctTypeByDataType(Integer dataType);

	
	String findByTypeAndDataType(String type, Integer dataType);

	
	void deleteById(String interfaceId);

	void updateInterfaceById(int isUsing, String interId);

	String getAllInterfaces(String typeName, Integer isUsing,
			Integer dataType, Pagination page);

	String findByIsUsing(int isUsing);

	void deleteByType(String type);

	void updatetTypeNameByType(String typeName, String type);

	void updatetTypeNameAndType(String typeName, String newType, String oldType);

	void updateResultType(String newType, String oldType);

	void deleteByResultType(String resultType);
}
