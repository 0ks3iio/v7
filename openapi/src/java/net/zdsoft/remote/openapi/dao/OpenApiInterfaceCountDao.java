package net.zdsoft.remote.openapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.remote.openapi.entity.OpenApiInterfaceCount;

/**
 * @author yangsj  2018年1月5日上午10:23:20
 */
public interface OpenApiInterfaceCountDao extends BaseJpaRepositoryDao<OpenApiInterfaceCount, String>{

	/**
	 * @param ticketKey
	 * @param types
	 * @return
	 */
	@Query("From OpenApiInterfaceCount where ticketKey = ?1 and type in (?2)")
	List<OpenApiInterfaceCount> findByTicketKeyAndTypeIn(String ticketKey, String[] types);
    
	@Query(value="select type, count(*) From OpenApiInterfaceCount where ticketKey = ?1 and type in (?2) group by type")
	List<Object[]> getTypeCountMap(String ticketKey, String[] types);
	
	@Query(value="select ticketKey, count(*) From OpenApiInterfaceCount group by ticketKey")
	List<Object[]> findCountGroupByTicketKey();

	@Query(value="select ticketKey, count(*) From OpenApiInterfaceCount where type =?1 group by ticketKey")
	List<Object[]> findCountGroupByTicketKeyByType(String type);
	

}
