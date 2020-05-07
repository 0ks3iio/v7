package net.zdsoft.system.service.impl;

import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.system.dao.ServerRegionDao;
import net.zdsoft.system.entity.ServerRegion;
import net.zdsoft.system.service.ServerRegionService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ke_shen@126.com
 * @since 2018/2/5 下午3:45
 */
@Service
public class ServerRegionServiceImpl extends BaseServiceImpl<ServerRegion, String> implements ServerRegionService {

    @Resource
    private ServerRegionDao serverRegionDao;

    @Override
    public List<ServerRegion> findByRegion(String regionCode) {
        return serverRegionDao.findByRegion(regionCode);
    }

    @Override
    public String findRegionByDomain(String domain) {
        List<String> regions = serverRegionDao.findRegionByDomain(domain);
        return regions.stream().findFirst().orElse(null);
    }

    @Override
    public ServerRegion findByRegionAndServerId(String region, int serverId) {
        return serverRegionDao.findByRegionAndServerId(region, serverId);
    }

    @Override
    public List<ServerRegion> findByServerIdsAndRegion(Integer[] serverIds, String region) {
        return serverRegionDao.findByServerIdInAndRegion(serverIds, region);
    }

    @Override
    public List<String> findAllRegion() {
        return serverRegionDao.findAllRegion();
    }

    @Override
    protected BaseJpaRepositoryDao<ServerRegion, String> getJpaDao() {
        return serverRegionDao;
    }

    @Override
    protected Class<ServerRegion> getEntityClass() {
        return ServerRegion.class;
    }

	@Override
	public List<ServerRegion> findByRegionAndUnitId(String regionCode,
			String unitId) {
		return serverRegionDao.findByRegionAndUnitId(regionCode,unitId);
	}

	@Override
	public Map<Integer, ServerRegion> findByUnitIdMap(String unitId) {
	    List<ServerRegion> serverRegionList = serverRegionDao.findByUnitId(unitId);
        return EntityUtils.getMap(serverRegionList, ServerRegion::getServerId);
	}

	@Override
	public Map<Integer, ServerRegion> findByRegionMap(String deployRegion) {
		List<ServerRegion> serverRegionList = serverRegionDao.findByRegion(deployRegion);
        return EntityUtils.getMap(serverRegionList, ServerRegion::getServerId);
	}
}
