package net.zdsoft.power.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.power.dao.SysPowerDao;
import net.zdsoft.power.entity.SysPower;
import net.zdsoft.power.service.SysPowerService;

/**
 * @author yangsj  2018年6月7日下午2:26:50
 */
@Service("sysPowerService")
public class SysPowerServiceImpl extends BaseServiceImpl<SysPower, String> implements SysPowerService {
    
	@Autowired
	private SysPowerDao sysPowerDao;
	@Override
	protected BaseJpaRepositoryDao<SysPower, String> getJpaDao() {
		return sysPowerDao;
	}

	@Override
	protected Class<SysPower> getEntityClass() {
		return SysPower.class;
	}

	@Override
	public List<SysPower> findBySource(int source) {
		return sysPowerDao.findBySource(source);
	}

	@Override
	public SysPower findByValueAndIdIn(String value, String[] ids) {
		return sysPowerDao.findByValueAndIdIn(value,ids);
	}

	@Override
	public SysPower findByValueAndSource(String value, int defaultSourceValue) {
		return sysPowerDao.findByValueAndSource(value,defaultSourceValue);
	}

	@Override
	public List<SysPower> findBySourceAndUnitId(int source, String unitId) {
		return sysPowerDao.findBySourceAndUnitId(source,unitId);
	}


}
