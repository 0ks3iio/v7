package net.zdsoft.api.base.dao;

import net.zdsoft.api.base.entity.eis.ApiEntity;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApiEntityJpaDao extends BaseJpaRepositoryDao<ApiEntity, String> {

    public List<ApiEntity> findByIsUsingAndTypeInOrderByDisplayOrder(int isUsing, String... types);

    @Modifying
    @Query("update ApiEntity set isUsing = 0 where entityName = ?1 and type = ?2")
    public int updateEntityHide(String entityName, String type);

    @Modifying
    @Query(nativeQuery = true, value = "update base_openapi_entity set mcode_id = :mcodeId where id = :id")
    public int updateEntityMcodeId(@Param("mcodeId") String mcodeId, @Param("id") String id);

    public ApiEntity findByTypeAndEntityName(String type, String entityName);
    
    @Query("From ApiEntity where isUsing = 1 and type = ?1")
    public List<ApiEntity> findByType(String type);

    @Query("From ApiEntity where isUsing = ?1 and id in (?2)")
    public List<ApiEntity> findByIds(int isUsing, String[] ids);

    /**
     * 根据 （类型，是否应用，是否是敏感字段）获得数据 displayOrder排序
     * 
     * @author chicb
     * @param type
     * @param isUsing
     * @param isSensitive
     * @return
     */
    public List<ApiEntity> findByTypeAndIsUsingAndIsSensitiveOrderByDisplayOrder(String type, int isUsing,
            int isSensitive);

    public List<ApiEntity> findByTypeAndIsUsingOrderByDisplayOrder(
			String resultType, int isUsing);
    /**
     * 根据（isUsing,isSensitive,types）查询实体类
     * 
     * @author chicb
     * @param isUsing
     * @param isSensitive
     * @param types
     * @return
     */
    public List<ApiEntity> findByIsUsingAndIsSensitiveAndTypeIn(int isUsing, int isSensitive, String[] types);

    /**
     * 根据（类型，是否应用，字段名）获得数据,dispalyOrder排序
     * 
     * @author chicb
     * @param resultType
     * @param isUsing
     * @param entityColumnNames
     */
    public List<ApiEntity> findByTypeAndIsUsingAndEntityColumnNameInOrderByDisplayOrder(String resultType,
            int value, String... entityColumnNames);

    /**
     * 根据（isUsing,isSensitive）查询实体类
     * 
     * @author chicb
     * @param value
     * @param value2
     * @return
     */
    public List<ApiEntity> findByIsUsingAndIsSensitive(int isUsing, int isSensitive);

    
    @Modifying
    @Query("update ApiEntity set isUsing = ?1 where id = ?2")
	public void updateEntityById(int isUsing, String entityId);

    @Modifying
    @Query("delete from ApiEntity where type = ?1")
	public void deleteByType(String type);

    @Query("From ApiEntity where isUsing = 1 and type in (?1)")
	public List<ApiEntity> findByTypeIn(String[] types);

	public List<ApiEntity> findByIsUsingAndEntityColumnNameIn(int isUsing, String... columnName);

	@Query("From ApiEntity where type = ?1")
	public List<ApiEntity> findByTypeAll(String type);

	public List<ApiEntity> findByIsUsingAndEntityColumnNameInAndTypeIn(
			int isUsing, String[] columnName, String[] types);

	@Modifying
    @Query("update ApiEntity set type = ?1 where type = ?2")
	public void updateType(String newType, String oldType);

    @Modifying
    @Query("delete from ApiEntity where id in (?1)")
	void deleteByIds(String[] ids);

    @Query("From ApiEntity where metadataId in (?1)")
	public List<ApiEntity> findByMetadataIdIn(String[] metadataIds);

    @Query("From ApiEntity where metadataId = ?1 and entityColumnName =?2 and isUsing = 1")
    ApiEntity findByMetadataIdAndColumnName(String metadataId, String columnName);
}
