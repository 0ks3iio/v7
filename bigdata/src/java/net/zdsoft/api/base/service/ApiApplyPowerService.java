package net.zdsoft.api.base.service;

import java.util.List;

import net.zdsoft.api.base.entity.eis.ApiApplyPower;
import net.zdsoft.basedata.service.BaseService;

/**
 * @author yangsj  2018年7月12日上午9:14:20
 */
public interface ApiApplyPowerService extends BaseService<ApiApplyPower, String>{

	/**
	 * @param id
	 * @return
	 */
	List<ApiApplyPower> findByDeveloperId(String id);

	/**
	 * @param developerId
	 * @param unitId
	 */
	List<ApiApplyPower> findByDeveloperIdAndUnitIdIn(String developerId, String[] unitId);

	/**
	 * @param developerId
	 * @param unitId
	 */
	void deleteByDeveloperIdAndUnitIdIn(String developerId, String... unitId);

	
	List<ApiApplyPower> findByTicketKeyAndInterfaceIdOrType(
			String ticketKey, String interfaceId, String type);

	
}
