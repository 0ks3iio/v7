package net.zdsoft.remote.openapi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.remote.openapi.dao.DeveloperPowerDao;
import net.zdsoft.remote.openapi.entity.DeveloperPower;
import net.zdsoft.remote.openapi.service.DeveloperPowerService;

/**
 * @author yangsj  2018年7月12日上午9:15:36
 */
@Service("developerPowerService")
public class DeveloperPowerServiceImpl extends BaseServiceImpl<DeveloperPower, String> implements DeveloperPowerService {

	@Autowired
    private DeveloperPowerDao developerPowerDao;
	@Override
	protected BaseJpaRepositoryDao<DeveloperPower, String> getJpaDao() {
		return developerPowerDao;
	}

	@Override
	protected Class<DeveloperPower> getEntityClass() {
		return DeveloperPower.class;
	}

	@Override
	public List<DeveloperPower> findByDeveloperId(String developerId) {
		return developerPowerDao.findByDeveloperId(developerId);
	}

	@Override
	public List<DeveloperPower> findByDeveloperIdAndUnitIdIn(String developerId, String[] unitId) {
		return developerPowerDao.findByDeveloperIdAndUnitIdIn(developerId,unitId);
	}

	@Override
	public void deleteByDeveloperIdAndUnitIdIn(String developerId, String... unitId) {
		developerPowerDao.deleteByDeveloperIdAndUnitIdIn(developerId,unitId);
	}


}
