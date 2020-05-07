package net.zdsoft.remote.openapi.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.remote.openapi.entity.DeveloperPower;

/**
 * @author yangsj  2018年7月12日上午9:14:20
 */
public interface DeveloperPowerService extends BaseService<DeveloperPower, String>{

	/**
	 * @param id
	 * @return
	 */
	List<DeveloperPower> findByDeveloperId(String id);

	/**
	 * @param developerId
	 * @param unitId
	 */
	List<DeveloperPower> findByDeveloperIdAndUnitIdIn(String developerId, String[] unitId);

	/**
	 * @param developerId
	 * @param unitId
	 */
	void deleteByDeveloperIdAndUnitIdIn(String developerId, String... unitId);

	
}
