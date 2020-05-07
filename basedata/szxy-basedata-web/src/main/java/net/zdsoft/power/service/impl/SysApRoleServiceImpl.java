package net.zdsoft.power.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.power.dao.SysApRoleDao;
import net.zdsoft.power.entity.SysApRole;
import net.zdsoft.power.service.SysApRoleService;

/**
 * @author yangsj  2018年6月7日下午2:25:08
 */
@Service("sysApRoleService")
public class SysApRoleServiceImpl extends BaseServiceImpl<SysApRole, String> implements SysApRoleService{

	@Autowired
	private SysApRoleDao sysApRoleDao;
	@Override
	protected BaseJpaRepositoryDao<SysApRole, String> getJpaDao() {
		return sysApRoleDao;
	}

	@Override
	protected Class<SysApRole> getEntityClass() {
		return SysApRole.class;
	}

	@Override
	public void deleteByRoleId(String roleId) {
		sysApRoleDao.deleteByRoleId(roleId);
	}

	@Override
	public List<SysApRole> findByServerId(Integer serverId) {
		return sysApRoleDao.findByServerId(serverId);
	}

	@Override
	public List<SysApRole> findByServerIdIn(Integer[] serverIds) {
		return sysApRoleDao.findByServerIdIn(serverIds);
	}

	@Override
	public SysApRole findByRoleId(String roleId) {
		return sysApRoleDao.findByRoleId(roleId);
	}

	@Override
	public List<SysApRole> findByServerIdAndUnitId(Integer serverId, String unitId) {
		return sysApRoleDao.findByServerIdAndUnitId(serverId,unitId);
	}

	@Override
	public List<SysApRole> findByServerIdInAndUnitId(Integer[] serverIds, String unitId) {
		return sysApRoleDao.findByServerIdInAndUnitId(serverIds,unitId);
	}

}
