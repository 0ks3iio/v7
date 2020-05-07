package net.zdsoft.remote.openapi.remote.service.impl;

import net.zdsoft.basedata.remote.service.impl.BaseRemoteServiceImpl;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.remote.openapi.entity.OpenApiInterface;
import net.zdsoft.remote.openapi.remote.service.OpenApiInterfaceRemoteService;
import net.zdsoft.remote.openapi.service.OpenApiInterfaceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("openApiInterfaceRemoteService")
public class OpenApiInterfaceRemoteServiceImpl extends BaseRemoteServiceImpl<OpenApiInterface, String> implements
        OpenApiInterfaceRemoteService {
    @Autowired
    private OpenApiInterfaceService openApiInterfaceService;
 
    @Override
    protected BaseService<OpenApiInterface, String> getBaseService() {
        return openApiInterfaceService;
    }

    @Override
    public String findInterfaces(String type) {
        return SUtils.s(openApiInterfaceService.findInterfaces(type));
    }
    
    @Override
	public String getByType(String type) {
		return SUtils.s(openApiInterfaceService.getByType(type));
	}

	@Override
	public String getByResultType(String type) {
		return SUtils.s(openApiInterfaceService.getByResultType(type));
	}

	@Override
	public String findDistinctType() {
		return SUtils.s(openApiInterfaceService.findDistinctType());
	}

	@Override
	public String findDistinctTypeByDataType(Integer dataType) {
		return SUtils.s(openApiInterfaceService.findDistinctTypeByDataType(dataType));
	}

	@Override
	public String findByTypeAndDataType(String type, Integer dataType) {
		return SUtils.s(openApiInterfaceService.findByTypeAndDataType(type,dataType));
	}

	@Override
	public void deleteById(String interfaceId) {
		openApiInterfaceService.delete(interfaceId);
	}

	@Override
	public void updateInterfaceById(int isUsing, String interId) {
		openApiInterfaceService.updateInterfaceById(isUsing, interId);
	}

	@Override
	public String getAllInterfaces(String typeName, Integer isUsing,
			Integer dataType, Pagination page) {
		return SUtils.s(openApiInterfaceService.getAllInterfaces(typeName,isUsing,dataType,page));
	}

	@Override
	public String findByIsUsing(int isUsing) {
		return SUtils.s(openApiInterfaceService.findByIsUsing(isUsing));
	}

	@Override
	public void deleteByType(String type) {
		openApiInterfaceService.deleteByType(type);
	}

	@Override
	public void updatetTypeNameByType(String typeName, String type) {
		openApiInterfaceService.updatetTypeNameByType(typeName,type);
	}

	@Override
	public void updatetTypeNameAndType(String typeName, String newType,
			String oldType) {
		openApiInterfaceService.updatetTypeNameAndType(typeName,newType,oldType);
	}

	@Override
	public void updateResultType(String newType, String oldType) {
		openApiInterfaceService.updateResultType(newType,oldType);
	}

	@Override
	public void deleteByResultType(String resultType) {
		openApiInterfaceService.deleteByResultType(resultType);
	}
}
