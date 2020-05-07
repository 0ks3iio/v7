package net.zdsoft.stuwork.data.service;

import java.util.List;
import java.util.Set;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stuwork.data.entity.DyWeekCheckItem;

/**
 * dy_week_check_item
 * @author 
 * 
 */
public interface DyWeekCheckItemService extends BaseService<DyWeekCheckItem, String>{
	/**
	 * 获得某些角色能维护的考核项目
	 * @param schoolId
	 * @param roleTypes
	 * @return
	 */
	public List<DyWeekCheckItem> findByRoleTypes(String schoolId, Set<String> roleTypes);
	/**
	 * @param unitId
	 * @param roleType
	 * @param week 周几
	 * @return
	 */
	public List<DyWeekCheckItem> findByRoleTypeAndCheckWeek(String unitId,
			String roleType, Integer week);
	
	public List<DyWeekCheckItem> findBySchoolId(String unitId);
	/**
	 * 保存（更新）项目
	 * @param item
	 */
	public void saveItem(DyWeekCheckItem item);
	/**
	 * 删除
	 * @param itemId
	 */
	public void deleteItem(String itemId);
	/**
	 * 获得某一天的考核项目
	 * @param unitId
	 * @param weekday
	 * @return
	 */
	public List<DyWeekCheckItem> findBySchoolAndDay(String unitId,
			Integer weekday);
	
}