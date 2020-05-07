package net.zdsoft.base.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.zdsoft.base.dto.InterfaceDto;
import net.zdsoft.base.entity.eis.OpenApiApply;
import net.zdsoft.basedata.service.BaseService;

public interface OpenApiApplyService extends BaseService<OpenApiApply, String>{

    public void save(OpenApiApply openApiApply);

    public List<String> getTypes(int status, String developerId);

    public List<OpenApiApply> getApplys(String ticketKey);

    public List<OpenApiApply> getApplys(String[] developerIds);

    /**
     * 
     * @author chicb
     * @param openApiApplys
     */
    public void saveAll(List<OpenApiApply> openApiApplys);

    /**
     * 批量修改申请状态，根据id
     * 
     * @author chicb
     * @param modifyApplys
     */
    public void updateBatchStatus(int status, Date modifyTime, String[] ids);

    /**
     * 根据申请状态和开发者编号s查询申请接口
     * 
     * @author chicb
     * @param status
     * @param developerIds
     * @return id types[nameValue...]
     */
//    public Map<String, List<String>> getTypes(int status, String[] developerIds);

    /**
     * 删除已订阅的接口
     * 
     * @author chicb
     * @param type
     * @param ticketKey
     * @param developerId
     */
    public void removeInterface(String[] type, String ticketKey, String developerId);

    /**
     * 更改申请信息
     * 
     */
    public void updateApplyInterface(String[] types, String ticketKey, int status);

    /**
     * @author chicb
     * @param id
     * @param ticketKey
     * @param passColumnMap  审核通过的字段
     */
    public void updatePassApply(String developerId, String ticketKey, Map<String, List<String>> passColumnMap);

    
	public List<String> findByTicketKeyAndStatusIn(String ticketKey, int[] status);

	
	public List<OpenApiApply> findByTypeAndStatusIn(String type, int[] status);

	public List<OpenApiApply> findByTicketKeyAndTypeIn(String ticketKey,
			String... types);

	public List<OpenApiApply> findByTicketKeyAndTypeOrInterfaceId(
			String ticketKey, String type, String interfaceId);

	public List<OpenApiApply> findByTicketKeyAndStatus(String ticketKey,
			int state);

	public void updateApplyInterface(String ticketKey,List<InterfaceDto> allInterfaceDtos);

	public void deleteByTicketAndTypeIn(String ticketKey, String[] type);

}
