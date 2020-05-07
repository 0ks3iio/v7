package net.zdsoft.remote.openapi.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.remote.openapi.entity.OpenApiInterface;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface OpenApiInterfaceJpaDao extends BaseJpaRepositoryDao<OpenApiInterface, String> {
	String SQL_AFTER=" and isUsing = 1";
	
    @Query("select distinct type from OpenApiInterface where dataType = ?1")
    public List<String> findInterfaceType(int dataType);

    public List<OpenApiInterface> findByTypeOrderByDisplayOrder(String type);

    public OpenApiInterface findByUri(String uri);
    @Modifying
    @Query("update OpenApiInterface set isUsing = ?1 where id in (?2)")
    public void updateUsingByIds(int status, String[] ids);
    
    @Modifying
    @Query("update OpenApiInterface set isUsing = ?1 where id = ?2")
    public void updateUsingById(int status, String id);

    @Query("From OpenApiInterface where isUsing = ?1 and id in (?2)")
	public List<OpenApiInterface> findByIds(int status,String[] ids);
    
	
    public List<OpenApiInterface> findByType(String type);

    @Query("From OpenApiInterface where resultType = ?1 "  + SQL_AFTER)
    public List<OpenApiInterface> findByResultType(String type);
    
    @Query("select distinct type, typeName from OpenApiInterface where isUsing = 1 ")
    public List<String[]> findDistinctType();

	/**
	 * @param dataType
	 * @return
	 */
    @Query("select distinct type, typeName from OpenApiInterface where dataType = ?1" + SQL_AFTER)
	public List<String[]> findDistinctType(Integer dataType);

	 @Query("From OpenApiInterface where type = ?1 and  dataType = ?2 "  + SQL_AFTER)
	public List<OpenApiInterface> findByTypeAndDataType(String type, Integer dataType);

	@Modifying
    @Query("update OpenApiInterface set isUsing = ?1 where id = ?2")
	void updateInterfaceById(int isUsing, String interId);

	@Query("From OpenApiInterface where isUsing = ?1 ")
	public List<OpenApiInterface> findByIsUsing(int isUsing);

	public void deleteByType(String type);
	
	public void deleteByResultType(String resultType);
    
	@Modifying
    @Query("update OpenApiInterface set typeName = ?1 where type = ?2")
	public void updatetTypeNameByType(String typeName, String type);

	@Modifying
    @Query("update OpenApiInterface set typeName = ?1, type = ?2 where type = ?3")
	public void updatetTypeNameAndType(String typeName, String newType,
			String oldType);

	@Query("select distinct resultType from OpenApiInterface where isUsing = 1")
	public List<String> findDistinctResultType();

	@Query("select distinct resultType from OpenApiInterface where dataType = ?1" + SQL_AFTER)
	public List<String> findDistinctResultTypeByDataType(Integer dataType);

	 @Query("From OpenApiInterface where resultType = ?1 and  dataType = ?2 "  + SQL_AFTER)
	public List<OpenApiInterface> findByResultTypAndDataType(String type,
			Integer dataType);

	@Modifying
    @Query("update OpenApiInterface set resultType = ?1  where resultType = ?2")
	public void updateResultType(String newType, String oldType);

	@Query("From OpenApiInterface where uri in (?1)" +  SQL_AFTER)
	public List<OpenApiInterface> findByUriIn(String[] uris);
}
