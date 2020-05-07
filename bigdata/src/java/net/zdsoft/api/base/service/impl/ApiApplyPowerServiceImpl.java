package net.zdsoft.api.base.service.impl;

import java.util.List;

import net.zdsoft.api.base.dao.ApiApplyPowerDao;
import net.zdsoft.api.base.entity.eis.ApiApplyPower;
import net.zdsoft.api.base.service.ApiApplyPowerService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yangsj  2018年7月12日上午9:15:36
 */
@Service("apiApplyPowerService")
public class ApiApplyPowerServiceImpl extends BaseServiceImpl<ApiApplyPower, String> implements ApiApplyPowerService {

	@Autowired
    private ApiApplyPowerDao apiApplyPowerDao;
	@Override
	protected BaseJpaRepositoryDao<ApiApplyPower, String> getJpaDao() {
		return apiApplyPowerDao;
	}

	@Override
	protected Class<ApiApplyPower> getEntityClass() {
		return ApiApplyPower.class;
	}

	@Override
	public List<ApiApplyPower> findByDeveloperId(String developerId) {
		return apiApplyPowerDao.findByDeveloperId(developerId);
	}

	@Override
	public List<ApiApplyPower> findByDeveloperIdAndUnitIdIn(String developerId, String[] unitId) {
		return apiApplyPowerDao.findByDeveloperIdAndUnitIdIn(developerId,unitId);
	}

	@Override
	public void deleteByDeveloperIdAndUnitIdIn(String developerId, String... unitId) {
		apiApplyPowerDao.deleteByDeveloperIdAndUnitIdIn(developerId,unitId);
	}

	@Override
	public List<ApiApplyPower> findByTicketKeyAndInterfaceIdOrType(
			String ticketKey, String interfaceId, String type) {
		return apiApplyPowerDao.findByTicketKeyAndInterfaceIdOrType(ticketKey,interfaceId,type);
	}


}
