package net.zdsoft.stuwork.data.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.stuwork.data.dao.DyStuHealthItemDao;
import net.zdsoft.stuwork.data.entity.DyStuHealthItem;
import net.zdsoft.stuwork.data.entity.DyStuHealthProjectItem;
import net.zdsoft.stuwork.data.service.DyStuHealthItemService;
import net.zdsoft.stuwork.data.service.DyStuHealthProjectItemService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dyStuHealthItemService")
public class DyStuHealthItemServiceImpl extends BaseServiceImpl<DyStuHealthItem, String> implements DyStuHealthItemService{
	
	@Autowired
	private DyStuHealthItemDao dyStuHealthItemDao;
	@Autowired
	private DyStuHealthProjectItemService dyStuHealthProjectItemService;
	
	@Override
	public List<DyStuHealthItem> findByUnitId(String unitId){
		return dyStuHealthItemDao.findByUnitId(unitId);
	}
	@Override
	public List<DyStuHealthItem> findBySemester(String unitId,String acadyear,String semester){
		List<DyStuHealthItem> itemList=dyStuHealthItemDao.findByUnitId(unitId);
		List<DyStuHealthProjectItem> projectList=dyStuHealthProjectItemService.findBySemester(unitId, acadyear, semester);
		Set<String> itemIds=new HashSet<String>();
		Map<String,String> itemNameMap=EntityUtils.getMap(projectList, "itemId","itemName");
		if(CollectionUtils.isNotEmpty(projectList)){
			for(DyStuHealthProjectItem project:projectList){
				itemIds.add(project.getItemId());
			}
		}
		if(CollectionUtils.isNotEmpty(itemList)){
			for(DyStuHealthItem item:itemList){
				String itemId=item.getId();
				if(itemIds.contains(itemId)){
					item.setHaveSelected(true);
				}
				if(StringUtils.isNotBlank(itemNameMap.get(itemId))){
					item.setItemName(itemNameMap.get(itemId));
				}
			}
		}
		return itemList;
	}
	@Override
	public List<DyStuHealthItem> findbyItemName(String unitId,String itemName){
		return dyStuHealthItemDao.findbyItemName(unitId,itemName);
	}
	@Override
	protected BaseJpaRepositoryDao<DyStuHealthItem, String> getJpaDao() {
		return dyStuHealthItemDao;
	}

	@Override
	protected Class<DyStuHealthItem> getEntityClass() {
		return DyStuHealthItem.class;
	}

}
