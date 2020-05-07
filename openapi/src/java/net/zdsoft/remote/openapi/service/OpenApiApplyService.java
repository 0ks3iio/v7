package net.zdsoft.remote.openapi.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.remote.openapi.entity.OpenApiApply;

public interface OpenApiApplyService extends BaseService<OpenApiApply, String>{

    public void save(OpenApiApply openApiApply);

    public List<String> getTypes(int status, String developerId);

    public List<OpenApiApply> getApplys(String developerId);

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
     * @author chicb
     * @param types
     * @param developerId
     * @param ticketKey
     * @param status
     */
    public void updateApplyInterface(String[] types, String developerId, String ticketKey, int status);

    /**
     * @author chicb
     * @param id
     * @param ticketKey
     * @param passColumnMap  审核通过的字段
     */
    public void updatePassApply(String developerId, String ticketKey, Map<String, List<String>> passColumnMap);

    
	public List<String> findByDeveloperIdAndStatusIn(String developerId, int[] status);

	
	public List<OpenApiApply> findByTypeAndStatusIn(String type, int[] status);

	public List<OpenApiApply> findByDeveloperIdAndTypeIn(String developerId,
			String... types);

}
