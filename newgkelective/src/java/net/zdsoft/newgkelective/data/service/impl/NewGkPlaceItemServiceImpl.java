package net.zdsoft.newgkelective.data.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dao.NewGkPlaceItemDao;
import net.zdsoft.newgkelective.data.dao.NewGkPlaceItemJdbcDao;
import net.zdsoft.newgkelective.data.entity.NewGkPlaceItem;
import net.zdsoft.newgkelective.data.service.NewGkPlaceItemService;
import net.zdsoft.newgkelective.data.service.NewGkTimetableService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("newGkPlaceItemService")
public class NewGkPlaceItemServiceImpl extends BaseServiceImpl<NewGkPlaceItem, String> implements NewGkPlaceItemService{
	@Autowired
	private NewGkPlaceItemDao newGkPlaceItemDao;
	@Autowired
	private NewGkPlaceItemJdbcDao newGkPlaceItemJdbcDao;
	@Autowired
	private NewGkTimetableService newGkTimetableService;

	@Override
	protected BaseJpaRepositoryDao<NewGkPlaceItem, String> getJpaDao() {
		return newGkPlaceItemDao;
	}

	@Override
	protected Class<NewGkPlaceItem> getEntityClass() {
		return NewGkPlaceItem.class;
	}

	@Override
	public void save(List<NewGkPlaceItem> newGkPlaceItemList1,
			List<NewGkPlaceItem> newGkPlaceItemList2, String arrayId, String arrayItemId) {
		newGkPlaceItemDao.deleteByArrayItemId(arrayItemId);
		if(CollectionUtils.isEmpty(newGkPlaceItemList1)){
//			newGkPlaceItemDao.saveAll(newGkPlaceItemList1);
			newGkPlaceItemList1 = new ArrayList<NewGkPlaceItem>();
		}
		// 
		if(StringUtils.isNotBlank(arrayId)) {
			newGkTimetableService.updateTimetablePlaces(arrayId, newGkPlaceItemList1, new String[] {NewGkElectiveConstant.CLASS_TYPE_1});
		}
		if(CollectionUtils.isNotEmpty(newGkPlaceItemList2)){
//			newGkPlaceItemDao.saveAll(newGkPlaceItemList2);
			newGkPlaceItemList1.addAll(newGkPlaceItemList2);
		}
		newGkPlaceItemJdbcDao.saveAll(newGkPlaceItemList1.toArray(new NewGkPlaceItem[0]));
	}

	@Override
	public List<NewGkPlaceItem> findByArrayItemId(String arrayItemId) {
		return newGkPlaceItemDao.findByArrayItemId(arrayItemId);
	}
	@Override
	public List<NewGkPlaceItem> findByArrayItemIdWithMaster(String arrayItemId){
		return findByArrayItemId(arrayItemId);
	}

	@Override
	public void deleteByArrayItemId(String arrayItemId) {
		newGkPlaceItemDao.deleteByArrayItemId(arrayItemId);
	}

	@Override
	public List<NewGkPlaceItem> findByArrayItemIdAndTypeIn(String arrayItemId,
			String[] types) {
		return newGkPlaceItemDao.findByArrayItemIdAndTypeIn(arrayItemId,types);
	}
	@Override
	public List<NewGkPlaceItem> findByArrayItemIdAndTypeInWithMaster(String arrayItemId,
			String[] types){
		return findByArrayItemIdAndTypeIn(arrayItemId, types);
	}
	@Override
	public void deleteByArrayItemIdAndType(String arrayId, String arrayItemId, String type) {
		if(StringUtils.isNotBlank(type)) {
			newGkPlaceItemDao.deleteByArrayItemIdAndType(arrayItemId,type);
		}else {
			newGkPlaceItemDao.deleteByArrayItemId(arrayItemId);
		}
		if(StringUtils.isNotBlank(arrayId)) {
			String[] placeTypes = null;
			if(StringUtils.isBlank(type)) {
				placeTypes = new String[] {NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_2};
			}else {
				placeTypes = new String[] {type};
			}
			newGkTimetableService.updateTimetablePlaces(arrayId, null, placeTypes);
		}
	}

	@Override
	public void saveByType(List<NewGkPlaceItem> newGkPlaceItemList,
			String arrayId, String arrayItemId, String type) {
		deleteByArrayItemIdAndType(null,arrayItemId, type);
		if(CollectionUtils.isNotEmpty(newGkPlaceItemList)){
			newGkPlaceItemJdbcDao.saveAll(newGkPlaceItemList.toArray(new NewGkPlaceItem[0]));
		}
		if(StringUtils.isNotBlank(arrayId)) {
			newGkTimetableService.updateTimetablePlaces(arrayId, newGkPlaceItemList, new String[] {type});
		}
	}

	@Override
	public Map<String, Integer> findGroupInfo(String[] arrayItemIds) {
		Map<String, Integer> map = new HashMap<>();
		
		if(arrayItemIds == null || arrayItemIds.length == 0) {
			return map;
		}
		List<Object[]> groupInfo = newGkPlaceItemDao.findGroupInfo(arrayItemIds);
		for (Object[] obs : groupInfo) {
			String arrayItemId = (String) obs[0];
			Integer count = ((Long) obs[1]).intValue();
			
			map.put(arrayItemId, count);
		}
		
		return map;
	}

	@Override
	public void deleteNotInPlaceIds(String arrayItemId, String[] placeIds) {
		if(placeIds==null || placeIds.length<=0) {
			deleteByArrayItemId(arrayItemId);
		}else {
			newGkPlaceItemDao.deleteNotInPlaceIds(arrayItemId,placeIds);
		}
	}

//	@Override
//	public List<NewGkPlaceItem> findByTypeAndObjectIdIn(
//			String type, String[] objectIds) {
//		return newGkPlaceItemDao.findByTypeAndObjectIdIn(type,objectIds);
//	}


}
