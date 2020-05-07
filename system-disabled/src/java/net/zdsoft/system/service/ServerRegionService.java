package net.zdsoft.system.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.system.entity.ServerRegion;

import java.util.List;
import java.util.Map;

/**
 * @author ke_shen@126.com
 * @since 2018/2/5 下午3:45
 */
public interface ServerRegionService extends BaseService<ServerRegion, String> {

	List<ServerRegion> findByRegion(String regionCode);

	String findRegionByDomain(String domain);

	List<String> findAllRegion();

	ServerRegion findByRegionAndServerId(String region, int serverId);

	List<ServerRegion> findByServerIdsAndRegion(Integer[] serverIds, String region);

	List<ServerRegion> findByRegionAndUnitId(String regionCode, String unitId);

	Map<Integer, ServerRegion> findByUnitIdMap(String unitId);

	Map<Integer, ServerRegion> findByRegionMap(String deployRegion);
}
