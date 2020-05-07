package net.zdsoft.remote.openapi.remote.service.impl;

import net.zdsoft.basedata.remote.service.impl.BaseRemoteServiceImpl;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.remote.openapi.entity.OpenApiEntity;
import net.zdsoft.remote.openapi.remote.service.OpenApiEntityRemoteService;
import net.zdsoft.remote.openapi.service.EntityTicketService;
import net.zdsoft.remote.openapi.service.OpenApiEntityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("openApiEntityRemoteService")
public class OpenApiEntityRemoteServiceImpl extends BaseRemoteServiceImpl<OpenApiEntity, String> implements
        OpenApiEntityRemoteService {
    @Autowired
    private OpenApiEntityService openApiEntityService;
    @Autowired
    private EntityTicketService entityTicketService;

    @Override
    protected BaseService<OpenApiEntity, String> getBaseService() {
        return openApiEntityService;
    }

    @Override
    public String findByType(String type) {
        return SUtils.s(openApiEntityService.findByType(type));
    }

    
    
	@Override
	public void deleteById(String entityId) {
		openApiEntityService.delete(entityId);
	}

	@Override
	public void updateEntityById(int isUsing, String entityId) {
		openApiEntityService.updateEntityById(isUsing,entityId);
	}

	@Override
	public void deleteByType(String type) {
		openApiEntityService.deleteByType(type);
		entityTicketService.deleteByType(type);
	}

	@Override
	public String findByTypeAll(String type) {
		return SUtils.s(openApiEntityService.findByTypeAll(type));
	}

	@Override
	public void updateType(String newType, String oldType) {
		openApiEntityService.updateType(newType,oldType);
	}

}
