package net.zdsoft.base.service.impl;

import java.util.List;

import net.zdsoft.base.dao.OpenApiApplyPowerDao;
import net.zdsoft.base.entity.eis.OpenApiApplyPower;
import net.zdsoft.base.service.OpenApiApplyPowerService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yangsj  2018年7月12日上午9:15:36
 */
@Service("OpenApiApplyPowerService")
public class OpenApiApplyPowerServiceImpl extends BaseServiceImpl<OpenApiApplyPower, String> implements OpenApiApplyPowerService {

	@Autowired
    private OpenApiApplyPowerDao openApiApplyPowerDao;
	@Override
	protected BaseJpaRepositoryDao<OpenApiApplyPower, String> getJpaDao() {
		return openApiApplyPowerDao;
	}

	@Override
	protected Class<OpenApiApplyPower> getEntityClass() {
		return OpenApiApplyPower.class;
	}

	@Override
	public List<OpenApiApplyPower> findByDeveloperId(String developerId) {
		return openApiApplyPowerDao.findByDeveloperId(developerId);
	}

	@Override
	public List<OpenApiApplyPower> findByDeveloperIdAndUnitIdIn(String developerId, String[] unitId) {
		return openApiApplyPowerDao.findByDeveloperIdAndUnitIdIn(developerId,unitId);
	}

	@Override
	public void deleteByDeveloperIdAndUnitIdIn(String developerId, String... unitId) {
		openApiApplyPowerDao.deleteByDeveloperIdAndUnitIdIn(developerId,unitId);
	}

	@Override
	public List<OpenApiApplyPower> findByTicketKeyAndInterfaceIdOrType(
			String ticketKey, String interfaceId, String type) {
		return openApiApplyPowerDao.findByTicketKeyAndInterfaceIdOrType(ticketKey,interfaceId,type);
	}


}
