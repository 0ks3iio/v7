package net.zdsoft.remote.openapi.remote.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.remote.service.impl.BaseRemoteServiceImpl;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.remote.openapi.entity.DeveloperPower;
import net.zdsoft.remote.openapi.remote.service.DeveloperPowerRemoteService;
import net.zdsoft.remote.openapi.service.DeveloperPowerService;

/**
 * @author yangsj  2018年7月17日上午9:25:59
 */
@Service("developerPowerRemoteService")
public class DeveloperPowerRemoteServiceImpl extends BaseRemoteServiceImpl<DeveloperPower, String> implements DeveloperPowerRemoteService {

	@Autowired
	private  DeveloperPowerService developerPowerService;
	
	@Override
	protected BaseService<DeveloperPower, String> getBaseService() {
		return developerPowerService;
	}

	@Override
	public String findByDeveloperIdAndUnitIdIn(String developerId, String[] unitId) {
		return SUtils.s(developerPowerService.findByDeveloperIdAndUnitIdIn(developerId,unitId));
	}

	@Override
	public void deleteByDeveloperIdAndUnitIdIn(String developerId, String[] unitId) {
		developerPowerService.deleteByDeveloperIdAndUnitIdIn(developerId,unitId);
	}

	@Override
	public String findByDeveloperId(String developerId) {
		return SUtils.s(developerPowerService.findByDeveloperId(developerId));
	}


}
