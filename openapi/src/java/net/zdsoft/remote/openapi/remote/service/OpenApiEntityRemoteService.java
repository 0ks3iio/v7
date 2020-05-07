package net.zdsoft.remote.openapi.remote.service;

import net.zdsoft.basedata.remote.service.BaseRemoteService;
import net.zdsoft.remote.openapi.entity.OpenApiEntity;

public interface OpenApiEntityRemoteService extends BaseRemoteService<OpenApiEntity, String> {

    String findByType(String type);

	void deleteById(String entityId);

	void updateEntityById(int isUsing, String entityId);

	void deleteByType(String type);

	String findByTypeAll(String type);

	void updateType(String newType, String oldType);
}
