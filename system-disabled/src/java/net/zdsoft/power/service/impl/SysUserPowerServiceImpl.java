package net.zdsoft.power.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.power.dao.SysUserPowerDao;
import net.zdsoft.power.entity.SysUserPower;
import net.zdsoft.power.service.SysUserPowerService;

/**
 * @author yangsj  2018年6月7日下午2:28:45
 */
@Service("sysUserPowerService")
public class SysUserPowerServiceImpl extends BaseServiceImpl<SysUserPower, String> implements SysUserPowerService{

	@Autowired
	private SysUserPowerDao sysUserPowerDao;
	@Override
	protected BaseJpaRepositoryDao<SysUserPower, String> getJpaDao() {
		return sysUserPowerDao;
	}

	@Override
	protected Class<SysUserPower> getEntityClass() {
		return SysUserPower.class;
	}

	@Override
	public List<SysUserPower> findByTargetIdAndType(String targetId, int type) {
		return sysUserPowerDao.findByTargetIdAndType(targetId,type);
	}

	@Override
	public void deleteByTargetIdAndPowerIdInAndType(String targetId, String[] pids, int type) {
		sysUserPowerDao.deleteByTargetIdAndPowerIdInAndType(targetId,pids,type);
	}

	@Override
	public List<SysUserPower> findByTypeAndTargetIdIn(int type, String[] targetIds) {
		return sysUserPowerDao.findByTypeAndTargetIdIn(type,targetIds);
	}

	@Override
	public void deleteByTargetIdAndType(String targetId, int type) {
		sysUserPowerDao.deleteByTargetIdAndType(targetId,type);
	}

}
