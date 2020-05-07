package net.zdsoft.api.base.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.api.base.entity.eis.ApiInterfaceCount;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

/**
 * @author yangsj  2018年1月5日上午10:23:20
 */
public interface ApiInterfaceCountDao extends BaseJpaRepositoryDao<ApiInterfaceCount, String>{

	@Query(value="select count(*) From ApiInterfaceCount where ticketKey = ?1 and type = ?2 and creationTime >= ?3 "
			+ "and creationTime <= ?4 ")
	long findCountByType(String ticketKey, String type, Date startTime,
			Date endTime);

	@Query(value="select count(*) From ApiInterfaceCount where ticketKey = ?1 and interfaceId = ?2 and creationTime >= ?3 "
			+ "and creationTime <= ?4 ")
	long findCountByInterfaceId(String ticketKey, String interfaceId,
			Date startTime, Date endTime);

	@Query(value="From ApiInterfaceCount where resultType = ?1 and creationTime >= ?2 "
			+ "and creationTime <= ?3  order by  creationTime ")
	List<ApiInterfaceCount> findByResultTypeAndCreationTime(String resultType,
			Date mouthFirst, Date mouthEnd);

	@Query(value="select ticketKey From ApiInterfaceCount group by ticketKey ")
	List<String> getDistinctTicketKey();

	@Query(value="From ApiInterfaceCount where ticketKey = ?1 and creationTime >= ?2 "
			+ "and creationTime <= ?3  order by  creationTime ")
	List<ApiInterfaceCount> findByTicketKeyAndCreationTime(String ticketKey,
			Date dateStart, Date dataEnd);

	
	List<ApiInterfaceCount> findByTicketKey(String ticketKey);
	

}
