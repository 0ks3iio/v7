package net.zdsoft.remote.openapi.remote.service;

import net.zdsoft.basedata.remote.service.BaseRemoteService;
import net.zdsoft.remote.openapi.entity.DeveloperPower;

/**
 * @author yangsj  2018年7月17日上午9:24:52
 */
public interface DeveloperPowerRemoteService extends BaseRemoteService<DeveloperPower, String>{

	/**
	 * @param developerId
	 * @param unitId
	 * @return
	 */
	String findByDeveloperIdAndUnitIdIn(String developerId, String[] unitId);

	/**
	 * @param developerId
	 * @param unitId
	 */
	void deleteByDeveloperIdAndUnitIdIn(String developerId, String[] unitId);

	/**
	 * @param developerId
	 * @return
	 */
	String findByDeveloperId(String developerId);

}
