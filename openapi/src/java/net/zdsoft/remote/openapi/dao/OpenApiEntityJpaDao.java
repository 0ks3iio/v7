package net.zdsoft.remote.openapi.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.remote.openapi.entity.OpenApiEntity;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OpenApiEntityJpaDao extends BaseJpaRepositoryDao<OpenApiEntity, String> {

    public List<OpenApiEntity> findByIsUsingAndTypeInOrderByDisplayOrder(int isUsing, String... types);

    @Modifying
    @Query("update OpenApiEntity set isUsing = 0 where entityName = ?1 and type = ?2")
    public int updateEntityHide(String entityName, String type);

    @Modifying
    @Query(nativeQuery = true, value = "update base_openapi_entity set mcode_id = :mcodeId where id = :id")
    public int updateEntityMcodeId(@Param("mcodeId") String mcodeId, @Param("id") String id);

    public OpenApiEntity findByTypeAndEntityName(String type, String entityName);
    
    @Query("From OpenApiEntity where isUsing = 1 and type = ?1")
    public List<OpenApiEntity> findByType(String type);

    @Query("From OpenApiEntity where isUsing = ?1 and id in (?2)")
    public List<OpenApiEntity> findByIds(int isUsing, String[] ids);

    /**
     * 根据 （类型，是否应用，是否是敏感字段）获得数据 displayOrder排序
     * 
     * @author chicb
     * @param type
     * @param isUsing
     * @param isSensitive
     * @return
     */
    public List<OpenApiEntity> findByTypeAndIsUsingAndIsSensitiveOrderByDisplayOrder(String type, int isUsing,
            int isSensitive);

    public List<OpenApiEntity> findByTypeAndIsUsingOrderByDisplayOrder(
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
    public List<OpenApiEntity> findByIsUsingAndIsSensitiveAndTypeIn(int isUsing, int isSensitive, String[] types);

    /**
     * 根据（类型，是否应用，字段名）获得数据,dispalyOrder排序
     * 
     * @author chicb
     * @param resultType
     * @param isUsing
     * @param entityColumnNames
     */
    public List<OpenApiEntity> findByTypeAndIsUsingAndEntityColumnNameInOrderByDisplayOrder(String resultType,
            int value, String... entityColumnNames);

    /**
     * 根据（isUsing,isSensitive）查询实体类
     * 
     * @author chicb
     * @param value
     * @param value2
     * @return
     */
    public List<OpenApiEntity> findByIsUsingAndIsSensitive(int isUsing, int isSensitive);

    
    @Modifying
    @Query("update OpenApiEntity set isUsing = ?1 where id = ?2")
	public void updateEntityById(int isUsing, String entityId);

    @Modifying
    @Query("delete from OpenApiEntity where type = ?1")
	public void deleteByType(String type);

    @Query("From OpenApiEntity where isUsing = 1 and type in (?1)")
	public List<OpenApiEntity> findByTypeIn(String[] types);

	public List<OpenApiEntity> findByIsUsingAndEntityColumnNameIn(int isUsing, String... columnName);

	@Query("From OpenApiEntity where type = ?1")
	public List<OpenApiEntity> findByTypeAll(String type);

	public List<OpenApiEntity> findByIsUsingAndEntityColumnNameInAndTypeIn(
			int isUsing, String[] columnName, String[] types);

	@Modifying
    @Query("update OpenApiEntity set type = ?1 where type = ?2")
	public void updateType(String newType, String oldType);

}
