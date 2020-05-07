package net.zdsoft.stuwork.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stuwork.data.entity.DyStuHealthProjectItem;

public interface DyStuHealthProjectItemService extends BaseService<DyStuHealthProjectItem, String>{

	/**
	 * 通过itemId获取list
	 * @param unitId
	 * @param itemId
	 * @return
	 */
	public List<DyStuHealthProjectItem> findByItemId(String unitId,String itemId);
	/**
	 * 通过acadyear semester 获取list
	 * @param unitId
	 * @param itemId
	 * @return
	 */
	public List<DyStuHealthProjectItem> findBySemester(String unitId,String acadyear,String semester);
	/**
	 * 保存project
	 * @param unitId
	 * @param itemIds
	 * @return
	 */
	public String saveProject(String unitId,String[] itemIds,String acadyear,String semester);
 	/**
	 * @param studentId
	 * @param acadyear
	 * @param semester
	 * @return
	 */
	List<DyStuHealthProjectItem> findByUIdAndSemester(String unitId, String acadyear, String semester);

}

