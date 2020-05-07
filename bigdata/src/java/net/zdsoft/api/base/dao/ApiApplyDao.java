package net.zdsoft.api.base.dao;

import java.util.Date;
import java.util.List;

import net.zdsoft.api.base.dto.AuditApply;
import net.zdsoft.api.base.entity.eis.ApiApply;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ApiApplyDao extends BaseJpaRepositoryDao<ApiApply, String> {

    /**
     * 批量修改申请状态，根据 id
     * 
     * @author chicb
     * @param modifyApplys
     */
    @Modifying
    @Query("update ApiApply set status=?1,modifyTime=?2 where id in ?3")
    public void batchUpdateStatus(int status, Date modifyTime, String[] ids);

    @Query("from ApiApply where ticketKey = ?1 and status in ?2")
	public List<ApiApply> findByTicketKeyAndStatusIn(String ticketKey, int[] status);

    @Query("from ApiApply where type = ?1 and status in ?2")
	public List<ApiApply> findByTypeAndStatusIn(String type, int[] status);

    @Query("from ApiApply where ticketKey = ?1 and type in ?2")
	public List<ApiApply> findByTicketKeyAndTypeIn(String ticketKey,
			String[] types);

    @Query("from ApiApply where ticketKey = ?1 and (type = ?2 or interfaceId = ?3)")
	public List<ApiApply> findByTicketKeyAndTypeOrInterfaceId(String ticketKey, String type, String interfaceId);

    @Query("from ApiApply where ticketKey = ?1 and status = ?2")
	public List<ApiApply> findByTicketKeyAndStatus(String ticketKey,int status);

    @Modifying
    @Query("delete from ApiApply where ticketKey = ?1 and type in ?2")
	public void deleteByTicketAndTypeIn(String ticketKey, String[] type);

    @Modifying
    void deleteByInterfaceId(String interfaceId);

    @Query("select new net.zdsoft.api.base.dto.AuditApply(ticketKey, count(interfaceId)) from ApiApply where ticketKey in (?1) and status=3 group by ticketKey")
    List<AuditApply> getAuditApplies(String[] ticketKeys);

    @Query("from ApiApply where type = ?1 and status in ?2 and interfaceId in ?3")
	public List<ApiApply> findByTicketKeyAndStatusInAndInterfaceIdIn(
			String ticketKey, int[] status, String[] interfaceIds);

    @Query("from ApiApply where ticketKey = ?1 and status = ?2 and type = ?3")
	public List<ApiApply> findByTicketKeyAndStatusAndType(String ticketKey,
			int status, String type);
    
    @Query("from ApiApply where interfaceId = ?1 and status in ?2 ")
	public List<ApiApply> findByInterfaceIdAndStatusIn(String interfaceId, int[] status);

    @Query("from ApiApply where ticketKey = ?1 and interfaceId = ?2 and status = 1 ")
	public ApiApply findByTicketKeyAndInterfaceId(String ticketKey,String interfaceId);
}
