package net.zdsoft.remote.openapi.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.remote.openapi.entity.Developer;
import net.zdsoft.remote.openapi.entity.OpenApiEntity;

public interface OpenApiEntityService extends BaseService<OpenApiEntity, String> {

    Map<String, List<OpenApiEntity>> findEntities(Developer developer, String... types);

    int updateEntityHide(String entityName, String type);

    OpenApiEntity updateEntity(OpenApiEntity entity);

    int updateEntityMcode(String mcodeId, String id);

    OpenApiEntity findByTypeAndEntityName(String type, String entityName);

    List<OpenApiEntity> findByType(String type);

    /**
     * 获得接口返回回来的常数
     * 
     * @author chicb
     * @param applyStatus
     * @param resultType
     * @param ticketKey
     * @return
     */
    List<OpenApiEntity> getEntityParams(int applyStatus, String resultType, String ticketKey);

    /**
     * 获得敏感字段
     * 
     * @author chicb
     * @return map<Type,Map<id,entityClounmName>>
     */
    Map<String, Map<String, String>> getSensitiveFile();

    /**
     * 获取固定类型的敏感字段
     * 
     * @author chicb
     * @param type
     * @param isSensitive
     * @return
     */
    List<OpenApiEntity> getEntitys(String type, int isSensitive);

    /**
     * 批量查询固定类型的敏感字段
     * 
     * @author chicb
     * @param isSensitive
     * @param types
     * @return
     */
    List<OpenApiEntity> getEntities(int isSensitive, String[] types);

	void updateEntityById(int isUsing, String entityId);

	void deleteByType(String type);

	/**
	 * 批量查询类型的属性子段
	 * @param types
	 * @return
	 */
	List<OpenApiEntity> findByTypeIn(String[] types);

	List<OpenApiEntity> findByIsUsingAndEntityColumnNameIn(int isUsing,
			String... columnName);

	List<OpenApiEntity> findByTypeAll(String type);

	List<OpenApiEntity> findByIsUsingAndEntityColumnNameInAndTypeIn(int isUsing,
			String[] columnName, String[] types);

	void updateType(String newType, String oldType);

}
