package net.zdsoft.base.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.base.entity.eis.OpenApiInterfaceCount;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

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

	@Query(value="select ticketKey, count(*) From OpenApiInterfaceCount group by ticketKey")
	List<Object[]> findCountGroupByTicketKey();

	@Query(value="select ticketKey, count(*) From OpenApiInterfaceCount where type =?1 group by ticketKey")
	List<Object[]> findCountGroupByTicketKeyByType(String type);

	@Query(value="select count(*) From OpenApiInterfaceCount where ticketKey = ?1 and type = ?2 and creationTime >= ?3 "
			+ "and creationTime <= ?4 ")
	long findCountByType(String ticketKey, String type, Date startTime,
			Date endTime);

	@Query(value="select count(*) From OpenApiInterfaceCount where ticketKey = ?1 and interfaceId = ?2 and creationTime >= ?3 "
			+ "and creationTime <= ?4 ")
	long findCountByInterfaceId(String ticketKey, String interfaceId,
			Date startTime, Date endTime);
	

}
