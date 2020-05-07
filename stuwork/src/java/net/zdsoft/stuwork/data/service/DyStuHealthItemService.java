package net.zdsoft.stuwork.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stuwork.data.entity.DyStuHealthItem;


public interface DyStuHealthItemService extends BaseService<DyStuHealthItem, String>{

	/**
	 * 通过unitId 获取itemList
	 * @param unitId
	 * @return
	 */
	public List<DyStuHealthItem> findByUnitId(String unitId);
	/**
	 * 通过unitId 学年学期 获取itemList包涵学期设置
	 * @param 
	 * @return
	 */
	public List<DyStuHealthItem> findBySemester(String unitId,String acadyear,String semester);
	/**
	 * 通过unitId itemName 获取itemList
	 * @param unitId
	 * @return itemName
	 */
	public List<DyStuHealthItem> findbyItemName(String unitId,String itemName);
}
