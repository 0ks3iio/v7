package net.zdsoft.base.service;

import java.util.List;

import net.zdsoft.base.entity.eis.OpenApiApplyPower;
import net.zdsoft.basedata.service.BaseService;

/**
 * @author yangsj  2018年7月12日上午9:14:20
 */
public interface OpenApiApplyPowerService extends BaseService<OpenApiApplyPower, String>{

	/**
	 * @param id
	 * @return
	 */
	List<OpenApiApplyPower> findByDeveloperId(String id);

	/**
	 * @param developerId
	 * @param unitId
	 */
	List<OpenApiApplyPower> findByDeveloperIdAndUnitIdIn(String developerId, String[] unitId);

	/**
	 * @param developerId
	 * @param unitId
	 */
	void deleteByDeveloperIdAndUnitIdIn(String developerId, String... unitId);

	
	List<OpenApiApplyPower> findByTicketKeyAndInterfaceIdOrType(
			String ticketKey, String interfaceId, String type);

	
}
