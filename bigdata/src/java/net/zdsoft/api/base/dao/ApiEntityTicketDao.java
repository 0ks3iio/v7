package net.zdsoft.api.base.dao;

import net.zdsoft.api.base.entity.eis.ApiEntityTicket;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Chicb
 * @version $Revision: 1.0 $, $Date: 2017-3-1 上午10:01:49 $
 */
public interface ApiEntityTicketDao extends BaseJpaRepositoryDao<ApiEntityTicket, String> {

    @Modifying
    @Query("delete from ApiEntityTicket where type in ?1 and ticketKey =?2")
    void deleteByTypeInAndTicketKey(String[] type, String ticketKey);

    @Modifying
    @Query("delete from ApiEntityTicket where type=?1")
    void deleteByType(String type);


    List<ApiEntityTicket> findByTicketKeyAndTypeIn(String ticketKey, String... types);


    void deleteByIdIn(String... array);

    @Query("from ApiEntityTicket where ticketKey = ?1 and interfaceId = ?2 ")
    List<ApiEntityTicket> findByTicketKeyAndInterfaceId(String ticketKey, String interfaceId);

    @Modifying
    void deleteByInterfaceId(String interfaceId);

    @Modifying
    @Query("delete ApiEntityTicket where interfaceId=?1 and ticketKey=?2")
    void deleteByInterfaceIdAndTicketKey(String interfaceId, String ticketKey);

    @Query("from ApiEntityTicket where ticketKey = ?1 and interfaceId in (?2) ")
	List<ApiEntityTicket> fingByTicketKeyAndInterfaceIdIn(String ticketKey,
			String[] interfaceIds);
}
