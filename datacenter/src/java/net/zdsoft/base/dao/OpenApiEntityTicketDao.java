package net.zdsoft.base.dao;

import java.util.List;

import net.zdsoft.base.entity.eis.OpenApiEntityTicket;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Chicb
 * @version $Revision: 1.0 $, $Date: 2017-3-1 上午10:01:49 $
 */
public interface OpenApiEntityTicketDao extends BaseJpaRepositoryDao<OpenApiEntityTicket, String> {

    @Modifying
    @Query("delete from OpenApiEntityTicket where type in ?1 and ticketKey =?2")
    void deleteByTypeInAndTicketKey(String[] type, String ticketKey);

    @Modifying
    @Query("delete from OpenApiEntityTicket where type=?1")
	void deleteByType(String type);

    
	List<OpenApiEntityTicket> findByTicketKeyAndTypeIn(String ticketKey, String... types);

	
	void deleteByIdIn(String... array);

	@Query("from OpenApiEntityTicket where ticketKey = ?1 and (interfaceId = ?2 or type in (?3))")
	List<OpenApiEntityTicket> findByTicketKeyAndInterfaceIdOrTypeIn(
			String ticketKey, String interfaceId, String[] types);
}
