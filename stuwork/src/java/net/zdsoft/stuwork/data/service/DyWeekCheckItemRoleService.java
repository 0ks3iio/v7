package net.zdsoft.stuwork.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stuwork.data.entity.DyWeekCheckItemRole;

import org.springframework.data.jpa.repository.Query;

/**
 * dy_week_check_item_role
 * @author 
 * 
 */
public interface DyWeekCheckItemRoleService extends BaseService<DyWeekCheckItemRole, String>{
	
	public List<DyWeekCheckItemRole> findByItemIds(String[] itemIds);

	public void deleteByItemId(String itemId);

	public void saveList(List<DyWeekCheckItemRole> itemRoles);

}