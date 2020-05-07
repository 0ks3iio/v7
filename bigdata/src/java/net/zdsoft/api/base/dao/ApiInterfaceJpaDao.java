package net.zdsoft.api.base.dao;

import java.util.List;

import net.zdsoft.api.base.entity.eis.ApiInterface;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ApiInterfaceJpaDao extends BaseJpaRepositoryDao<ApiInterface, String> {
	String SQL_AFTER=" and isUsing = 1";
	
    @Query("select distinct type from ApiInterface where dataType = ?1")
    public List<String> findInterfaceType(int dataType);

    public List<ApiInterface> findByTypeOrderByDisplayOrder(String type);

    public ApiInterface findByUri(String uri);
    @Modifying
    @Query("update ApiInterface set isUsing = ?1 where id in (?2)")
    public void updateUsingByIds(int status, String[] ids);
    
    @Modifying
    @Query("update ApiInterface set isUsing = ?1 where id = ?2")
    public void updateUsingById(int status, String id);

    @Query("From ApiInterface where isUsing = ?1 and id in (?2)")
	public List<ApiInterface> findByIds(int status,String[] ids);
    
    @Query("From ApiInterface where type = ?1 " + SQL_AFTER)
    public List<ApiInterface> findByType(String type);

    @Query("From ApiInterface where resultType = ?1 "  + SQL_AFTER)
    public List<ApiInterface> findByResultType(String type);

	/**
	 * @param dataType
	 * @return
	 */
    @Query("select distinct type, typeName from ApiInterface where dataType = ?1" + SQL_AFTER)
	public List<String[]> findDistinctTypeAndName(Integer dataType);

	 @Query("From ApiInterface where type = ?1 and  dataType = ?2 "  + SQL_AFTER)
	public List<ApiInterface> findByTypeAndDataType(String type, Integer dataType);

	@Modifying
    @Query("update ApiInterface set isUsing = ?1 where id = ?2")
	void updateInterfaceById(int isUsing, String interId);

	@Query("From ApiInterface where isUsing = ?1 ")
	public List<ApiInterface> findByIsUsing(int isUsing);

	public void deleteByType(String type);
	
	public void deleteByResultType(String resultType);
    
	@Modifying
    @Query("update ApiInterface set typeName = ?1 where type = ?2")
	public void updatetTypeNameByType(String typeName, String type);

	@Modifying
    @Query("update ApiInterface set typeName = ?1, type = ?2 where type = ?3")
	public void updatetTypeNameAndType(String typeName, String newType,
			String oldType);

	@Query("select distinct type from ApiInterface where isUsing = 1")
	public List<String> findDistinctType();

	@Query("select distinct type from ApiInterface where dataType = ?1" + SQL_AFTER)
	public List<String> findDistinctTypeByDataType(Integer dataType);

	@Modifying
    @Query("update ApiInterface set resultType = ?1  where resultType = ?2")
	public void updateResultType(String newType, String oldType);

	@Query("From ApiInterface where uri in (?1)" +  SQL_AFTER)
	public List<ApiInterface> findByUriIn(String[] uris);

	@Query("From ApiInterface where dataType = (?1)" +  SQL_AFTER)
	public List<ApiInterface> findByDataType(Integer dataType);

	@Query("From ApiInterface where resultType = ?1 and dataType = ?2" +  SQL_AFTER)
	public List<ApiInterface> findByResultTypeAndDataType(String resultType,
			Integer dataType);
}
