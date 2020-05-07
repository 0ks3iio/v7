package net.zdsoft.api.base.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.api.base.entity.eis.ApiDeveloper;
import net.zdsoft.api.base.entity.eis.ApiEntity;
import net.zdsoft.basedata.service.BaseService;

public interface ApiEntityService extends BaseService<ApiEntity, String> {

    Map<String, List<ApiEntity>> findEntities(ApiDeveloper developer,String interfaceId, String... types);

    int updateEntityHide(String entityName, String type);

    ApiEntity updateEntity(ApiEntity entity);

    int updateEntityMcode(String mcodeId, String id);

    ApiEntity findByTypeAndEntityName(String type, String entityName);

    List<ApiEntity> findByType(String type);

    /**
     * 获得接口返回回来的常数
     * 
     * @param applyStatus
     * @param type
     * @param resultType
     * @param ticketKey
     * @return
     */
    List<ApiEntity> getEntityParams(int applyStatus,String type, String resultType, String ticketKey);

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
    List<ApiEntity> getEntitys(String type, int isSensitive);

    /**
     * 批量查询固定类型的敏感字段
     * 
     * @author chicb
     * @param isSensitive
     * @param types
     * @return
     */
    List<ApiEntity> getEntities(int isSensitive, String[] types);

	void updateEntityById(int isUsing, String entityId);

	void deleteByType(String type);

	/**
	 * 批量查询类型的属性子段
	 * @param types
	 * @return
	 */
	List<ApiEntity> findByTypeIn(String[] types);

	List<ApiEntity> findByIsUsingAndEntityColumnNameIn(int isUsing,
			String... columnName);

	List<ApiEntity> findByTypeAll(String type);

	List<ApiEntity> findByIsUsingAndEntityColumnNameInAndTypeIn(int isUsing,
			String[] columnName, String[] types);

	void updateType(String newType, String oldType);

	List<ApiEntity> findByMetadataIdIn(String... metadataIds);
	
	boolean findByMetadataIdAndColumnName(String metadataId,String columnName);
	
}
