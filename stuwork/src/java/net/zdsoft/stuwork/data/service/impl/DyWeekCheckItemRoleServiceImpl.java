package net.zdsoft.stuwork.data.service.impl;

import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.dao.DyWeekCheckItemRoleDao;
import net.zdsoft.stuwork.data.entity.DyWeekCheckItemRole;
import net.zdsoft.stuwork.data.service.DyWeekCheckItemRoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dyWeekCheckItemRoleService")
public class DyWeekCheckItemRoleServiceImpl extends BaseServiceImpl<DyWeekCheckItemRole, String>  implements DyWeekCheckItemRoleService{
	
	@Autowired
	private DyWeekCheckItemRoleDao dyWeekCheckItemRoleDao;
	
	@Override
	protected BaseJpaRepositoryDao<DyWeekCheckItemRole, String> getJpaDao() {
		return dyWeekCheckItemRoleDao;
	}

	@Override
	protected Class<DyWeekCheckItemRole> getEntityClass() {
		return DyWeekCheckItemRole.class;
	}
	@Override
	public List<DyWeekCheckItemRole> findByItemIds(String[] itemIds) {
		return dyWeekCheckItemRoleDao.findByItems(itemIds);
	}
	@Override
	public void deleteByItemId(String itemId) {
		dyWeekCheckItemRoleDao.deleteByItemId(itemId);
	}
	
	@Override
	public void saveList(List<DyWeekCheckItemRole> itemRoles) {
		DyWeekCheckItemRole[] roleArrs = itemRoles.toArray(new DyWeekCheckItemRole[0]);
		checkSave(roleArrs);
		saveAll(roleArrs);
	}
}
