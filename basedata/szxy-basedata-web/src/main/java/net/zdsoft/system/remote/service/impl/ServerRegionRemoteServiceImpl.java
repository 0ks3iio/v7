package net.zdsoft.system.remote.service.impl;

import net.zdsoft.basedata.remote.service.impl.BaseRemoteServiceImpl;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.system.entity.ServerRegion;
import net.zdsoft.system.remote.service.ServerRegionRemoteService;
import net.zdsoft.system.service.ServerRegionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ke_shen@126.com
 * @since 2018/3/6 下午4:03
 */
@Service("serverRegionRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class ServerRegionRemoteServiceImpl extends BaseRemoteServiceImpl<ServerRegion, String> implements ServerRegionRemoteService {

	@Autowired
	private ServerRegionService serverRegionService;

	@Override
	protected BaseService<ServerRegion, String> getBaseService() {
		return serverRegionService;
	}

	@Override
	public String findRegionByDomain(String domain) {
		return serverRegionService.findRegionByDomain(domain);
	}

	@Override
	public String findByRegionAndServerId(String region, int serverId) {
		return SUtils.s(serverRegionService.findByRegionAndServerId(region, serverId));
	}

	@Override
	public String findByServerIdsAndRegion(Integer[] serverIds, String region) {
		return SUtils.s(serverRegionService.findByServerIdsAndRegion(serverIds, region));
	}

	@Override
	public String findByUnitIdMap(String unitId) {
		return SUtils.s(serverRegionService.findByUnitIdMap(unitId));
	}

	@Override
	public String findByRegionMap(String deployRegion) {
		return SUtils.s(serverRegionService.findByRegionMap(deployRegion));
	}
}
