package net.zdsoft.system.remote.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.entity.server.ServerAuthorize;
import net.zdsoft.system.remote.service.ServerAuthorizeRemoteService;
import net.zdsoft.system.service.server.ServerAuthorizeService;

@Service
public class ServerAuthorizeRemoteServiceImpl implements ServerAuthorizeRemoteService {

	@Autowired
	private ServerAuthorizeService serverAuthorizeService;
	
	@Override
	public Map<String, List<Server>> getUnitIdAndAuthorizeServerMap(String[] unitIds) {
		return serverAuthorizeService.getUnitIdAndAuthorizeServerMap(unitIds);
	}

	@Override
	public List<Integer> listServerIds(String unitId) {
		return serverAuthorizeService.listServerIds(unitId);
	}

	@Override
	public void updateUnitAuthorize(String unitId, List<ServerAuthorize> serverAuthorizeList) {
		serverAuthorizeService.updateUnitAuthorize(unitId, serverAuthorizeList);
	}

	@Override
	public void saveServerAuthorizes(List<ServerAuthorize> serverAuthorizeList) {
		serverAuthorizeService.saveServerAuthorizes(serverAuthorizeList);
	}

	@Override
	public void removeServerAuthorizes(Integer serverId, String[] unitIds) {
		serverAuthorizeService.removeServerAuthorizes(serverId, unitIds);		
	}

	@Override
	public List<String> listUnitIds(Integer serverId) {
		return serverAuthorizeService.listUnitIds(serverId);
	}

	@Override
	public List<Server> listServers(String unitId, Integer unitClass, Integer ownerType, Integer[] orderTypes,
			String sections, Integer isVisible, String subIdCondition) {
		return serverAuthorizeService.listServers(unitId, unitClass, ownerType, orderTypes, sections, isVisible, subIdCondition);
	}

	@Override
	public void removeByServerIdAndUnitCondition(Integer serverId, String region, Integer[] unitClass) {
		serverAuthorizeService.removeByServerIdAndUnitCondition(serverId, region, unitClass);		
	}

	
}
