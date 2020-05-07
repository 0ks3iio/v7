package net.zdsoft.system.remote.service;

import net.zdsoft.basedata.remote.service.BaseRemoteService;
import net.zdsoft.system.entity.ServerRegion;

/**
 * @author ke_shen@126.com
 * @since 2018/3/6 下午4:01
 */
public interface ServerRegionRemoteService extends BaseRemoteService<ServerRegion, String> {

	String findRegionByDomain(String domain);

	String findByRegionAndServerId(String region, int serverId);

	String findByServerIdsAndRegion(Integer[] serverIds, String region);
	
	String findByUnitIdMap(String unitId);

	String findByRegionMap(String deployRegion);
}
