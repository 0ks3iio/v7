package net.zdsoft.newgkelective.data.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.entity.NewGkPlaceItem;

public interface NewGkPlaceItemService extends BaseService<NewGkPlaceItem, String>{

	public void save(List<NewGkPlaceItem> newGkPlaceItemList1, List<NewGkPlaceItem> newGkPlaceItemList2, String arrayId, String arrayItemId);
	
	public List<NewGkPlaceItem> findByArrayItemId(String arrayItemId);
	public List<NewGkPlaceItem> findByArrayItemIdWithMaster(String arrayItemId);
	
	public void deleteByArrayItemId(String arrayItemId);
	
	public void deleteByArrayItemIdAndType(String arrayId,String arrayItemId, String type);

	public List<NewGkPlaceItem> findByArrayItemIdAndTypeIn(String arrayItemId,
			String[] types);
	public List<NewGkPlaceItem> findByArrayItemIdAndTypeInWithMaster(String arrayItemId,
			String[] types);
	
	public void saveByType(List<NewGkPlaceItem> newGkPlaceItemList,String arrayId,String arrayItemId, String type);

	//public List<NewGkPlaceItem> findByTypeAndObjectIdIn(String type, String[] objectIds);
	
	public Map<String,Integer> findGroupInfo(String[] arrayItemIds);

	/**
	 * 如果placeIds为空 就是删除所有安排场地的班级数据
	 * placeIds不为空 就是删除不属于placeIds的班级数据
	 * @param arrayItemId
	 * @param placeIds
	 */
	public void deleteNotInPlaceIds(String arrayItemId, String[] placeIds);
}
