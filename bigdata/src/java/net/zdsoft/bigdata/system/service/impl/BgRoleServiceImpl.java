package net.zdsoft.bigdata.system.service.impl;

import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.system.dao.BgRoleDao;
import net.zdsoft.bigdata.system.entity.BgRole;
import net.zdsoft.bigdata.system.service.BgRolePermService;
import net.zdsoft.bigdata.system.service.BgRoleService;
import net.zdsoft.bigdata.system.service.BgUserRoleService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("bgRoleService")
public class BgRoleServiceImpl  extends BaseServiceImpl<BgRole, String> implements BgRoleService{

	@Autowired
	private BgRoleDao bgRoleDao;
	
	@Autowired
	private BgUserRoleService bgUserRoleService;
	
	@Autowired
	private BgRolePermService bgRolePermService;
	
	@Override
	protected BaseJpaRepositoryDao<BgRole, String> getJpaDao() {
		return bgRoleDao;
	}

	@Override
	protected Class<BgRole> getEntityClass() {
		return BgRole.class;
	}

	@Override
	public List<BgRole> findRoleListListByUnitId(String unitId) {
		return bgRoleDao.findRoleListListByUnitId(unitId);
	}

	@Override
	public BgRole findRoleByUnitIdAndName(String unitId, String name) {
		return bgRoleDao.findRoleByUnitIdAndName(unitId, name);
	}

	@Override
	public Integer getMaxOrderIdByUnitId(String unitId) {
		return bgRoleDao.getMaxOrderIdByUnitId(unitId);
	}

	@Override
	public void deleteRole4All(String id) {
		delete(id);
		bgUserRoleService.deleteByRoleId(id);
		bgRolePermService.deleteByRoleId(id);
	}


}
