package net.zdsoft.api.base.service;

import java.util.Date;
import java.util.List;

import net.zdsoft.api.base.dto.AuditApply;
import net.zdsoft.api.base.dto.InterfaceDto;
import net.zdsoft.api.base.entity.eis.ApiApply;
import net.zdsoft.api.base.entity.eis.ApiEntityTicket;
import net.zdsoft.api.openapi.remote.openapi.dto.ApiInterfaceApplyPass;
import net.zdsoft.basedata.service.BaseService;

public interface ApiApplyService extends BaseService<ApiApply, String>{

    void deleteByInterfaceId(String interfaceId);

    void passApply(ApiInterfaceApplyPass applyPass);

    public void save(ApiApply openApiApply);

//    public List<String> getTypes(int status, String developerId);

//    public List<ApiApply> getApplys(String ticketKey);
//
//    public List<ApiApply> getApplys(String[] developerIds);

    /**
     * 
     * @author chicb
     * @param openApiApplys
     */
    public void saveAll(List<ApiApply> openApiApplys);

    /**
     * 批量修改申请状态，根据id
     * 
     * @author chicb
     * @param modifyApplys
     */
    public void updateBatchStatus(int status, Date modifyTime, String[] ids);

    
	public List<ApiApply> findByTicketKeyAndStatusIn(String ticketKey, int[] status);

	public List<ApiApply> findByTypeAndStatusIn(String type, int[] status);

	public List<ApiApply> findByTicketKeyAndTypeIn(String ticketKey,
			String... types);

	public List<ApiApply> findByTicketKeyAndTypeOrInterfaceId(
			String ticketKey, String type, String interfaceId);

	public List<ApiApply> findByTicketKeyAndStatus(String ticketKey,
			int state);

	public void updateApplyInterface(String ticketKey,List<InterfaceDto> allInterfaceDtos);

	public void deleteByTicketAndTypeIn(String ticketKey, String[] type);

    void deleteApply(String applyId);

    List<AuditApply> getAuditApplies(String[] ticketKeys);

	void saveAllAndEntityTicket(List<ApiApply> saveApiApplies,List<ApiEntityTicket> saveEntityTickets);

	List<ApiApply> findByTicketKeyAndStatusInAndInterfaceIdIn(String ticketKey,
			int[] status, String[] interfaceIds);

	List<ApiApply> findByTicketKeyAndStatusAndType(String ticketKey,
			int status, String type);

	List<ApiApply> findByInterfaceIdAndStatusIn(String interfaceId, int[] status);

	ApiApply findByTicketKeyAndInterfaceId(String ticketKey, String interfaceId);
}
