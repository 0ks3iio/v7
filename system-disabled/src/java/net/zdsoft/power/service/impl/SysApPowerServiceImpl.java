package net.zdsoft.power.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.power.dao.SysApPowerDao;
import net.zdsoft.power.entity.SysApPower;
import net.zdsoft.power.service.SysApPowerService;

/**
 * @author yangsj  2018年6月7日下午2:23:34
 */
@Service("sysApPowerService")
public class SysApPowerServiceImpl extends BaseServiceImpl<SysApPower, String> implements SysApPowerService {

	@Autowired
	private SysApPowerDao sysApPowerDao;
	@Override
	protected BaseJpaRepositoryDao<SysApPower, String> getJpaDao() {
		return sysApPowerDao;
	}

	@Override
	protected Class<SysApPower> getEntityClass() {
		return SysApPower.class;
	}

	@Override
	public void deleteBypowerIdAndServerId(String powerId, Integer serverId) {
		if(serverId == null) {
			sysApPowerDao.deleteBypowerId(powerId);
		}else {
			sysApPowerDao.deleteBypowerIdAndServerId(powerId,serverId);
		}
	}

	@Override
	public List<SysApPower> findByServerId(Integer serverId) {
		return sysApPowerDao.findByServerId(serverId);
	}

	@Override
	public List<SysApPower> findByServerIdIn(Integer[] serverIds) {
		return sysApPowerDao.findByServerIdIn(serverIds);
	}

	@Override
	public SysApPower findByPowerId(String powerId) {
		return sysApPowerDao.findByPowerId(powerId);
	}

	@Override
	public List<SysApPower> findByServerIdInAndUnitId(Integer[] serverIds, String unitId) {
		return sysApPowerDao.findByServerIdInAndUnitId(serverIds,unitId);
	}

	@Override
	public List<SysApPower> findByServerIdAndUnitId(Integer serverId, String unitId) {
		return sysApPowerDao.findByServerIdAndUnitId(serverId,unitId);
	}


}
